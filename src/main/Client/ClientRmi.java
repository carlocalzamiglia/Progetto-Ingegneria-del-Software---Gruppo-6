package Client;


import Server.ServerRmiInt;

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
                        System.out.println("Login effettuato correttamente, benvenuto "+nickname);
                    } else{
                        System.out.println("Username già in uso. Inserisci un nuovo username.");

                    }
                }
            }


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




        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }

    public void serverMessage(String message) throws RemoteException {
        System.out.println(message);
    }

}
