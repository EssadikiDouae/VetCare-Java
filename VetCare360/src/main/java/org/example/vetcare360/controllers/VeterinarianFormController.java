package org.example.vetcare360.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.example.vetcare360.models.Veterinarian;
import org.example.vetcare360.database.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VeterinarianFormController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField specialtiesField;

    @FXML
    private void handleAddVeterinarian() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String specialties = specialtiesField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || specialties.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields.");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO veterinarians (first_name, last_name, specialties) VALUES (?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, specialties);
            statement.executeUpdate();


            firstNameField.getScene().getWindow().hide();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
