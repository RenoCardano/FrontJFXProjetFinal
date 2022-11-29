package com.thales.ajc.projet.modele;

import java.util.List;

public class Etablissement {

    private int idEtablissement;
    private String nom;
    private String adresse;
    private String type;
    private String numeroTelephone;
    private String logo;

    public Etablissement(int idEtablissement, String nom, String adresse, String type, String numeroTelephone, String logo) {
        this.idEtablissement = idEtablissement;
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
        this.numeroTelephone = numeroTelephone;
        this.logo = logo;
    }

    public int getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(int idEtablissement) {
        this.idEtablissement = idEtablissement;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
