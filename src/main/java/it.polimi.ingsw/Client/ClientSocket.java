package it.polimi.ingsw.Client;

import java.io.*;
import java.net.Socket;

import java.io.Serializable;

public class ClientSocket {
    private int PORT;
    private String root;
    private String name;
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;
    private ObjectOutputStream out;
    private ObjectInputStream in;




    //GAME VARIABLES
    private boolean yourturn;
    private String dicepos;
    private String row;
    private String col;
    //---------------------------------------------------launch execute-------------------------------------------------
    public ClientSocket() {
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

    //--------------------------------------launch connect, then login and then play------------------------------------
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

    //----------------------------------------create the connection with server-----------------------------------------
    private void connect()
    {
        try
        {
            root=leggiDaFile();
            String[] parts=root.split(":");
            PORT=Integer.parseInt(parts[1]);
            root=parts[0];
            socket = new Socket(root, PORT);
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

    //---------------------------------------------------login part-----------------------------------------------------
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

    //----------------------------------------------------game part-----------------------------------------------------
    private void play() throws IOException {
        //for now we did't implement the complete protocol for the socket comunication but it will be implement in this while loop
        //this is for the client part
        new ListenFromServer().start();
        while(10>1){
           /* outVideo.println("Cosa vuoi fare?");
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
*/

        }
    }

    //---------------------------------------------class for server messages--------------------------------------------
    class ListenFromServer extends Thread {
        public void run() {
            while(true) {
                try {
                    in = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    //---------------------HERE THE IMPLEMENTATION OF THE PROTOCOL------------------------------

                    String msg = (String) in.readObject();
                    String [] arrOfStr = msg.split("-");

                    // print the message
                    if(arrOfStr[0].equals("@ALIVE")) {
                        //System.out.println("Hanno appena controllato che sia ancora online. Affermativo!");
                    }

                    else if(arrOfStr[0].equals("@SCHEME")) {
                        String scheme;
                        System.out.println(arrOfStr[1]);
                        scheme=inKeyboard.readLine();
                        sendMessage("@SCHEME-"+scheme);

                    }

                    else if(arrOfStr[0].equals("@YOURTURN")) { //enables turn
                        if(arrOfStr[1].equals("true"))
                            yourturn=true;
                        else
                            yourturn=false;
                    }
                    else if(arrOfStr[0].equals("@USETOOL")){
                        outVideo.println("Inserisci il numero della carta tool da usare.");
                        String tool = inKeyboard.readLine();
                        sendMessage("@TOOLUSED-"+tool);
                    }

                    else if(arrOfStr[0].equals("@PLACEDICE")){          //choose and place dice
                        if(yourturn==true) {
                            outVideo.println("Scegli dado dalla riserva.");
                            dicepos = inKeyboard.readLine();
                            outVideo.println("Scegli la riga");
                            row = inKeyboard.readLine();
                            outVideo.println("Scegli la colonna");
                            col = inKeyboard.readLine();
                            sendMessage("@DICEPLACED-" + dicepos + "-" + row + "-" + col);
                        }
                    }else if(arrOfStr[0].equals("@CHOOSEACTION")){
                        outVideo.println("scegli azione");
                        String action = inKeyboard.readLine();
                        sendMessage("@ACTIONCHOSE-"+action);

                    }else if(arrOfStr[0].equals("@ERROR")){
                        outVideo.println(arrOfStr[1]);
                    }else if(arrOfStr[0].equals("@TOOL")){
                        String choose="a";
                        if(!(arrOfStr[1].equals("61"))) {       //avoids a double check.
                            while ((!(choose.equals("y"))) && (!(choose.equals("n")))) {
                                outVideo.println("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                                choose = inKeyboard.readLine();
                            }
                        }else
                            choose="y";
                        //--------------------USE TOOL CARDS------------------------
                        if(choose.equals("y")) {
                            if (arrOfStr[1].equals("1")) {
                                outVideo.println("Inserisci la riga del dado che vuoi scegliere");
                                String row = inKeyboard.readLine();
                                outVideo.println("Inserisci la colonna del dado che vuoi scegliere");
                                String col = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova riga");
                                String newrow = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova colonna");
                                String newcol = inKeyboard.readLine();
                                sendMessage("@TOOLUSED1-" + row + "-" + col + "-" + newrow + "-" + newcol);
                            }
                            if (arrOfStr[1].equals("2")) {
                                outVideo.println("PRIMO DADO:\n");
                                outVideo.println("Inserisci la riga del dado che vuoi scegliere");
                                String row = inKeyboard.readLine();
                                outVideo.println("Inserisci la colonna del dado che vuoi scegliere");
                                String col = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova riga");
                                String newrow = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova colonna");
                                String newcol = inKeyboard.readLine();
                                outVideo.println("SECONDO DADO:\n");
                                outVideo.println("Inserisci la riga del dado che vuoi scegliere");
                                String row2 = inKeyboard.readLine();
                                outVideo.println("Inserisci la colonna del dado che vuoi scegliere");
                                String col2 = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova riga");
                                String newrow2 = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova colonna");
                                String newcol2 = inKeyboard.readLine();
                                sendMessage("@TOOLUSED2-" + row + "-" + col + "-" + newrow + "-" + newcol + "-" + row2 + "-" + col2 + "-" + newrow2 + "-" + newcol2);
                            }
                            if (arrOfStr[1].equals("3")) {
                                outVideo.println("INSERISCI IL NUMERO DI DADI CHE VUOI SPOSTARE (1 o 2):\n");
                                String ndice = inKeyboard.readLine();
                                outVideo.println("Inserisci il numero del round da cui prendere il dado\n");
                                String round = inKeyboard.readLine();
                                outVideo.println("Inserisci la posizione del dado nel round (numero di riga)\n");
                                String dice = inKeyboard.readLine();

                                outVideo.println("PRIMO DADO:\n");
                                outVideo.println("Inserisci la riga del dado che vuoi scegliere");
                                String row = inKeyboard.readLine();
                                outVideo.println("Inserisci la colonna del dado che vuoi scegliere");
                                String col = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova riga");
                                String newrow = inKeyboard.readLine();
                                outVideo.println("Inserisci la nuova colonna");
                                String newcol = inKeyboard.readLine();
                                if (ndice.equals("2")) {
                                    outVideo.println("SECONDO DADO:\n");
                                    outVideo.println("Inserisci la riga del dado che vuoi scegliere");
                                    String row2 = inKeyboard.readLine();
                                    outVideo.println("Inserisci la colonna del dado che vuoi scegliere");
                                    String col2 = inKeyboard.readLine();
                                    outVideo.println("Inserisci la nuova riga");
                                    String newrow2 = inKeyboard.readLine();
                                    outVideo.println("Inserisci la nuova colonna");
                                    String newcol2 = inKeyboard.readLine();
                                    sendMessage("@TOOLUSED3-" + ndice + "-" + row + "-" + col + "-" + newrow + "-" + newcol + "-" + row2 + "-" + col2 + "-" + newrow2 + "-" + newcol2 + "-" + round + "-" + dice);
                                } else
                                    sendMessage("@TOOLUSED3-" + ndice + "-" + row + "-" + col + "-" + newrow + "-" + newcol + "-" + round + "-" + dice);
                            }
                            if(arrOfStr[1].equals("4")){
                                outVideo.println("Inserisci il numero del dado della riserva che vuoi utilizzare");
                                String vdice=inKeyboard.readLine();
                                outVideo.println("Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
                                String dicechose=inKeyboard.readLine();
                                while(!(dicechose.equals("c")) && !(dicechose.equals("d"))){
                                    outVideo.println("Scelta errata. Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
                                    dicechose=inKeyboard.readLine();
                                }
                                if(dicechose.equals("c"))
                                    sendMessage("@TOOLUSED4-"+vdice+"-1");
                                else
                                    sendMessage("@TOOLUSED4-"+vdice+"-2");
                            }
                            if(arrOfStr[1].equals("6")){
                                outVideo.println("Inserisci il numero del dado della riserva che vuoi utilizzare");
                                String ndice=inKeyboard.readLine();
                                sendMessage("@TOOLUSED6-"+ndice);
                            }
                            if(arrOfStr[1].equals("61")){
                                outVideo.println("Il dado è stato nuovamente lanciato. E' uscito: "+arrOfStr[2]+". Sei pregato di indicare dove piazzarlo.\nInserisci la riga.\n");
                                String row=inKeyboard.readLine();
                                outVideo.println("Ora inserisci la colonna.");
                                String col=inKeyboard.readLine();
                                sendMessage("@TOOLUSED61-"+row+"-"+col);
                            }
                            if(arrOfStr[1].equals("5")){
                                outVideo.println("Inserisci il numero del dado della riserva che vuoi utilizzare");
                                String vdice=inKeyboard.readLine();
                                outVideo.println("Inserisci il round da cui vuoi prelevare il dado da scambiare");
                                String round=inKeyboard.readLine();
                                outVideo.println("Inserisci la posizione del dado nel round (numero di riga)");
                                String dicepos = inKeyboard.readLine();
                                sendMessage("@TOOLUSED5-"+vdice+"-"+round+"-"+dicepos);
                            }
                        }else
                            sendMessage("@TOOLEXIT");

                    } else {
                        System.out.println(msg);
                    }
                }
                catch(IOException e) {
                    break;
                }
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }

    //---------------------------------------------send message to server-----------------------------------------------
    public void sendMessage(String message) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(message);
    }

    //-----------------------------------------get connection properties from file--------------------------------------
    private String leggiDaFile() throws IOException {
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/client_config.txt");

        BufferedReader b = new BufferedReader(f);
        String root;
        try {
            root = (b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return root;
    }


    //******************************************* now all the game methods *********************************************

}

