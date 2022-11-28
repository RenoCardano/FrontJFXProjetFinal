package com.thales.ajc.projet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button goto2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goto2.addEventHandler(MouseEvent.MOUSE_CLICKED, this::setPage);

    }

    private <T extends Event> void setPage(T t) {
        MainApplication.setPane(1);
    }
}