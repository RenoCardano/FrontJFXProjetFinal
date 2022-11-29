package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.Enseignant;
import com.thales.ajc.projet.modele.Etablissement;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EnseignantController implements Initializable{

    @FXML
    private Button login;
    Stage stage;
    Parent root;
    Scene scene;
    @FXML
    private TableColumn colonneNom;
    @FXML
    private TableColumn  colonneDate;

    public void switchToMenu(MouseEvent event) throws IOException {
        root = FXMLLoader.load(MainApplication.class.getResource("/fxml/menu2.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Button idButtonValiderEns, idButtonResetEns;
    @FXML
    private TableView listeEnseignant;
    @FXML
    private Label status;
    @FXML
    private TextField idNomEnseignant, idTelEnseignant;

    @FXML
    private DatePicker DateNaissance;
    @FXML
    private ComboBox comboEta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idButtonValiderEns.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetEns.getStyleClass().setAll("btn","btn-danger");

        //Get Etablissement to fill ComboBox
        GluonObservableList<Etablissement> etablissements = getAllEtablissement();
            etablissements.setOnSucceeded( a -> {
                System.out.println("succeed !");
                List<String> nomEtablissement = new ArrayList<String>();
                nomEtablissement = etablissements.stream().map(c -> c.getNom()).toList();
                comboEta.getItems().addAll(nomEtablissement);
                comboEta.getSelectionModel().selectFirst();

        });
        etablissements.setOnFailed( a -> {
            status.setText("Erreur pendant la recherche des établissements ");
            status.setTextFill(Color.RED);
        });

        //create a teacher
        idButtonValiderEns.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            status.setText("");
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(idNomEnseignant.getText());
            enseignant.setDateDeNaissance(String.valueOf(DateNaissance.getValue()));
             //enseignant.setEtablissement());


            //GERE LA CREATION d'UN VEHICULE
            GluonObservableObject EnseignantCreated = createEnseignant(enseignant);

            EnseignantCreated.setOnSucceeded( a -> {
                status.setText("Enregistrement réussie ");
                status.setTextFill(Color.GREEN);
                listeEnseignant.setItems(null);
                fetchEnseignant();
            });
            EnseignantCreated.setOnFailed( a -> {
                status.setText("Erreur pendant l'enregistrement ");
                status.setTextFill(Color.RED);
            });

        });
        fetchEnseignant();
        }

    private GluonObservableList<Etablissement> getAllEtablissement() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/etablissement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(Etablissement.class));
    }

    private void fetchEnseignant() {
        GluonObservableList<Enseignant> enseignants = getAllEnseignant();

        colonneNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneDate.setCellValueFactory(new PropertyValueFactory<>("dateDeNaissance"));

        enseignants.setOnSucceeded( e -> {
            listeEnseignant.setItems(enseignants);
        });
        enseignants.setOnFailed( e -> {
            status.setText("Erreur de chargement ");
            status.setTextFill(Color.RED);
        });

    }

    private GluonObservableList<Enseignant> getAllEnseignant() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/enseignant")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(Enseignant.class));
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



