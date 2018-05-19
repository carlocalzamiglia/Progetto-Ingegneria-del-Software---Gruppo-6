package it.polimi.ingsw.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket {
    private final static int PORT=3001;
    private final static String address="localhost";
    private String name;
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    //quando viene chiamato dal client principale il costruttore fa partire il metodo esegui (speculare a rmi)
    public ClientSocket()
    {

        try
        {
            execute();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
    //Stessa struttura di rmi
    private void execute()
    {
        try
        {
            connect();
            login();
            play();
            //chiudi();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
    //crea connessione
    private void connect()
    {
        try
        {

            socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }
    private void login()
    {
        try
        {
            String logged ="2";

            while(logged.equals("2") || logged.equals("3"))
            {
                //legge nickname dal client
                outVideo.println("Inserire nickname:");
                String nickname=inKeyboard.readLine();

                //legge password dal client
                outVideo.println("Inserire password:");
                String password=inKeyboard.readLine();


                //pulisce l'uscita sul socket
                outSocket.flush();
                //manda al socket il nickname
                outSocket.println(nickname);
                //pulisce l'uscita sul socket
                outSocket.flush();
                //manda al socket il nickname
                outSocket.println(password);
                outSocket.flush();

                //legge il valore se true o false all'ingresso del socket
                //logged=Boolean.valueOf(inSocket.readLine()).booleanValue();
                logged=inSocket.readLine();

                if(logged.equals("1")||logged.equals("0")) {
                    outSocket.println(nickname);
                    outSocket.flush();
                    this.name=nickname;
                }
                else if(logged.equals("2"))
                    outVideo.println("Password di " + nickname + " errata");
                else if(logged.equals("3"))
                    outVideo.println("L'utente selezionato è già connesso. Deve esserci un errore!");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

            try {
                socket.close();
            }
            catch(IOException ex)
            {
                System.err.println("Socket not closed");
            }
        }
    }
    private void play() throws IOException {
        //for now we did't implement the complete protocol for the socket comunication but it will be implement in this while loop
        //this is for the client part
        new ListenFromServer().start();
        sendMessage("@LOGGED");
        while(10>1){
            outVideo.println("Cosa vuoi fare?");
            outVideo.println("0)manda messaggio");
            outVideo.println("1)chiudi");
            String choice= inKeyboard.readLine();

            switch (choice) {
                case "0":
                    outVideo.println("Scrivi messaggio:");
                    String message = inKeyboard.readLine();
                    sendMessage("@SEND"+message);
                    break;

                case "1":
                    sendMessage("@LOGOUT");
                    System.out.println("Disconnessione eseguita con successo. Arrivederci.");
                    System.exit(0);
                    break;
                default:
                    break;

            }
        }
    }


    class ListenFromServer extends Thread {

        public void run() {
            while(true) {
                try {
                    in = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    //---------------------HERE THE IMPLEMENTATION OF THE PROTOCOL------------------------------


                    String msg = (String) in.readObject();
                    // print the message
                    if(msg.equals("@ALIVE")) {
                        sendMessage("@ALIVE"); //just to try, useless.
                        System.out.println("Hanno appena controllato che sia ancora online. Affermativo!");
                    }else
                        System.out.println(msg);
                }
                catch(IOException e) {
                    break;
                }
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }


    public void sendMessage(String message) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(message);
    }

}

