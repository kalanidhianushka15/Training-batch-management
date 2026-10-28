package com.tt.backend.service;

import com.tt.backend.model.Expense;
import com.tt.backend.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Service = business logic, sits between controller and repository
@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    // All expenses, or only one category if a category is given
    public List<Expense> getAll(String category) {
        if (category == null || category.isBlank() || category.equals("All")) {
            return repository.findAll();
        }
        return repository.findByCategory(category);
    }

    public Expense getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found: " + id));
    }

    public Expense add(Expense expense) {
        return repository.save(expense);
    }

    public Expense update(Long id, Expense newData) {
        Expense expense = getById(id);
        expense.setTitle(newData.getTitle());
        expense.setAmount(newData.getAmount());
        expense.setCategory(newData.getCategory());
        expense.setExpenseDate(newData.getExpenseDate());
        return repository.save(expense);
    }

    public void delete(Long id) {
        getById(id); // throws 404 if it doesn't exist
        repository.deleteById(id);
    }

    // Total amount spent across ALL saved expenses
    public double getTotal() {
        double total = 0;
        for (Expense e : repository.findAll()) {
            total += e.getAmount();
        }
        return total;
    }
}
