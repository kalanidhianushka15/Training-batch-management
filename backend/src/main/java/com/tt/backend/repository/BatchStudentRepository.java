package com.tt.backend.repository;

import com.tt.backend.model.BatchStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchStudentRepository extends JpaRepository<BatchStudent, Long> {

    List<BatchStudent> findByBatchId(Long batchId);

    // Same roll number in the SAME batch?
    boolean existsByRollNoAndBatchId(String rollNo, Long batchId);

    List<BatchStudent> findByRollNoContainingIgnoreCase(String rollNo);
}
