package org.example.vetcare360.controllers;

import org.example.vetcare360.database.database;
import org.example.vetcare360.models.Veterinarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VeterinarianController {

    public static void addVeterinarian(Veterinarian veterinarian) {
        String query = "INSERT INTO veterinarians (first_name, last_name, specialties) VALUES (?, ?, ?)";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, veterinarian.getFirstName());
            statement.setString(2, veterinarian.getLastName());
            statement.setString(3, veterinarian.getSpecialties());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Veterinarian added successfully");
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while adding the veterinarian.: " + e.getMessage());
        }
    }
}
