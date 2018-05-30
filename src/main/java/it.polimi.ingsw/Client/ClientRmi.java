package it.polimi.ingsw.Client;


import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Matches;
import it.polimi.ingsw.Game.Ruler;
import it.polimi.ingsw.Game.Scheme;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.Colour;

import it.polimi.ingsw.Server.ServerRmiClientHandlerInt;
import it.polimi.ingsw.ServertoClientHandler.ServertoClient;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;


public class ClientRmi extends UnicastRemoteObject implements ClientRmiInt, ServertoClient {

    private ClientRmiInt client;
    private ServerRmiClientHandlerInt server;
    BufferedReader inKeyboard = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
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
            //play();
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
            server=(ServerRmiClientHandlerInt) registry.lookup("RMICONNECTION");
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
                    server.addToMatches(username);
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
    public void sendMessageOut(String message) throws RemoteException{
        System.out.println(message);
    }


    //******************************************game methods***********************************************+
    @Override
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        String message;
        do {
            sendMessageOut("Scegli uno schema:\n" + scheme1 + "\n" + scheme2 + "\n" + scheme3 + "\n" + scheme4);
            message = in.nextLine();
        }while (stringToInt(message)<=0 || stringToInt(message)>4);
        System.out.println(stringToInt(message));
        return stringToInt(message);
    }


    //to be implement
    @Override
    public void handleturn(GreenCarpet greenCarpet, Player player, int i, String playersscheme) throws IOException, InterruptedException {
        boolean usedDice=false;
        boolean flagTool=false;
        boolean usedTool=false;
        Ruler ruler = new Ruler();
        String value;
        while(true){
            sendMessageOut("Ecco lo schema degli altri giocatori, nell'ordine: "+ playersscheme);
            sendMessageOut("Ecco qui il tavolo e il tuo schema:\n");
            sendMessageOut(greenCarpet.toString()+"\n");
            sendMessageOut(player.toString()+"\n");
            sendMessageOut("1)passa il turno\n2)inserisci dado\n3)usa carta utensile\n");
            value = inKeyboard.readLine();
            if(value.equals("1")){
                return;
            }else if(value.equals("2")){
                if(ruler.checkAvailable(greenCarpet, player.getScheme())) {
                    System.out.println("E' stato scelto il dado");
                    if (!usedDice) {
                        placedice(greenCarpet, player, i);
                        usedDice = true;
                        if (usedTool)
                            return;
                    }else
                        sendMessageOut("Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                }else
                    sendMessageOut("Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
            }else if(value.equals("3")){
                System.out.println("E' stata scelta la tool");
                flagTool = placeTool(greenCarpet, player, i, usedDice);
                if (flagTool) {
                    sendMessageOut("@YOURTURN-false");
                    return;
                }
                usedTool = true;
            }
        }
    }

    private boolean placeTool(GreenCarpet greenCarpet, Player player, int i, boolean usedDice) {
        return true;
    }


    private void placedice(GreenCarpet greenCarpet, Player player, int i) throws IOException, InterruptedException {   //i is player's number for "getplayer"
        Boolean checkdice = false;
        Random random = new Random();
        Ruler ruler = new Ruler();
        String value="";
        String row="";
        String col="";
        while (!checkdice) {
            sendMessageOut("Inserisci il numero del dado della riserva che vuoi piazzare.");
            value=inKeyboard.readLine();
            sendMessageOut("Inserisci la riga dove vuoi piazzare il dado.");
            row=inKeyboard.readLine();
            sendMessageOut("Inserisci la colonna dove vuoi piazzare il dado.");
            col=inKeyboard.readLine();
            Dice dice = greenCarpet.checkDiceFromStock(stringToInt(value));
            if(dice!=null) {
                checkdice = ruler.checkCorrectPlacement(stringToInt(row), stringToInt(col), dice, player.getScheme());
                if (!checkdice)
                    sendMessageOut("Il dado non può essere inserito");
            }else
                sendMessageOut("Hai scelto un dado non valido");
        }
        player.getScheme().setBoxes(greenCarpet.getDiceFromStock(stringToInt(value)), stringToInt(row), stringToInt(col));
        sendMessageOut("Ecco lo schema aggiornato:\n"+player.getScheme().toString());
    }




    private int stringToInt(String message){
        if(message.equals("1"))
            return 1;
        else if(message.equals("2"))
            return 2;
        else if(message.equals("3"))
            return 3;
        else if(message.equals("4"))
            return 4;
        else if(message.equals("5"))
            return 5;
        else if(message.equals("6"))
            return 6;
        else if(message.equals("7"))
            return 7;
        else if(message.equals("8"))
            return 8;
        else if(message.equals("9"))
            return 9;
        else
            return 0;
    }

}
