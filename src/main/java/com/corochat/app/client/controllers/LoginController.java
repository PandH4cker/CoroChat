package com.corochat.app.client.controllers;

import animatefx.animation.*;
import com.corochat.app.client.communication.ClientCommand;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.views.ChatView;
import com.corochat.app.server.handlers.ServerCommand;
import com.corochat.app.utils.setters.ImageSetter;
import com.corochat.app.utils.setters.LinkSetter;
import com.corochat.app.utils.validations.EmailValidator;
import com.corochat.app.utils.validations.PasswordValidator;
import com.corochat.app.utils.validations.StringContaining;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static javafx.scene.control.Alert.AlertType;

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
        this.tfSignUpFirstName.clear();
        this.tfSignUpLastName.clear();
        this.tfSignUpEmail.clear();
        this.tfSignUpPseudo.clear();
        this.pfPassword.clear();
        this.pfRepeatPassword.clear();
        this.tfEmail.clear();
        this.tfForgotPasswordEmail.clear();
        this.tfVerificationCode.clear();
        this.pfNewPassword.clear();
        this.pfSignUpPassword.clear();
        this.pfRepeatNewPassword.clear();
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
                    else
                        this.btnGetStarted.setDisable(true);
                }
                else
                    this.btnGetStarted.setDisable(true);
            } else this.btnGetStarted.setDisable(true);
        }
    }

    public void handleLogInAction(MouseEvent mouseEvent) {
        this.btnSignUp.setDisable(true);
        this.lblForgot.setDisable(true);
        this.btnClose.setDisable(true);
        this.btnReduce.setDisable(true);


        String email = this.tfEmail.getText();
        String password = this.pfPassword.getText();

        UserModel user = new UserModel(null, null, null, email, password);
        String command = ClientCommand.LOGIN.getCommand()+" "+new Gson().toJson(user);
        try {
            this.socket = new Socket("localhost", 4444);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(command); //envoyer la commande au server
            if(in.hasNextLine()) {
                String response = in.nextLine();
                if(response.startsWith(ServerCommand.DISPLAY_SUCCESS.getCommand())) {
                    String successMessage = response.substring(14);
                    UserModel userRetrieved = new Gson().fromJson(successMessage, new TypeToken<UserModel>(){}.getType());
                    try {
                        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
                        this.chatView.start(new Stage(), userRetrieved, this.socket, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(response.startsWith(ServerCommand.DISPLAY_ERROR.getCommand())){
                    String errorMessage = response.substring(12);
                }else{
                    System.out.println("ça s'est mal passé Felicia :'(");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tfSignUpFirstName.clear();
        this.tfSignUpLastName.clear();
        this.tfSignUpEmail.clear();
        this.tfSignUpPseudo.clear();
        this.pfSignUpPassword.clear();
        this.pfRepeatPassword.clear();

        this.btnSignUp.setDisable(false);
        this.lblForgot.setDisable(false);
        this.btnClose.setDisable(false);
        this.btnReduce.setDisable(false);
    }

    public void handleGetStartedAction(MouseEvent mouseEvent) {
        this.btnGetStarted.setDisable(true);
        this.btnBack.setDisable(true);
        this.btnClose.setDisable(true);
        this.btnReduce.setDisable(true);

        String firstName = this.tfSignUpFirstName.getText();
        String lastName = this.tfSignUpLastName.getText();
        String email = this.tfSignUpEmail.getText();
        String pseudo = this.tfSignUpPseudo.getText();
        String password = this.pfSignUpPassword.getText();
        password = BCrypt.hashpw(password,BCrypt.gensalt());
        UserModel user = new UserModel(firstName, lastName, pseudo, email, password);
        String command = ClientCommand.SIGNUP.getCommand()+" "+new Gson().toJson(user);
        try {
            this.socket = new Socket("localhost", 4444);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(command); //envoyer la commande au server
            if(in.hasNextLine()) {
                String response = in.nextLine();
                if(response.startsWith(ServerCommand.DISPLAY_SUCCESS.getCommand())) {
                    String successMessage = response.substring(14);
                    try {
                        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
                        this.chatView.start(new Stage(), user, this.socket, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(response.startsWith(ServerCommand.DISPLAY_ERROR.getCommand())){
                    String errorMessage = response.substring(12);
                    //TODO afficher message dans le terminal, puis plus tard dans une textview

                    Alert alert=new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(errorMessage.replace("\"",""));
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();


                    System.out.println("ça s'est mal passé Felicia :'( " + errorMessage);
                }else{
                    System.out.println("ça s'est mal passé Felicia :'(");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tfSignUpFirstName.clear();
        this.tfSignUpLastName.clear();
        this.tfSignUpEmail.clear();
        this.tfSignUpPseudo.clear();
        this.pfSignUpPassword.clear();
        this.pfRepeatPassword.clear();

        this.btnGetStarted.setDisable(true);
        this.btnBack.setDisable(false);
        this.btnClose.setDisable(false);
        this.btnReduce.setDisable(false);
    }
}
