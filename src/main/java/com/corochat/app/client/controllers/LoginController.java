package com.corochat.app.client.controllers;

import animatefx.animation.*;
import com.corochat.app.client.views.ChatView;
import com.corochat.app.utils.setters.ImageSetter;
import com.corochat.app.utils.setters.LinkSetter;
import com.corochat.app.utils.validations.EmailValidator;
import com.corochat.app.utils.validations.PasswordValidator;
import com.corochat.app.utils.validations.StringContaining;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private ChatView chatView;
    private Pane currentPane;
    private Socket socket;

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
    private PasswordField pfPassword;
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
    private PasswordField pfSignUpPassword;
    @FXML
    private PasswordField pfRepeatPassword;
    /** Forgot Password Screen First **/
    @FXML
    private Pane pnlForgotPasswordFirst;
    @FXML
    private ImageView btnForgotPasswordFirstBack;
    @FXML
    private TextField tfForgotPasswordEmail;
    @FXML
    private Button btnForgotPasswordFirstContinue;
    /** Forgot Password Screen Second **/
    @FXML
    private Pane pnlForgotPasswordSecond;
    @FXML
    private ImageView btnForgotPasswordSecondBack;
    @FXML
    private TextField tfVerificationCode;
    @FXML
    private Label lblSendItBack;
    @FXML
    private Button btnForgotPasswordSecondContinue;
    /** Forgot Password Screen Third **/
    @FXML
    private Pane pnlForgotPasswordThird;
    @FXML
    private ImageView btnForgotPasswordThirdBack;
    @FXML
    private PasswordField pfNewPassword;
    @FXML
    private PasswordField pfRepeatNewPassword;
    @FXML
    private Button btnFinish;


    public LoginController() {
        this.chatView = new ChatView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pnlSignIn.toFront();
        this.disableButtons();
    }

    private void disableButtons() {
        this.btnSignIn.setDisable(true);
        this.btnGetStarted.setDisable(true);
        this.btnFinish.setDisable(true);
        this.btnForgotPasswordFirstContinue.setDisable(true);
        this.btnForgotPasswordSecondContinue.setDisable(true);
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
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack) {
            this.backAction(this.pnlSignIn);
        }
    }

    private boolean isValidEmail(final TextField email) {
        return EmailValidator.isValid(email.getText());
    }

    private boolean isValidPassword(final PasswordField password) {
        return PasswordValidator.isValid(password.getText());
    }

    private void backAction(final Pane pane) {
        new ZoomOutRight(this.currentPane).play();
        this.currentPane = pane;
    }

    private void inAction(final Pane pane) {
        pane.toFront();
        new ZoomInRight(pane).play();
        this.currentPane = pane;
    }

    @FXML
    public void handleSignUpAction(MouseEvent event) {
        if (event.getSource() == this.btnSignUp) {
            this.inAction(this.pnlSignUp);
        }
    }

    @FXML
    public void handleLabelForgotAction(MouseEvent event) {
        if (event.getSource() == this.lblForgot) {
            this.inAction(this.pnlForgotPasswordFirst);
        }
    }

    @FXML
    public void handleLabelHover(MouseEvent event) {
        if (event.getSource() == this.lblForgot ||
            event.getSource() == this.lblSendItBack) {
            LinkSetter.setAsLink((Label) event.getSource());
        }
    }

    @FXML
    public void handleLabelExited(MouseEvent event) {
        if (event.getSource() == this.lblForgot ||
            event.getSource() == this.lblSendItBack) {
            LinkSetter.unsetAsLink((Label) event.getSource());
        }
    }

    @FXML
    public void handleBackHover(MouseEvent event) {
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack ||
            event.getSource() == this.btnForgotPasswordSecondBack ||
            event.getSource() == this.btnForgotPasswordThirdBack)
            this.backHovering((ImageView) event.getSource());
    }

    private void backHovering(final ImageView back) {
        ImageSetter.set(back, "backHover.png");
    }

    private void backUnhovering(final ImageView back) {
        ImageSetter.set(back, "back.png");
    }

    @FXML
    public void handleBackExited(MouseEvent event) {
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack ||
            event.getSource() == this.btnForgotPasswordSecondBack ||
            event.getSource() == this.btnForgotPasswordThirdBack)
            this.backUnhovering((ImageView) event.getSource());
    }

    public void handleSignInEnable(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.tfEmail ||
            keyEvent.getSource() == this.pfPassword) {
            if (!this.tfEmail.getText().equals("") && !this.pfPassword.getText().equals(""))
                this.btnSignIn.setDisable(false);
            else
                this.btnSignIn.setDisable(true);
        }
    }

    public void handleGetStartedEnable(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.tfSignUpFirstName ||
            keyEvent.getSource() == this.tfSignUpLastName  ||
            keyEvent.getSource() == this.tfSignUpPseudo    ||
            keyEvent.getSource() == this.tfSignUpEmail     ||
            keyEvent.getSource() == this.pfSignUpPassword  ||
            keyEvent.getSource() == this.pfRepeatPassword) {
            //Checking for non null texts
            if (!this.tfSignUpFirstName.getText().equals("") && !this.tfSignUpLastName.getText().equals("") &&
                !this.tfSignUpPseudo.getText().equals("") && !this.tfSignUpEmail.getText().equals("") &&
                !this.pfSignUpPassword.getText().equals("") && !this.pfRepeatPassword.getText().equals(""))
                //Checking for non containing numbers in first/last name
            {
                if (!StringContaining.numbers(this.tfSignUpFirstName.getText()) && !StringContaining.numbers(this.tfSignUpLastName.getText()) &&
                    !this.tfSignUpPseudo.getText().matches("^[\\d !\"#$%&'()*+,-./\\\\:;<=>?@\\[\\]^_`{|}~].*"))
                    //Checking for valid email and password
                {
                    if (this.isValidEmail(this.tfSignUpEmail) && this.isValidPassword(this.pfSignUpPassword) &&
                        this.pfSignUpPassword.getText().equals(this.pfRepeatPassword.getText())) {
                        this.btnGetStarted.setDisable(false);
                    }
                }
            } else this.btnGetStarted.setDisable(true);
        }
    }

    public void handleLogInAction(MouseEvent mouseEvent) {
        try {
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
            this.chatView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
