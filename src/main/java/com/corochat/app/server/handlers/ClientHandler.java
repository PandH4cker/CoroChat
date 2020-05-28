package com.corochat.app.server.handlers;

import com.corochat.app.client.communication.ClientCommand;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.MultiThreadedServer;
import com.corochat.app.server.data.UserRepository;
import com.corochat.app.utils.validations.EmailValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientHandler implements Runnable {
    private String pseudo;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private final UserRepository userRepository = UserRepository.getInstance();

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
            for (PrintWriter writer : MultiThreadedServer.getWriters()) {
                writer.println(ServerCommand.CONNECT.getCommand() + " " + this.pseudo + " has joined the chat.");
            }
            MultiThreadedServer.getWriters().add(this.out);
            for(String pseudo : MultiThreadedServer.getPseudos())
                this.out.println(ServerCommand.CONNECT.getCommand()+" " + pseudo + " has joined the chat.");
            MultiThreadedServer.getPseudos().add(this.pseudo);
            while (true) {
                String input = this.in.nextLine();
                System.out.println(input);
                if (input.toLowerCase().startsWith(ClientCommand.QUIT.getCommand()))
                    return;
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println(ServerCommand.MESSAGE.getCommand()+" " + this.pseudo + ": " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.out != null)
                MultiThreadedServer.getWriters().remove(this.out);
            if (this.pseudo != null) {
                System.out.println(this.pseudo + " is leaving.");
                MultiThreadedServer.getPseudos().remove(this.pseudo);
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println(ServerCommand.DISCONNECT.getCommand()+" " + this.pseudo.substring(0, this.pseudo.length()-1) + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
