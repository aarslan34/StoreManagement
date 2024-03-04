package com.arslan.store.dao;

import com.arslan.store.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDao extends JpaRepository<Bill, Integer> {
}
