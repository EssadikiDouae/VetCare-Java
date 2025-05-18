package org.example.vetcare360.models;

public class OwnerWithPets {
    private final int id;
    private final String name;
    private final String address;
    private final String city;
    private final String telephone;
    private final String pets;

    public OwnerWithPets(int id, String name, String address, String city, String telephone, String pets) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.pets = pets;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPets() {
        return pets;
    }
}
