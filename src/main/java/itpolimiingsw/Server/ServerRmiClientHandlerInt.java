package itpolimiingsw.Server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import itpolimiingsw.ClientController.ClientRmiInt;

public interface ServerRmiClientHandlerInt extends Remote {

    /**
     * The method add the new user, and send him a welcome messages. Send also a message to all the other users.
     * @param client is the reference to the client. Necessary to communicate with it.
     * @param nickname of the new user
     */
    void addRmi(ClientRmiInt client, String nickname) throws RemoteException;

    /**
     * The method print on the server a "new user connected" string.
     * @param us is the username
     */
    void publish(String us) throws RemoteException;

    /**
     *
     * @param us is the username
     * @param psw is the password
     * @return a number (0, 1, 2, 3) from the method DBUsers.login
     * @throws RemoteException for rmi connection
     * @throws InterruptedException
     */
    int login(String us,String psw) throws RemoteException, InterruptedException;

    /**
     * The method checks if a certain user is connected yet. In case he is not, handle all the disconnection part.
     * @param nickname is the user's name
     * @return a boolean: true if connected, false if it's not.
     * @throws IOException
     * @throws InterruptedException
     */
    boolean clientAlive(String nickname) throws IOException, InterruptedException, RemoteException;

    /**
     * The method sends a message to all the other users, when a user connects or disconnects.
     * @param nickname
     * @param message to send
     * @throws IOException for socket users.
     */
    void newUserMessage(String nickname, String message) throws IOException, RemoteException;

    /**
     * Add the new user to a game.
     * @param username
     * @throws IOException
     * @throws InterruptedException
     */
    void addToMatches(String username) throws IOException, InterruptedException, RemoteException;

    /**
     * Handles all the reconnection part. Send the user to the same game (in case is playing yet).
     * @param username
     * @param client client interface for connection
     * @return true/false if the reconnection part worked/not.
     * @throws RemoteException for rmi connection
     */
    boolean reconnectUser(String username, ClientRmiInt client) throws RemoteException;

    /**
     * A method used by the client to "ping" the server and understand if it is online.
     * @return true by default
     * @throws RemoteException for rmi connection
     */
    boolean serverConnected() throws RemoteException;
}
