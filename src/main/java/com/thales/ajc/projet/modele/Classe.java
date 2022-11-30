package com.thales.ajc.projet.modele;


public class Classe {

    private int idClasse;
    private String nomClasse;


   public Classe() {
   }

    public Classe(String nomClasse) {
       this.nomClasse = nomClasse;

    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }
}
