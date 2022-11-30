package com.thales.ajc.projet.modele;


public class Enseignant {

    private int idEns;
    private String nom;

    private String dateDeNaissance;

   public Enseignant () {

   }

    public Enseignant(int idEns, String nom, String date_de_naissance) {
       this.idEns = idEns;
       this.nom = nom;
        this.dateDeNaissance = date_de_naissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public int getIdEns() {
        return idEns;
    }

    public void setIdEns(int idEns) {
        this.idEns = idEns;
    }
}
