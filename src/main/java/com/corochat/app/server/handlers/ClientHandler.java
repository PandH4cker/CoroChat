package com.corochat.app.server.handlers;

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
                if (command.toLowerCase().startsWith("/login")) {
                    String userCredentials = command.substring(7);
                    UserModel givenUser = new Gson().fromJson(userCredentials, new TypeToken<UserModel>() {
                    }.getType());
                    UserModel fetchedUser = this.userRepository.getUser(givenUser.getEmail());
                    if (fetchedUser != null && BCrypt.checkpw(givenUser.getHashedPassword(), fetchedUser.getHashedPassword())) {
                        String success = new Gson().toJson(fetchedUser);
                        this.out.println("/displaySuccess " + success);
                        System.out.println(fetchedUser.getFirstName() + " is connected");
                        this.pseudo =fetchedUser.getPseudo();
                    } else {
                        String error = new Gson().toJson("Wrong email or password!");
                        this.out.println("/displayError " + error);
                        return;
                    }
                } else if (command.toLowerCase().startsWith("/signup")) {
                    String userInfo = command.substring(8);
                    UserModel user = new Gson().fromJson(userInfo, new TypeToken<UserModel>() {
                    }.getType());
                    if (this.userRepository.insertUser(user)) {
                        String success = new Gson().toJson("Account created");
                        this.out.println("/displaySuccess " + success);
                        System.out.println(user.getFirstName() + " is connected");
                        this.pseudo =user.getPseudo();
                    } else {
                        String error = new Gson().toJson("User already exists");
                        this.out.println("/displayError " + error);
                    }
                } else {
                    String error = new Gson().toJson("Please login first");
                    this.out.println("/displayError " + error);
                    return;
                }
            }

            synchronized (MultiThreadedServer.getPseudos()) {
                if (!this.pseudo.equals("") &&
                    !MultiThreadedServer.getPseudos().contains(this.pseudo) &&
                    EmailValidator.isValid(this.pseudo)) {
                    MultiThreadedServer.getPseudos().add(this.pseudo);
                }
            }

            for (PrintWriter writer : MultiThreadedServer.getWriters()) {
                writer.println("MESSAGE " + this.pseudo + " has joined the chat.");
            }
            System.out.println(this.pseudo.substring(0, this.pseudo.length()-1) + " has joined the chat.");
            MultiThreadedServer.getWriters().add(this.out);

            while (true) {
                String input = this.in.nextLine();
                System.out.println(input);
                if (input.toLowerCase().startsWith("/quit"))
                    return;
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println("MESSAGE " + this.pseudo + ": " + input);
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
                    writer.println("MESSAGE " + this.pseudo.substring(0, this.pseudo.length()-1) + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
