package com.tt.backend.controller;

import com.tt.backend.model.Trip;
import com.tt.backend.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService service;

    public TripController(TripService service) {
        this.service = service;
    }

    @GetMapping
    public List<Trip> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Trip getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip add(@Valid @RequestBody Trip trip) {
        return service.add(trip);
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable Long id, @Valid @RequestBody Trip trip) {
        return service.update(id, trip);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
