package org.example.vetcare360.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.vetcare360.models.Veterinarian;
import org.example.vetcare360.database.database;

import java.io.IOException;
import java.sql.*;

public class VeterinarianTableController {

    @FXML
    private VBox rootVBox;

    @FXML
    private TableView<Veterinarian> veterinarianTable;

    @FXML
    private TableColumn<Veterinarian, Integer> idColumn;

    @FXML
    private TableColumn<Veterinarian, String> firstNameColumn;

    @FXML
    private TableColumn<Veterinarian, String> lastNameColumn;

    @FXML
    private TableColumn<Veterinarian, String> specialtiesColumn;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField specialtiesField;


    public void loadVeterinarians() {
        String query = "SELECT * FROM veterinarians";
        veterinarianTable.getItems().clear();

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String specialties = resultSet.getString("specialties");

                Veterinarian veterinarian = new Veterinarian(id, firstName, lastName, specialties);
                veterinarianTable.getItems().add(veterinarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        rootVBox.getStylesheets().add(getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm());
        rootVBox.getStyleClass().add("vet-background");


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        specialtiesColumn.setCellValueFactory(new PropertyValueFactory<>("specialties"));


        veterinarianTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        veterinarianTable.getColumns().removeIf(column -> column.getText() == null || column.getText().isEmpty());

        loadVeterinarians();
    }


    public void handleAddVeterinarian(ActionEvent actionEvent) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String specialties = specialtiesField.getText();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !specialties.isEmpty()) {
            try (Connection connection = database.getConnection()) {
                String query = "INSERT INTO veterinarians (first_name, last_name, specialties) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, specialties);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        loadVeterinarians();
                        firstNameField.clear();
                        lastNameField.clear();
                        specialtiesField.clear();
                    } else {
                        System.out.println("Veterinarian not added.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please fill in all fields.");
        }
    }


    @FXML
    private void showVeterinarianForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/Veterinarians.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Veterinarian");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
