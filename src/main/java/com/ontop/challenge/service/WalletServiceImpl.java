package com.ontop.challenge.service;

import com.ontop.challenge.exception.*;
import com.ontop.challenge.infraestructure.endpoint.CreatePaymentEndpoint;
import com.ontop.challenge.infraestructure.endpoint.CreateWalletTransactionEndpoint;
import com.ontop.challenge.infraestructure.endpoint.GetBalanceEndpoint;
import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.entity.Wallet;
import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.model.enums.TransactionTypes;
import com.ontop.challenge.model.request.PaymentRequest;
import com.ontop.challenge.model.request.TransactionUpdateRequest;
import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.CreateWalletTransactionRequest;
import com.ontop.challenge.model.response.PaymentProviderResponse;
import com.ontop.challenge.repository.TransactionRepository;
import com.ontop.challenge.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService{


    private static final String DEFAULT_CURRENCY = "USD";
    private static final String SOURCE_ACCOUNT = "0245253419";
    private static final String SOURCE_ROUTING = "028444018";
    private static final String TYPE_AMOUNT_OUTCOME = "01"; // 01 Outcome
    private static final String TYPE_AMOUNT_INCOME = "02"; // 02 = Income
    private static final String PROVIDER_STATUS_FAILED = "Transaction Failed";
    private static final String PROVIDER_STATUS_REFUND = "Refunded to your wallet";
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final BankAccountService bankAccountService;
    private final CreateWalletTransactionEndpoint createWalletTransactionEndpoint;
    private final CreatePaymentEndpoint createPaymentEndpoint;
    private final GetBalanceEndpoint getBalanceEndpoint;


    @Override
    public BasicResponse<Transaction> processTransfer(WalletTransactionRequest request) {
        Transaction transaction = new Transaction();
        try {
            logger.info("Starting process transfer for user id: {}", request.getUserId());
            logger.info("1.Validating balance ");
            if (request.getAmount() <= 0){
                throw new AmountLessOrEqualsThanZeroException("Calculated amount must be greater than 0");
            }
            double currentBalance = getBalanceEndpoint.invoke(request.getUserId());
            validateBalance(currentBalance, request.getAmount());

            logger.info("2. Validating bank account");
            BankAccount bankAccount = bankAccountService.getBankAccountByUserId(request.getUserId());

            logger.info("3. Creating wallet transaction");
            CreateWalletTransactionRequest createWalletTransactionRequest = new CreateWalletTransactionRequest(request.getUserId(),request.getAmount());
            String walletTransactionId = createWalletTransactionEndpoint.invoke(createWalletTransactionRequest)
                    .getWalletTransactionId();

            logger.info("4. Creating transaction in database");
            transaction.buildTransaction(walletTransactionId,
                    request.getUserId(),
                    request.getAmount(),
                    request.getFee(),
                    request.getInputAmount(),
                    TYPE_AMOUNT_OUTCOME,
                    DEFAULT_CURRENCY,
                    TransactionTypes.WITHDRAWAL.getType());
            transactionRepository.save(transaction);

            logger.info("5. Creating payment in provider");
            PaymentRequest paymentRequest = buildPaymentRequest(request, bankAccount);
            PaymentProviderResponse paymentProviderResponse = createPaymentEndpoint.invoke(paymentRequest);

            logger.info("6. Updating transaction in database");
            transaction.setProviderTransactionId(paymentProviderResponse.getPaymentInfo().getId());
            transaction.setStatus(paymentProviderResponse.getRequestInfo().getStatus());
            transactionRepository.save(transaction);

            logger.info("7. Updating balance in wallet");
            Wallet wallet = walletRepository.findByUserId(request.getUserId()).orElseThrow(
                    () -> new NotInfoFoundException("No wallet found for user id: " + request.getUserId()));
            wallet.setBalance(currentBalance - request.getAmount());
            walletRepository.save(wallet);

            logger.info("Transaction completed for user id: {}", request.getUserId());
            return new BasicResponse<>(ResponseCodes.SUCCESS.getCode(),
                    ResponseCodes.SUCCESS.getMessage(),
                    transaction);

        }catch (PaymentProviderApiException e) {
            transaction.setStatus(PROVIDER_STATUS_FAILED);
            transactionRepository.save(transaction);
            generateRefundProcess(transaction);
            return new BasicResponse<>(ResponseCodes.ERROR_CONSUMING_EXTERNAL_API.getCode(),
                    e.toString());
        }
    }


    private static PaymentRequest buildPaymentRequest(WalletTransactionRequest request, BankAccount bankAccount) {
        PaymentRequest paymentRequest =new PaymentRequest();
        PaymentRequest.PaymentSource paymentSource = new PaymentRequest.PaymentSource();
        PaymentRequest.SourceInfo sourceInfo = new PaymentRequest.SourceInfo();
        PaymentRequest.Account accountSource = new PaymentRequest.Account();
        accountSource.setAccountNumber(SOURCE_ACCOUNT);
        accountSource.setRoutingNumber(SOURCE_ROUTING);
        paymentSource.setSourceInformation(sourceInfo);
        paymentSource.setAccount(accountSource);

        PaymentRequest.PaymentDestination paymentDestination = new PaymentRequest.PaymentDestination();
        PaymentRequest.Account accountDestination = new PaymentRequest.Account();
        accountDestination.setAccountNumber(bankAccount.getAccountNumber());
        accountDestination.setRoutingNumber(bankAccount.getRoutingNumber());
        paymentDestination.setName(bankAccount.getFirstName() + " " + bankAccount.getLastName());
        paymentDestination.setAccount(accountDestination);

        paymentRequest.setAmount(request.getAmount());
        paymentRequest.setSource(paymentSource);
        paymentRequest.setDestination(paymentDestination);
        return paymentRequest;
    }

    public void validateBalance(double currentBalance, double amount){
        if (currentBalance < amount ){
            throw new NotEnoughBalanceException("Insufficient funds");
        }
    }


    public BasicResponse<Transaction> generateRefundProcess(Transaction blueprintTransaction) {
        logger.info("Generating refund process for user id: {}", blueprintTransaction.getUserId());
        Transaction transaction = new Transaction();
        transaction.buildTransaction(blueprintTransaction.getProviderTransactionId(),
                blueprintTransaction.getUserId(),
                blueprintTransaction.getInputAmount(),
                0.0,
                blueprintTransaction.getInputAmount(),
                TYPE_AMOUNT_INCOME,
                DEFAULT_CURRENCY,
                TransactionTypes.REFUND.getType());
        transaction.setStatus(PROVIDER_STATUS_REFUND);
        transactionRepository.save(transaction);
        logger.info("Refund process completed for user id: {}", blueprintTransaction.getUserId());
        return new BasicResponse<>(ResponseCodes.SUCCESS.getCode(),
                ResponseCodes.SUCCESS.getMessage(),
                transaction);
    }

    @Override
    public BasicResponse<Transaction> updateTransferProcess(TransactionUpdateRequest request) {
        logger.info("Updating transaction for request payment provider with id: {}", request.getProviderTransactionId());
        Transaction transaction = transactionRepository.findByProviderTransactionId(request.getProviderTransactionId()).orElseThrow(
                () -> new NotInfoFoundException("No transaction found for provider id: " + request.getProviderTransactionId()));
        if (request.getStatus().equals(PROVIDER_STATUS_FAILED)){
            logger.info("Transaction failed for request payment provider with id: {}. Requesting refund process", request.getProviderTransactionId());
            return generateRefundProcess(transaction);
        }
        transaction.updateStatusTransaction(request.getStatus());
        transactionRepository.save(transaction);
        logger.info("Transaction updated for request payment provider with id: {}", request.getProviderTransactionId());
        return new BasicResponse<>(ResponseCodes.SUCCESS.getCode(),
                ResponseCodes.SUCCESS.getMessage(),
                transaction);

    }
}
