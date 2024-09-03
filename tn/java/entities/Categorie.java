package tn.java.entities;

public class Categorie {
    private int id;
    private String nom;
    private String image;

    // Constructeur par défaut
    public Categorie() {
    }

    public Categorie(int id, String nom, String image) {
        this.id = id;
        this.nom = nom;
        this.image = image;
    }

    // Constructeur
    public Categorie(String nom, String image) {
        this.nom = nom;
        this.image = image;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getImage() {
        return image;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
