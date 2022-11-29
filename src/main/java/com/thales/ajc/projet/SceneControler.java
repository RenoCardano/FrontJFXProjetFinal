package com.thales.ajc.projet;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneControler {

    public static Stage stage;
    public static Parent root;
    public static Scene scene;

    public static void switchScene(MouseEvent event, String newScene) throws IOException {
        root = FXMLLoader.load(MainApplication.class.getResource("/fxml/"+newScene+".fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
