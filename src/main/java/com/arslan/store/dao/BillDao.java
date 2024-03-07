package com.arslan.store.dao;

import com.arslan.store.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDao extends JpaRepository<Bill, Integer> {
    List<Bill> getAllBills();
}
