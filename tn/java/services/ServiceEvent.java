package tn.java.services;

import tn.java.entities.Event;
import tn.java.entities.Categorie;
import tn.java.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceEvent implements IService<Event> {

    // Utilisation de la connexion depuis le singleton DataSource
    private Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Event event) throws SQLException {
        String query = "INSERT INTO event (nom, description, emplacement, nombre_places, date, categorie_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getEmplacement());
            ps.setInt(4, event.getNombrePlaces());
            ps.setDate(5, Date.valueOf(event.getDate()));

            // Si aucune catégorie n'est spécifiée, on utilise la catégorie par défaut (id 1 dans cet exemple)
            if (event.getCategorie() == null) {
                ps.setInt(6, 1);  // ID de la catégorie par défaut
            } else {
                ps.setInt(6, event.getCategorie().getId());
            }

            ps.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Event event) throws SQLException {
        String query = "UPDATE event SET nom = ?, description = ?, emplacement = ?, nombre_places = ?, date = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getEmplacement());
            ps.setInt(4, event.getNombrePlaces());
            ps.setDate(5, Date.valueOf(event.getDate()));
            ps.setInt(6, event.getId());
            ps.executeUpdate();
            System.out.println("Event modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la modification de l'événement : " + e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Event supprimé avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    @Override
    public Event getOneById(int id) throws SQLException {
        String query = "SELECT e.*, c.id as cat_id, c.nom as cat_nom FROM event e LEFT JOIN categorie c ON e.categorie_id = c.id WHERE e.id = ?";
        Event event = null;
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("cat_nom")); // Créez l'objet catégorie
                event = new Event(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate(),
                        categorie // Passez la catégorie
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération de l'événement par ID : " + e.getMessage());
        }
        return event;
    }

    @Override
    public List<Event> getAll() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, c.id as cat_id, c.nom as cat_nom FROM event e LEFT JOIN categorie c ON e.categorie_id = c.id";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("cat_nom")); // Créez l'objet catégorie
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate(),
                        categorie // Passez la catégorie
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération des événements : " + e.getMessage());
        }
        return events;
    }

    @Override
    public List<Event> afficher() {
        List<Event> events = new ArrayList<>();
        try {
            String query = "SELECT * FROM event";
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"), // Ajout de l'ID ici
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<Event> rechercherParNom(String nom) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event WHERE nom LIKE ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
                events.add(event);
            }
        }
        return events;
    }
}