package com.ontop.challenge.service;

import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.request.TransactionUpdateRequest;
import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.model.response.BasicResponse;

public interface WalletService {

    BasicResponse<Transaction> processTransfer(WalletTransactionRequest request);

    BasicResponse<Transaction> updateTransferProcess(TransactionUpdateRequest request);

}
