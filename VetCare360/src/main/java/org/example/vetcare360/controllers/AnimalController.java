package org.example.vetcare360.controllers;

import org.example.vetcare360.database.database;
import org.example.vetcare360.models.Animal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalController {


    public static void addAnimal(Animal animal) {
        String sql = "INSERT INTO animals (name, birth_date, type, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getBirthDate());
            stmt.setString(3, animal.getType());
            stmt.setInt(4, animal.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Animal> getAnimalsByOwnerId(int ownerId) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals WHERE owner_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                animals.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("birth_date"),
                        rs.getString("type"),
                        rs.getInt("owner_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }


    public static void updateAnimal(Animal animal) {
        String sql = "UPDATE animals SET name = ?, birth_date = ?, type = ? WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getBirthDate());
            stmt.setString(3, animal.getType());
            stmt.setInt(4, animal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAnimal(int animalId) {
        String sql = "DELETE FROM animals WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, animalId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}