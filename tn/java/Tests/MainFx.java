package tn.java.Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;


public class MainFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(new File("tn/java/resources/AjoutEvent.fxml").toURI().toURL());
        Scene scene = new Scene(parent); // Initialiser la scène avec les dimensions souhaitées
        stage.setScene(scene);
        stage.setTitle("Gestion des évènements");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Démarre l'application JavaFX
    }
}

