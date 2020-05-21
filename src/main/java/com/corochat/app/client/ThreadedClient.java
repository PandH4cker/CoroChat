package com.corochat.app.client;

import com.corochat.app.client.models.UserModel;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ThreadedClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 4444);
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner input = new Scanner(System.in);
        doReceive dr = new doReceive(socket, in, out, input);
        dr.start();
    }
}


