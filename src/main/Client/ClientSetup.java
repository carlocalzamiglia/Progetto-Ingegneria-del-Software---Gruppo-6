package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientSetup {
    public static void main(String[]args) throws RemoteException {
        try {
            ClientRmi rmiClientRmi =new ClientRmi();
            rmiClientRmi.startRmiClient();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
