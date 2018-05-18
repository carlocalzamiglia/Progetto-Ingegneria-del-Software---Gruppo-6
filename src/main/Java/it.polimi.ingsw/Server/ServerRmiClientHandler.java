package it.polimi.ingsw.Server;


import it.polimi.ingsw.Client.ClientRmiInt;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ServerRmiClientHandler extends UnicastRemoteObject implements ServerRmiClientHandlerInt {
    public DBUsers DB ;
    protected ServerRmiClientHandler(DBUsers DB) throws RemoteException{
        this.DB=DB;
    }
    @Override
    public int login(String nickname, String password) throws RemoteException {
        return DB.login(nickname,password) ;
    }
    public void addRmi(ClientRmiInt client, String nickname) throws RemoteException{
            DB.getUser(nickname).setRmiClient(client);
            new HandleDisconnection(nickname, this).start();
    }
    @Override
    public void publish(String us) throws RemoteException{
        System.out.println(us+" loggato con connessione Rmi");
    }
    @Override
    public void sendMessage(String nickname, String s) throws RemoteException {
        System.out.println(nickname+": "+s);
    }
    @Override
    public Vector getConnected() throws RemoteException {
        return null;
    }

    public boolean manageDisconnection(String nickname) throws RemoteException{
        try {
            DB.getUser(nickname).setOnline(false);
            DB.getUser(nickname).setRmiClient(null);
            System.out.println(nickname+" si è appena disconnesso");
            return true;
        }catch(Exception e){
            return false;
        }

    }

    public boolean clientAlive(String nickname) throws RemoteException{
        boolean flag=false;
        if(DB.getUser(nickname).isOnline()==true){
            try{
                flag=DB.getUser(nickname).getClient().aliveMessage();
            }catch (Exception e){
                flag=false;
                DB.getUser(nickname).setOnline(false);
                DB.getUser(nickname).setRmiClient(null);
                System.out.println(nickname+" ha perso la connessione ed è stato rimosso dal server");
            }
        }else
            flag=false;

        return flag;
    }
}

