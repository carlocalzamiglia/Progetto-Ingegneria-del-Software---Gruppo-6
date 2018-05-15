package it.polimi.ingsw.Client;


import it.polimi.ingsw.Server.ServerRmiInt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientRmi extends UnicastRemoteObject implements ClientRmiInt {
    private String nickname;
    private String address="localhost";
    protected ServerRmiInt serverInt;




    Scanner in=new Scanner(System.in);


    public ClientRmi() throws RemoteException{
        super();
    }

    class handleExit extends Thread{
        public void run(){
            try {
                serverInt.manageDisconnection(nickname);
                System.out.println("Eseguo disconnessione.");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRmiClient() throws RemoteException, NotBoundException {
        try{
            System.out.println("Inserisci l'username per effettuare la connessione!");
            boolean flag=true;
            while (flag==true) {                //ciclo for per attendere username corretto.
                serverInt = (ServerRmiInt) Naming.lookup("rmi://" + address + "/" + "1234");

                Scanner in=new Scanner(System.in);
                nickname=in.nextLine();
               synchronized (serverInt) {
                    if (serverInt.checkUser(nickname) == true) {
                        Naming.rebind("rmi://" + address + "/Client_" + nickname, this);
                        serverInt.login(nickname);

                        flag=false;


                    } else{
                        System.out.println("Username già in uso. Inserisci un nuovo username.");

                    }
                }
            }

            //-----------gestisce la chiusura del client-------------
            Runtime.getRuntime().addShutdownHook(new handleExit());






            //codice da elaborare ed adattare alla partita.


            //--------------------------codice di prova--------------------------------------------------
            String name;
            String message;
            System.out.println("A chi vuoi mandare il messaggio?");
            name=in.nextLine();
            System.out.println("e che cosa vuoi dirgli?");
            message=in.nextLine();
            serverInt.sendMessage(name, message);
            //--------------------------codice di prova--------------------------------------------------


            //gestione chiusura Client.



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }

    public void serverMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public boolean aliveMessage(){
        return true;
    }



}