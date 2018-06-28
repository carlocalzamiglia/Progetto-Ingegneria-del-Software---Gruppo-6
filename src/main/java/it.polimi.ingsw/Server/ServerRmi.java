package it.polimi.ingsw.Server;


import it.polimi.ingsw.Game.Matches;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerRmi implements Runnable{
    private DBUsers DB;
    private Matches matches;
    private int PORT;


    //-------------------------------------------------constructor------------------------------------------------------
    public ServerRmi(DBUsers DB,Matches matches){
        this.DB=DB;
        this.matches=matches;
    }

    //-----------------------------------------launch the connection method---------------------------------------------
    public void run() {
        ServerRmi serverRmi = new ServerRmi(DB,matches);
        try {
            serverRmi.connect();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    //---------------------------------------starts the RMI and the client handler--------------------------------------
    public void connect() throws IOException {
        try {
            PORT=leggiDaFile();
            java.rmi.registry.LocateRegistry.createRegistry(PORT);
            Registry registry = LocateRegistry.getRegistry(PORT);
            ServerRmiClientHandlerInt conn = new ServerRmiClientHandler(DB,matches);
            registry.rebind("RMICONNECTION", conn);
            System.out.println("Server rmi ready on port:"+PORT);


        }catch (Exception e) {
            System.out.println("Chat Server failed: " );
            e.printStackTrace();
        }
    }

    //-------------------------------------------------read from file---------------------------------------------------
    private int leggiDaFile() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/server_config.txt");

        BufferedReader b = new BufferedReader(f);
        int port;
        try {
            String useless = b.readLine();
            port = Integer.parseInt(b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return port;
    }



}

