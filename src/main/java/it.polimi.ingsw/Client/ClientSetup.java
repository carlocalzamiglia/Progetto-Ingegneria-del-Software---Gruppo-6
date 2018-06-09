package it.polimi.ingsw.Client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientSetup {

    //-----------------------------------------launch RMI or Socket client----------------------------------------------
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI();
        System.out.println("scegli tipo di connessione:");
        System.out.println(("1)Socket\n2)Rmi"));

        Scanner input = new Scanner(System.in);

        String choice = input.nextLine();

        while(!choice.equals("0") && !choice.equals("1") && !choice.equals("2")){
            System.out.println("selezione sbagliata\npremi 0 per uscire 1 per connessione socket 2 per connesione rmi");
            input = new Scanner(System.in);
            choice = input.nextLine();
        }

        if(choice.equals("1")) {
            new ClientSocket(cli);
        }
        else if(choice.equals("2"))
            new ClientRmi(cli);

    }
}
