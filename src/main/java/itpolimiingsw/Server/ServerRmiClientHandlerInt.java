package itpolimiingsw.Server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import itpolimiingsw.Client.ClientRmiInt;

public interface ServerRmiClientHandlerInt extends Remote {
    public void sendMessage (String nickname,String s)throws RemoteException ;
    public void addRmi(ClientRmiInt client, String nickname) throws RemoteException;
    public void publish(String us)throws RemoteException;
    public int login(String us,String psw) throws RemoteException, InterruptedException;
    public boolean manageDisconnection(String nickname) throws RemoteException;
    public boolean clientAlive(String nickname) throws RemoteException;
    public void newUserMessage(String nickname) throws IOException;
    public void addToMatches(String username) throws IOException, InterruptedException;
    public boolean reconnectUser(String username, ClientRmiInt client) throws RemoteException;
    public boolean serverConnected() throws RemoteException;
}