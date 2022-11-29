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


    @FXML  private ObservableList<User> listOfUsers;
    private static final String JSON_URL = "http://localhost:8081/monapp/api/users";
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button buttonEntry;

    @FXML
    private TextField fieldUser;

    @FXML
    private Button buttonCancel;

    @FXML private void cancel(ActionEvent event) {
        buttonCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.exit(0);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonEntry.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                switchToMenu(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }



    private void log(MouseEvent mouseEvent) throws IOException {

        String UserLogin = fieldUser.getText();
        ;

        String PassWord = "Recap.2022!"; //passwordField.getText();
        String url = "http://localhost:8080/repaircar/api/individu/";
        //checkCredential(UserLogin, PassWord, mouseEvent );

    }
    /*
    public void add(User object) {

        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/repaircar/api/individu/")
                .connectTimeout(20000)
                .readTimeout(20000)
                .dataString(jsonClass.getStringJson(object))
                .contentType("application/json");

        GluonObservableObject<java.lang.Object> question = DataProvider.retrieveObject(client.createObjectDataReader(java.lang.Object.class));

    }

    public static GluonObservableObject<User> getDataByNamePassWord(String userLogin, String passWord){

        RestClient client = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/repaircar/api/individu/"+ userLogin+"/" + passWord)
                .connectTimeout(20000)
                .readTimeout(20000);

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    private void checkCredential(String userLogin, String passWord, MouseEvent mouseEvent) {

        GluonObservableObject<User> isUserExist = getDataByNamePassWord(userLogin,passWord);

        isUserExist.setOnSucceeded( e-> {
           // userConnected = new User();
            //System.out.println(isUserExist.asString());
            constructNextScene(isUserExist.get().getRole().toLowerCase());
        });
        isUserExist.setOnFailed( e-> {
            verifAuth.setText("Erreur de login ou de mot de passe !");
            verifAuth.setTextFill(Color.RED);
        });


    }

    private void constructNextScene (String roleName){
        System.out.println(roleName);
        if(roleName.equals("admin")){
            ReparcarApplication.setPane(1);
        }
        if(roleName.equals("magasinier")){
            ReparcarApplication.setPane(2);
        }

    }
  */




}



