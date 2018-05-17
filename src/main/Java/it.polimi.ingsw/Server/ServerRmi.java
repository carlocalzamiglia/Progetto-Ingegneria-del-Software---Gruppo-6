package it.polimi.ingsw.Server;


import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class ServerRmi implements Runnable{
    private DBUsers DB;

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
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            ServerRmiClientHandlerInt conn = new ServerRmiClientHandler(DB);
            Naming.rebind("rmi://127.0.0.1/myabc", conn);
            System.out.println("Server rmi ready.");


        }catch (Exception e) {
            System.out.println("Chat Server failed: " + e);
        }
    }



}
