package tn.java.services;

import tn.java.entities.Event;
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
        String query = "INSERT INTO event (nom, description, image, emplacement, nombre_places, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getImage());
            ps.setString(4, event.getEmplacement());
            ps.setInt(5, event.getNombrePlaces());
            ps.setDate(6, Date.valueOf(event.getDate()));
            ps.executeUpdate();
            System.out.println("Event ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Event event) throws SQLException {
        String query = "UPDATE event SET nom = ?, description = ?, image = ?, emplacement = ?, nombre_places = ?, date = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getImage());
            ps.setString(4, event.getEmplacement());
            ps.setInt(5, event.getNombrePlaces());
            ps.setDate(6, Date.valueOf(event.getDate()));
            ps.setInt(7, event.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event modifié avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec l'ID spécifié.");
            }
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
        String query = "SELECT * FROM event WHERE id = ?";
        Event event = null;
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                event = new Event(
                        rs.getInt("id"), // Ajout de l'ID ici
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération de l'événement par ID : " + e.getMessage());
        }
        return event;
    }

    @Override
    public Set<Event> getAll() throws SQLException {
        String query = "SELECT * FROM event";
        Set<Event> events = new HashSet<>();
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"), // Ajout de l'ID ici
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération de tous les événements : " + e.getMessage());
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
                        rs.getString("image"),
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
                        rs.getString("image"),
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

