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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UtilisateurController implements Initializable {

    Stage stage;
    Parent root;
    Scene scene;

    public void switchToMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(MainApplication.class.getResource("/fxml/menu2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button idButtonValider, idButtonReset, idBoutonJour,
            idBoutonUtilisateur,
            idBoutonDeconnexion,
            idBoutonClasse,
            idBoutonMatiere,
            idBoutonSalle,
            idBoutonEtablissement,exit, undo, save, quit, record, idDeleteEtablissement, planning;

    @FXML
    private Label status, nomEta, NomEns;

    @FXML
    private TableView tableUser;
    @FXML
    private TableColumn EtabMainCol;

    @FXML
    private
    TableColumn<User, String> classeUtilisateur = new TableColumn<User, String>("Enseignants");
    @FXML
    private
    TableColumn<User, String> nomEtabl = new TableColumn<User, String>("Nom Etablissement");

    @FXML
    private TextField idUtilisateur, loginUtilisateur, motdepasse;

    @FXML
    private ImageView loading;
    @FXML
    private ComboBox comboEtab;

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
                status.setText("Erreur pendant le chargement des données");
                status.setTextFill(Color.RED);
            });
        }

        //////////////////////////////////////////////////////////////////////////


        idButtonValider.getStyleClass().setAll("btn", "btn-info");
        idButtonReset.getStyleClass().setAll("btn", "btn-danger");

        //////////////////ClIQUER ET SELECTIONNER/////////////////////////////////


        ///RECUPERATION DE LA LISTES DES ETABLISSEMENTS////////
        getComboEtablissement();

        /////SUPRESION/////////////
        idButtonReset.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (idUtilisateur.getText() != "") {
                tableUser.setItems(null);
                GluonObservableObject<Boolean> delUser = deleteByID(Integer.parseInt(idUtilisateur.getText()));
            }
            loading.setVisible(true);
            status.setText("Suppression effectué");
            tableUser.setItems(null);
            fetchUtilisateur();
            status.setText("");
            loading.setVisible(false);
        });
        tableUser.setItems(null);
        fetchUtilisateur();

        idButtonValider.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (loginUtilisateur.getText() != "") {
                User utilisateur = new User();
                String login = loginUtilisateur.getText();
                utilisateur.setLogin(login);
                String pw = motdepasse.getText();
                utilisateur.setMotdepasse(pw);

                if (idUtilisateur.getText() != "") {
                    int idC = Integer.parseInt(idUtilisateur.getText());
                    utilisateur.setId(idC);
                    idUtilisateur.setText("");
                }

                ///GET L'ID DE SELECTION DE L'ETABLISSEMENT/////////////////////
                //reccupere la valeur de l'index de l'enseignant dans la comboBox
                String comboEtablissement = String.valueOf(comboEtab.getValue());
                String idEtablissement = String.valueOf(comboEtablissement.charAt(0));
                GluonObservableObject<Etablissement> etablisementSelected = getAllEtablissementByID(idEtablissement);

                etablisementSelected.setOnRunning(chargement -> {
                    loading.setVisible(true);
                });

                etablisementSelected.setOnSucceeded(chargement -> {
                    loading.setVisible(false);
                    utilisateur.setEtablissement(etablisementSelected.get());
                    GluonObservableObject<User> createNewClass = createUtilisateur(utilisateur);

                    createNewClass.setOnRunning(classe -> {
                        loading.setVisible(true);
                    });

                    createNewClass.setOnSucceeded(classe -> {
                        status.setText("L'utilisateur à bien été crée ");
                        status.setTextFill(Color.GREEN);
                        loading.setVisible(false);
                        tableUser.setItems(null);
                        fetchUtilisateur();
                    });
                    createNewClass.setOnFailed(classe -> {
                        status.setText("Erreur pendant l'enregistrement de l'utlisateur");
                        status.setTextFill(Color.GREEN);
                    });
                    status.setText("");
                });
            }
        });

        /////////////////////////////FETCH///////////////////////////////////////
        fetchUtilisateur();
        /////////////////////////////SELECTIOND AND TABLEAU///////////////////////////////////////
        HandleSelectedTab();
        ////////////////////////////INIT FIELD///////////////////
        //REINITIALISATION DES CHAMPS
        idButtonReset.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idUtilisateur.setText("");
            loginUtilisateur.setText("");
            motdepasse.setText("");
        });
        //REINITIALISATION DES CHAMPS
        undo.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idUtilisateur.setText("");
            loginUtilisateur.setText("");
            motdepasse.setText("");
        });
    }

    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<User> selectionModel =
                tableUser.getSelectionModel();
        //Mode de selection Single OR Multiple
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<User> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<User>() {
                    @Override
                    public void onChanged(
                            Change<? extends User> change) {
                        ObservableList<? extends User> selectedUser = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        idUtilisateur.setText(String.valueOf(selectedUser.get(0).getId()));
                        loginUtilisateur.setText(selectedUser.get(0).getLogin());
                        motdepasse.setText(selectedUser.get(0).getMotdepasse());
                    }
                });

    }

    private GluonObservableObject<Boolean> deleteByID(int id) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8081/api/users/delete/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Boolean.class));
    }


    private GluonObservableObject<User> createUtilisateur(User utilisateur) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/users/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(utilisateur))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    private GluonObservableObject<Etablissement> getAllEtablissementByID(String idEtablissement) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/etablissement/" + idEtablissement)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Etablissement.class));
    }

    private void getComboEtablissement() {
        GluonObservableList<Etablissement> etablissements = getAllEtablissement();
        etablissements.setOnSucceeded(a -> {
            comboEtab.getItems().clear();
            comboEtab.getItems().addAll(etablissements);
            comboEtab.getSelectionModel().selectLast();

        });
        etablissements.setOnFailed(a -> {
            status.setText("Erreur pendant la recherche des enseignants ");
            status.setTextFill(Color.RED);
        });
    }

    private GluonObservableList<Etablissement> getAllEtablissement() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/etablissement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Etablissement.class));
    }

    /*
    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<Classe> selectionModel =
                tableUser.getSelectionModel();
        //Mode de selection Single OR Multiple
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<Classe> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<Classe>() {
                    @Override
                    public void onChanged(
                            Change<? extends Classe> change) {
                        ObservableList<? extends Classe> SelectedClasse = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        idUtilisateur.setText(String.valueOf(SelectedClasse.get(0).getIdClasse()));
                        loginUtilisateur.setText(SelectedClasse.get(0).getNomClasse());
                        motdepasse.setText(SelectedClasse.get(0).getNomClasse());
                    }
                });

    }

     */

    private void fetchUtilisateur() {
        GluonObservableList<User> usersInformationList = getUsers();

        classeUtilisateur.setCellValueFactory(new PropertyValueFactory<>("login"));
        nomEtabl.setCellValueFactory(new PropertyValueFactory<>("nom"));

        classeUtilisateur.setCellValueFactory(pieceStringCellDataFeatures -> {
            String login = pieceStringCellDataFeatures.getValue().getLogin();
            return new SimpleStringProperty(login);
        });
        nomEtabl.setCellValueFactory(pieceStringCellDataFeatures -> {
            String matiereEnseignees = pieceStringCellDataFeatures.getValue().getEtablissement().getNom();
            return new SimpleStringProperty(matiereEnseignees);
        });

        usersInformationList.setOnRunning(e -> {
            loading.setVisible(true);
        });

        usersInformationList.setOnSucceeded(e -> {
            loading.setVisible(false);
            tableUser.setItems(null);
            tableUser.setItems(usersInformationList);

        });
        usersInformationList.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

    }

    private GluonObservableList<User> getUsers() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/users/")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(User.class));
    }

    private GluonObservableObject<User> getUserById(int id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

}



