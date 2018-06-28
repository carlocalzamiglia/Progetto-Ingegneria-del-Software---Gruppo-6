package it.polimi.ingsw.Client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientSetup {

    //-----------------------------------------launch RMI or Socket client----------------------------------------------
    public static void main(String[] args) throws IOException {
        CLI cli;

        String view = args[0];
        String choice = args[1];


        if(choice.equals("socket")) {
            if(view.equals("CLI")) {
                cli = new CLI();
                new ClientSocket(cli);
            }else{
                //gui here
            }

        }
        else if(choice.equals("rmi"))
            if(view.equals("CLI")) {
                cli = new CLI();
                new ClientRmi(cli);
            }else{
                //gui here
            }

    }
}
