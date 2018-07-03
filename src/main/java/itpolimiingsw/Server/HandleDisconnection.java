package itpolimiingsw.Server;

import java.io.IOException;
import java.rmi.RemoteException;

public class HandleDisconnection extends Thread{
    boolean alive=true;
    ServerRmiClientHandlerInt serverRmi;
    final String nickname;
    int i=0;

    //---------------------------------------------constructor for RMI--------------------------------------------------
    public HandleDisconnection(String nickname, ServerRmiClientHandlerInt serverRmi) throws RemoteException {
        this.nickname=nickname;
        this.serverRmi=serverRmi;
    }

    //----------------------------------------check if client is alive yet----------------------------------------------
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
