package Client;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRmiInt extends Remote {
    public void serverMessage(String message) throws RemoteException;

    public boolean aliveMessage() throws RemoteException;
}
