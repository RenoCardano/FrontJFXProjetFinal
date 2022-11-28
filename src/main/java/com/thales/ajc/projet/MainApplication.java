package com.thales.ajc.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloApplication extends Application {
    public static Pane root;
    private static int index_cur = 0;
    static List<BorderPane> paneList = new ArrayList<>();


    @Override
    public void start(Stage stage) throws IOException {
        root = FXMLLoader.load(HelloApplication.class.getResource("/fxml/menu.fxml"));
        //TODO demander comment on peut agrandir la taille de l'enfant
        paneList.add(FXMLLoader.load(HelloApplication.class.getResource("/fxml/login.fxml")));
        paneList.add(FXMLLoader.load(HelloApplication.class.getResource("/fxml/menu.fxml")));
        root.getChildren().add(paneList.get(0));
        Scene scene = new Scene(root);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }
    public static BorderPane getPane (int index) {
        return paneList.get(index);
    }

    public static void setPane (int index) {
        root.getChildren().remove(paneList.get(index_cur));
        root.getChildren().add(paneList.get(index));
        index_cur = index;
    }

    public static void main(String[] args) {
        launch();
    }
}