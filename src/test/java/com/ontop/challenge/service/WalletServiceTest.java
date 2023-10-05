package com.ontop.challenge.service;

import com.ontop.challenge.exception.*;
import com.ontop.challenge.infraestructure.endpoint.CreatePaymentEndpoint;
import com.ontop.challenge.infraestructure.endpoint.CreateWalletTransactionEndpoint;
import com.ontop.challenge.infraestructure.endpoint.GetBalanceEndpoint;
import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.entity.Wallet;
import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.model.request.PaymentRequest;
import com.ontop.challenge.model.request.TransactionUpdateRequest;
import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.model.response.CreateWalletTransactionRequest;
import com.ontop.challenge.model.response.PaymentProviderResponse;
import com.ontop.challenge.model.response.WalletTransactionResponse;
import com.ontop.challenge.repository.TransactionRepository;
import com.ontop.challenge.repository.WalletRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private GetBalanceEndpoint getBalanceEndpoint;
    @Mock
    private CreateWalletTransactionEndpoint createWalletTransactionEndpoint;
    @Mock
    private CreatePaymentEndpoint createPaymentEndpoint;
    @Mock
    private BankAccountServiceImpl bankAccountService;


    @InjectMocks
    private WalletServiceImpl walletService;




    @Test
    void processTransferTestFailed_InvalidAmount (){
        WalletTransactionRequest request = mockWalletTransactionZeroRequest();
        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        assertThrows(AmountLessOrEqualsThanZeroException.class, () -> {
            walletService.processTransfer(request);
        });
    }



    @Test
    void processTransferTestFailed_InvalidBalance (){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        when(getBalanceEndpoint.invoke(Mockito.anyString())).thenReturn(0.0);
        assertThrows(NotEnoughBalanceException.class, () -> {
            walletService.processTransfer(request);
        });
    }

    @Test
    void processTransferTestFailed_ExternalServerError (){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        when(getBalanceEndpoint.invoke(mockBankAccount().getUserId())).thenThrow(ExternalServerErrorException.class);
        assertThrows(ExternalServerErrorException.class, () -> {
            walletService.processTransfer(request);
        });
    }

    @Test
    void processTransferTestFailed_NoBankAccountFound(){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenThrow(NotInfoFoundException.class);
        assertThrows(NotInfoFoundException.class, () -> {
            walletService.processTransfer(request);
        });
    }

    @Test
    void processTransferTestFailed_ExternalServerErrorCreatingWalletTransaction(){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        when(getBalanceEndpoint.invoke(mockBankAccount().getUserId())).thenReturn(10000.0);
        when(createWalletTransactionEndpoint.invoke(Mockito.any())).thenThrow(ExternalServerErrorException.class);
        assertThrows(ExternalServerErrorException.class, () -> {
            walletService.processTransfer(request);
        });
    }

    @Test
    void processTransferTestFailed_ExternalServerErrorCreatingPayment(){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        CreateWalletTransactionRequest createWalletTransactionRequest = mockCreateWalletTransactionRequest(request);

        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        when(getBalanceEndpoint.invoke(mockBankAccount().getUserId())).thenReturn(10000.0);
        when(createWalletTransactionEndpoint.invoke(createWalletTransactionRequest)).thenReturn(mockWalletTransactionResponse());
        when(walletRepository.findByUserId(mockBankAccount().getUserId())).thenReturn(Optional.of(mockWalletFromBD()));
        when(createPaymentEndpoint.invoke(Mockito.any())).thenThrow(PaymentProviderApiException.class);

        assertThat(walletService.processTransfer(request).getStatus()).
                isEqualTo(ResponseCodes.ERROR_CONSUMING_EXTERNAL_API.getCode());
    }



    @Test
    void processTransferTest_WhenNotWalletFound(){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        CreateWalletTransactionRequest createWalletTransactionRequest = mockCreateWalletTransactionRequest(request);

        when(bankAccountService.getBankAccountByBankAccountId(request.getBankAccountId())).thenReturn(mockBankAccount());
        when(getBalanceEndpoint.invoke(mockBankAccount().getUserId())).thenReturn(10000.0);
        when(createWalletTransactionEndpoint.invoke(createWalletTransactionRequest)).thenReturn(mockWalletTransactionResponse());
        when(walletRepository.findByUserId(mockBankAccount().getUserId())).thenReturn(Optional.empty());

        assertThrows(NotInfoFoundException.class, () -> {
            walletService.processTransfer(request);
        });
    }

    @Test
    void processTransferTest_WhenSuccess(){
        WalletTransactionRequest request = mockWalletTransactionRequest();
        CreateWalletTransactionRequest createWalletTransactionRequest = mockCreateWalletTransactionRequest(request);

        doReturn(mockBankAccount()).when(bankAccountService).getBankAccountByBankAccountId(request.getBankAccountId());
        doReturn(10000.0).when(getBalanceEndpoint).invoke(mockBankAccount().getUserId());
        doReturn(mockWalletTransactionResponse()).when(createWalletTransactionEndpoint).invoke(createWalletTransactionRequest);
        doReturn(Optional.of(mockWalletFromBD())).when(walletRepository).findByUserId(mockBankAccount().getUserId());
        doReturn(mockPaymentProviderResponse()).when(createPaymentEndpoint).invoke(Mockito.any());

        assertThat(walletService.processTransfer(request).getStatus()).
                isEqualTo(ResponseCodes.SUCCESS.getCode());

    }

    @Test
    void updateTransactionTest_WhenSuccess(){
        TransactionUpdateRequest request = mockTransactionUpdateRequest();
        when(transactionRepository.findByProviderTransactionId(request.getProviderTransactionId()))
                .thenReturn(Optional.of(mockTransactionFromBD()));
        assertThat(walletService.updateTransferProcess(request).getStatus()).
                isEqualTo(ResponseCodes.SUCCESS.getCode());

    }

    @Test
    void updateTransactionTest_WhenNotInfoFound(){
        TransactionUpdateRequest request = mockTransactionUpdateRequest();
        when(transactionRepository.findByProviderTransactionId(request.getProviderTransactionId()))
                .thenReturn(Optional.empty());
        assertThrows(NotInfoFoundException.class, () -> {
            walletService.updateTransferProcess(request);
        });
    }

    @Test
    void updateTransactionTest_WhenProviderRespondFailed(){
        TransactionUpdateRequest request = mockTransactionUpdateRequest();
        request.setStatus("Transaction Failed");
        when(transactionRepository.findByProviderTransactionId(request.getProviderTransactionId()))
                .thenReturn(Optional.of(mockTransactionFromBD()));
        assertThat(walletService.updateTransferProcess(request).getStatus()).
                isEqualTo(ResponseCodes.SUCCESS.getCode());
    }

    private static PaymentProviderResponse mockPaymentProviderResponse(){
        PaymentProviderResponse paymentProviderResponse = new PaymentProviderResponse();
        PaymentProviderResponse.PaymentInfo paymentInfo = new PaymentProviderResponse.PaymentInfo();
        paymentInfo.setId("123456789");
        PaymentProviderResponse.RequestInfo requestInfo = new PaymentProviderResponse.RequestInfo();
        requestInfo.setStatus("SUCCESS");
        paymentProviderResponse.setPaymentInfo(paymentInfo);
        paymentProviderResponse.setRequestInfo(requestInfo);
        return paymentProviderResponse;
    }

    private static CreateWalletTransactionRequest mockCreateWalletTransactionRequest(WalletTransactionRequest request) {
        CreateWalletTransactionRequest createWalletTransactionRequest = new CreateWalletTransactionRequest(request.getBankAccountId(), request.getAmount());
        createWalletTransactionRequest.setUserId(request.getBankAccountId());
        createWalletTransactionRequest.setAmount(request.getAmount());
        return createWalletTransactionRequest;
    }

    private static Wallet mockWalletFromBD() {
        Wallet wallet = new Wallet();
        wallet.setId(new ObjectId());
        wallet.setBalance(100.0);
        wallet.setUserId("123456789");
        wallet.setCurrency("USD");
        return wallet;
    }

    private static Transaction mockTransactionFromBD() {
        Transaction transaction = new Transaction();
        transaction.setId(new ObjectId());
        transaction.setAmount(100.0);
        transaction.setFee(0.1);
        return transaction;
    }

    private static WalletTransactionRequest mockWalletTransactionRequest() {
        return new WalletTransactionRequest(
                "123456789",
                100,
                0.1
        );
    }

    private static TransactionUpdateRequest mockTransactionUpdateRequest() {
        TransactionUpdateRequest request = new TransactionUpdateRequest();
        request.setStatus("SUCCESS");
        request.setProviderTransactionId("123456789");
        return request;
    }

    private static WalletTransactionRequest mockWalletTransactionZeroRequest() {
        return new WalletTransactionRequest(
                "123456789",
                0.0,
                0.1
        );
    }

    private static WalletTransactionResponse mockWalletTransactionResponse(){
        WalletTransactionResponse walletTransactionResponse = new WalletTransactionResponse();
        walletTransactionResponse.setWalletTransactionId("123456789");
        walletTransactionResponse.setUserId("123456789");
        walletTransactionResponse.setAmount("100");
        return walletTransactionResponse;
    }


    private static BankAccount mockBankAccount(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId("123456789");
        bankAccount.setAccountNumber("123456789");
        bankAccount.setUserId("123456789");
        bankAccount.setFirstName("MockName");
        bankAccount.setLastName("MockLastName");
        bankAccount.setRoutingNumber("1234567");
        bankAccount.setNationalId("per-1234");
        bankAccount.setCurrency("USD");
        bankAccount.setBankAccountId("123456789");
        return bankAccount;
    }

    private static PaymentRequest buildPaymentRequest(WalletTransactionRequest request, BankAccount bankAccount) {
        PaymentRequest paymentRequest =new PaymentRequest();
        PaymentRequest.PaymentSource paymentSource = new PaymentRequest.PaymentSource();
        PaymentRequest.SourceInfo sourceInfo = new PaymentRequest.SourceInfo();
        PaymentRequest.Account accountSource = new PaymentRequest.Account();
        accountSource.setAccountNumber("SOURCE_ACCOUNT");
        accountSource.setRoutingNumber("SOURCE_ROUTING");
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

}
