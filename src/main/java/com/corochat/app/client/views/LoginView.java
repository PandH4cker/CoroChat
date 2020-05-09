package com.corochat.app.client.views;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Paths;

public class LoginView extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Paths.get("src/main/resources/fxmls/LoginView.fxml").toUri().toURL());
        stage.setTitle("CoroChat");

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(this::handleMousePressed);
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - this.xOffset);
            stage.setY(mouseEvent.getScreenY() - this.yOffset);
        });

        stage.show();
        new ZoomInUp(root).play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        this.xOffset = mouseEvent.getSceneX();
        this.yOffset = mouseEvent.getSceneY();
    }
}
