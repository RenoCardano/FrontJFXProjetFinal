package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.modele.Jour;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class JourController implements Initializable {

    @FXML
    private Button idBoutonEtablissement;
    @FXML
    private Button idBoutonMatiere;
    @FXML
    private Button idBoutonSalle, idBoutonDeconnexion;
    @FXML
    private Button idBoutonClasse;
    @FXML
    private Button idBoutonProfesseur;
    @FXML
    private Button idButtonUtlisateur;
    @FXML
    private Button idBoutonJour;
    @FXML
    private Button idButtonValiderJour;
    @FXML
    private Button idButtonResetJour;
    @FXML
    private ComboBox idComboJour;
    @FXML
    private int idJour;



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

        idBoutonMatiere.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Enseignant");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonSalle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "SalleDeClasse");
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
        idButtonUtlisateur.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                SceneControler.switchScene(e, "Utilisateur");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        idBoutonJour.addEventHandler(MouseEvent.MOUSE_CLICKED, ejour -> {
            try {
                SceneControler.switchScene(ejour, "Jour");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Action idButtonValiderJour = null;
        idButtonValiderJour.getStyleClass().setAll("btn", "btn-primary");
        Action idButtonResetJour = null;
        idButtonResetJour.getStyleClass().setAll("btn", "btn-warning");
    }

    //ACTIONNER LA COMBOBOX
   /* public final EventHandler<Event> getOnShowing(MouseEvent.MOUSE_CLICKED,e ->
    private final int idJour = 0; */

    {
        idComboJour.setId(String.valueOf(idJour)); };

    public ComboBox getIdComboJour() {
        return idComboJour;
    }

    //Get Jour to fill ComboBox
   /* GluonObservableList<Jour> jours = getAllJour();
            jours.setOnSucceeded( a ->
    private final List comboJour = null;

    {
        System.out.println("succeed !");
        List<String> nomJour = new ArrayList<>();
        nomJour = jours.stream().map(c -> c.getJour()).toList();
        comboJour.get(0).getClass();
        comboJour.get(0).toString();

    });
            //FATAL ERREUR
        jours.setOnFailed( a -> {
        idBoutonJour.setText("Erreur pendant la recherche des jours ");
        idBoutonJour.setTextFill(Color.RED);
    });


    //REINITIALISATION DES CHAMPS
        idButtonResetJour.addEventHandler(MouseEvent.MOUSE_CLICKED,e ->
    {
        idComboJour.setId(String.valueOf(getAllJour()));
    });  */

    private GluonObservableList<Jour> getAllJour() {
        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/etablissement")
                .connectTimeout(10000)
                .readTimeout(10000);
        return  DataProvider.retrieveList(client.createListDataReader(Jour.class));
    }
}
