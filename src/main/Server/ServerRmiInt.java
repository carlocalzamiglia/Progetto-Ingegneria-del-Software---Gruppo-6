package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRmiInt extends Remote {
    //metodi che chiamerà client per parlare col server
    public boolean checkUser(String nickname) throws RemoteException;
    public void login(String nickname) throws RemoteException;
    public void sendMessage(String nickname, String message)throws RemoteException;
    public void manageDisconnection(String nickname) throws RemoteException;
    public boolean clientAlive(String nickname) throws RemoteException;
}
