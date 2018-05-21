package it.polimi.ingsw.Server;


import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class ServerRmi implements Runnable{
    private DBUsers DB;
    private int PORT;
    public ServerRmi(DBUsers DB){
        this.DB=DB;
    }





    public void run() {
        ServerRmi serverRmi = new ServerRmi(DB);
        try {
            serverRmi.connect();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }




    public void connect() throws IOException {
        try {
            PORT=leggiDaFile();
            java.rmi.registry.LocateRegistry.createRegistry(PORT);

            ServerRmiClientHandlerInt conn = new ServerRmiClientHandler(DB);
            Naming.rebind("rmi://localhost/ser_con", conn);
            System.out.println("Server rmi ready on port:"+PORT);


        }catch (Exception e) {
            System.out.println("Chat Server failed: " );
            e.printStackTrace();
        }
    }

    private int leggiDaFile() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/server_config.txt");

        BufferedReader b = new BufferedReader(f);
        int port;
        try {
            b.readLine();
            port = Integer.parseInt(b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return port;
    }



}

