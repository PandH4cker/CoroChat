package com.corochat.app.client;

import com.corochat.app.client.models.UserModel;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class doReceive extends Thread {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Scanner inp;
    private boolean loggedIN;
    private String command;

    public doReceive(Socket socket, Scanner in, PrintWriter out , Scanner inp) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.inp = inp;
        this.loggedIN = false;
    }

    public doReceive(Socket socket, Scanner in, PrintWriter out, String command) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.loggedIN = false;
        this.command = command;
    }

    public void run()
    {
       // this.command = "";
       // command += "/login ";
        // command += new Gson().toJson(new UserModel(null, null, null, "dray@et.esiea.fr", "Mazgan"));
        out.println(command); //envoyer la commande au server
        while(in.hasNextLine()) {
            String response = in.nextLine();
            System.out.println(response);

            if(response.startsWith("/displaySuccess")){

            }else{

            }
        }




        /*while(in.hasNextLine()){
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
        }*/
    }
}