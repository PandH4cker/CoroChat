package com.corochat.app.server.handlers;

import com.corochat.app.server.MultiThreadedServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private String name;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                out.println("SUBMITNAME");
                this.name = in.nextLine();
                if (name == null)
                    return;
                synchronized (MultiThreadedServer.getNames()) {
                    if (!this.name.equals("") && !MultiThreadedServer.getNames().contains(this.name)) {
                        MultiThreadedServer.getNames().add(this.name);
                        System.out.println("Thread " + Thread.currentThread().getName());
                        break;
                    }
                }
            }

            out.println("NAMEACCEPTED " + this.name);
            for (PrintWriter writer : MultiThreadedServer.getWriters()) {
                writer.println("MESSAGE " + this.name + " has joined the chat.");
            }
            System.out.println(this.name + " has joined the chat.");
            MultiThreadedServer.getWriters().add(this.out);

            while (true) {
                String input = this.in.nextLine();
                if (input.toLowerCase().startsWith("/quit"))
                    return;
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println("MESSAGE " + this.name + ": " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.out != null)
                MultiThreadedServer.getWriters().remove(this.out);
            if (this.name != null) {
                System.out.println(this.name + " is leaving.");
                MultiThreadedServer.getNames().remove(this.name);
                for (PrintWriter writer : MultiThreadedServer.getWriters())
                    writer.println("MESSAGE " + this.name + " has left.");
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
