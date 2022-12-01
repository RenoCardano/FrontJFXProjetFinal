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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClasseController implements Initializable {

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
            idBoutonDeconnexion,
            idBoutonEtablissement,
            exit;

    @FXML
    private Label status, nomEta, NomEns;

    @FXML
    private TableView tableClasse;
    @FXML
    private TableColumn classeNom;
    @FXML
    private TableColumn classeId;

    @FXML
    private TextField idClasse, nomClasse;

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
                status.setText("Erreur pendant le chargement des données");
                status.setTextFill(Color.RED);
            });
        }

        //////////////////////////////////////////////////////////////////////////


        idButtonValider.getStyleClass().setAll("btn", "btn-primary");
        idButtonReset.getStyleClass().setAll("btn", "btn-danger");

        //////////////////ClIQUER ET SELECTIONNER/////////////////////////////////


        idButtonValider.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (nomClasse.getText() != "") {
                Classe newClasse = new Classe();
                String nom = nomClasse.getText();
                newClasse.setNomClasse(nom);
                if(idClasse.getText() != "") {
                    int idC = Integer.parseInt(idClasse.getText());
                    newClasse.setIdClasse(idC);
                    idClasse.setText("");
                }
                GluonObservableObject<Classe> createNewClass = createClasse(newClasse);
                createNewClass.setOnSucceeded(classe -> {
                    status.setText("La classe à bien été crée ");
                    status.setTextFill(Color.GREEN);
                });
                createNewClass.setOnFailed(classe -> {
                    status.setText("Erreur pendant l'enregistrement dela classe");
                    status.setTextFill(Color.GREEN);
                });
                status.setText("");
            }
        });

        /////////////////////////////FETCH///////////////////////////////////////
        fetchClasse();
        /////////////////////////////SELECTIOND AND TABLEAU///////////////////////////////////////
        HandleSelectedTab();
        ////////////////////////////INIT FIELD///////////////////
        //REINITIALISATION DES CHAMPS
        idButtonReset.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            idClasse.setText("");
            nomClasse.setText("");
        });
    }

    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<Classe> selectionModel =
                tableClasse.getSelectionModel();
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
                        idClasse.setText(String.valueOf(SelectedClasse.get(0).getIdClasse()));
                        nomClasse.setText(SelectedClasse.get(0).getNomClasse());
                    }
                });

    }

    private void fetchClasse() {
        GluonObservableList<Classe> classes = getAllClasses();

        classeId.setCellValueFactory(new PropertyValueFactory<>("idClasse"));
        classeNom.setCellValueFactory(new PropertyValueFactory<>("nomClasse"));
        classes.setOnSucceeded(e -> {
            tableClasse.setItems(classes);
        });
        classes.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });
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



