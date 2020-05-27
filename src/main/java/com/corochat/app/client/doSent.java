package com.corochat.app.client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class doSent extends Thread {
    Socket socket;
    Scanner in;
    PrintWriter out;
    Scanner inp;

    public doSent(Socket socket,Scanner in , PrintWriter out , Scanner inp ){
        this.socket = socket ;
        this.in = in ;
        this.out = out ;
        this.inp = inp ;
    }

    public void run() {
        while(true) out.println(inp.nextLine());
    }
}

