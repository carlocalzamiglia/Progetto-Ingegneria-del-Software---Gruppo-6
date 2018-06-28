package it.polimi.ingsw.Client;

import java.rmi.RemoteException;

public class HandleServerConnectionForRmi extends Thread{
    private ClientRmiInt client;
    private boolean alive = true;

    public HandleServerConnectionForRmi(ClientRmiInt client){
        this.client = client;
    }


    public void run() {
        while(alive){
            try {
                sleep(5000);
                alive = client.serverAlive();
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
