package it.polimi.ingsw.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientSetup {
    public static void main(String[]args) throws RemoteException {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Benvenuto, quale connessione vuoi utilizzare?");
            System.out.println("Inserisci 1 per la connessione RMI ");
            System.out.println("Inserisci 2 per la connessione Socket ");

            int choose = in.nextInt();
            if (choose == 1) {
                ClientRmi rmiClientRmi = new ClientRmi();
                rmiClientRmi.startRmiClient();
            }
            if(choose == 2){
                //do something
            }

        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
