package org.example.vetcare360.controllers;

import org.example.vetcare360.database.database;
import org.example.vetcare360.models.Proprietaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietaireController {

    public static int addOwner(Proprietaire owner) {
        String sql = "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, owner.getFirstName());
            stmt.setString(2, owner.getLastName());
            stmt.setString(3, owner.getAddress());
            stmt.setString(4, owner.getCity());
            stmt.setString(5, owner.getTelephone());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in addOwner: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }


    public static List<Proprietaire> getAllOwners() {
        List<Proprietaire> owners = new ArrayList<>();
        String sql = "SELECT * FROM owners";
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                owners.add(new Proprietaire(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("telephone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllOwners: " + e.getMessage());
            e.printStackTrace();
        }
        return owners;
    }


    public static List<Proprietaire> searchByLastName(String lastName) {
        List<Proprietaire> owners = new ArrayList<>();
        String sql = "SELECT * FROM owners WHERE last_name LIKE ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + lastName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    owners.add(new Proprietaire(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("telephone")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in searchByLastName: " + e.getMessage());
            e.printStackTrace();
        }
        return owners;
    }


    public static void updateOwner(Proprietaire owner) {
        String sql = "UPDATE owners SET first_name=?, last_name=?, city=?, address=?, telephone=? WHERE id=?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, owner.getFirstName());
            stmt.setString(2, owner.getLastName());
            stmt.setString(3, owner.getCity());
            stmt.setString(4, owner.getAddress());
            stmt.setString(5, owner.getTelephone());
            stmt.setInt(6, owner.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error in updateOwner: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static Proprietaire getOwnerById(int id) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM owners WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Proprietaire(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getOwnerById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteOwnerAndRelatedData(int ownerId) {
        String deleteVisitsSql = "DELETE FROM visites WHERE animal_id IN (SELECT id FROM animals WHERE owner_id = ?)";
        String deleteAnimalsSql = "DELETE FROM animals WHERE owner_id = ?";
        String deleteOwnerSql = "DELETE FROM owners WHERE id = ?";

        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            try (
                    PreparedStatement deleteVisitsStmt = conn.prepareStatement(deleteVisitsSql);
                    PreparedStatement deleteAnimalsStmt = conn.prepareStatement(deleteAnimalsSql);
                    PreparedStatement deleteOwnerStmt = conn.prepareStatement(deleteOwnerSql)
            ) {
                deleteVisitsStmt.setInt(1, ownerId);
                deleteVisitsStmt.executeUpdate();

                deleteAnimalsStmt.setInt(1, ownerId);
                deleteAnimalsStmt.executeUpdate();

                deleteOwnerStmt.setInt(1, ownerId);
                deleteOwnerStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erreur lors de la suppression du propriétaire et ses données: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


