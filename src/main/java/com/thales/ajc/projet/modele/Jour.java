package com.thales.ajc.projet.modele;

import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class Jour {

    @NonNull
    @Generated int idJour;
    @NonNull String jour ;

    @Override
    public String toString() {
        return "Jour{" +
                "idJour=" + idJour +
                '}';
    }
    public Jour(int idJour, String jour) {
        this.idJour =idJour;
        this.jour = jour;
    }

    public int getIdJour(int idJour) {
        return idJour;
    }

    public void setIdJour(int idJour) {
        this.idJour = idJour;
    }
    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }
}


