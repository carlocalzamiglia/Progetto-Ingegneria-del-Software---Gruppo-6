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
    //quando viene chiamato dal client principale il costruttore fa partire il metodo esegui (speculare a rmi)
    public ClientSocket()
    {
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
            System.out.println("Il client tenta di connettersi");

            socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

            System.out.println("ClientSetup connesso");
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
            boolean logged=false;

            while(!logged)
            {
                //legge nickname dal client
                outVideo.println("Inserire nickname:");
                String username=inKeyboard.readLine();

                //legge password dal client
                outVideo.println("Inserire password:");
                String password=inKeyboard.readLine();


                //pulisce l'uscita sul socket
                outSocket.flush();
                //manda al socket il nickname
                outSocket.println(username);
                //pulisce l'uscita sul socket
                outSocket.flush();
                //manda al socket il nickname
                outSocket.println(password);
                outSocket.flush();

                //legge il valore se true o false all'ingresso del socket
                logged=Boolean.valueOf(inSocket.readLine()).booleanValue();


                if(logged) {
                    outVideo.println("Login effettuato correttamente");
                    outSocket.println(username);
                    outSocket.flush();
                    this.name=username;
                }
                else
                    outVideo.println("Login errato. Riprova");
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
        while(10>1){
            outVideo.println("Cosa vuoi fare?");
            outVideo.println("0)manda messaggio");
            String choice= inKeyboard.readLine();

            switch (choice) {
                case "0":
                    outVideo.println("Scrivi messaggio:");
                    String message = inKeyboard.readLine();
                    outSocket.flush();
                    outSocket.println(message);
                    outSocket.flush();
                    outSocket.println(name);
                    outSocket.flush();
                    break;
                default:
                    break;
            }
        }
    }

}

