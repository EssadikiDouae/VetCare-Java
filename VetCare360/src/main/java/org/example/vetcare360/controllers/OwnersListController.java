package org.example.vetcare360.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.vetcare360.database.database;
import org.example.vetcare360.models.Proprietaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OwnersListController {

    @FXML
    private TableView<Proprietaire> ownersTable;

    @FXML
    private TableColumn<Proprietaire, String> firstNameColumn;

    @FXML
    private TableColumn<Proprietaire, String> lastNameColumn;

    @FXML
    private TableColumn<Proprietaire, String> cityColumn;

    @FXML
    private TableColumn<Proprietaire, String> addressColumn;

    @FXML
    private TableColumn<Proprietaire, String> telephoneColumn;

    @FXML
    private TableColumn<Proprietaire, Integer> idColumn;

    private final ObservableList<Proprietaire> ownerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        ownersTable.setItems(ownerList);
    }

    public void loadOwners(String lastName) {
        ownerList.clear();
        String query = "SELECT * FROM owners WHERE last_name LIKE ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + lastName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proprietaire p = new Proprietaire(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("city"),
                        rs.getString("address"),
                        rs.getString("telephone")
                );
                ownerList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenOwnersList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnersList.fxml"));
            Parent root = loader.load();

            String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            Stage stage = new Stage();
            stage.setTitle("Owners List");

            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
