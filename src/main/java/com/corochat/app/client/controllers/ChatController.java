package com.corochat.app.client.controllers;

import animatefx.animation.ZoomOutDown;
import com.corochat.app.client.communications.ClientCommand;
import com.corochat.app.client.views.ChatView;
import com.corochat.app.client.views.LoginView;
import com.corochat.app.client.communications.ServerCommand;
import com.corochat.app.client.utils.logger.Logger;
import com.corochat.app.client.utils.logger.LoggerFactory;
import com.corochat.app.client.utils.logger.level.Level;
import com.corochat.app.client.utils.setters.ImageSetter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <h1>The second view that observe actions on the chat view</h1>
 * <p>
 *     This ChatController implements the Initializable interface
 *     in order to setup the emoji text flow, start a thread to listen the server actions
 *     and setup the vbars and hbars
 *     It handle Button, Text Area and Text Input mouse events / context menu events
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.8
 * @since 0.0.3
 * @see Initializable
 * @see LoginView
 * @see AnchorPane
 * @see Circle
 * @see Button
 * @see TextFlow
 * @see ImageView
 * @see TextArea
 * @see ScrollPane
 * @see VBox
 * @see Label
 */
public class ChatController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class.getSimpleName());

    private LoginView loginView;
    private Thread thread;

    @FXML
    private AnchorPane anchRoot;
    @FXML
    private Circle btnClose;
    @FXML
    private Circle btnReduce;
    @FXML
    private Button btnSend;
    @FXML
    private TextFlow emojiList;
    @FXML
    private Button btnEmoji;
    @FXML
    private ImageView btnlogout;
    @FXML
    private TextArea txtMsg;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private VBox vBoxUserList;
    @FXML
    private Label labelCorochat;
    @FXML
    private ScrollPane userScrollPane;

    /**
     * Instantiate the login view
     * @see LoginView
     */
    public ChatController() {
        this.loginView = new LoginView();
        logger.log("Chat controller created", Level.INFO);
    }

    /**
     * Initialize the emojis in the text flow.
     * Start a new thread to listen on the server actions and perform the update of the UI
     * @param url The url of the resource
     * @param resourceBundle The resource bundle
     * @see URL
     * @see ResourceBundle
     * @see Node
     * @see Thread
     * @see Scanner
     * @see ServerCommand#MESSAGE
     * @see ChatController#sendAction(String, boolean)
     * @see ChatController#sendAction(String, String, Calendar, boolean)
     * @see ServerCommand#CONNECT
     * @see Text
     * @see Platform#runLater(Runnable)
     * @see ServerCommand#DISCONNECT
     * @see ServerCommand#RETRIEVE
     * @see Date
     * @see SimpleDateFormat
     * @see Calendar
     * @see GregorianCalendar
     * @see ServerCommand#MESSAGE_DELETED
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                if(txtMsg.getText().equals(""))
                    txtMsg.setText(txtMsg.getText()+""+((Text)text).getText());
                else
                    txtMsg.setText(txtMsg.getText()+" "+((Text)text).getText());
                emojiList.setVisible(false);
            });
        }
        logger.log("Added emojis to the text flow box", Level.INFO);

        //Update on each client
        thread = new Thread(() -> {
            try {
                Scanner in = new Scanner(ChatView.getSocket().getInputStream());
                while(in.hasNextLine()){
                    String message=in.nextLine();
                    //Send message
                    if(message.startsWith(ServerCommand.MESSAGE.getCommand())) {
                        String userMessage = message.substring(8);
                        String[] splittedUserMessage = userMessage.split(" ", 2);
                        String pseudo = splittedUserMessage[0];
                        if(pseudo.contains(":"))
                            pseudo = pseudo.substring(0,pseudo.length()-1);
                        userMessage = splittedUserMessage[1].replace("\t","\n");
                        if(!pseudo.equals(ChatView.getUserModel().getPseudo()))
                            sendAction(pseudo+": "+userMessage, false);
                    } else if(message.startsWith(ServerCommand.CONNECT.getCommand())){
                        //Add user in userList
                        String userMessage = message;
                        String[] splittedUserMessage = message.split(" ", 3);
                        String pseudo = splittedUserMessage[1];
                        this.vBoxUserList.setPadding(new Insets(20, 20, 20, 20));
                        this.vBoxUserList.setSpacing(20);
                        Text text=new Text(pseudo); //on get le text de l'user
                        text.setFill(Color.BLACK);

                        Platform.runLater(() -> {
                            this.vBoxUserList.getChildren().add(text);
                            //sendAction(userMessage, true);
                        });
                    } else if (message.startsWith(ServerCommand.SELFCONNECTED.getCommand())){
                        //Add self connected
                        String userMessage = message.substring(14);

                        Platform.runLater(() -> {
                            sendAction(userMessage, true);
                        });
                    } else if(message.startsWith(ServerCommand.DISCONNECT.getCommand())){
                        String[] splittedUserMessage = message.split(" ", 3);
                        String pseudo = splittedUserMessage[1];

                        Text tempText;
                        int i=0;
                        while(true){
                            if(i < this.vBoxUserList.getChildren().size()) {
                                tempText = (Text) this.vBoxUserList.getChildren().get(i);
                                tempText.getText();
                                if (tempText.getText().equals(pseudo)) {
                                    int j = i;
                                    Platform.runLater(() -> {
                                        this.vBoxUserList.getChildren().remove(j);
                                    });
                                    i = 0;
                                }
                                i++;
                            }
                            else{
                                break;
                            }
                        }
                    } else if (message.startsWith(ServerCommand.RETRIEVE.getCommand())){
                        //Retrieve messages
                        message = message.substring(9);
                        String[] splittedUserMessage = message.split("\\|", 3);
                        String dateTime = splittedUserMessage[0];
                        String userPseudo = splittedUserMessage[1];
                        String userMessage = splittedUserMessage[2];
                        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(dateTime);
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        sendAction(userMessage.replace("\t", "\n"), userPseudo, calendar, userPseudo.equals(ChatView.getUserModel().getPseudo()));
                    } else if(message.startsWith(ServerCommand.MESSAGE_DELETED.getCommand())) {
                        message = message.substring(15);
                        int index = Integer.parseInt(message);

                        Platform.runLater(() -> {
                            this.vBox.getChildren().remove(index);
                        });
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        logger.log("Thread started for listening on the server requests", Level.INFO);
        userScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        userScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.vvalueProperty().bind(vBox.heightProperty());
        logger.log("Chat controller initialized", Level.INFO);
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
            zoomOutDown.setOnFinished(e -> {
                ChatView.getOut().println(ClientCommand.QUIT.getCommand());
                thread.interrupt();
                System.exit(0);
            });
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
     * Handle emojis displaying
     * @param actionEvent The click on the button to display the emojis
     * @see ActionEvent
     */
    @FXML
    public void handleEmojiAction(ActionEvent actionEvent) {
        logger.log("Emoji action triggered", Level.INFO);
        emojiList.setVisible(!emojiList.isVisible());
    }

    /**
     * Send message to other users and add it to our view
     * @param message The message to be sent
     * @param pseudo The pseudo who sends the message
     * @param calendar The date of when the message is sent
     * @param tqtfelicia True if it's our message, else, false
     * @see Text
     * @see TextFlow
     * @see BorderPane
     * @see ImageView
     * @see ImageView#setOnMouseClicked(EventHandler)
     * @see ImageView#setOnMouseEntered(EventHandler)
     * @see VBox
     * @see HBox
     * @see DropShadow
     * @see Platform#runLater(Runnable)
     */
    private void sendAction(String message, String pseudo, Calendar calendar, boolean tqtfelicia){
        Text date=new Text(
                ((calendar.get(Calendar.HOUR_OF_DAY)<10)
                        ?"0"+(calendar.get(Calendar.HOUR_OF_DAY))
                        :(calendar.get(Calendar.HOUR_OF_DAY))) +":"+
                        (
                        (calendar.get(Calendar.MINUTE)<10)
                                ? "0"+(calendar.get(Calendar.MINUTE))
                                :(calendar.get(Calendar.MINUTE))
                        )
        );
        date.setFill(Color.WHITE);

        TextFlow textFlow = new TextFlow();
        BorderPane borderPane1 = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        BorderPane borderPane3 = new BorderPane();
        BorderPane borderPane4 = new BorderPane();
        ImageView imageView = new ImageView();
        if(tqtfelicia) {
            imageView.setOnMouseClicked(mouseEvent -> {
                ImageView imageView1 = (ImageView) mouseEvent.getSource();
                labelCorochat.requestFocus();
                BorderPane borderPane4Felicia = (BorderPane) imageView1.getParent();
                BorderPane borderPane3Felicia = (BorderPane) borderPane4Felicia.getParent();
                TextFlow textFlowFelicia = (TextFlow) borderPane3Felicia.getParent();
                BorderPane borderPane2Felicia = (BorderPane) textFlowFelicia.getParent();
                BorderPane borderPane1Felicia = (BorderPane) borderPane2Felicia.getParent();

                ChatView.getOut().println(ClientCommand.DELETE_MESSAGE.getCommand() + " " +
                        new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(calendar.getTime()) +"|" +
                        pseudo+"|" +
                        this.vBox.getChildren().indexOf(borderPane1Felicia)+"|"+message.replace("\n", "\t"));
            });

            imageView.setOnMouseEntered(mouseEvent -> {
                imageView.setCursor(Cursor.HAND);
            });
            ImageSetter.set(imageView, "DeleteBtn.png");
        }
        borderPane3.setMinWidth(150);

        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        VBox vBox;
        HBox hBox = new HBox(date);

        this.vBox.setPadding(new Insets(20, 20, 20, 20));
        this.vBox.setSpacing(20);
        if(tqtfelicia) {  //current user
            Text text=new Text(message);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Verdana",13));
            vBox = new VBox(text);
            borderPane1.setRight(borderPane2);
            textFlow.getStyleClass().add("feliciaText");
            borderPane4.setRight(imageView); //ajout a droite
        }
        else {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetY(4.0f);
            dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));

            Text text=new Text(message);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Verdana",13));

            Text pseudoText = new Text(pseudo);
            pseudoText.setFill(Color.BLACK);
            pseudoText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            pseudoText.setEffect(dropShadow);
            pseudoText.setCache(true);


            borderPane4.setLeft(pseudoText);
            vBox = new VBox(text);

            borderPane1.setLeft(borderPane2);
            textFlow.getStyleClass().add("notFeliciaText");
        }
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.setAlignment(Pos.TOP_LEFT);

        borderPane2.setCenter(textFlow);
        textFlow.getChildren().add(borderPane3);
        borderPane3.setCenter(vBox);
        borderPane3.setBottom(hBox);
        borderPane3.setTop(borderPane4);
        Platform.runLater(() -> this.vBox.getChildren().add(borderPane1));
        logger.log("New message has been added to the view", Level.INFO);
    }


    //for current user send
    /**
     * Send message to other users and add it to our view
     * @param message The message to be sent
     * @param tqtfelicia True if it's our message, else, false
     * @see Date
     * @see Text
     * @see TextFlow
     * @see BorderPane
     * @see ImageView
     * @see ServerCommand#CONNECT
     * @see ImageView#setOnMouseClicked(EventHandler)
     * @see ImageView#setOnMouseEntered(EventHandler)
     * @see VBox
     * @see HBox
     * @see DropShadow
     * @see Platform#runLater(Runnable)
     */
    private void sendAction(String message, boolean tqtfelicia){
        Date messageDate = new Date();
        Text date=new Text(new SimpleDateFormat("HH:mm").format(messageDate));
        date.setFill(Color.WHITE);

        TextFlow textFlow = new TextFlow();
        BorderPane borderPane1 = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        BorderPane borderPane3 = new BorderPane();
        BorderPane borderPane4 = new BorderPane();
        ImageView imageView = new ImageView();
        if(tqtfelicia && !message.startsWith(ServerCommand.CONNECT.getCommand())) {
            imageView.setOnMouseClicked(mouseEvent -> {
                ImageView imageView1 = (ImageView) mouseEvent.getSource();
                labelCorochat.requestFocus();
                BorderPane borderPane4Felicia = (BorderPane) imageView1.getParent();
                BorderPane borderPane3Felicia = (BorderPane) borderPane4Felicia.getParent();
                TextFlow textFlowFelicia = (TextFlow) borderPane3Felicia.getParent();
                BorderPane borderPane2Felicia = (BorderPane) textFlowFelicia.getParent();
                BorderPane borderPane1Felicia = (BorderPane) borderPane2Felicia.getParent();


                ChatView.getOut().println(ClientCommand.DELETE_MESSAGE.getCommand() + " " +
                        new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(messageDate) +"|" +
                        ChatView.getUserModel().getPseudo()+"|" +
                        this.vBox.getChildren().indexOf(borderPane1Felicia)+"|"+message.replace("\n", "\t"));
            });

            imageView.setOnMouseEntered(mouseEvent -> {
                imageView.setCursor(Cursor.HAND);
            });
            ImageSetter.set(imageView, "DeleteBtn.png");
        }
        borderPane3.setMinWidth(150);

        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        VBox vBox;
        HBox hBox = new HBox(date);

        this.vBox.setPadding(new Insets(20, 20, 20, 20));
        this.vBox.setSpacing(20);
        if(tqtfelicia) { //current user
            Text text;
            if(!message.startsWith(ServerCommand.CONNECT.getCommand()))
                text=new Text(message);
            else
                text=new Text(message.substring(8));
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Verdana",13));
            vBox = new VBox(text);
            borderPane1.setRight(borderPane2);
            textFlow.getStyleClass().add("feliciaText");
            if(!message.startsWith(ServerCommand.CONNECT.getCommand()))
                borderPane4.setRight(imageView);
        }
        else {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetY(4.0f);
            dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));

            String[] splittedMessage = message.split(" ", 2);
            splittedMessage[0]=splittedMessage[0].substring(0,splittedMessage[0].length()-1);
            splittedMessage[1]=splittedMessage[1].trim();

            Text text=new Text(splittedMessage[1]);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Verdana",13));

            Text pseudoText = new Text(splittedMessage[0]);
            pseudoText.setFill(Color.BLACK);
            pseudoText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            pseudoText.setEffect(dropShadow);
            pseudoText.setCache(true);

            borderPane4.setLeft(pseudoText);
            vBox = new VBox(text);

            borderPane1.setLeft(borderPane2);
            textFlow.getStyleClass().add("notFeliciaText");
        }
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.setAlignment(Pos.TOP_LEFT);

        borderPane2.setCenter(textFlow);
        textFlow.getChildren().add(borderPane3);
        borderPane3.setCenter(vBox);
        borderPane3.setBottom(hBox);
        borderPane3.setTop(borderPane4);

        Platform.runLater(() -> this.vBox.getChildren().add(borderPane1));
        logger.log("New message has been added to the view", Level.INFO);
    }

    /**
     * Handle send button action
     * @param actionEvent The click on the send button
     * @see ActionEvent
     * @see ChatController#sendAction(String, boolean)
     */
    @FXML
    public void handleSendAction(ActionEvent actionEvent) {
        logger.log("Send action triggered", Level.INFO);
        sendAction(txtMsg.getText(), true);
        txtMsg.setText(txtMsg.getText().replace("\n","\t"));
        ChatView.getOut().println(txtMsg.getText()); //parle sur le server
        txtMsg.setText("");
        txtMsg.requestFocus();
    }

    /**
     * Handle log out button action
     * @param mouseEvent The click on the log out button action
     * @see ClientCommand#QUIT
     * @see LoginView#start(Stage)
     */
    @FXML
    public void HandleLogoutAction(MouseEvent mouseEvent) {
        logger.log("Logout action triggered", Level.INFO);
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        ChatView.getOut().println(ClientCommand.QUIT.getCommand());
        try {
            this.loginView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSendHoverEntered(MouseEvent mouseEvent) {
        btnSend.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleEmojiHoverEntered(MouseEvent mouseEvent) {
        btnEmoji.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleLogoutHoverEntered(MouseEvent mouseEvent) {
        btnlogout.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleCloseHoverEntered(MouseEvent mouseEvent) {
        btnClose.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleReduceHoverEntered(MouseEvent mouseEvent) {
        btnReduce.setCursor(Cursor.HAND);
    }
}
