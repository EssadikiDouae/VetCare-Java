package org.example.vetcare360.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public HelloApplication() {

    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/Home.fxml"));
            VBox root = loader.load();
            Scene scene = new Scene(root, 560, 320);
            stage.setTitle("VetCare360");
            stage.setScene(scene);


            stage.setMaximized(true);



            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
