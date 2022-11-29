package com.thales.ajc.projet.modele;

public class Etablissement {

    private String nom;
    private String adresse;
    private String type;
    private String numeroTelephone;

    public Etablissement(String nom, String adresse, String type, String numeroTelephone) {
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
        this.numeroTelephone = numeroTelephone;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    @Override
    public String toString() {
        return "Etablissement{" +
                "nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", type='" + type + '\'' +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                '}';
    }
}
