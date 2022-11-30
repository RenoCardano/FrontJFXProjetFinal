package com.thales.ajc.projet.modele;

public class SalleDeClasse {
    private String nom;
    private String capacite;
    private String matiereExcluClasse;
    private Etablissement etablissement;
    private int idSalleClasse;

    public SalleDeClasse(){

    }
    public SalleDeClasse(String nom, String capacite, String matiereExcluClasse, Etablissement etablissement, int idSalleClasse) {
        this.nom = nom;
        this.capacite = capacite;
        this.matiereExcluClasse = matiereExcluClasse;
        this.etablissement = etablissement;
        this.idSalleClasse = idSalleClasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCapacite() {
        return capacite;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    public String getMatiereExcluClasse() {
        return matiereExcluClasse;
    }

    public void setMatiereExcluClasse(String matiereExcluClasse) {
        this.matiereExcluClasse = matiereExcluClasse;
    }

    @Override
    public String toString() {
        return "SalleDeClasse{" +
                "nom='" + nom + '\'' +
                ", capacite='" + capacite + '\'' +
                ", matiereExcluClasse='" + matiereExcluClasse + '\'' +
                ", etablissement=" + etablissement +
                ", idSalleClasse=" + idSalleClasse +
                '}';
    }
}
