package com.tt.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Number of days is required")
    @Min(value = 1, message = "Days must be greater than 0")
    private Integer days;

    @NotNull(message = "Number of travellers is required")
    @Min(value = 1, message = "Travellers must be greater than 0")
    private Integer travellers;

    @NotNull(message = "Travel expense is required")
    @PositiveOrZero(message = "Expenses cannot be negative")
    private Double travelCost;

    @NotNull(message = "Accommodation expense is required")
    @PositiveOrZero(message = "Expenses cannot be negative")
    private Double accommodationCost;

    @NotNull(message = "Food expense is required")
    @PositiveOrZero(message = "Expenses cannot be negative")
    private Double foodCost;

    // Calculated by the backend (service) before saving
    private Double totalCost;
    private Double costPerTraveller;

    public Trip() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public Integer getTravellers() { return travellers; }
    public void setTravellers(Integer travellers) { this.travellers = travellers; }

    public Double getTravelCost() { return travelCost; }
    public void setTravelCost(Double travelCost) { this.travelCost = travelCost; }

    public Double getAccommodationCost() { return accommodationCost; }
    public void setAccommodationCost(Double accommodationCost) { this.accommodationCost = accommodationCost; }

    public Double getFoodCost() { return foodCost; }
    public void setFoodCost(Double foodCost) { this.foodCost = foodCost; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public Double getCostPerTraveller() { return costPerTraveller; }
    public void setCostPerTraveller(Double costPerTraveller) { this.costPerTraveller = costPerTraveller; }
}
