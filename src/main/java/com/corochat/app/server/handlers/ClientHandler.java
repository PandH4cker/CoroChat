package com.corochat.app.server.handlers;

import com.corochat.app.client.communication.ClientCommand;
import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.MultiThreadedServer;
import com.corochat.app.server.data.repositories.MessageRepository;
import com.corochat.app.server.data.repositories.UserRepository;
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

public class ClientHandler implements Runnable {
    private String pseudo;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private final UserRepository userRepository = UserRepository.getInstance();
    private final MessageRepository messageRepository = MessageRepository.getInstance();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);

            if (this.in.hasNextLine()) {
                String command = this.in.nextLine();
                if (command.toLowerCase().startsWith(ClientCommand.LOGIN.getCommand())) {
                    String userCredentials = command.substring(7);
                    UserModel givenUser = new Gson().fromJson(userCredentials, new TypeToken<UserModel>() {
                    }.getType());
                    UserModel fetchedUser = this.userRepository.getUser(givenUser.getEmail());
                    if (fetchedUser != null && BCrypt.checkpw(givenUser.getHashedPassword(), fetchedUser.getHashedPassword())) {
                        String success = new Gson().toJson(fetchedUser);
                        this.out.println(ServerCommand.DISPLAY_SUCCESS.getCommand()+" " + success);
                        System.out.println(fetchedUser.getFirstName() + " is connected");
                        this.pseudo =fetchedUser.getPseudo();
                    } else {
                        String error = new Gson().toJson("Wrong email or password!");
                        this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                        return;
                    }
                } else if (command.toLowerCase().startsWith(ClientCommand.SIGNUP.getCommand())) {
                    String userInfo = command.substring(8);
                    UserModel user = new Gson().fromJson(userInfo, new TypeToken<UserModel>() {
                    }.getType());
                    try {
                        this.userRepository.insertUser(user);
                        String success = new Gson().toJson("Account created");
                        this.out.println(ServerCommand.DISPLAY_SUCCESS.getCommand()+" " + success);
                        System.out.println(user.getFirstName() + " is connected");
                        this.pseudo =user.getPseudo();
                    } catch (InterruptedException | ExecutionException e) {
                        String error = new Gson().toJson(e.getMessage().split(":",2)[1].trim());
                        this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                        return;
                    }
                } else {
                    String error = new Gson().toJson("Please login first");
                    this.out.println(ServerCommand.DISPLAY_ERROR.getCommand()+" " + error);
                    return;
                }
            }

            //this.out.println(ServerCommand.SELFCONNECTED.getCommand()+ " "+ "You have joined the chat.");
            ArrayList<Message> messages = messageRepository.getMessages(0);

            for (PrintWriter writer : MultiThreadedServer.getWriters()) {
                writer.println(ServerCommand.CONNECT.getCommand() + " " + this.pseudo + " has joined the chat.");
            }
            MultiThreadedServer.getWriters().add(this.out);
            for(String pseudo : MultiThreadedServer.getPseudos())
                this.out.println(ServerCommand.CONNECT.getCommand()+" " + pseudo + " has joined the chat.");
            MultiThreadedServer.getPseudos().add(this.pseudo);

            for(Message m: messages){
                this.out.println(ServerCommand.RETRIEVE.getCommand()+" "+ m.getDate()+"|"+ m.getUserPseudo()+"|"+m.getMessage());
            }





            while (true) {
                String input = this.in.nextLine();
                System.out.println(input);
                if (input.toLowerCase().startsWith(ClientCommand.QUIT.getCommand()))
                    return;
                else if(input.toLowerCase().startsWith(ClientCommand.DELETE_MESSAGE.getCommand())){
                    String[] splittedInput = input.split("\\|", 4);
                    String date = splittedInput[0].split(" ",2)[1];
                    String pseudo = splittedInput[1];
                    String userMessage = splittedInput[2];
                    int index = Integer.parseInt(splittedInput[3]);
                    Message message = new Message(userMessage, pseudo, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date));
                    messageRepository.deleteMessage(message);
                    System.out.println("ERROR: "+message);

                    //DELETE MESSAGE
                    for (PrintWriter writer : MultiThreadedServer.getWriters())
                        writer.println(ServerCommand.MESSAGE_DELETED.getCommand() + " " +index);
                }

                if(!input.toLowerCase().startsWith(ClientCommand.DELETE_MESSAGE.getCommand())) {
                    for (PrintWriter writer : MultiThreadedServer.getWriters())
                        writer.println(ServerCommand.MESSAGE.getCommand() + " " + this.pseudo + ": " + input);
                    messageRepository.insertMessage(new Message(input, this.pseudo, new Date()));
                }
            }







        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (this.out != null)
                MultiThreadedServer.getWriters().remove(this.out);
            if (this.pseudo != null) {
                System.out.println(this.pseudo + " is leaving.");
                MultiThreadedServer.getPseudos().remove(this.pseudo);
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println(ServerCommand.DISCONNECT.getCommand()+" " + this.pseudo + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
