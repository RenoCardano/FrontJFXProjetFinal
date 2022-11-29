package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamIterableInputConverter;
import com.gluonhq.connect.converter.JsonIterableInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.modele.Etablissement;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EtablissementController implements Initializable {

    @FXML
    private Button idBoutonEtablissement;
    @FXML
    private Button idBoutonMatiere;
    @FXML
    private Button idBoutonSalle;
    @FXML
    private Button idBoutonClasse;
    @FXML
    private Button idBoutonProfesseur;
    @FXML
    private Button idBoutonUtilisateur;
    @FXML
    private Button idBoutonJour;
    @FXML
    private TextField idNomEtablissement;
    @FXML
    private TextField idAdresseEtablissement;
    @FXML
    private TextField idTypeEtablissement;
    @FXML
    private TextField idTelEtablissement;
    @FXML
    private TextField idLogoEtablissement;
    @FXML
    private Button idButtonResetEtablissement;
    @FXML
    private Button idButtonValiderEtablissement;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idButtonValiderEtablissement.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetEtablissement.getStyleClass().setAll("btn", "btn-warning");


    }



}
