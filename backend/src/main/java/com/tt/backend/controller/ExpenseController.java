package com.tt.backend.controller;

import com.tt.backend.model.Expense;
import com.tt.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller = REST API endpoints that React calls
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*") // allow the React app (any port) to call this API
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    // GET /api/expenses              -> all
    // GET /api/expenses?category=Food -> filter by category
    @GetMapping
    public List<Expense> getAll(@RequestParam(required = false) String category) {
        return service.getAll(category);
    }

    // GET /api/expenses/total -> total amount spent
    @GetMapping("/total")
    public double getTotal() {
        return service.getTotal();
    }

    @GetMapping("/{id}")
    public Expense getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // @Valid = check the rules written in Expense.java before saving
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense add(@Valid @RequestBody Expense expense) {
        return service.add(expense);
    }

    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        return service.update(id, expense);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}


