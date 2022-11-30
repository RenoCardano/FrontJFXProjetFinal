package com.thales.ajc.projet.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Enseignement {

    private int idEnseignement;
    @NonNull
    private Enseignant enseignant;
    @NonNull
    private int idmatiereEnseignee;

}


