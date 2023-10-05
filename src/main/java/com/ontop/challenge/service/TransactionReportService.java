package com.ontop.challenge.service;

import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;

import java.util.Date;
import java.util.List;

public interface TransactionReportService {

    BasicResponse<List<TransactionReportResponse>> fetchTransactionReport(String bankAccountId,
                                                                          Double amount,
                                                                         Date startDate,
                                                                            Date endDate,
                                                                          int page,
                                                                          int size);

}
