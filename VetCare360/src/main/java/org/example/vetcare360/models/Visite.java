package org.example.vetcare360.models;

import java.time.LocalDate;

public class Visite {
    private int id;
    private LocalDate date;  // تغيير النوع إلى LocalDate
    private String description;
    private int animalId;
    private int ownerId;

    // Constructor with LocalDate for date
    public Visite(int id, LocalDate date, String description, int animalId, int ownerId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.animalId = animalId;
        this.ownerId = ownerId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
