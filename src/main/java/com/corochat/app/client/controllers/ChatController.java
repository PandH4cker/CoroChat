package com.corochat.app.client.controllers;

import animatefx.animation.ZoomOutDown;
import com.corochat.app.client.views.ChatView;
import com.corochat.app.client.views.LoginView;
import com.corochat.app.server.handlers.ClientHandler;
import com.google.gson.internal.$Gson$Types;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatController implements Initializable {
    private LoginView loginView;
    private List<String> arrayList;

    @FXML
    private AnchorPane anchRoot;
    @FXML
    private Pane pnlChat;
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
    private AnchorPane chatPane;

    public ChatController() {
        this.loginView = new LoginView();
        this.arrayList = new ArrayList();
    }

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
        new Thread(() -> {
            try {
                Scanner in = new Scanner(ChatView.getSocket().getInputStream());
                String message;
                while(true/*in.hasNextLine()*/){
                    if(arrayList.isEmpty())
                        message = in.nextLine();
                    else
                        message = arrayList.get(0);

                    if(message.startsWith("MESSAGE")) {
                        String userMessage = message.substring(8);//remove MESSAGE

                        if (arrayList.size() > 0) {
                            for (int i = 1; i < this.arrayList.size(); i++){
                                userMessage = userMessage + this.arrayList.get(i);
                                System.out.println("test "+userMessage);
                            }
                            this.arrayList.clear();
                        }
                        System.out.println("Message complet : "+ userMessage);

                        String[] splittedUserMessage = userMessage.split(" ", 2);
                        String pseudo = splittedUserMessage[0];
                        if(pseudo.contains(":"))
                            pseudo = pseudo.substring(0,pseudo.length()-1);
                        userMessage = splittedUserMessage[1];
                        //System.out.println(pseudo);
                        //System.out.println(ChatView.getUserModel().getPseudo());

                        if(!pseudo.equals(ChatView.getUserModel().getPseudo()))
                            sendAction(pseudo+": "+userMessage, false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        scrollPane.vvalueProperty().bind(vBox.heightProperty());
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

    public void handleEmojiAction(ActionEvent actionEvent) {
        emojiList.setVisible(!emojiList.isVisible());
    }

    public void sendAction(String message, boolean tqtfelicia){
        Text text=new Text(message); //on get le text de l'user
        text.setFill(Color.WHITE);
        Text date=new Text(new SimpleDateFormat("HH:mm").format(new Date())); //on get le text de l'user
        date.setFill(Color.WHITE);

        TextFlow textFlow = new TextFlow();
        BorderPane borderPane1 = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        BorderPane borderPane3 = new BorderPane();
        VBox vBox = new VBox(text);
        HBox hBox = new HBox(date);

        this.vBox.setPadding(new Insets(20, 20, 20, 20));
        this.vBox.setSpacing(20);
        if(tqtfelicia) {  //current user
            borderPane1.setRight(borderPane2);
            textFlow.getStyleClass().add("feliciaText");
        }
        else {
            borderPane1.setLeft(borderPane2);
            textFlow.getStyleClass().add("notFeliciaText");
        }
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.setAlignment(Pos.TOP_LEFT);

        borderPane2.setCenter(textFlow);
        textFlow.getChildren().add(borderPane3);
        borderPane3.setCenter(vBox);
        borderPane3.setBottom(hBox);
        Platform.runLater(() -> this.vBox.getChildren().add(borderPane1));
    }

    public void handleSendAction(ActionEvent actionEvent) {
        System.out.println(txtMsg.getText().trim());
        sendAction(txtMsg.getText(), true);
        ChatView.getOut().println(txtMsg.getText()); //parle sur le server
        txtMsg.setText("");
        txtMsg.requestFocus();
    }

    public void handleEnterKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if(this.arrayList.isEmpty())
                this.arrayList.add("MESSAGE "+txtMsg.getText()+"\t"); //ajout du message : MESSAGE
            else
                this.arrayList.add(txtMsg.getText()+"\t");
        }

     //   if(keyEvent.getCode()) touche suppr text
    }

    public void HandleLogoutAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        try {
           PrintWriter out = new PrintWriter(ChatView.getSocket().getOutputStream(), true);
            out.println("/quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.loginView.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSendHoverEntered(MouseEvent mouseEvent) {
        btnSend.setCursor(Cursor.HAND);
    }

    public void handleEmojiHoverEntered(MouseEvent mouseEvent) {
        btnEmoji.setCursor(Cursor.HAND);
    }

    public void handleLogoutHoverEntered(MouseEvent mouseEvent) {
        btnlogout.setCursor(Cursor.HAND);
    }
}
