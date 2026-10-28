package com.tt.backend.controller;

import com.tt.backend.model.Batch;
import com.tt.backend.model.BatchStudent;
import com.tt.backend.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BatchController {

    private final BatchService service;

    public BatchController(BatchService service) {
        this.service = service;
    }

    @GetMapping("/batches")
    public List<Batch> getBatches() {
        return service.getBatches();
    }

    @PostMapping("/batches")
    @ResponseStatus(HttpStatus.CREATED)
    public Batch addBatch(@Valid @RequestBody Batch batch) {
        return service.addBatch(batch);
    }

    @DeleteMapping("/batches/{id}")
    public void deleteBatch(@PathVariable Long id) {
        service.deleteBatch(id);
    }

    // GET /api/students            -> all students (with their batch)
    // GET /api/students?rollNo=24  -> search by roll number
    @GetMapping("/students")
    public List<BatchStudent> getStudents(@RequestParam(required = false) String rollNo) {
        return service.getStudents(rollNo);
    }

    // POST /api/students { "name": "...", "rollNo": "...", "batch": { "id": 1 } }
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public BatchStudent addStudent(@Valid @RequestBody BatchStudent student) {
        return service.addStudent(student);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }
}
