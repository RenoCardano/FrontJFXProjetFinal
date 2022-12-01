package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
    private Button idBoutonUtlisateur;
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
    private TableColumn idColumnIdMatiere;
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


        //REDIRECTION VERS Salle De Classe
        idBoutonEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Etablissement");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        idBoutonMatiere.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Enseignant");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "SalleDeClasse");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonClasse.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Classe");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonUtlisateur.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Utilisateur");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonJour.addEventHandler(MouseEvent.MOUSE_CLICKED, ejour -> {
            try {
                SceneControler.switchScene(ejour, "Jour");
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

        //Creation d'une salle de classe
        idButtonValiderSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    SalleDeClasse salleDeClasses = new SalleDeClasse();

                    salleDeClasses.setIdSalleClasse(Integer.parseInt(idIDSalle.getText()));
                    salleDeClasses.setNom(idNomSalle.getText());
                    salleDeClasses.setCapacite(idCapaciteSalle.getText());
                    salleDeClasses.setMatiereExcluClasse(idMatiereExcluesSalle.getText());

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
        fetchMatieres();

    }

    private void fetchMatieres() {
        GluonObservableList<Matiere> matiereExclus = getAllMatiere();

        idColumnNomSalleMatiere.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idColumnIdMatiere.setCellValueFactory(new PropertyValueFactory<>("id"));

        matiereExclus.setOnSucceeded( e -> {
            listeSalleMatieres.setItems(matiereExclus);
        });
        matiereExclus.setOnFailed( e -> {
            fetchStatus.setText("Erreur de chargement ");
            listeSalleMatieres.setItems(matiereExclus);
        });
    }

    private void fetchSalleDeClasse() {
        GluonObservableList<SalleDeClasse> salleDeClasse = getAllSalleDeClasse();

        idColumnNomSalle.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idColumnCapaciteSalle.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        idColumnMatiereExclus.setCellValueFactory(new PropertyValueFactory<>("matiereExcluClasse"));

        salleDeClasse.setOnSucceeded( e -> {
            listeSalleMatieres.setItems(salleDeClasse);
        });
        salleDeClasse.setOnFailed( e -> {
            fetchStatus.setText("Erreur de chargement ");
            listeSalleMatieres.setItems(salleDeClasse);
        });
    }



    private GluonObservableObject<Matiere> getMatiereById(TextField id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/matiere/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Matiere.class));
    }

    private GluonObservableObject createSalleDeClasse(SalleDeClasse salleDeClasse) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/api/salleclasse/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(salleDeClasse))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(SalleDeClasse.class));
    }


    private GluonObservableList<SalleDeClasse> getAllSalleDeClasse() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/salleclasse")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(SalleDeClasse.class));
    }

    private GluonObservableList<Matiere> getAllMatiere() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/matiere")
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
                        idMatiereExcluesSalle.setText(String.valueOf(SelectedMatiereExclus.get(0).getId()));

                    }
                });

    }
}
