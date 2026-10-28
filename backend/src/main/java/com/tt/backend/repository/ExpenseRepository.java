package com.tt.backend.repository;

import com.tt.backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository gives save, findAll, findById, deleteById... for free
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Spring builds the SQL from the method name:
    // select * from expenses where category = ?
    List<Expense> findByCategory(String category);
}
