package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.BillRequest;
import com.codegym.kfcbackend.dto.request.SummaryReportRequest;
import com.codegym.kfcbackend.dto.response.SummaryReportResponse;
import com.codegym.kfcbackend.entity.Bill;

import java.time.LocalDate;
import java.util.List;

public interface IBillService {
    Bill createBill(BillRequest request);
    List<Bill> getAllBills();
    SummaryReportResponse getBillsBetween(SummaryReportRequest request);
    void cancelBill(Long id);
}
