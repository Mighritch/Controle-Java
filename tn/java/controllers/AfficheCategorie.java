package tn.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.java.entities.Categorie;
import tn.java.entities.Event;
import tn.java.services.ServiceCategorie;
import tn.java.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AfficheCategorie {

    @FXML
    private TextField SearchField;

    @FXML
    private Button btnRecherche;

    @FXML
    private ListView<String> listeview; // Utiliser une ListView pour afficher les détails des catégories

    @FXML
    private ListView<String> eventListView; // Ajout de la ListView pour afficher les événements

    private ServiceCategorie serviceCategorie = new ServiceCategorie();

    @FXML
    void initialize() {
        // Appeler la méthode pour afficher toutes les catégories lors de l'initialisation
        afficherCategories();

        // Ajouter un listener pour effectuer une recherche lorsque le texte dans SearchField change
        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                afficherCategories();
            } else {
                afficherCategoriesParNom(newValue.trim());
            }
        });

        listeview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedCategoryId = getSelectedCategoryId(newValue);
                afficherEvenementsParCategorie(selectedCategoryId);
            }
        });
    }

    @FXML
    void Recherche(ActionEvent event) {
        String searchTerm = SearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            afficherCategories();
        } else {
            afficherCategoriesParNom(searchTerm);
        }
    }

    public ObservableList<Categorie> getCategories() {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setNom(rs.getString("nom"));
                categories.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ObservableList<Categorie> getCategoriesByName(String name) {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie WHERE nom LIKE ?";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setNom(rs.getString("nom"));
                categories.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void afficherCategories() {
        ObservableList<Categorie> categories = getCategories();
        ObservableList<String> categoryDetails = FXCollections.observableArrayList();

        for (Categorie categorie : categories) {
            String details = "Nom: " + categorie.getNom();
            categoryDetails.add(details);
        }

        listeview.setItems(categoryDetails);
    }

    public void afficherCategoriesParNom(String nom) {
        ObservableList<Categorie> categories = getCategoriesByName(nom);
        ObservableList<String> categoryDetails = FXCollections.observableArrayList();

        for (Categorie categorie : categories) {
            String details = "Nom: " + categorie.getNom();
            categoryDetails.add(details);
        }

        listeview.setItems(categoryDetails);
    }

    private int getSelectedCategoryId(String selectedCategory) {
        String[] parts = selectedCategory.split(":");
        if (parts.length > 1) {
            String nom = parts[1].trim();
            try {
                Categorie categorie = serviceCategorie.rechercherParNom(nom).get(0);
                return categorie.getId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    // Méthode déplacée à l'intérieur de la classe AfficheCategorie
    public void afficherEvenementsParCategorie(int categorieId) {
        ObservableList<String> eventDetails = FXCollections.observableArrayList();
        try {
            List<Event> events = serviceCategorie.getEventsByCategorie(categorieId);
            for (Event event : events) {
                String details = "Nom: " + event.getNom() + ", Date: " + event.getDate();
                eventDetails.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eventListView.setItems(eventDetails);
    }
}
