package tn.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.java.entities.Event;
import tn.java.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AfficheEvent {

    @FXML
    private TextField SearchField;

    @FXML
    private Button btnRecherche;

    @FXML
    private ListView<String> listViewEvents; // ListView pour afficher les événements

    @FXML
    void initialize() {
        // Appel de la méthode pour afficher les événements lors de l'initialisation
        afficherEvenements();
    }

    @FXML
    void Recherche(ActionEvent event) {
        // Vous pouvez ajouter ici la logique de recherche si vous souhaitez
    }

    public ObservableList<Event> getEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT * FROM event";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement sp = con.prepareStatement(query);
            ResultSet rs = sp.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
                event.setImage(rs.getString("image"));
                event.setEmplacement(rs.getString("emplacement"));
                event.setNombrePlaces(rs.getInt("nombre_places"));
                event.setDate(rs.getDate("date").toLocalDate());
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void afficherEvenements() {
        ObservableList<Event> events = getEvents(); // Récupère la liste des événements

        // Crée une ObservableList pour les noms des événements à afficher dans la ListView
        ObservableList<String> eventNames = FXCollections.observableArrayList();

        // Ajoute les noms des événements dans la liste
        for (Event event : events) {
            eventNames.add(event.getNom()); // On peut afficher d'autres informations si nécessaire
        }

        // Met à jour la ListView avec les noms des événements
        listViewEvents.setItems(eventNames);
    }
}

