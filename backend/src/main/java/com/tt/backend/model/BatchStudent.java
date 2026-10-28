package com.tt.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Many students belong to one batch (ManyToOne relationship)
@Entity
@Table(name = "batch_students")
public class BatchStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Roll number is required")
    private String rollNo;

    @NotNull(message = "Please select a batch")
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    public BatchStudent() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
}
