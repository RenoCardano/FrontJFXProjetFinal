package com.thales.ajc.projet;

import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.ajc.projet.MainApplication;
import com.thales.ajc.projet.modele.User;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController implements Initializable{

    @FXML
    private Button login;


    @FXML
    private PasswordField passwordField;

    @FXML
    private Button buttonEntry;
    @FXML
    private Label verifAuth;

    @FXML
    private TextField fieldUser;

    @FXML
    private Button buttonCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.exit(0);
        });

        buttonEntry.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String UserLogin = "Alex";//fieldUser.getText();
            String PassWord = "Alex"; passwordField.getText();
            System.out.println(UserLogin);
            System.out.println(PassWord);
            checkCredential(UserLogin, PassWord, e );
        });

    }

    private void checkCredential(String userLogin, String passWord, MouseEvent e) {
        GluonObservableObject<User> isUserExist = getDataByNamePassWord(userLogin,passWord);

        isUserExist.setOnSucceeded( a-> {
            try {
              SceneControler.switchScene(e, "Etablissement");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        isUserExist.setOnFailed( a-> {
            verifAuth.setText("Erreur de login ou de mot de passe !");
            verifAuth.setTextFill(Color.RED);
        });

    }

    public static GluonObservableObject<User> getDataByNamePassWord(String userLogin2, String passWord2){

        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8081/api/users/"+userLogin2+"/" + passWord2)
                .connectTimeout(20000)
                .readTimeout(20000);

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }


}



