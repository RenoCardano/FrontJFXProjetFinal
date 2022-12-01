package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    private Button idBoutonDeconnexion;
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
    private Button reset, exit;
    @FXML
    private Button idButtonValiderSalle, idButtonSuppSalle;
    @FXML
    private Label fetchStatus, NomEns, nomEta, status;
    @FXML
    private TextField idRechercheMatiere;
    @FXML
    private TableView listeSalleMatieres;
    @FXML
    private TableColumn idColumnNomSalleMatiere;
    @FXML
    private TableColumn idColumnIdMatiere;
    @FXML
    private
    TableColumn<SalleDeClasse, String> idColumnNomSalle = new TableColumn<SalleDeClasse, String>("Nom");
    @FXML
    private
    TableColumn<SalleDeClasse, String> idColumnCapaciteSalle = new TableColumn<SalleDeClasse, String>("Capacité");
    @FXML
    private
    TableColumn<SalleDeClasse, String> idColumnMatiereExclus = new TableColumn<SalleDeClasse, String>("Matière exclues");

    @FXML
    private TextField idRechercherSalle;
    @FXML
    private TableView listSalleClasse;

    @FXML
    private ImageView loader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.exit(0);
        });

        //REDIRECTION VERS Salle De Classe
        idBoutonEtablissement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Etablissement");
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
        idBoutonMatiere.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Enseignant");
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

        idBoutonUtilisateur.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Utilisateur");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonDeconnexion.addEventHandler(MouseEvent.MOUSE_CLICKED, ejour -> {
            try {
                SceneControler.switchScene(ejour, "Login");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ///RECUPERATION DES INFOMATION DE L'UTILISATEUR//////////
        if (LoginController.isUserExist.get().getId() != 0) {
            GluonObservableObject<User> userInfo = getUserById(1);
            userInfo.setOnSucceeded(a -> {
                NomEns.setText(userInfo.get().getLogin());
                Etablissement nomEtablissement;
                nomEtablissement = userInfo.get().getEtablissement();
                nomEta.setText(nomEtablissement.getNom());
                //logo.setImage(new Image("../resources/icons/about.png"));
            });

            userInfo.setOnFailed(a -> {
                status.setText("Erreur pendant le chargement de l'utilisateur");
                status.setTextFill(Color.RED);
            });
        }

        idButtonValiderSalle.getStyleClass().setAll("btn", "btn-info");
        idButtonSuppSalle.getStyleClass().setAll("btn", "btn-danger");

        ///SELECTION DES MATIERES A EXCLURES
        HandleSelectedTab();
        ///SELECTION DES CHAMPS POUR UPDATE
        HandleSelectedTabSalle();

        //REINITIALISATION DES CHAMPS
        reset.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idIDSalle.setText("");
            idNomSalle.setText("");
            idCapaciteSalle.setText("");
            idMatiereExcluesSalle.setText("");
        });

        //Recupération des données dans la bdd
        GluonObservableList<SalleDeClasse> salles = getAllSalleDeClasse();

        //ON CREE UNE LISTE FILTRER
        FilteredList<SalleDeClasse> filteredSalle = new FilteredList<>(salles, s -> true);

        idRechercherSalle.textProperty().addListener((observalble, oldText, newText) -> {

            filteredSalle.setPredicate(c -> {
                //comparer le nom et le type du tableau avec l'input de recherche
                String lowerCaseFilter = newText.toLowerCase();
                //recherche par Nom de la salle
                String fullName = ((c.getNom()).toLowerCase());
                if(fullName.contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
            //je sors la liste filtrée dans une sorted list
            SortedList<SalleDeClasse> sortedSalle = new SortedList<>(filteredSalle);

            //Bind the sorted list comparator to the table view
            sortedSalle.comparatorProperty().bind(listSalleClasse.comparatorProperty());

            //Add the refreshed sorted data to the table
            listSalleClasse.setItems(sortedSalle);
        });

        //Creation d'une salle de classe
        idButtonValiderSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            if (idNomSalle.getText() != "") {

                SalleDeClasse salleDeClasses = new SalleDeClasse();
                salleDeClasses.setNom(idNomSalle.getText());
                salleDeClasses.setCapacite(idCapaciteSalle.getText());

                if (idIDSalle.getText() != "") {
                    salleDeClasses.setIdSalleClasse(Integer.parseInt(idIDSalle.getText()));
                }
                if (idMatiereExcluesSalle.getText() != "") {
                    GluonObservableObject<Matiere> selectedMatiere = getMatiereById(Integer.parseInt(idMatiereExcluesSalle.getText()));

                    selectedMatiere.setOnRunning(change -> {
                        loader.setVisible(true);
                    });
                    selectedMatiere.setOnSucceeded(change -> {
                        loader.setVisible(false);
                        salleDeClasses.setMatiereExcluClasse(selectedMatiere.get());
                        GluonObservableObject<SalleDeClasse> newSalleClasse = createSalleDeClasse(salleDeClasses);

                        newSalleClasse.setOnRunning(load -> {
                            loader.setVisible(true);
                        });

                        newSalleClasse.setOnSucceeded(load -> {
                            loader.setVisible(false);
                            fetchStatus.setText("La salle de classe a été créée");
                            fetchStatus.setTextFill(Color.GREEN);
                            listSalleClasse.setItems(null);
                            fetchSalleDeClasse();
                        });
                        newSalleClasse.setOnFailed(load -> {
                            loader.setVisible(false);
                            fetchStatus.setText("Problème pendannt la création de la salle de classe");
                            fetchStatus.setTextFill(Color.RED);
                            // fetchSalleDeClasse();
                        });

                    });
                    selectedMatiere.setOnFailed(change -> {
                        loader.setVisible(false);
                        fetchStatus.setText("Problème pendant la recherche de la matière ");
                        fetchStatus.setTextFill(Color.RED);
                    });

                } else {
                    //SI CREATION SANS MATIERE A EXCLURE
                    GluonObservableObject<SalleDeClasse> newSalleClasses = createSalleDeClasse(salleDeClasses);

                    newSalleClasses.setOnRunning(load -> {
                        loader.setVisible(true);
                    });

                    newSalleClasses.setOnSucceeded(load -> {
                        loader.setVisible(false);
                        fetchStatus.setText("La salle de classe a été créée");
                        fetchStatus.setTextFill(Color.GREEN);
                        listSalleClasse.setItems(null);
                        fetchSalleDeClasse();
                    });

                    newSalleClasses.setOnFailed(load -> {
                        loader.setVisible(false);
                        fetchStatus.setText("Problème pendant la création de la salle de classe");
                        fetchStatus.setTextFill(Color.RED);

                    });
                }
            }

        });
        fetchSalleDeClasse();
        fetchMatieres();
    }

    private GluonObservableObject<User> getUserById(int id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
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

        idColumnNomSalle.setCellValueFactory(pieceStringCellDataFeatures -> {
            String nomClasse = pieceStringCellDataFeatures.getValue().getNom();
            return new SimpleStringProperty(nomClasse);
        });
        idColumnCapaciteSalle.setCellValueFactory(pieceStringCellDataFeatures -> {
            String cap = pieceStringCellDataFeatures.getValue().getCapacite();
            return new SimpleStringProperty(cap);
        });

        salleDeClasse.setOnSucceeded( e -> {
            listSalleClasse.setItems(salleDeClasse);
        });
        salleDeClasse.setOnFailed( e -> {
            fetchStatus.setText("Erreur de chargement ");
            listSalleClasse.setItems(salleDeClasse);
        });
    }


    private GluonObservableObject<Matiere> getMatiereById(int id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/matiere/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Matiere.class));
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
                        idMatiereExcluesSalle.setText(String.valueOf(SelectedMatiereExclus.get(0).getId()));

                    }
                });

    }
    private void HandleSelectedTabSalle() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<SalleDeClasse> selectionModel =
                listSalleClasse.getSelectionModel();
        //Mode de selection Sinlge OR Multpile
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<SalleDeClasse> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<SalleDeClasse>() {
                    @Override
                    public void onChanged(
                            Change<? extends SalleDeClasse> change) {
                        ObservableList<? extends SalleDeClasse> SelectedSalleDeClasse = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        idNomSalle.setText(SelectedSalleDeClasse.get(0).getNom());
                        idMatiereExcluesSalle.setText(String.valueOf(SelectedSalleDeClasse.get(0).getMatiereExcluClasse()));
                        idCapaciteSalle.setText(String.valueOf(SelectedSalleDeClasse.get(0).getCapacite()));
                        idIDSalle.setText(String.valueOf(SelectedSalleDeClasse.get(0).getIdSalleClasse()));

                    }
                });

    }
}
