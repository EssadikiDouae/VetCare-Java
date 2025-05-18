package org.example.vetcare360.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.vetcare360.models.Animal;
import org.example.vetcare360.models.OwnerWithPets;
import org.example.vetcare360.models.Proprietaire;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OwnersAnimalsListController {

    @FXML private VBox rootVBox;

    @FXML private TableView<OwnerWithPets> ownersAnimalsTable;
    @FXML private TableColumn<OwnerWithPets, String> nameColumn;
    @FXML private TableColumn<OwnerWithPets, String> addressColumn;
    @FXML private TableColumn<OwnerWithPets, String> cityColumn;
    @FXML private TableColumn<OwnerWithPets, String> telephoneColumn;
    @FXML private TableColumn<OwnerWithPets, String> petsColumn;
    @FXML private TableColumn<OwnerWithPets, Void> actionColumn;

    @FXML
    public void initialize() {

        rootVBox.getStylesheets().add(getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm());
        rootVBox.getStyleClass().add("backgroundpet");

        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        addressColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAddress()));
        cityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCity()));
        telephoneColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelephone()));
        petsColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPets()));

        ownersAnimalsTable.getColumns().removeIf(col -> col.getText() == null || col.getText().trim().isEmpty());

        nameColumn.setCellFactory(tc -> new TableCell<OwnerWithPets, String>() {
            private final Hyperlink link = new Hyperlink();
            {
                link.setOnAction(e -> {
                    OwnerWithPets ownerWithPets = getTableView().getItems().get(getIndex());
                    handleOwnerDetails(ownerWithPets);
                });
            }

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setGraphic(null);
                } else {
                    link.setText(name);
                    setGraphic(link);
                }
            }
        });

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(event -> {
                    OwnerWithPets owner = getTableView().getItems().get(getIndex());
                    ProprietaireController.deleteOwnerAndRelatedData(owner.getId());
                    reloadOwners();
                });
                deleteBtn.getStyleClass().add("delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        actionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(null));

        loadOwnersWithPets();
    }

    public void reloadOwners() {
        loadOwnersWithPets();
    }

    private void loadOwnersWithPets() {
        List<Proprietaire> owners = ProprietaireController.getAllOwners();
        List<OwnerWithPets> data = new ArrayList<>();

        for (Proprietaire owner : owners) {
            List<Animal> animals = AnimalController.getAnimalsByOwnerId(owner.getId());
            String pets = animals.stream().map(Animal::getName).collect(Collectors.joining(", "));

            data.add(new OwnerWithPets(
                    owner.getId(),
                    owner.getFirstName() + " " + owner.getLastName(),
                    owner.getAddress(),
                    owner.getCity(),
                    owner.getTelephone(),
                    pets
            ));
        }

        System.out.println("Columns in table:");
        for (TableColumn<?, ?> col : ownersAnimalsTable.getColumns()) {
            System.out.println("-> " + col.getText());
        }
        ownersAnimalsTable.getColumns().removeIf(col -> {
            String text = col.getText();
            return text == null || text.trim().isEmpty();
        });


        ownersAnimalsTable.setItems(FXCollections.observableArrayList(data));
    }

    private void handleOwnerDetails(OwnerWithPets ownerWithPets) {
        try {
            Proprietaire owner = ProprietaireController.getOwnerById(ownerWithPets.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnerDetails.fxml"));
            Parent root = loader.load();
            OwnerDetailsController controller = loader.getController();
            controller.setOwner(owner);

            Stage stage = new Stage();
            stage.setTitle("Owner Details");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> reloadOwners());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddOwner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnerForm.fxml"));
            Parent root = loader.load();
            OwnerFormController controller = loader.getController();
            controller.setOwnersAnimalsListController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Owner");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> reloadOwners());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
}
