package org.example.vetcare360.controllers;

import org.example.vetcare360.database.database;
import org.example.vetcare360.models.Visite;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisiteController {


    public static void addVisite(Visite visite) {
        String sql = "INSERT INTO visites (date_visite, description, animal_id, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(visite.getDate()));
            stmt.setString(2, visite.getDescription());
            stmt.setInt(3, visite.getAnimalId());
            stmt.setInt(4, visite.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Visite> getVisitsByAnimalId(int animalId) {
        List<Visite> visits = new ArrayList<>();
        String sql = "SELECT * FROM visites WHERE animal_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, animalId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                LocalDate visitDate = rs.getDate("date_visite").toLocalDate();

                visits.add(new Visite(
                        rs.getInt("id"),
                        visitDate,
                        rs.getString("description"),
                        rs.getInt("animal_id"),
                        rs.getInt("owner_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visits;
    }


    public static void deleteVisite(int visiteId) {
        String sql = "DELETE FROM visites WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, visiteId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
