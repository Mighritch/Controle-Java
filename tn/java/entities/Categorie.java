package tn.java.entities;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private int id;
    private String nom;
    private List<Event> events; // Ajout de la liste des événements

    // Constructeur par défaut
    public Categorie() {
        this.events = new ArrayList<>(); // Initialisation de la liste des événements
    }

    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.events = new ArrayList<>(); // Initialisation de la liste des événements
    }

    public Categorie(String nom) {
        this.nom = nom;
        this.events = new ArrayList<>(); // Initialisation de la liste des événements
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    // Ajouter un événement
    public void addEvent(Event event) {
        this.events.add(event);
    }

    // Supprimer un événement
    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", events=" + events +
                '}';
    }
}


