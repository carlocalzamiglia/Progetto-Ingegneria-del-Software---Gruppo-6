package it.polimi.ingsw.Client;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRmiInt extends Remote{
    public void tell (String name)throws RemoteException ;
    public boolean aliveMessage() throws RemoteException;
}