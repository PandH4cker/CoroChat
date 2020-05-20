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
    private String email;
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

            while (this.in.hasNextLine()) {
                String command = this.in.nextLine();
                if (command.toLowerCase().startsWith("/login")) {
                    String userCredentials = command.substring(7);
                    UserModel givenUser = new Gson().fromJson(userCredentials, new TypeToken<UserModel>(){}.getType());
                    UserModel fetchedUser = this.userRepository.getUser(givenUser.getEmail());
                    if (fetchedUser != null && BCrypt.checkpw(givenUser.getHashedPassword(), fetchedUser.getHashedPassword())) {
                            this.out.println("Welcome back " + fetchedUser.getFirstName() + "!");
                    } else {
                        out.println("Wrong email or password!");
                        return;
                    }
                } else if (command.toLowerCase().startsWith("/signup")) {
                    String userInfo = command.substring(8);
                    UserModel user = new Gson().fromJson(userInfo, new TypeToken<UserModel>(){}.getType());
                    this.userRepository.insertUser(user);
                    this.out.println("Welcome " + user.getFirstName() + " you just signed up!");
                } else {
                    out.println("You must be logged first !");
                    return;
                }

                /*out.println("SUBMITNAME");
                this.email = in.nextLine();
                if (email == null)
                    return;
                synchronized (MultiThreadedServer.getEmails()) {
                    if (!this.email.equals("") &&
                        !MultiThreadedServer.getEmails().contains(this.email) &&
                        EmailValidator.isValid(this.email)) {
                        MultiThreadedServer.getEmails().add(this.email);
                        System.out.println("Thread " + Thread.currentThread().getName());
                        break;
                    }
                }*/
            }

            /*out.println("NAMEACCEPTED " + this.email);
            for (PrintWriter writer : MultiThreadedServer.getWriters()) {
                writer.println("MESSAGE " + this.email + " has joined the chat.");
            }
            System.out.println(this.email + " has joined the chat.");
            MultiThreadedServer.getWriters().add(this.out);

            while (true) {
                String input = this.in.nextLine();
                if (input.toLowerCase().startsWith("/quit"))
                    return;
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println("MESSAGE " + this.email + ": " + input);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.out != null)
                MultiThreadedServer.getWriters().remove(this.out);
            if (this.email != null) {
                System.out.println(this.email + " is leaving.");
                MultiThreadedServer.getEmails().remove(this.email);
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println("MESSAGE " + this.email + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
