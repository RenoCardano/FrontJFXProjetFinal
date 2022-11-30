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
    private TableColumn colonneDate, colonneNom, TabCodMat, tabNomEns;
    @FXML private TreeView treeView;

    public void switchToMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(MainApplication.class.getResource("/fxml/menu2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Button idButtonValiderEns, idButtonResetEns, validerAssociation, delMat;
    @FXML
    private Button creationMat;
    @FXML
    private TableView listeEnseignant, tableAssosEM;
    @FXML
    private Label status, nomEta, NomEns, statusMat;
    @FXML
    private TextField idNomEnseignant, ensID, nomMat, codeMat, selectedEnseiMat, selectMatmat;
    @FXML
    private DatePicker DateNaissance;
    @FXML
    private ComboBox comboEta, comboMat, comboEns;
    @FXML
    private ImageView logo;
    @FXML
    private ColorPicker colorPicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ///////////////////GESTION DES MATIERES ET ASSOCIATION MAT/ENS


        //CREATION D'UNE MATIERE
        creationMat.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            Matiere matiere = new Matiere();
            matiere.setNom(nomMat.getText());
            matiere.setCodeMat(codeMat.getText());
            System.out.println(colorPicker.getValue());
            matiere.setCouleur(String.valueOf(colorPicker.getValue()));
            System.out.println(matiere.getCouleur());

            GluonObservableObject<Matiere> creationMatiere = createMat(matiere);

            creationMatiere.setOnSucceeded(a -> {

                statusMat.setText("Enregistrement réussie ");
                statusMat.setTextFill(Color.GREEN);
                statusMat.setText("");
                comboMat.setValue(null);
                getComboMatiere();

            });
            creationMatiere.setOnFailed(a -> {
                statusMat.setText("Erreur pendant l'enregistrement ");
                statusMat.setTextFill(Color.RED);
                statusMat.setText("");
            });

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
            selectedEnseiMat.setText(idEnseignant);
            //reccupere la valeur de l'index de la matiere dans la comboBox
            String comboMatiere = String.valueOf(comboMat.getValue());
            String idMatiere = String.valueOf(comboMatiere.charAt(0));
            selectMatmat.setText(idMatiere);
            statusMat.setText("");

            GluonObservableObject<Enseignant> enseignantSelected = getAllEnseignantbyID(idEnseignant);

            enseignantSelected.setOnSucceeded(enseignant -> {

                GluonObservableObject<Matiere> matiereSelected = getAllMatierebyID(idMatiere);

                matiereSelected.setOnSucceeded(matiere -> {

                    Enseignement enseignement = new Enseignement(enseignantSelected.get(), matiereSelected.get());
                    System.out.println(enseignement.toString());

                    GluonObservableObject<Enseignement> association = associationEnSMat(enseignement);
                    association.setOnSucceeded(a -> {
                        statusMat.setText("Association effectuée ");
                        status.setTextFill(Color.GREEN);

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


        idButtonValiderEns.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetEns.getStyleClass().setAll("btn", "btn-danger");

        //////////////////ClIQUER ET SELECTIONNER/////////////////////////////////
        ///SELECTION DES CHAMPS POUR UPDATE
        HandleSelectedTab();

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
    }

    private GluonObservableObject<Enseignant> getAllEnseignantbyID(String idEnseignant) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/enseignant/" + idEnseignant)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));
    }

    private GluonObservableObject<Matiere> getAllMatierebyID(String idMatiere) {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/matiere/" + idMatiere)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(Matiere.class));
    }


    private void getComboEnseignant() {
        GluonObservableList<Enseignant> enseignants = getAllEnseignant();
        enseignants.setOnSucceeded(a -> {
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
                .host("http://localhost:8081/api/matiere/")
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
                .host("http://localhost:8081/api/users/" + id)
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    ///////////ONGLET MATIERE ASSCIATION//////////

    ///TODO faire un tableau d'enrgistrement de matiere
    //faire les association entre les deux combox

    /*
     Enseignement tableMatiereEnseignant = new Enseignement();
                //reccupere la valeur de l'index dans la comboBox
                String combo = String.valueOf(comboEta.getValue());
                String idMatiere = String.valueOf(combo.charAt(0));
                //set l'id de l'établissement pour enregistrement en BDD
                tableMatiereEnseignant.setEnseignant(EnseignantCreated.get());
                tableMatiereEnseignant.setIdmatiereEnseignee(Integer.parseInt(idMatiere));

    GluonObservableObject<Enseignement> enseigmentMatiereList = createEnseignement(tableMatiereEnseignant);
                enseigmentMatiereList.setOnSucceeded( b -> {
        status.setText("Association à la matière effectué");
        status.setTextFill(Color.GREEN);

    });
                enseigmentMatiereList.setOnFailed( b -> {
        status.setText("Association à la matière à échoué");
        status.setTextFill(Color.RED);

    });
    */


    private GluonObservableObject<Enseignement> associationEnSMat(Enseignement tableMatiereEnseignant) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/enseignement/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(tableMatiereEnseignant))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignement.class));
    }

    private GluonObservableList<Matiere> getAllMatiere() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/matiere")
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

        colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneDate.setCellValueFactory(new PropertyValueFactory<>("codeMat"));

        List<Enseignement> ens = enseignement.stream().toList();
        System.out.println(ens);

        enseignement.setOnSucceeded(e -> {
            tableAssosEM.setItems(enseignement);
        });
        enseignement.setOnFailed(e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

    }

    private GluonObservableList<Enseignement> getAllEnseignementt() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/enseignement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Enseignement.class));
    }

    private GluonObservableList<Enseignant> getAllEnseignant() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/enseignant")
                .connectTimeout(10000)
                .readTimeout(10000);
        return DataProvider.retrieveList(client.createListDataReader(Enseignant.class));
    }

    private GluonObservableObject createEnseignant(Enseignant enseignant) {

        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/enseignant/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(enseignant))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));
    }

}



