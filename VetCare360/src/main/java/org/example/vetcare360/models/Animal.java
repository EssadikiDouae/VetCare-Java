package org.example.vetcare360.models;

public class Animal {
    private int id;
    private String name;
    private String birthDate;
    private String type;
    private int ownerId;

    public Animal(int id, String name, String birthDate, String type, int ownerId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.ownerId = ownerId;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBirthDate() { return birthDate; }
    public String getType() { return type; }
    public int getOwnerId() { return ownerId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setType(String type) { this.type = type; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}