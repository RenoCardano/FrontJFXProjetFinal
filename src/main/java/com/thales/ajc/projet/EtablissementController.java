package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamIterableInputConverter;
import com.gluonhq.connect.converter.JsonIterableInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.Etablissement;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
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
    @FXML
    private TableView listeEtablissement;
    @FXML
    private TableColumn idColumnNomEtablissement;
    @FXML
    private TableColumn idColumnAdresseEtablissement;
    @FXML
    private TableColumn idColumnTypeEtablissement;
    @FXML
    private TableColumn idColumnTelEtablissement;
    @FXML
    private Label fetchStatus;
    @FXML
    private TextField idRechercheEtablissement;
    @FXML
    private TextField idIDEtablissement;
    @FXML
    private Button idDeleteEtablissement;

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
        idBoutonSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "SalleDeClasse");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        idButtonValiderEtablissement.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetEtablissement.getStyleClass().setAll("btn", "btn-warning");


        ///SELECTION DES CHAMPS POUR UPDATE
        HandleSelectedTab();

        //REINITIALISATION DES CHAMPS
        idButtonResetEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idIDEtablissement.setText("");
            idNomEtablissement.setText("");
            idAdresseEtablissement.setText("");
            idTypeEtablissement.setText("");
            idTelEtablissement.setText("");
            idLogoEtablissement.setText("");
        });

        //Supp un Etablissement
        idDeleteEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            GluonObservableObject<Boolean> response = deleteEtablissement(idIDEtablissement.getText());
            response.setOnSucceeded( x-> {
                idIDEtablissement.setText("");
                idNomEtablissement.setText("");
                idAdresseEtablissement.setText("");
                idTypeEtablissement.setText("");
                idTelEtablissement.setText("");
                idLogoEtablissement.setText("");
                fetchStatus.setText("Enregistrement OK");
            });
            response.setOnFailed( x-> {
                System.out.println(response.get().booleanValue());
                fetchStatus.setText("Echec de l'enregistrement");
            });
        });

        //Recupération des données dans la bdd
        GluonObservableList<Etablissement> etablissements = getAllEtablissement();

        //ON CREE UNE LISTE FILTRER
        FilteredList<Etablissement> filteredData = new FilteredList<>(etablissements, b -> true);

        idRechercheEtablissement.textProperty().addListener((observalble, oldText, newText) -> {

            filteredData.setPredicate(e -> {
                //comparer le nom et le type du tableau avec l'input de recherche
                String lowerCaseFilter = newText.toLowerCase();
                //recherche par Nom ou Type d'établissement
                String fullName = ((e.getNom()+e.getType()).toLowerCase());
                if(fullName.contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
            //je sors la liste filtrée dans une sorted list
            SortedList<Etablissement> sortedData = new SortedList<>(filteredData);

            //Bind the sorted list comparator to the table view
            sortedData.comparatorProperty().bind(listeEtablissement.comparatorProperty());

            //Add the refreshed sorted data to the table
            listeEtablissement.setItems(sortedData);
        });

        idButtonValiderEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Etablissement etablissement = new Etablissement();

            etablissement.setIdEtablissement(Integer.parseInt(idIDEtablissement.getText()));
            etablissement.setNom(idNomEtablissement.getText());
            etablissement.setAdresse(idAdresseEtablissement.getText());
            etablissement.setNumeroTelephone(idTelEtablissement.getText());
            etablissement.setType(idTypeEtablissement.getText());
            etablissement.setLogo(idLogoEtablissement.getText());

            if (idIDEtablissement.getText() != "") {
                etablissement.setIdEtablissement(Integer.parseInt(idIDEtablissement.getText()));
                System.out.println(etablissement.getIdEtablissement());
            }
            //CETTE FCT UPDATE SI DEJA CREEE
            GluonObservableObject<Etablissement> etablissementCreated = createEtablissement(etablissement);

            etablissementCreated.setOnSucceeded(a -> {
                fetchStatus.setText("Enregistrement effectué");
                fetchStatus.setTextFill(Color.GREEN);
            });
            etablissementCreated.setOnFailed(a -> {
                fetchStatus.setText("Erreur pendant l'enregistrement effectué");
                fetchStatus.setTextFill(Color.RED);
            });

            /*
                Mets a jours la liste après une insertion
             */
            listeEtablissement.setItems(null);
            GluonObservableList<Etablissement> refresh = null;
            refresh = getAllEtablissement();
            GluonObservableList<Etablissement> finalRefresh = refresh;
            refresh.setOnSucceeded(connectStateEvent -> {
                listeEtablissement.setItems(finalRefresh);
            });

        });
        fetchEtablissement();

    }

    private GluonObservableObject<Boolean> deleteEtablissement(String id) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8081/api/etablissement/delete/"+ id)
                .connectTimeout(20000)
                .readTimeout(20000);

        return DataProvider.retrieveObject(client.createObjectDataReader(Boolean.class));
    }

    private void fetchEtablissement() {
        GluonObservableList<Etablissement> etablissement = getAllEtablissement();

        idColumnNomEtablissement.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idColumnAdresseEtablissement.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        idColumnTypeEtablissement.setCellValueFactory(new PropertyValueFactory<>("type"));
        idColumnTelEtablissement.setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));

        etablissement.setOnSucceeded( e -> {
            listeEtablissement.setItems(etablissement);
        });
        etablissement.setOnFailed( e -> {
            fetchStatus.setText("Erreur de chargement ");
            listeEtablissement.setItems(etablissement);
        });
    }

    private GluonObservableObject createEtablissement(Etablissement etablissement) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/etablissement/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(etablissement))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Etablissement.class));
    }


    private GluonObservableList<Etablissement> getAllEtablissement() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/etablissement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(Etablissement.class));
    }
    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<Etablissement> selectionModel =
                listeEtablissement.getSelectionModel();
        //Mode de selection Sinlge OR Multpile
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<Etablissement> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<Etablissement>() {
                    @Override
                    public void onChanged(
                            Change<? extends Etablissement> change) {
                        ObservableList<? extends Etablissement> SelectedEtablissement = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        idIDEtablissement.setText(String.valueOf(SelectedEtablissement.get(0).getIdEtablissement()));
                        idNomEtablissement.setText(SelectedEtablissement.get(0).getNom());
                        idAdresseEtablissement.setText(SelectedEtablissement.get(0).getAdresse());
                        idTypeEtablissement.setText(SelectedEtablissement.get(0).getType());
                        idTelEtablissement.setText(SelectedEtablissement.get(0).getNumeroTelephone());
                        idLogoEtablissement.setText(SelectedEtablissement.get(0).getLogo());

                    }
                });

    }

}
