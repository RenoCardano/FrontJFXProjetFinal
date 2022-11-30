package com.thales.ajc.projet.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


public class User {

    private int id;
    private String login;
    private String motdepasse;
    private Etablissement etablissement;

    public User(int id, String login, String motdepasse, Etablissement etablissement) {
        this.id = id;
        this.login = login;
        this.motdepasse = motdepasse;
        this.etablissement = etablissement;
    }
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", motdepasse='" + motdepasse + '\'' +
                ", etablissement=" + etablissement +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }
}
