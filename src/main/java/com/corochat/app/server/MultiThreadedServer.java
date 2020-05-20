package com.corochat.app.server;

import com.corochat.app.server.handlers.ClientHandler;

import java.io.PrintWriter;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class MultiThreadedServer {
    private static final int DEFAULT_PORT = 8080;
    private int port;
    private String host;
    private static Set<String> emails = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();

    /**
     * Constructor
     * @param host
     * @param port
     */
    public MultiThreadedServer(final String host, final int port) {
        this.host = host;
        try {
            if (port > 65536) {
                this.port = DEFAULT_PORT;
                throw new PortUnreachableException("Port higher than 65536");
            }
            this.port = port;
        } catch (PortUnreachableException e) {
            e.printStackTrace();
        }
    }

    public int actualPort() {
        return this.port;
    }

    public static Set<String> getEmails() {
        return emails;
    }

    public static Set<PrintWriter> getWriters() {
        return writers;
    }

    public static void main(String[] args) throws Exception {
        MultiThreadedServer server = new MultiThreadedServer("localhost", 4444);
        System.out.println("Listening on port " + server.actualPort());
        try(ServerSocket listener = new ServerSocket(server.actualPort())) {
            //noinspection InfiniteLoopStatement
            while (true) new Thread(new ClientHandler(listener.accept())).start();
        }
    }
}
