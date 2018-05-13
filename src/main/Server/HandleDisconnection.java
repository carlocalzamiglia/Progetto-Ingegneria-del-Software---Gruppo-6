package Server;

import java.rmi.RemoteException;

public class HandleDisconnection extends Thread{
    boolean alive=true;
    ServerRmi server;
    final String nickname;

    public HandleDisconnection(String nickname, ServerRmi serverRmi) throws RemoteException {
        this.nickname=nickname;
        this.server=serverRmi;
    }

    public void run() {
        while(10>0 && alive){
            try {
                sleep(10000);
                alive=server.clientAlive(nickname);
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
