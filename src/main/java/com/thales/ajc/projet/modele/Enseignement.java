package com.thales.ajc.projet.modele;

import com.gluonhq.connect.GluonObservableObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Enseignement {

    private int idEnseignement;
    @NonNull
    private Enseignant enseignant;
    @NonNull
    private Matiere matiereEnseignee;

    @Override
    public String toString() {
        return "Enseignement{" +
                "idEnseignement=" + idEnseignement +
                ", enseignant=" + enseignant +
                ", matiereEnseignee=" + matiereEnseignee +
                '}';
    }
}


