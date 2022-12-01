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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
            idBoutonClasse,
            idBoutonMatiere,
            idBoutonSalle,
            idBoutonEtablissement, undo, save, quit, record, idDeleteEtablissement,planning;

    @FXML
    private Label status, nomEta, NomEns;

    @FXML
    private TableView tableUser;
    @FXML
    private TableColumn nomEtabl, EtabMainCol;
    @FXML
    private TableColumn classeUtilisateur;

    @FXML
    private TextField idUtilisateur,idUtlisateur,  loginUtilisateur, motdepasse;

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
        idBoutonJour.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Jour");
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


        idButtonValider.getStyleClass().setAll("btn", "btn-primary");
        idButtonReset.getStyleClass().setAll("btn", "btn-danger");

        //////////////////ClIQUER ET SELECTIONNER/////////////////////////////////


        ///RECUPERATION DE LA LISTES DES ETABLISSEMENTS////////
        getComboEtablissement();

        

        idButtonValider.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (loginUtilisateur.getText() != "") {
                User utilisateur = new User();
                String login = loginUtilisateur.getText();
                utilisateur.setLogin(login);
                String pw = motdepasse.getText();
                utilisateur.setMotdepasse(pw);

                if(idUtilisateur.getText() != "") {
                    int idC = Integer.parseInt(idUtilisateur.getText());
                    utilisateur.setId(idC);
                    idUtilisateur.setText("");
                }

                ///GET L'ID DE SELECTION DE L'ETABLISSEMENT/////////////////////
                //reccupere la valeur de l'index de l'enseignant dans la comboBox
                String comboEtablissement = String.valueOf(comboEtab.getValue());
                String idEtablissement = String.valueOf(comboEtablissement.charAt(0));
                status.setText("");

                GluonObservableObject<Etablissement> etablisementSelected = getAllEtablissementByID(idEtablissement);

                etablisementSelected.setOnRunning( chargement -> {
                    status.setText("Chargement des données en cours ");
                });

                etablisementSelected.setOnSucceeded( chargement -> {
                    status.setText("Chargement des données en cours ");
                        utilisateur.setEtablissement(etablisementSelected.get());
                        GluonObservableObject<User> createNewClass = createUtilisateur(utilisateur);

                            createNewClass.setOnSucceeded(classe -> {
                                status.setText("L'utilisateur à bien été crée ");
                                status.setTextFill(Color.GREEN);
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
       // HandleSelectedTab();
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
      /*  GluonObservableList<Classe> classes = getAllClasses();

        classeId.setCellValueFactory(new PropertyValueFactory<>("idClasse"));
        classeNom.setCellValueFactory(new PropertyValueFactory<>("nomClasse"));
        classes.setOnSucceeded(e -> {
            tableClasse.setItems(classes);
        });
        classes.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

       */
    }

    private GluonObservableObject<User> getUserById(int id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    private GluonObservableList<Classe> getAllClasses() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/classe")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Classe.class));
    }


    private GluonObservableObject createClasse(Classe classe) {

        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/classe/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(classe))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Classe.class));
    }

}



