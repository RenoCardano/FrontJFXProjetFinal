package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.Etablissement;
import com.thales.ajc.projet.modele.Matiere;
import com.thales.ajc.projet.modele.SalleDeClasse;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SalleDeClasseController implements Initializable {

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
    private TextField idNomSalle;
    @FXML
    private TextField idCapaciteSalle;
    @FXML
    private TextField idMatiereExcluesSalle;
    @FXML
    private TextField idIDSalle;
    @FXML
    private ComboBox idComboEtablissement;
    @FXML
    private Button idButtonResetSalle;
    @FXML
    private Button idButtonValiderSalle;
    @FXML
    private Label fetchStatus;
    @FXML
    private TextField idRechercheMatiere;
    @FXML
    private TableView listeSalleMatieres;
    @FXML
    private TableColumn idColumnNomSalleMatiere;
    @FXML
    private TextField idRechercheSalle;
    @FXML
    private TableView listSalleClasse;
    @FXML
    private TableColumn idColumnNomSalle;
    @FXML
    private TableColumn idColumnCapaciteSalle;
    @FXML
    private TableColumn idColumnMatiereExclus;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //REDIRECTION VERS PROFFESSEUR
        idBoutonProfesseur.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Enseignant");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //REDIRECTION VERS Salle De Classe
        idBoutonEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Etablissement");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        idButtonValiderSalle.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetSalle.getStyleClass().setAll("btn", "btn-warning");

        ///SELECTION DES CHAMPS POUR UPDATE
        HandleSelectedTab();

        //REINITIALISATION DES CHAMPS
        idButtonResetSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idIDSalle.setText("");
            idNomSalle.setText("");
            idCapaciteSalle.setText("");
            idMatiereExcluesSalle.setText("");
        });

        //Recupération des données dans la bdd
        GluonObservableList<SalleDeClasse> salle = getAllSalleDeClasse();

        //ComboBox d'établissement :
        ObservableList<String> dataCombo = FXCollections.observableArrayList("Etablissement 1","Etablissement 2");
        idComboEtablissement.getItems().addAll(dataCombo);
        idComboEtablissement.getSelectionModel().selectFirst();

        //Creation d'une salle de classe
        idButtonValiderSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            SalleDeClasse salleDeClasses = new SalleDeClasse();

            salleDeClasses.setIdSalleClasse(Integer.parseInt(idIDSalle.getText()));
            salleDeClasses.setNom(idNomSalle.getText());
            salleDeClasses.setCapacite(idCapaciteSalle.getText());
            salleDeClasses.setMatiereExcluClasse(idMatiereExcluesSalle.getText());
            salleDeClasses.setEtablissement((Etablissement) idComboEtablissement.getSelectionModel().getSelectedItem());


            GluonObservableObject salleDeClasseCreated = createSalleDeClasse(salleDeClasses);

            salleDeClasseCreated.setOnSucceeded( a -> {
                fetchStatus.setText("Enregistrement réussie");

                listeSalleMatieres.setItems(null);
                fetchSalleDeClasse();
            });
            salleDeClasseCreated.setOnFailed( a -> {
                fetchStatus.setText("Erreur pendant l'enregistrement");
            });
        });
        fetchSalleDeClasse();

    }

    private void fetchSalleDeClasse() {
        GluonObservableList<Matiere> matiereExclus = getAllMatiere();

        idColumnNomSalleMatiere.setCellValueFactory(new PropertyValueFactory<>("nom"));

        matiereExclus.setOnSucceeded( e -> {
            listeSalleMatieres.setItems(matiereExclus);
        });
        matiereExclus.setOnFailed( e -> {
            fetchStatus.setText("Erreur de chargement ");
            listeSalleMatieres.setItems(matiereExclus);
        });
    }

    private GluonObservableObject createSalleDeClasse(SalleDeClasse salleDeClasse) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/salleclasse/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(salleDeClasse))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(SalleDeClasse.class));
    }


    private GluonObservableList<SalleDeClasse> getAllSalleDeClasse() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/salleclasse")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(SalleDeClasse.class));
    }

    private GluonObservableList<Matiere> getAllMatiere() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/matiere")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(Matiere.class));
    }
    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<Matiere> selectionModel =
                listeSalleMatieres.getSelectionModel();
        //Mode de selection Sinlge OR Multpile
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<Matiere> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<Matiere>() {
                    @Override
                    public void onChanged(
                            Change<? extends Matiere> change) {
                        ObservableList<? extends Matiere> SelectedMatiereExclus = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        idMatiereExcluesSalle.setText(SelectedMatiereExclus.get(0).getNom());

                    }
                });

    }
}
