package com.thales.ajc.projet.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Enseignant {
    @NonNull
    private String nom;
    @NonNull
    private String date_de_naissance;
    //a mettre non null;
    private Etablissement etablissement;

}
