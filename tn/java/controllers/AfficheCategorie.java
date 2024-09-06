package tn.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.java.entities.Categorie;
import tn.java.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AfficheCategorie {

    @FXML
    private TextField SearchField;

    @FXML
    private Button btnRecherche;

    @FXML
    private ListView<String> listeview; // Utiliser une ListView pour afficher les détails des catégories

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
    }

    @FXML
    void Recherche(ActionEvent event) {
        String searchTerm = SearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            // Afficher toutes les catégories si aucun terme de recherche n'est spécifié
            afficherCategories();
        } else {
            // Filtrer les catégories par nom
            afficherCategoriesParNom(searchTerm);
        }
    }

    // Méthode pour récupérer toutes les catégories depuis la base de données
    public ObservableList<Categorie> getCategories() {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie"; // Requête pour récupérer toutes les catégories
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

    // Méthode pour récupérer les catégories par nom depuis la base de données
    public ObservableList<Categorie> getCategoriesByName(String name) {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie WHERE nom LIKE ?"; // Requête SQL pour filtrer par nom
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + name + "%"); // Utiliser LIKE pour une recherche partielle
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

    // Méthode pour afficher toutes les catégories dans la ListView
    public void afficherCategories() {
        ObservableList<Categorie> categories = getCategories(); // Récupère toutes les catégories
        ObservableList<String> categoryDetails = FXCollections.observableArrayList();

        // Ajoute les détails de chaque catégorie (nom) dans la ListView
        for (Categorie categorie : categories) {
            String details = "Nom: " + categorie.getNom();
            categoryDetails.add(details); // Ajoute les détails de la catégorie
        }

        listeview.setItems(categoryDetails); // Met à jour la ListView avec les détails
    }

    // Méthode pour afficher les catégories filtrées par nom
    public void afficherCategoriesParNom(String nom) {
        ObservableList<Categorie> categories = getCategoriesByName(nom); // Récupère les catégories par nom
        ObservableList<String> categoryDetails = FXCollections.observableArrayList();

        // Ajoute les détails de chaque catégorie (nom) dans la ListView
        for (Categorie categorie : categories) {
            String details = "Nom: " + categorie.getNom();
            categoryDetails.add(details); // Ajoute les détails de la catégorie
        }

        listeview.setItems(categoryDetails); // Met à jour la ListView avec les détails
    }
}

