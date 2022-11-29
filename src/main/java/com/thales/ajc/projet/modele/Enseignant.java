package com.thales.ajc.projet.modele;


import java.time.LocalDate;
import java.util.Locale;

public class Enseignant {

    private String nom;

    private String dateDeNaissance;

    private Etablissement etablissement;

   public Enseignant () {

   }

   // TODO ajouter etablissement idem pour login
    public Enseignant(String nom, String date_de_naissance) {
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

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }
}
