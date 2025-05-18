package org.example.vetcare360.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class OwnersSearchController {

    @FXML
    private TextField lastNameField;

    @FXML
    private VBox rootVBox;


    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            try {

                String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
                rootVBox.getStylesheets().add(cssPath);
            } catch (Exception e) {
                System.err.println("CSS could not be loaded: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleFindOwner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnersList.fxml"));
            Parent root = loader.load();

            OwnersListController controller = loader.getController();
            String lastName = lastNameField.getText().trim();
            controller.loadOwners(lastName);

            Stage stage = new Stage();
            stage.setTitle("Owners List");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) lastNameField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddOwner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnerForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Owner");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
