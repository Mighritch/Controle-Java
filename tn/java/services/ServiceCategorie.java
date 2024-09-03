package tn.java.services;

import java.sql.*;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.java.entities.Categorie;
import tn.java.utils.DataSource;

public class ServiceCategorie implements IService<Categorie> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Categorie categorie) throws SQLException {
        String query = "INSERT INTO categorie (nom, image) VALUES (?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, categorie.getNom());
            statement.setString(2, categorie.getImage());
            statement.executeUpdate();
        }
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException {
        String query = "UPDATE categorie SET nom = ?, image = ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, categorie.getNom());
            statement.setString(2, categorie.getImage());
            statement.setInt(3, categorie.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Categorie getOneById(int id) throws SQLException {
        String query = "SELECT * FROM categorie WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String image = resultSet.getString("image");
                return new Categorie(id, nom, image);
            }
        }
        return null;
    }

    @Override
    public Set<Categorie> getAll() throws SQLException {
        Set<Categorie> categories = new HashSet<>();
        String query = "SELECT * FROM categorie";
        try (Statement statement = cnx.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String image = resultSet.getString("image");
                categories.add(new Categorie(id, nom, image));
            }
        }
        return categories;
    }

    @Override
    public List<Categorie> afficher() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try (Statement statement = cnx.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String image = resultSet.getString("image");
                categories.add(new Categorie(id, nom, image));
            }
        }
        return categories;
    }

    @Override
    public List<Categorie> rechercherParNom(String nom) throws SQLException {
        List<Categorie> filteredCategories = new ArrayList<>();
        String query = "SELECT * FROM categorie WHERE nom LIKE ?";
        try (PreparedStatement sp = cnx.prepareStatement(query)) {
            sp.setString(1, "%" + nom + "%"); // Partial search
            ResultSet rs = sp.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomCategorie = rs.getString("nom");
                String image = rs.getString("image");
                filteredCategories.add(new Categorie(id, nomCategorie, image));
            }
        }
        return filteredCategories;
    }
}


