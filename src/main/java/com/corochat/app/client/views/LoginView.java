package com.corochat.app.client.views;

import animatefx.animation.*;
import com.corochat.app.client.utils.logger.Logger;
import com.corochat.app.client.utils.logger.LoggerFactory;
import com.corochat.app.client.utils.logger.level.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Paths;

/**
 * <h1>The view for creating the login view</h1>
 * <p>
 *     This LoginView class allows to login and to sign up
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.1
 */
public class LoginView extends Application {
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class.getSimpleName());

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Start the login view
     * @param stage The stage, javafx convention for creating a view
     * @throws Exception In case of the resource is not found, the exception is thrown
     * @see Stage
     * @see Parent
     * @see Scene
     * @see LoginView#handleMousePressed(MouseEvent)
     * @see FadeInDown
     */
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
        new FadeInDown(root).play();
        logger.log("Login view has been created", Level.INFO);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Handle drag and drop of the view
     * @param mouseEvent The mouse event
     */
    private void handleMousePressed(MouseEvent mouseEvent) {
        this.xOffset = mouseEvent.getSceneX();
        this.yOffset = mouseEvent.getSceneY();
    }
}
