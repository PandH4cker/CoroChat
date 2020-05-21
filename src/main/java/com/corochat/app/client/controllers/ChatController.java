package com.corochat.app.client.controllers;

import animatefx.animation.ZoomOutDown;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane anchRoot;

    @FXML
    private Pane pnlChat;
    @FXML
    private Circle btnClose;
    @FXML
    private Circle btnReduce;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pnlChat.toFront();
    }

    @FXML
    public void handleCloseAction(MouseEvent event) {
        if (event.getSource() == this.btnClose) {
            ZoomOutDown zoomOutDown = new ZoomOutDown(this.anchRoot);
            zoomOutDown.setOnFinished(e -> System.exit(0));
            zoomOutDown.play();
        }
    }

    @FXML
    public void handleReduceAction(MouseEvent event) {
        if (event.getSource() == this.btnReduce) {
            ((Stage)((Circle) event.getSource()).getScene().getWindow()).setIconified(true);
        }
    }
}
