package it.polimi.ingsw.Server;

import java.rmi.RemoteException;

public class HandleDisconnection extends Thread{
    boolean alive=true;
    ServerRmiClientHandlerInt serverRmi;
    ServerSocketClientHandler serverSocket;
    final String nickname;

    public HandleDisconnection(String nickname, ServerRmiClientHandlerInt serverRmi) throws RemoteException {
        this.nickname=nickname;
        this.serverRmi=serverRmi;
        this.serverSocket=null;
    }

    public HandleDisconnection(String nickname, ServerSocketClientHandler serverSocket) throws RemoteException {
        this.nickname=nickname;
        System.out.println(nickname);
        this.serverSocket=serverSocket;
        this.serverRmi=null;
    }

    public void run() {
        while(10>0 && alive){
            try {
                sleep(1000);
                if(serverSocket==null) {
                    alive = serverRmi.clientAlive(nickname);
                }
                    //alive=serverSocket.clientAlive(nickname);

            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
