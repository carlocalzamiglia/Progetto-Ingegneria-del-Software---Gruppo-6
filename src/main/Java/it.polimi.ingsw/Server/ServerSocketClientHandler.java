package it.polimi.ingsw.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerSocketClientHandler implements Runnable {
    private Socket socket;
    private DBUsers DB;

    public ServerSocketClientHandler(Socket socket, DBUsers DB) {
        this.socket = socket;
        this.DB=DB;
    }

    @Override
    public void run() {
        try {

            Scanner in = new Scanner(socket.getInputStream());
            String user = in.nextLine();
            String psw = in.nextLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while(DB.login(user, psw)!=0 && DB.login(user,psw)!=1){
                out.println(false);
                out.flush();
                user = in.nextLine();
                psw = in.nextLine();
            }
            out.println(true);
            out.flush();
            DB.getUser(user).setClientHandler(this);
            String nome=in.nextLine();
            System.out.println(nome+" loggato con connessione socket");
            //for now we did't implement the complete protocol for the socket comunication but it will be implement in this while loop
            //this is for the server part
            while(1>0) {
                String message = in.nextLine();
                String name = in.nextLine();
                System.out.println(name+": "+message);
            }
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}

