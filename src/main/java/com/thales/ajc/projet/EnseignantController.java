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
import javafx.scene.control.skin.SliderSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EnseignantController implements Initializable {

    Stage stage;
    Parent root;
    Scene scene;

    @FXML
    private TableColumn colonneDate, colonneNom, colMat ;

    public void switchToMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(MainApplication.class.getResource("/fxml/menu2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private
    TableColumn<Enseignement, String> tabNomEns  = new TableColumn<Enseignement, String>("Enseignants");
    @FXML
    private
    TableColumn<Enseignement, String> TabCodMat  = new TableColumn<Enseignement, String>("Matières");
    @FXML
    private Button idButtonValiderEns,idBoutonDeconnexion, idButtonResetEns, validerAssociation, idBoutonSalle, delMat;
    @FXML
    private Button creationMat,idBoutonMatiere,idBoutonClasse,
            idBoutonJour, idBoutonUtilisateur, idBoutonEtablissement, exit;

    @FXML
    private Button undo, record,quit, planning;
    @FXML
    private TableView listeEnseignant, tableAssosEM, allMatiere;
    @FXML
    private Label status, nomEta, NomEns, statusMat;
    @FXML
    private TextField idNomEnseignant, ensID, nomMat, codeMat, selectMatmat;

    @FXML
    private DatePicker DateNaissance;
    @FXML
    private ComboBox  comboMat, comboEns;
    @FXML
    private ImageView logo, loader, loader2;
    @FXML
    private ColorPicker colorPicker;

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

      /*  idBoutonUtilisateur.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
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

       */

        //Toot bar///////////////////////////////////
        // undo, record,quit, planning;
        undo.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            ensID.setText("");
            idNomEnseignant.setText("");
            DateNaissance.setValue(null);
            status.setText("");
            nomMat.setText("");
            codeMat.setText("");
            statusMat.setText("");
        });
        record.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            status.setText("");
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(idNomEnseignant.getText());
            enseignant.setDateDeNaissance(String.valueOf(DateNaissance.getValue()));
            if (ensID.getText() != "") {
                enseignant.setIdEns(Integer.parseInt(ensID.getText()));
            }
            //GERE LA CREATION d'UN VEHICULE
            GluonObservableObject<Enseignant> EnseignantCreated = createEnseignant(enseignant);

            EnseignantCreated.setOnSucceeded(a -> {
                loader.setVisible(true);
            });


            EnseignantCreated.setOnSucceeded(a -> {

                status.setText("Enregistrement réussie ");
                status.setTextFill(Color.GREEN);

                listeEnseignant.setItems(null);
                fetchEnseignant();
                status.setText("");
                ensID.setText("");

            });
            EnseignantCreated.setOnFailed(a -> {
                status.setText("Erreur pendant l'enregistrement ");
                status.setTextFill(Color.RED);
                status.setText("");
            });

        });
        fetchEnseignant();

        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.exit(0);
        });
        planning.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {

        });

        //CREATION D'UNE MATIERE
                creationMat.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            createMatiere();
        });
        //Get Matière to fill ComboBox
        getComboMatiere();
        //Get Enseignant to fill ComboBox
        getComboEnseignant();

        //fill the table
        fetchEnseignement();


        ///////FAIRE LES ASSOC ENSEIGNANT MATIERE////////////
        validerAssociation.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            //reccupere la valeur de l'index de l'enseignant dans la comboBox
            String comboEnseignant = String.valueOf(comboEns.getValue());
            String idEnseignant = String.valueOf(comboEnseignant.charAt(0));

            //reccupere la valeur de l'index de la matiere dans la comboBox
            String comboMatiere = String.valueOf(comboMat.getValue());
            String idMatiere = String.valueOf(comboMatiere.charAt(0));
            statusMat.setText("");

            GluonObservableObject<Enseignant> enseignantSelected = getAllEnseignantbyID(idEnseignant);

            enseignantSelected.setOnSucceeded(enseignant -> {

                GluonObservableObject<Matiere> matiereSelected = getAllMatierebyID(idMatiere);

                matiereSelected.setOnSucceeded(matiere -> {

                    Enseignement enseignement = new Enseignement(enseignantSelected.get(), matiereSelected.get());

                    GluonObservableObject<Enseignement> association = associationEnSMat(enseignement);
                    association.setOnSucceeded(a -> {
                        statusMat.setText("Association effectuée ");
                        statusMat.setTextFill(Color.GREEN);

                    });
                    association.setOnFailed(a -> {
                        statusMat.setText("Erreur pendant l'enregistrement ");
                        statusMat.setTextFill(Color.RED);
                        statusMat.setText("");
                    });

                });
                matiereSelected.setOnFailed(matiere -> {
                    statusMat.setText("Erreur pendant la recherche de la matière ");
                    statusMat.setTextFill(Color.RED);
                });

            });
            enseignantSelected.setOnFailed(enseignant -> {
                statusMat.setText("Erreur pendant la recherche de l'enseignant ");
                statusMat.setTextFill(Color.RED);
            });

            //TODO rafraichir les selection tab assoc, comobox, couleur

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
        delMat.getStyleClass().setAll("btn", "btn-danger");
        creationMat.getStyleClass().setAll("btn", "btn-info");
        idButtonValiderEns.getStyleClass().setAll("btn", "btn-info");
        idButtonResetEns.getStyleClass().setAll("btn", "btn-danger");

        //////////////////ClIQUER ET SELECTIONNER/////////////////////////////////
        ///SELECTION DES CHAMPS POUR UPDATE
        HandleSelectedTab();


        ///////////////////////delete enseignant/////////////////

        idButtonResetEns.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
           GluonObservableObject<Enseignant> delEns = delEnsByID(Integer.parseInt(ensID.getText()));
           status.setText("Suppresion effectué");
           status.setTextFill(Color.GREEN);
            ensID.setText("");
            idNomEnseignant.setText("");
            DateNaissance.setValue(null);
            status.setText("");

        });

        ///////////////////////delete enseignant/////////////////
        delMat.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            GluonObservableObject<Boolean> delEns = delMAtByID(ensID.getText());
            loader2.setVisible(true);
            status.setText("Suppresion de la matière effectué");
            status.setTextFill(Color.GREEN);
            loader2.setVisible(false);
            fetchEnseignement();
        });

        fetchMatiere();



        //create a teacher
        idButtonValiderEns.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            status.setText("");
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(idNomEnseignant.getText());
            enseignant.setDateDeNaissance(String.valueOf(DateNaissance.getValue()));
            if (ensID.getText() != "") {
                enseignant.setIdEns(Integer.parseInt(ensID.getText()));
            }
            //GERE LA CREATION d'UN VEHICULE
            GluonObservableObject<Enseignant> EnseignantCreated = createEnseignant(enseignant);

            EnseignantCreated.setOnRunning( a-> {
                loader.setVisible(true);
            });

            EnseignantCreated.setOnSucceeded(a -> {
                loader.setVisible(false);
                status.setText("Enregistrement réussie ");
                status.setTextFill(Color.GREEN);

                listeEnseignant.setItems(null);
                fetchEnseignant();
                status.setText("");
                ensID.setText("");

            });
            EnseignantCreated.setOnFailed(a -> {
                status.setText("Erreur pendant l'enregistrement ");
                status.setTextFill(Color.RED);
                status.setText("");
            });

        });
        fetchEnseignant();

    }

    private void fetchMatiere() {

        colMat.setCellValueFactory(new PropertyValueFactory<>("nom"));

        GluonObservableList<Matiere> allMAtTab = getAllMatiere();
        allMAtTab.setOnRunning(mat -> {
            loader2.setVisible(true);
        });

        allMAtTab.setOnSucceeded(mat-> {
            loader2.setVisible(false);
            allMatiere.setItems(null);
            allMatiere.setItems(allMAtTab);
        });
    }

    private GluonObservableObject<Boolean> delMAtByID(String idMat) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8080/api/matiere/delete/" + idMat)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Boolean.class));
    }

    private GluonObservableObject<Enseignant> delEnsByID(int idEnseignant) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8080/api/enseignant/delete/" + idEnseignant)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));
    }


    private GluonObservableObject<Enseignant> getAllEnseignantbyID(String idEnseignant) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/enseignant/" + idEnseignant)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));
    }

    private GluonObservableObject<Matiere> getAllMatierebyID(String idMatiere) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/matiere/" + idMatiere)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Matiere.class));
    }


    private void getComboEnseignant() {
        GluonObservableList<Enseignant> enseignants = getAllEnseignant();

        enseignants.setOnRunning(a -> {
            loader.setVisible(true);
        });

        enseignants.setOnSucceeded(a -> {
            loader.setVisible(false);
            comboEns.getItems().removeAll();
            comboEns.getItems().addAll(enseignants);
            comboEns.getSelectionModel().selectLast();

        });
        enseignants.setOnFailed(a -> {
            statusMat.setText("Erreur pendant la recherche des enseignants ");
            statusMat.setTextFill(Color.RED);
        });
    }


    private void getComboMatiere() {
        GluonObservableList<Matiere> matieres = getAllMatiere();
        matieres.setOnSucceeded(a -> {
            comboMat.getItems().removeAll();
            comboMat.getItems().addAll(matieres);
            comboMat.getSelectionModel().selectLast();
        });
        matieres.setOnFailed(a -> {
            status.setText("Erreur pendant la recherche des matières ");
            status.setTextFill(Color.RED);
        });
    }

    private GluonObservableObject<Matiere> createMat(Matiere matiere) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/api/matiere/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(matiere))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Matiere.class));
    }

    private void HandleSelectedTab() {
        //selection dans un tableau
        TableView.TableViewSelectionModel<Enseignant> selectionModel =
                listeEnseignant.getSelectionModel();
        //Mode de selection Single OR Multiple
        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);
        //recuperation des données dans une liste d'observable
        ObservableList<Enseignant> selectedItems =
                selectionModel.getSelectedItems();

        //ecoute des modifcations
        selectedItems.addListener(
                new ListChangeListener<Enseignant>() {
                    @Override
                    public void onChanged(
                            Change<? extends Enseignant> change) {
                        ObservableList<? extends Enseignant> SelectedEtablissement = change.getList();
                        //ATTRIBUTION DES VALEURS DANS LES CHAMPS CORRESPONDANT
                        ensID.setText(String.valueOf(SelectedEtablissement.get(0).getIdEns()));
                        idNomEnseignant.setText(SelectedEtablissement.get(0).getNom());
                        //Convert String to local date
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String date = SelectedEtablissement.get(0).getDateDeNaissance();
                        //convert String to LocalDate
                        LocalDate localDate = LocalDate.parse(date, formatter);
                        //attribue converted value
                        DateNaissance.setValue(localDate);

                    }
                });

    }

    private GluonObservableObject<User> getUserById(int id) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    private GluonObservableObject<Enseignement> associationEnSMat(Enseignement tableMatiereEnseignant) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/api/enseignement/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(tableMatiereEnseignant))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignement.class));
    }

    private GluonObservableList<Matiere> getAllMatiere() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/matiere")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Matiere.class));
    }

    private void fetchEnseignant() {
        GluonObservableList<Enseignant> enseignants = getAllEnseignant();

        colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneDate.setCellValueFactory(new PropertyValueFactory<>("dateDeNaissance"));

        enseignants.setOnSucceeded(e -> {
            listeEnseignant.setItems(enseignants);
        });
        enseignants.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

    }

    private void fetchEnseignement() {
        GluonObservableList<Enseignement> enseignement = getAllEnseignementt();
        tabNomEns.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TabCodMat.setCellValueFactory(new PropertyValueFactory<>("codeMat"));

        tabNomEns.setCellValueFactory(pieceStringCellDataFeatures -> {
            String nomEnseignant = pieceStringCellDataFeatures.getValue().getEnseignant().getNom();
            return new SimpleStringProperty(nomEnseignant);
        });
        TabCodMat.setCellValueFactory(pieceStringCellDataFeatures -> {
            String matiereEnseignees = pieceStringCellDataFeatures.getValue().getMatiereEnseignee().getNom();
            return new SimpleStringProperty(matiereEnseignees);
        });

        enseignement.setOnSucceeded(e -> {
           loader.setVisible(true);
        });

        enseignement.setOnSucceeded(e -> {
            loader.setVisible(false);
            tableAssosEM.setItems(null);
            tableAssosEM.setItems(enseignement);
        });
        enseignement.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

    }
    public void createMatiere() {
        Matiere matiere = new Matiere();
        matiere.setNom(nomMat.getText());
        matiere.setCodeMat(codeMat.getText());
        matiere.setCouleur(String.valueOf(colorPicker.getValue()));

        GluonObservableObject<Matiere> creationMatiere = createMat(matiere);

        creationMatiere.setOnRunning(e -> {
            loader2.setVisible(true);
        });

        creationMatiere.setOnSucceeded(a -> {
            loader2.setVisible(false);
            statusMat.setText("Enregistrement réussie de la matière ");
            statusMat.setTextFill(Color.GREEN);
         //   fetchMatiere();
         //   getComboMatiere();
        });
        creationMatiere.setOnFailed(a -> {
            loader2.setVisible(false);
            statusMat.setText("Erreur pendant l'enregistrement ");
            statusMat.setTextFill(Color.RED);
            statusMat.setText("");
        });

    }

    private GluonObservableList<Enseignement> getAllEnseignementt() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/enseignement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Enseignement.class));
    }

    private GluonObservableList<Enseignant> getAllEnseignant() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/api/enseignant")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Enseignant.class));
    }

    private GluonObservableObject createEnseignant(Enseignant enseignant) {

        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/api/enseignant/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(enseignant))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));
    }

}



