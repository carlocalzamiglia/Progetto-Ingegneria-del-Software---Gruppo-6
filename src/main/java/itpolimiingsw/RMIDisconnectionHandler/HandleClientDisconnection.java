package itpolimiingsw.RMIDisconnectionHandler;

import itpolimiingsw.Server.ServerRmiClientHandlerInt;

import java.io.IOException;
import java.rmi.RemoteException;

public class HandleClientDisconnection extends Thread{
    private boolean alive=true;
    private ServerRmiClientHandlerInt serverRmi;
    private final String nickname;

    //---------------------------------------------constructor for RMI--------------------------------------------------
    public HandleClientDisconnection(String nickname, ServerRmiClientHandlerInt serverRmi){
        this.nickname=nickname;
        this.serverRmi=serverRmi;
    }

    /**
     * This method calls method "clientAlive" in "serverRMI" every second, to handle a possible disconnection.
     */
    public void run() {
        while(alive){
            try {
                sleep(1000);
                    alive = serverRmi.clientAlive(nickname);
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}