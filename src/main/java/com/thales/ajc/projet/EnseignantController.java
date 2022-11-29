package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.api.jsonClass;
import com.thales.ajc.projet.modele.Enseignant;
import com.thales.ajc.projet.modele.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.kordamp.bootstrapfx.BootstrapFX;

public class EnseignantController implements Initializable{

    @FXML
    private Button login;
    Stage stage;
    Parent root;
    Scene scene;

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
    private TextField idNomEnseignant, idPrenomEnseignant, DateNaissance,idTelEnseignant;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
/*
        idButtonValiderEns.getStyleClass().setAll("btn", "btn-primary");
        idButtonResetEns.getStyleClass().setAll("btn","btn-danger");

        idButtonValiderEns.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(idNomEnseignant.getText());
            enseignant.setDate_de_naissance(String.valueOf(DateNaissance.getText());
            // enseignant.setEtablissement());

            //GERE LA CREATION d'UN VEHICULE
            GluonObservableObject vehiculeCreated = createEnseignant(enseignant);

            vehiculeCreated.setOnSucceeded( a -> {
                status.setText("Enregistrement réussie ");
                status.setTextFill(Color.GREEN);
                listeEnseignant.setItems(null);
                fetchEnseignant();
            });
            vehiculeCreated.setOnFailed( a -> {
                status.setText("Erreur pendant l'enregistrement ");
                status.setTextFill(Color.RED);
            });
        });

        }

    private void fetchEnseignant(Enseignant enseignant) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8081/api/enseignant/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(enseignant))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(Enseignant.class));

    }

    private GluonObservableObject createEnseignant(Enseignant enseignant) {
    }*/

}
}



