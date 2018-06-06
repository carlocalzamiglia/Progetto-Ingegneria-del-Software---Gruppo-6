package it.polimi.ingsw.Client;

import it.polimi.ingsw.ServertoClientHandler.ClientInterface;

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
    ClientInterface clientInt;




    //GAME VARIABLES
    private boolean yourturn;
    private String dicepos;
    private String row;
    private String col;
    //---------------------------------------------------launch execute-------------------------------------------------
    public ClientSocket(ClientInterface clientInt) {
        this.clientInt=clientInt;
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
            clientInt.showError("Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                clientInt.showError("Socket not closed");
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

                String[] login=clientInt.loginMessages();
                String nickname=login[0];
                String password=login[1];

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
                logged=inSocket.readLine();

                if(logged.equals("1")||logged.equals("0")) {
                    outSocket.println(nickname);
                    outSocket.flush();
                    this.name=nickname;
                }
                else if(logged.equals("2"))
                    clientInt.showError("Password di " + nickname + " errata");
                else if(logged.equals("3"))
                    clientInt.showError("L'utente selezionato è già connesso. Deve esserci un errore!");
            }
        }
        catch(Exception e)
        {
            clientInt.showError("Exception: "+e);
            try {
                socket.close();
            }
            catch(IOException ex)
            {
                clientInt.showError("Socket not closed");
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
                    if(arrOfStr[0].equals("@ALIVE")){}

                     else if(arrOfStr[0].equals("@SCHEME")) {
                        int scheme=clientInt.schemeMessages(arrOfStr[1]);
                        sendMessage("@SCHEME-"+scheme);

                    }

                    else if(arrOfStr[0].equals("@PRINTALL")) {
                        clientInt.printCarpetFirst(arrOfStr[1], arrOfStr[2]);
                    }

                    else if(arrOfStr[0].equals("@YOURTURN")) { //enables turn
                        if(arrOfStr[1].equals("true"))
                            yourturn=true;
                        else {
                            clientInt.endTurn();
                            yourturn=false;
                        }
                    }
                    else if(arrOfStr[0].equals("@USETOOL")){
                        int tool = clientInt.chooseToolMessages();
                        sendMessage("@TOOLUSED-"+tool);
                    }

                    else if(arrOfStr[0].equals("@PLACEDICE")){          //choose and place dice
                        if(yourturn==true) {
                            int[] coordinates;
                            int dicepos=clientInt.chooseDice();
                            coordinates=clientInt.chooseCoordinates();
                            sendMessage("@DICEPLACED-" + dicepos + "-" + coordinates[0] + "-" + coordinates[1]);
                        }
                    }

                    else if(arrOfStr[0].equals("@SCHEMEUPDATE")){
                        clientInt.schemeUpdated(arrOfStr[1]);
                    }

                    else if(arrOfStr[0].equals("@CHOOSEACTION")){
                        String action = clientInt.handleTurnMenu();
                        sendMessage("@ACTIONCHOSE-"+action);
                    }

                    else if(arrOfStr[0].equals("@ERROR")){
                        clientInt.showError(arrOfStr[1]);
                    }

                    else if(arrOfStr[0].equals("@TOOL")){
                        String choose;
                        if(!(arrOfStr[1].equals("61"))) {       //avoids a double check.
                            choose=clientInt.goOnTool();
                        }else
                            choose="y";
                        //--------------------USE TOOL CARDS------------------------
                        if(choose.equals("y")) {
                            if (arrOfStr[1].equals("1")) {
                                int[] coordinates;
                                coordinates=clientInt.tool23Messages();
                                sendMessage("@TOOLUSED1-" + coordinates[0] + "-" + coordinates[1] + "-" + coordinates[2] + "-" + coordinates[3]);
                            }
                            if (arrOfStr[1].equals("2")) {
                                int[] coordinates;
                                coordinates=clientInt.tool4Messages();
                                sendMessage("@TOOLUSED2-" + coordinates[0] + "-" + coordinates[1] + "-" + coordinates[2] + "-" + coordinates[3] + "-" + coordinates[4] + "-" + coordinates[5] + "-" + coordinates[6] + "-" + coordinates[7]);
                            }
                            if (arrOfStr[1].equals("3")) {
                                int[] coordinates12 = clientInt.tool12Messages();
                                sendMessage("@TOOLUSED3-" + coordinates12[8] + "-" + coordinates12[0] + "-" + coordinates12[1] + "-" + coordinates12[2] + "-" + coordinates12[3] + "-" + coordinates12[4] + "-" + coordinates12[5] + "-" + coordinates12[6] + "-" + coordinates12[7] + "-" + coordinates12[9] + "-" + coordinates12[10]);
                            }
                            if(arrOfStr[1].equals("4")){
                                int vdice=clientInt.chooseDice();
                                int dicechose=clientInt.tool1Messages();
                                sendMessage("@TOOLUSED4-"+vdice+"-"+dicechose);
                            }
                            if(arrOfStr[1].equals("6")){
                                int ndice = clientInt.chooseDice();
                                sendMessage("@TOOLUSED6-"+ndice);
                            }
                            if(arrOfStr[1].equals("61")){
                                int[] coordinates = clientInt.tool6Messages(arrOfStr[1]);
                                sendMessage("@TOOLUSED61-"+coordinates[0]+"-"+coordinates[1]);
                            }
                            if(arrOfStr[1].equals("5")){
                                int vdice = clientInt.chooseDice();
                                int[] dicepos = clientInt.chooseFromPath();
                                sendMessage("@TOOLUSED5-"+vdice+"-"+dicepos[0]+"-"+dicepos[1]);
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

