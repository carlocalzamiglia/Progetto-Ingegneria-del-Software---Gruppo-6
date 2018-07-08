package itpolimiingsw.ClientController;


import itpolimiingsw.ClientController.ServertoClient;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRmiInt extends Remote, ServertoClient{

    /**
     * Forwards a message to the client.
     * @param name
     * @throws RemoteException
     */
    void sendMessageOut (String name)throws RemoteException ;

    /**
     * Ping method.
     * @returns a value if the client is still connected.
     * @throws RemoteException if the client is disconnected.
     */
    boolean aliveMessage() throws RemoteException;

    /**
     * Ping method for the server
     * @returns a value if the server is still up.
     * @throws RemoteException if the server is down.
     */
    boolean serverAlive() throws RemoteException;
}