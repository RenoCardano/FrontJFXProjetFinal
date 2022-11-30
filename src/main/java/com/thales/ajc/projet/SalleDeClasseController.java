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

        //ComboBox d'établissement :
        ObservableList<String> dataCombo = FXCollections.observableArrayList("Etablissement 1","Etablissement 2");
        idComboEtablissement.getItems().addAll(dataCombo);
        idComboEtablissement.getSelectionModel().selectFirst();

        //Recupération des données dans la bdd
        GluonObservableList<SalleDeClasse> salle = getAllSalleDeClasse();

        //ON CREE UNE LISTE FILTRER
        /*FilteredList<SalleDeClasse> filteredData = new FilteredList<>(salle, b -> true);

        idRechercheSalle.textProperty().addListener((observalble, oldText, newText) -> {

            filteredData.setPredicate(e -> {
                //comparer le nom et le type du tableau avec l'input de recherche
                String lowerCaseFilter = newText.toLowerCase();
                //recherche par Nom de la salle de classe ou etablissement
                String fullName = ((e.getNom()+e.getEtablissement()).toLowerCase());
                if(fullName.contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
            //je sors la liste filtrée dans une sorted list
            SortedList<SalleDeClasse> sortedData = new SortedList<>(filteredData);

            //Bind the sorted list comparator to the table view
            sortedData.comparatorProperty().bind(listeSalleMatieres.comparatorProperty());

            //Add the refreshed sorted data to the table
            listeSalleMatieres.setItems(sortedData);
        });

        idButtonValiderSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            SalleDeClasse salleDeClasses = new SalleDeClasse();

            salleDeClasses.setIdSalleClasse(Integer.parseInt(idIDSalle.getText()));
            salleDeClasses.setNom(idNomSalle.getText());
            salleDeClasses.setCapacite(idCapaciteSalle.getText());
            salleDeClasses.setMatiereExcluClasse(idMatiereExcluesSalle.getText());
            salleDeClasses.setEtablissement((Etablissement) idComboEtablissement.getSelectionModel().getSelectedItem());


            if (idIDSalle.getText() != "") {
                salleDeClasses.setIdSalleClasse(Integer.parseInt(idIDSalle.getText()));
                System.out.println(salleDeClasses.getIdSalleClasse());
            }
            //CETTE FCT UPDATE SI DEJA CREEE
            GluonObservableObject<SalleDeClasse> salleDeClasseCreated = createSalleDeClasse(salleDeClasses);

            salleDeClasseCreated.setOnSucceeded(a -> {
                fetchStatus.setText("Enregistrement effectué");
                fetchStatus.setTextFill(Color.GREEN);
            });
            salleDeClasseCreated.setOnFailed(a -> {
                fetchStatus.setText("Erreur pendant l'enregistrement effectué");
                fetchStatus.setTextFill(Color.RED);
            });*/

            /*
                Mets a jours la liste après une insertion
             */
            /*listeSalleMatieres.setItems(null);
            GluonObservableList<SalleDeClasse> refresh = null;
            refresh = getAllSalleDeClasse();
            GluonObservableList<SalleDeClasse> finalRefresh = refresh;
            refresh.setOnSucceeded(connectStateEvent -> {
                listeSalleMatieres.setItems(finalRefresh);
            });

        });
        fetchSalleDeClasse();*/

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
