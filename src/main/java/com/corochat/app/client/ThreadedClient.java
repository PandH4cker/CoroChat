package com.corochat.app.client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class doReceive extends Thread {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Scanner inp;
    private boolean loggedIN;

    public doReceive(Socket socket, Scanner in, PrintWriter out , Scanner inp) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.inp = inp;
        this.loggedIN = false;
    }

    public void run()
    {
        while(in.hasNextLine()){
            String line = in.nextLine();
            if (line.startsWith("SUBMITNAME")) {
                System.out.print("Submit Name : ");
                out.println(inp.nextLine());
            } else if (line.startsWith("NAMEACCEPTED")) {
                System.out.println("-- Connected -- ");
                System.out.println("Chatter - " + line.substring(13) );
                if(!loggedIN){
                    doSent ds = new doSent(socket, in, out, inp);
                    ds.start();
                }

            } else if (line.startsWith("MESSAGE")) {
                System.out.println(" ---- "+ line.substring(8) + "\n");
            }
        }
    }
}

class doSent extends Thread {   Socket socket;
    Scanner in;
    PrintWriter out;
    Scanner inp;

    public doSent(Socket socket,Scanner in , PrintWriter out , Scanner inp ){
        this.socket = socket ;
        this.in = in ;
        this.out = out ;
        this.inp = inp ;
    }

    public void run()
    {
        while(true){
            out.println(inp.nextLine());
        }
    }
}

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


