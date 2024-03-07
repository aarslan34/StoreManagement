package com.arslan.store.service;

import com.arslan.store.model.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);
}
