package com.corochat.app.server.handlers;

import com.corochat.app.client.communication.ClientCommand;
import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.models.exceptions.MalformedMessageParameterException;
import com.corochat.app.server.MultiThreadedServer;
import com.corochat.app.server.data.repositories.MessageRepository;
import com.corochat.app.server.data.repositories.UserRepository;
import com.corochat.app.utils.logger.Logger;
import com.corochat.app.utils.logger.LoggerFactory;
import com.corochat.app.utils.logger.level.Level;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * <h1>The ClientHandler object</h1>
 * <p>
 *     The ClientHandler class implements the interface Runnable in order to be run in a Thread.
 *     It handles client interaction/communication with the server asynchronously.
 * </p>
 * //TODO Include diagram of ClientHandler
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.1
 * @see Runnable
 * @see Socket
 * @see Scanner
 * @see PrintWriter
 */
public class ClientHandler implements Runnable {
    private String pseudo;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final MessageRepository messageRepository = MessageRepository.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class.getSimpleName());

    /**
     * This constructor initialize its attributes
     * @param socket The user socket for communicating with the server
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
        logger.log("Creating a new client handler", Level.INFO);
    }

    /**
     * Perform checks over the received command.
     * Log in or Sign up an user.
     * Send messages to the other users.
     */
    @Override
    public void run() {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);

            if (this.in.hasNextLine()) {
                String command = this.in.nextLine();
                logger.log("Command received", Level.INFO);
                if (command.toLowerCase().startsWith(ClientCommand.LOGIN.getCommand())) {
                    logger.log("Login requested", Level.INFO);
                    String userCredentials = command.substring(7);
                    UserModel givenUser = new Gson().fromJson(userCredentials, new TypeToken<UserModel>() {}.getType());
                    UserModel fetchedUser = this.userRepository.getUser(givenUser.getEmail());
                    if (fetchedUser != null && BCrypt.checkpw(givenUser.getHashedPassword(), fetchedUser.getHashedPassword())) {
                        logger.log("User fetched from database with the email: " + givenUser.getEmail(), Level.INFO);
                        if(MultiThreadedServer.getPseudos().contains(fetchedUser.getPseudo())){
                            String error = new Gson().toJson("An user with the same pseudo is already connected!");
                            this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                            logger.log("Error during login request, an user with the same pseudo is already connected", Level.INFO);
                            return;
                        }

                        String success = new Gson().toJson(fetchedUser);
                        this.out.println(ServerCommand.DISPLAY_SUCCESS.getCommand()+" " + success);
                        logger.log(fetchedUser.getFirstName() + " is connected", Level.INFO);
                        this.pseudo = fetchedUser.getPseudo();
                    } else {
                        String error = new Gson().toJson("Wrong email or password!");
                        this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                        logger.log("Error during login request, wrong email or password given.", Level.INFO);
                        return;
                    }
                } else if (command.toLowerCase().startsWith(ClientCommand.SIGNUP.getCommand())) {
                    logger.log("Signup requested", Level.INFO);
                    String userInfo = command.substring(8);
                    UserModel user = new Gson().fromJson(userInfo, new TypeToken<UserModel>() {
                    }.getType());
                    try {
                        this.userRepository.insertUser(user);
                        String success = new Gson().toJson("Account created");
                        this.out.println(ServerCommand.DISPLAY_SUCCESS.getCommand()+" " + success);
                        logger.log(user.getFirstName() + " is connected", Level.INFO);
                        this.pseudo = user.getPseudo();
                    } catch (InterruptedException | ExecutionException e) {
                        String error = new Gson().toJson(e.getMessage().split(":",2)[1].trim());
                        this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                        logger.log("Error during sign up request: " + e.getMessage(), Level.WARNING);
                        return;
                    }
                } else {
                    String error = new Gson().toJson("Please login first");
                    this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                    logger.log("An user attempted to provide another command other than `/login` or `/signup`", Level.INFO);
                    return;
                }
            }

            logger.log("Retrieving messages from database", Level.INFO);
            ArrayList<Message> messages = messageRepository.getMessages(0);
            logger.log("Sending the information of a new user has connected to the chat", Level.INFO);
            for (PrintWriter writer : MultiThreadedServer.getWriters())
                writer.println(ServerCommand.CONNECT.getCommand() + " " + this.pseudo + " has joined the chat.");
            MultiThreadedServer.getWriters().add(this.out);
            for(String pseudo : MultiThreadedServer.getPseudos())
                this.out.println(ServerCommand.CONNECT.getCommand()+" " + pseudo + " has joined the chat.");
            MultiThreadedServer.getPseudos().add(this.pseudo);
            logger.log("Sending retrieved messages", Level.INFO);
            for(Message m: messages)
                this.out.println(ServerCommand.RETRIEVE.getCommand()+" "+ m.getDate()+"|"+ m.getUserPseudo()+"|"+m.getMessage());
            while (true) {
                String input = this.in.nextLine();
                if (input.toLowerCase().startsWith(ClientCommand.QUIT.getCommand())){
                    logger.log("Quit command received for " + this.pseudo, Level.INFO);
                    return;
                }
                else if(input.toLowerCase().startsWith(ClientCommand.DELETE_MESSAGE.getCommand())){
                    logger.log("Delete message command received for " + this.pseudo, Level.INFO);
                    String[] splittedInput = input.split("\\|", 4);
                    String date = splittedInput[0].split(" ",2)[1];
                    String pseudo = splittedInput[1];
                    int index = Integer.parseInt(splittedInput[2]);
                    String userMessage = splittedInput[3];
                    Message message = null;
                    try {
                        message = new Message(userMessage, pseudo, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date));
                    } catch (MalformedMessageParameterException e) {
                        e.printStackTrace();
                    }
                    messageRepository.deleteMessage(message);

                    logger.log("Sending information to delete the messages to other clients", Level.INFO);
                    for (PrintWriter writer : MultiThreadedServer.getWriters())
                        writer.println(ServerCommand.MESSAGE_DELETED.getCommand() + " " +index);
                }

                if(!input.toLowerCase().startsWith(ClientCommand.DELETE_MESSAGE.getCommand())) {
                    logger.log("Sending the message to other clients", Level.INFO);
                    for (PrintWriter writer : MultiThreadedServer.getWriters())
                        writer.println(ServerCommand.MESSAGE.getCommand() + " " + this.pseudo + ": " + input);
                    try {
                        messageRepository.insertMessage(new Message(input, this.pseudo, new Date()));
                    } catch (MalformedMessageParameterException e) {
                        e.printStackTrace();
                    }
                }
            }







        } catch (IOException | ParseException e) {
            logger.log(e.getMessage(), Level.ERROR);
        } finally {
            if (this.out != null)
                MultiThreadedServer.getWriters().remove(this.out);
            if (this.pseudo != null) {
                logger.log(this.pseudo + " is leaving.", Level.INFO);
                MultiThreadedServer.getPseudos().remove(this.pseudo);
                logger.log("Sending disconnection to other clients", Level.INFO);
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println(ServerCommand.DISCONNECT.getCommand()+" " + this.pseudo + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                logger.log(e.getMessage(), Level.ERROR);
            }
        }
    }
}
