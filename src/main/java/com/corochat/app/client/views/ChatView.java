package com.corochat.app.client.views;

import animatefx.animation.*;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.utils.logger.Logger;
import com.corochat.app.utils.logger.LoggerFactory;
import com.corochat.app.utils.logger.level.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;

/**
 * <h1>The view for creating the chat</h1>
 * <p>
 *     The ChatView class allows several users to communicate via a chat
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.3
 * @see UserModel
 * @see Socket
 * @see PrintWriter
 */
public class ChatView {
    private final Logger logger = LoggerFactory.getLogger(ChatView.class.getSimpleName());

    private double xOffset = 0;
    private double yOffset = 0;
    private static UserModel userModel;
    private static Socket socket;
    private static PrintWriter out;

    /**
     * Start the view and initialize its attributes
     * @param stage The stage, javafx convention for creating a view
     * @param user The user which creates the view
     * @param socketo The socket for the connection between the server and the user
     * @param outo The printwriter for sending commands and information to the server
     * @throws Exception In case of the resource is not found, the exception is thrown
     * @see Stage
     * @see UserModel
     * @see Socket
     * @see PrintWriter
     * @see Parent
     * @see Scene
     * @see FadeInUp
     * @see ChatView#handleMousePressed(MouseEvent)
     */
    public void start(Stage stage, UserModel user, Socket socketo, PrintWriter outo) throws Exception {
        userModel = user;
        socket = socketo;
        out = outo;
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
        logger.log("Chat view has been created", Level.INFO);
    }

    /**
     * Handle drag and drop of the view
     * @param mouseEvent The mouse event
     */
    private void handleMousePressed(MouseEvent mouseEvent) {
        this.xOffset = mouseEvent.getSceneX();
        this.yOffset = mouseEvent.getSceneY();
    }

    /**
     * Getter of the socket
     * @return Socket - The socket for communicating with the server
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * Getter of the user
     * @return UserModel - The user which creates the view
     */
    public static UserModel getUserModel() {
        return userModel;
    }

    /**
     * Getter of the printwriter
     * @return PrintWriter - The writer for sending information to the server
     */
    public static PrintWriter getOut() {
        return out;
    }
}
