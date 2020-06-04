package com.corochat.app.client.controllers;

import animatefx.animation.*;
import com.corochat.app.client.communication.ClientCommand;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import com.corochat.app.client.views.ChatView;
import com.corochat.app.server.handlers.ServerCommand;
import com.corochat.app.utils.logger.Logger;
import com.corochat.app.utils.logger.LoggerFactory;
import com.corochat.app.utils.logger.level.Level;
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

/**
 * <h1>The main controller of the FXML App</h1>
 * <p>
 *     The LoginController class implements the Initializable interface
 *     in order to setup our panels and disable the buttons.
 *     It handle Button, Text Area and Text Input mouse events / context menu events
 * </p>
 * //TODO Include the diagram of LoginController
 *
 * @author Dray Raphael
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.1
 * @see Initializable
 * @see FXML
 * @see ChatView
 * @see Pane
 * @see Socket
 * @see AnchorPane
 * @see Circle
 * @see StackPane
 * @see Pane
 * @see TextField
 * @see Button
 * @see Label
 * @see PasswordField
 * @see ImageView
 */
public class LoginController implements Initializable {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class.getSimpleName());

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

    /**
     * Instantiate the chat view
     * @see ChatView
     */
    public LoginController() {
        this.chatView = new ChatView();
        logger.log("Login controller created", Level.INFO);
    }

    /**
     * Disable buttons and bring to front the panel sign in
     * @param url The url of the resource
     * @param resourceBundle The resource bundle
     * @see URL
     * @see ResourceBundle
     * @see LoginController#disableButtons()
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pnlSignIn.toFront();
        this.disableButtons();
        logger.log("Login controller initialized", Level.INFO);
    }

    /**
     * Disable all buttons
     */
    private void disableButtons() {
        this.btnSignIn.setDisable(true);
        this.btnGetStarted.setDisable(true);
        this.btnFinish.setDisable(true);
        this.btnForgotPasswordFirstContinue.setDisable(true);
        this.btnForgotPasswordSecondContinue.setDisable(true);
    }

    /**
     * Handle close action on the view
     * @param event The click on the button close
     * @see MouseEvent
     * @see ZoomOutDown
     */
    @FXML
    public void handleCloseAction(MouseEvent event) {
        if (event.getSource() == this.btnClose) {
            logger.log("Close action triggered", Level.INFO);
            ZoomOutDown zoomOutDown = new ZoomOutDown(this.anchRoot);
            zoomOutDown.setOnFinished(e -> System.exit(0));
            zoomOutDown.play();
        }
    }

    /**
     * Handle reduce action on the view
     * @param event The click on the reduce button
     * @see MouseEvent
     * @see Stage
     * @see Circle
     */
    @FXML
    public void handleReduceAction(MouseEvent event) {
        if (event.getSource() == this.btnReduce) {
            logger.log("Reduce action triggered", Level.INFO);
            ((Stage)((Circle) event.getSource()).getScene().getWindow()).setIconified(true);
        }
    }

    /**
     * Handle back action over all the panels
     * @param event The click on the back button of a panel
     * @see MouseEvent
     * @see LoginController#backAction(Pane)
     */
    @FXML
    public void handleBackAction(MouseEvent event) {
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack) {
            logger.log("Back action triggered", Level.INFO);
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

    /**
     * Check if an email in a text field is a correct email
     * @param email The text field that has the email
     * @return boolean - True if the email is valid, else, false
     * @see EmailValidator
     * @see EmailValidator#isValid(String)
     * @see TextField
     */
    private boolean isValidEmail(final TextField email) {
        return EmailValidator.isValid(email.getText());
    }

    /**
     * Check if the complexity of a password.
     * @param password The password to be check
     * @return boolean - True if the password contains at least
     * one lowercase, one uppercase, one number, one special character
     * and a minimum of 8 to a maximum of 32 characters
     * @see PasswordField
     * @see PasswordValidator
     * @see PasswordValidator#isValid(String)
     */
    private boolean isValidPassword(final PasswordField password) {
        return PasswordValidator.isValid(password.getText());
    }

    /**
     * Perform a back action from a pane to a previous pane
     * @param pane The pane that we want to go back
     * @see Pane
     * @see ZoomOutRight
     */
    private void backAction(final Pane pane) {
        new ZoomOutRight(this.currentPane).play();
        this.currentPane = pane;
    }

    /**
     * Perform an in action from a pane to a next pane
     * @param pane The pane that we want to go next
     * @see Pane
     * @see ZoomInRight
     */
    private void inAction(final Pane pane) {
        pane.toFront();
        new ZoomInRight(pane).play();
        this.currentPane = pane;
    }

    /**
     * Handle sign up button action
     * @param event The mouse event when we click on the button it triggers the function
     * @see MouseEvent
     * @see LoginController#inAction(Pane)
     */
    @FXML
    public void handleSignUpAction(MouseEvent event) {
        if (event.getSource() == this.btnSignUp) {
            logger.log("Sign up action triggered", Level.INFO);
            this.inAction(this.pnlSignUp);
        }
    }

    /**
     * Handle label forgot password button action
     * @param event The mouse event when we click on the button it triggers the function
     * @see MouseEvent
     * @see LoginController#inAction(Pane)
     */
    @FXML
    public void handleLabelForgotAction(MouseEvent event) {
        if (event.getSource() == this.lblForgot) {
            logger.log("Label forgot action triggered", Level.INFO);
            this.inAction(this.pnlForgotPasswordFirst);
        }
    }

    /**
     * Handle label hovering
     * @param event The mouse event when we hover on the button it triggers the function
     * @see MouseEvent
     * @see LinkSetter
     * @see LinkSetter#setAsLink(Label)
     */
    @FXML
    public void handleLabelHover(MouseEvent event) {
        if (event.getSource() == this.lblForgot ||
            event.getSource() == this.lblSendItBack) {
            logger.log("Label hover triggered", Level.INFO);
            LinkSetter.setAsLink((Label) event.getSource());
        }
    }

    /**
     * Handle label exiting
     * @param event The mouse event when we exit the range of the button it triggers the function
     * @see MouseEvent
     * @see LinkSetter
     * @see LinkSetter#unsetAsLink(Label)
     */
    @FXML
    public void handleLabelExited(MouseEvent event) {
        if (event.getSource() == this.lblForgot ||
            event.getSource() == this.lblSendItBack) {
            logger.log("Label exited triggered", Level.INFO);
            LinkSetter.unsetAsLink((Label) event.getSource());
        }
    }

    /**
     * Handle back button hovering
     * @param event The mouse event when we hover on the button it triggers the function
     * @see MouseEvent
     * @see LoginController#backHovering(ImageView)
     */
    @FXML
    public void handleBackHover(MouseEvent event) {
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack ||
            event.getSource() == this.btnForgotPasswordSecondBack ||
            event.getSource() == this.btnForgotPasswordThirdBack) {
            logger.log("Back hover action triggered", Level.INFO);
            this.backHovering((ImageView) event.getSource());
        }
    }

    /**
     * Set the image for hovering a back button
     * @param back The button to be set
     * @see ImageView
     * @see ImageSetter
     * @see ImageSetter#set(ImageView, String)
     */
    private void backHovering(final ImageView back) {
        ImageSetter.set(back, "backHover.png");
    }

    /**
     * Unset the image for unhovering a back button
     * @param back The button to be unset
     * @see ImageView
     * @see ImageSetter
     * @see ImageSetter#set(ImageView, String)
     */
    private void backUnhovering(final ImageView back) {
        ImageSetter.set(back, "back.png");
    }

    /**
     * Handle back button exiting
     * @param event The mouse event when we exit the range of the button it triggers the function
     * @see MouseEvent
     * @see LoginController#backUnhovering(ImageView)
     */
    @FXML
    public void handleBackExited(MouseEvent event) {
        if (event.getSource() == this.btnBack ||
            event.getSource() == this.btnForgotPasswordFirstBack ||
            event.getSource() == this.btnForgotPasswordSecondBack ||
            event.getSource() == this.btnForgotPasswordThirdBack) {
            logger.log("Back exited action triggered", Level.INFO);
            this.backUnhovering((ImageView) event.getSource());
        }
    }

    /**
     * Handle the sign in button enabling
     * @param keyEvent The key event when we type in the text field it triggers the function
     * @see KeyEvent
     */
    @FXML
    public void handleSignInEnable(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.tfEmail ||
            keyEvent.getSource() == this.pfPassword) {
            logger.log("Sign in enabling triggered", Level.INFO);
            if (!this.tfEmail.getText().equals("") && !this.pfPassword.getText().equals(""))
                this.btnSignIn.setDisable(false);
            else
                this.btnSignIn.setDisable(true);
        }
    }

    /**
     * Handle the get started button enabling
     * @param keyEvent The key event when we type in the text field it triggers the function
     * @see KeyEvent
     * @see StringContaining
     * @see StringContaining#numbers(String)
     * @see LoginController#isValidEmail(TextField)
     * @see LoginController#isValidPassword(PasswordField)
     */
    @FXML
    public void handleGetStartedEnable(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.tfSignUpFirstName ||
            keyEvent.getSource() == this.tfSignUpLastName  ||
            keyEvent.getSource() == this.tfSignUpPseudo    ||
            keyEvent.getSource() == this.tfSignUpEmail     ||
            keyEvent.getSource() == this.pfSignUpPassword  ||
            keyEvent.getSource() == this.pfRepeatPassword) {
            logger.log("Get started enabling triggered", Level.INFO);
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

    /**
     * Handle the log in button clicking
     * @param mouseEvent The mouse event when we click on the button it triggers the function
     * @see MouseEvent
     * @see UserModel
     * @see ClientCommand
     * @see ClientCommand#LOGIN
     * @see Socket
     * @see Scanner
     * @see PrintWriter
     * @see ServerCommand
     * @see ServerCommand#DISPLAY_SUCCESS
     * @see Gson
     * @see TypeToken
     * @see ChatView#start(Stage, UserModel, Socket, PrintWriter)
     * @see Alert
     */
    @FXML
    public void handleLogInAction(MouseEvent mouseEvent) {
        logger.log("Login action triggered", Level.INFO);
        this.btnSignUp.setDisable(true);
        this.lblForgot.setDisable(true);
        this.btnClose.setDisable(true);
        this.btnReduce.setDisable(true);


        String email = this.tfEmail.getText();
        String password = this.pfPassword.getText();

        UserModel user = new UserModel(email, password);
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
                        logger.log(e.getMessage(), Level.ERROR);
                    }
                } else if(response.startsWith(ServerCommand.DISPLAY_ERROR.getCommand())){
                    String errorMessage = response.substring(12);
                    logger.log(errorMessage, Level.WARNING);
                    Alert alert=new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(errorMessage.replace("\"",""));
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();

                } else
                    logger.log("Unknown error occurred during login", Level.ERROR);
            }
        } catch (IOException e) {
            logger.log(e.getMessage(), Level.ERROR);
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

    /**
     * Handle the get started button clicking
     * @param mouseEvent The mouse event when we click on the button it triggers the function
     * @see MouseEvent
     * @see BCrypt#hashpw(String, String)
     * @see UserModel
     * @see ClientCommand#SIGNUP
     * @see Gson
     * @see Socket
     * @see PrintWriter
     * @see ServerCommand#DISPLAY_SUCCESS
     * @see ServerCommand#DISPLAY_ERROR
     * @see ChatView#start(Stage, UserModel, Socket, PrintWriter)
     * @see Alert
     */
    public void handleGetStartedAction(MouseEvent mouseEvent) {
        logger.log("Get started action triggered", Level.INFO);
        this.btnGetStarted.setDisable(true);
        this.btnBack.setDisable(true);
        this.btnClose.setDisable(true);
        this.btnReduce.setDisable(true);

        String firstName = this.tfSignUpFirstName.getText();
        String lastName = this.tfSignUpLastName.getText();
        String email = this.tfSignUpEmail.getText();
        String pseudo = this.tfSignUpPseudo.getText();
        String password = this.pfSignUpPassword.getText();
        UserModel user = null;
        try {
            user = new UserModel(firstName, lastName, pseudo, email, BCrypt.hashpw(password,BCrypt.gensalt()));
        } catch (MalformedUserModelParameterException e) {
            e.printStackTrace();
        }
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
                    logger.log(successMessage, Level.INFO);
                    try {
                        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
                        this.chatView.start(new Stage(), user, this.socket, out);
                    } catch (Exception e) {
                        logger.log(e.getMessage(), Level.ERROR);
                    }
                } else if(response.startsWith(ServerCommand.DISPLAY_ERROR.getCommand())){
                    String errorMessage = response.substring(12);
                    logger.log(errorMessage, Level.WARNING);
                    Alert alert=new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(errorMessage.replace("\"",""));
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                } else
                    logger.log("Unknown error occurred during sign up", Level.ERROR);
            }
        } catch (IOException e) {
            logger.log(e.getMessage(), Level.ERROR);
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
