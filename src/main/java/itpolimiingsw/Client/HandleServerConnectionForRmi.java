package itpolimiingsw.Client;

import java.rmi.RemoteException;

public class HandleServerConnectionForRmi extends Thread{
    private ClientRmiInt client;
    private boolean alive = true;

    public HandleServerConnectionForRmi(ClientRmiInt client){
        this.client = client;
    }


    /**
     * Used by the client to check server connection.
     */
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
