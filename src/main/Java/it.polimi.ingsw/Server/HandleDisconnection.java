package it.polimi.ingsw.Server;

import java.io.IOException;
import java.rmi.RemoteException;

public class HandleDisconnection extends Thread{
    boolean alive=true;
    ServerRmiClientHandlerInt serverRmi;
    ServerSocketClientHandler serverSocket;
    final String nickname;
    int i=0;

    public HandleDisconnection(String nickname, ServerRmiClientHandlerInt serverRmi) throws RemoteException {
        this.nickname=nickname;
        this.serverRmi=serverRmi;
        this.serverSocket=null;
    }

    public HandleDisconnection(String nickname, ServerSocketClientHandler serverSocket) throws RemoteException {
        this.nickname=nickname;
        this.serverSocket=serverSocket;
        this.serverRmi=null;
    }

    public void run() {
        while(alive){
            try {
                sleep(10000);
                if(serverSocket==null) {
                    alive = serverRmi.clientAlive(nickname);
                }else {
                    alive = serverSocket.clientAlive(nickname);
                }

            } catch (InterruptedException | RemoteException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
