package com.thales.ajc.projet.modele;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {

    @NonNull
    private String login;
    @NonNull
    private String motdepasse;
    private Etablissement etablissement;

}
