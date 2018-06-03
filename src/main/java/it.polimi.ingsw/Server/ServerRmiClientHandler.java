package it.polimi.ingsw.Server;


import it.polimi.ingsw.Client.ClientRmiInt;
import it.polimi.ingsw.Game.Matches;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ServerRmiClientHandler extends UnicastRemoteObject implements ServerRmiClientHandlerInt {
    public DBUsers DB ;
    private Matches matches;
    protected ServerRmiClientHandler(DBUsers DB,Matches matches) throws RemoteException{
        this.DB=DB;
        this.matches=matches;
    }


    //---------------------------------------check if login is all right------------------------------------------------
    @Override
    public int login(String nickname, String password) throws RemoteException {
        return DB.login(nickname,password) ;
    }


    //--------------------------------add on user's array the RMI connection link---------------------------------------
    public void addRmi(ClientRmiInt client, String nickname) throws RemoteException {
        DB.getUser(nickname).setRmiClient(client);
        try {
            newUserMessage(nickname);
            DB.getUser(nickname).getConnectionType().sendMessageOut("Benvenuto, "+nickname+". Ora puoi giocare!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new HandleDisconnection(nickname, this).start();
    }

    public void addToMatches(String username) throws IOException {
        matches.addUser(DB.getUser(username));
    }



    //------------------------------------new user connected message on CLI---------------------------------------------
    @Override
    public void publish(String us) throws RemoteException{
        System.out.println(us+" loggato con connessione Rmi");
    }


    //-----------------------------------send message to a certain user-------------------------------------------------
    @Override
    public void sendMessage(String nickname, String message) throws RemoteException {
        try {
            DB.getUser(nickname).getConnectionType().sendMessageOut(message);
        }catch (IOException e){}
    }


    //---------------------------------close connections when a client decides to logout--------------------------------
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


    //--------------------------------------check if client is connected yet--------------------------------------------
    public boolean clientAlive(String nickname) throws RemoteException{
        boolean flag=false;
        if(DB.getUser(nickname).isOnline()==true){
            try{
                flag=DB.getUser(nickname).getConnectionType().aliveMessage();
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


    //--------------------------------------new client connected message------------------------------------------------
    public void newUserMessage(String nickname) throws IOException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getConnectionType() != null)
                    DB.getUser(i).getConnectionType().sendMessageOut(nickname + " ha appena effettuato il login ed è pronto a giocare.");
                else if (DB.getUser(i).getConnectionType() != null)
                    DB.getUser(i).getConnectionType().sendMessageOut(nickname+" ha appena effettuato il login ed è pronto a giocare.");
            }
        }
    }
}

