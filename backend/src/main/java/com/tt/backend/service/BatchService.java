package com.tt.backend.service;

import com.tt.backend.model.Batch;
import com.tt.backend.model.BatchStudent;
import com.tt.backend.repository.BatchRepository;
import com.tt.backend.repository.BatchStudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final BatchStudentRepository studentRepository;

    public BatchService(BatchRepository batchRepository, BatchStudentRepository studentRepository) {
        this.batchRepository = batchRepository;
        this.studentRepository = studentRepository;
    }

    // ----- batches -----

    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    public Batch addBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch not found: " + id));
        studentRepository.deleteAll(studentRepository.findByBatchId(id)); // remove its students first
        batchRepository.delete(batch);
    }

    // ----- students -----

    // rollNo given -> search; otherwise all students
    public List<BatchStudent> getStudents(String rollNo) {
        if (rollNo == null || rollNo.isBlank()) {
            return studentRepository.findAll();
        }
        return studentRepository.findByRollNoContainingIgnoreCase(rollNo);
    }

    public BatchStudent addStudent(BatchStudent student) {
        Long batchId = student.getBatch().getId();
        Batch batch = batchRepository.findById(batchId == null ? -1L : batchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select a valid batch"));
        if (studentRepository.existsByRollNoAndBatchId(student.getRollNo(), batch.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Roll number " + student.getRollNo() + " already exists in " + batch.getBatchName());
        }
        student.setBatch(batch);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + id);
        }
        studentRepository.deleteById(id);
    }
}
