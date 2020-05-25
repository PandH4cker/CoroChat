package com.corochat.app.client.views;

import animatefx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Paths;

public class ChatView {
    private double xOffset = 0;
    private double yOffset = 0;

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Paths.get("src/main/resources/fxmls/ChatView.fxml").toUri().toURL());
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
        new FadeInUp(root).play();
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        this.xOffset = mouseEvent.getSceneX();
        this.yOffset = mouseEvent.getSceneY();
    }
}
