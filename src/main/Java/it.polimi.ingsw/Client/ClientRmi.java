package it.polimi.ingsw.Client;


import it.polimi.ingsw.Server.DBUsers;
import it.polimi.ingsw.Server.HandleDisconnection;
import it.polimi.ingsw.Server.ServerRmiClientHandlerInt;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientRmi extends UnicastRemoteObject implements ClientRmiInt {

    private ClientRmiInt client;
    private ServerRmiClientHandlerInt server;
    BufferedReader inKeyboard = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
    DBUsers DB=new DBUsers();
    private String nickname;
    private String root;
    private int PORT;

    //-----------------------------------------launch execute method---------------------------------------------
    public ClientRmi()         throws RemoteException{
        super();
        System.out.println("ClientSetup avviato");
        try
        {
            execute();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }

    }

    //-----------------------------------launch connect, then login and then play---------------------------------------
    private void execute()       throws RemoteException{
        try
        {
            connect();
            login();
            play();
            //chiudi();
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    //------------------------------------------does the all RMI connection---------------------------------------------
    private void connect()     throws RemoteException{
        try
        {
            System.out.println("Il client tenta di connettersi");
            root=leggiDaFile();
            String[] parts=root.split(":");
            PORT=Integer.parseInt(parts[1]);
            root=parts[0];
            Registry registry = LocateRegistry.getRegistry(root,PORT);
            server=(ServerRmiClientHandlerInt) registry.lookup("rmi://"+root+"/ser_con");
            System.out.println("ClientSetup connesso");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    //------------------------------------login part. If it is all right adds to DBUser---------------------------------
    private void login()        throws RemoteException{
        try
        {
            int logged=3;

            while(logged!=0 && logged !=1)
            {

                System.out.println("Inserire nickname:");
                String username=inKeyboard.readLine();

                System.out.println("Inserire password:");
                String password=inKeyboard.readLine();

                logged=server.login(username,password);

                if(logged==0 || logged==1) {
                    System.out.println("Login effettuato correttamente");

                    this.nickname=username;
                    server.addRmi(this, username);
                    server.publish(username);

                }
                else if(logged==3)
                    System.out.println("L'utente selezionato è già connesso. Deve esserci un errore!");
                else if(logged==2)
                    System.out.println("Password di " + username + " errata");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    //------------------------------------------------the game method---------------------------------------------------
    private void play()        throws RemoteException{
        boolean disc;

        //for now we use a while loop always true for send message to the server
        while(10>1){
            System.out.println("Cosa vuoi fare?");
            System.out.println("0)manda messaggio");
            System.out.println("1)esci");
            Scanner in = new Scanner(System.in);
            String choice = in.nextLine();
            switch (choice) {
                case "0":
                    System.out.println("Scrivi messaggio:");
                    String message =in.nextLine();
                    server.sendMessage(nickname,message);
                    break;
                case "1":
                    try {
                        disc=server.manageDisconnection(nickname);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        disc=false;
                    }
                    if(disc==true){
                        System.out.println("Disconnessione eseguita con successo. Arrivederci.");
                        System.exit(0);
                    }else
                        System.out.println("Errore nella discossessione. Riprova.");
                    break;
                default:
                    break;
            }

        }
    }

    //-----------------------------------------check if client is alive yet---------------------------------------------
    public boolean aliveMessage(){
        System.out.println("Hanno appena controllato che sia ancora online. Affermativo!");
        return true;
    }

    //--------------------------------------get connection properties from file-----------------------------------------
    private String leggiDaFile() throws IOException {
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/client_config.txt");

        BufferedReader b = new BufferedReader(f);
        String root;
        try {
            b.readLine();
            root = (b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return root;
    }

    //---------------------------------------print a message on the CLI-------------------------------------------------
    public void tell(String message) throws RemoteException{
        System.out.println(message);
    }
}
