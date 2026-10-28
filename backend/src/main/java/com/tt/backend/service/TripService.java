package com.tt.backend.service;

import com.tt.backend.model.Trip;
import com.tt.backend.repository.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TripService {

    private final TripRepository repository;

    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public List<Trip> getAll() {
        return repository.findAll();
    }

    public Trip getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found: " + id));
    }

    // Total = travel + accommodation + food ; per traveller = total / travellers
    private void calculate(Trip trip) {
        double total = trip.getTravelCost() + trip.getAccommodationCost() + trip.getFoodCost();
        trip.setTotalCost(total);
        trip.setCostPerTraveller(total / trip.getTravellers());
    }

    public Trip add(Trip trip) {
        calculate(trip);
        return repository.save(trip);
    }

    public Trip update(Long id, Trip newData) {
        Trip trip = getById(id);
        trip.setDestination(newData.getDestination());
        trip.setDays(newData.getDays());
        trip.setTravellers(newData.getTravellers());
        trip.setTravelCost(newData.getTravelCost());
        trip.setAccommodationCost(newData.getAccommodationCost());
        trip.setFoodCost(newData.getFoodCost());
        calculate(trip);
        return repository.save(trip);
    }

    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }
}
