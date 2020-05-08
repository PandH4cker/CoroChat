package com.corochat.app.client.controllers;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    /** Main Screen **/
    @FXML
    private AnchorPane anchRoot;
    @FXML
    private Circle btnClose;
    @FXML
    private Circle btnReduce;
    @FXML
    private StackPane pnlStack;
    /** Sign In Screen **/
    @FXML
    private Pane pnlSignIn;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private Label lblForgot;
    @FXML
    private PasswordField tfPassword;
    /** Sign Up Screen **/
    @FXML
    private Pane pnlSignUp;
    @FXML
    private ImageView btnBack;
    @FXML
    private TextField tfSignUpFirstName;
    @FXML
    private Button btnGetStarted;
    @FXML
    private TextField tfSignUpLastName;
    @FXML
    private TextField tfSignUpPseudo;
    @FXML
    private TextField tfSignUpEmail;
    @FXML
    private TextField tfSignUpPassword;
    /** Forgot Password Screen **/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.btnSignIn.setDisable(true);
        this.btnGetStarted.setDisable(true);
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

    @FXML
    public void handleBackAction(MouseEvent event) {
        if (event.getSource() == this.btnBack) {
            this.pnlSignIn.toFront();
            new FadeInLeftBig(this.pnlSignIn).play();
        }
    }

    @FXML
    public void handleSignUpAction(MouseEvent event) {
        if (event.getSource() == this.btnSignUp) {
            this.pnlSignUp.toFront();
            new FadeInRightBig(this.pnlSignUp).play();
        }
    }

    public void handleLabelForgotAction(MouseEvent event) {
        if (event.getSource() == this.lblForgot) {

        }
    }

    public void handleLabelForgotHover(MouseEvent event) {
        if (event.getSource() == this.lblForgot) {
            this.lblForgot.setCursor(Cursor.HAND);
            this.lblForgot.setTextFill(Paint.valueOf("#0000EE"));
        }
    }

    public void handleLabelForgotExited(MouseEvent event) {
        if (event.getSource() == this.lblForgot) {
            this.lblForgot.setTextFill(Paint.valueOf("#948e8e"));
        }
    }

    public void handleBackHover(MouseEvent event) {
        if (event.getSource() == this.btnBack)
            this.btnBack.setImage(new Image(Paths.get("src/main/resources/images/backHover.png").toUri().toString()));
    }

    public void handleBackExited(MouseEvent event) {
        if (event.getSource() == this.btnBack)
            this.btnBack.setImage(new Image(Paths.get("src/main/resources/images/back.png").toUri().toString()));
    }

    //Test @GigaBite
}
