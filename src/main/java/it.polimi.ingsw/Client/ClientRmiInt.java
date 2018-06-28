package it.polimi.ingsw.Client;


import it.polimi.ingsw.ServertoClientHandler.ServertoClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRmiInt extends Remote, ServertoClient{
    public void sendMessageOut (String name)throws RemoteException ;
    public boolean aliveMessage() throws RemoteException;
    public boolean serverAlive() throws RemoteException;
}