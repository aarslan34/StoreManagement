package com.arslan.store.dao;

import com.arslan.store.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryDao extends JpaRepository<Category, Integer> {
    List<Category> getAllCategory();
}
