package org.example.vetcare360.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.vetcare360.models.Proprietaire;

public class OwnerFormController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField telephoneField;
    @FXML
    private Button saveButton;

    private Proprietaire ownerToEdit = null;
    private OwnersAnimalsListController ownersAnimalsListController;

    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            String css = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            if (firstNameField.getScene() != null) {
                firstNameField.getScene().getStylesheets().add(css);
            }
        });
    }


    public void setOwnersAnimalsListController(OwnersAnimalsListController controller) {
        this.ownersAnimalsListController = controller;
    }

    public void setOwnerToEdit(Proprietaire owner) {
        this.ownerToEdit = owner;
        firstNameField.setText(owner.getFirstName());
        lastNameField.setText(owner.getLastName());
        addressField.setText(owner.getAddress());
        cityField.setText(owner.getCity());
        telephoneField.setText(owner.getTelephone());
        saveButton.setText("Update");
    }

    @FXML
    private void handleSaveOwner() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String telephone = telephoneField.getText();


        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || city.isEmpty() || telephone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields!");
            alert.showAndWait();
            return;
        }


        if (!telephone.matches("\\d{10}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid phone number! Must contain 10 digits");
            alert.showAndWait();
            return;
        }

        if (ownerToEdit == null) {
            Proprietaire owner = new Proprietaire(0, firstName, lastName, address, city, telephone);
            ProprietaireController.addOwner(owner);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Owner added successfully!");
            alert.showAndWait();
        } else {
            ownerToEdit.setFirstName(firstName);
            ownerToEdit.setLastName(lastName);
            ownerToEdit.setAddress(address);
            ownerToEdit.setCity(city);
            ownerToEdit.setTelephone(telephone);
            ProprietaireController.updateOwner(ownerToEdit);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Owner data updated successfully!");
            alert.showAndWait();
        }


        if (ownersAnimalsListController != null) {
            ownersAnimalsListController.reloadOwners();
        }


        ((Stage) firstNameField.getScene().getWindow()).close();
    }
}
