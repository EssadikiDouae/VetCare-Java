package org.example.vetcare360.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.vetcare360.models.Animal;
import org.example.vetcare360.models.Visite;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisiteFormController {

    @FXML private Label petNameLabel;
    @FXML private DatePicker visitDatePicker;
    @FXML private TextField descriptionField;
    @FXML private TableView<Visite> visitsTable;
    @FXML private TableColumn<Visite, String> visitDateColumn;
    @FXML private TableColumn<Visite, String> visitDescriptionColumn;
    @FXML private TableColumn<Visite, Void> deleteColumn;

    private Animal animal;
    private OwnerDetailsController ownerDetailsController;


    private static final Logger LOGGER = Logger.getLogger(VisiteFormController.class.getName());

    @FXML
    public void initialize() {
        try {
            if (visitDateColumn != null && visitDescriptionColumn != null) {
                visitDateColumn.setCellValueFactory(cellData ->
                        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString())
                );
                visitDescriptionColumn.setCellValueFactory(cellData ->
                        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription())
                );
            } else {
                LOGGER.warning("One of the columns is not bound in FXML!");
            }

            if (deleteColumn != null) {
                addDeleteButtonToTable();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during initialization", e);
        }
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Visite visite = getTableView().getItems().get(getIndex());
                    if (visite != null) {
                        try {
                            VisiteController.deleteVisite(visite.getId());
                            getTableView().getItems().remove(visite);
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Failed to delete visit:", e);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
        if (petNameLabel != null && animal != null) {
            petNameLabel.setText(animal.getName());
        }
        loadVisits();
    }

    public void setOwnerDetailsController(OwnerDetailsController controller) {
        this.ownerDetailsController = controller;
    }

    private void loadVisits() {
        if (visitsTable == null) {
            LOGGER.severe("visitsTable is null!");
            return;
        }

        try {
            List<Visite> visits = VisiteController.getVisitsByAnimalId(animal.getId());
            visitsTable.setItems(FXCollections.observableArrayList(visits));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load visits:", e);
        }
    }

    @FXML
    private void handleAddVisit() {
        try {
            LocalDate visitDate = visitDatePicker.getValue();
            String description = descriptionField.getText();

            if (visitDate == null || description == null || description.isEmpty() || animal == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.!");
                alert.showAndWait();
                return;
            }

            int ownerId = ownerDetailsController != null ? ownerDetailsController.getOwnerId() : -1;

            if (ownerId == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Owner not found.");
                alert.showAndWait();
                return;
            }

            Visite visite = new Visite(0, visitDate, description, animal.getId(), ownerId);
            VisiteController.addVisite(visite);
            loadVisits();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The visit was added successfully!");
            alert.showAndWait();

            if (ownerDetailsController != null) {
                ownerDetailsController.reloadPets();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding visit");
        }
    }
}
