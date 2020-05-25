package com.corochat.app.client.views;

import animatefx.animation.*;
import com.corochat.app.client.models.UserModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.Socket;
import java.nio.file.Paths;

public class ChatView {
    private double xOffset = 0;
    private double yOffset = 0;
    private static UserModel userModel;
    private static Socket socket;

    public void start(Stage stage, UserModel user, Socket socketo) throws Exception {
        userModel = user;
        socket = socketo;
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

    public static Socket getSocket() {
        return socket;
    }

    public static UserModel getUserModel() {
        return userModel;
    }
}
