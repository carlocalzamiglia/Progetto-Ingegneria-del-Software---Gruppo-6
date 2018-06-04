package it.polimi.ingsw.Client;


import it.polimi.ingsw.Game.Game;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Matches;
import it.polimi.ingsw.Game.Ruler;
import it.polimi.ingsw.Game.Scheme;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.Colour;
import it.polimi.ingsw.Game.ToolCardsExecutor;

import it.polimi.ingsw.Server.ServerRmiClientHandlerInt;
import it.polimi.ingsw.ServertoClientHandler.ServertoClient;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Thread.sleep;


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
            root=leggiDaFile();
            String[] parts=root.split(":");
            PORT=Integer.parseInt(parts[1]);
            root=parts[0];
            Registry registry = LocateRegistry.getRegistry(root,PORT);
            server=(ServerRmiClientHandlerInt) registry.lookup("RMICONNECTION");
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
            /*
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
*/
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
        sendMessageOut("Hai scelto lo schema "+message+". Ora attendi il tuo turno!");
        return stringToInt(message);
    }


    //to be implement
    @Override
    public Game handleturn(GreenCarpet greenCarpet, Player player, int i, String playersscheme) throws IOException, InterruptedException {

        Game game = new Game(0);
        boolean usedDice=false;
        int flagTool=0;
        boolean usedTool=false;
        Ruler ruler = new Ruler();
        String value;
        while(true){
            sendMessageOut("\n\n*************************** E' IL TUO TURNO ***************************");
            sendMessageOut("Ecco lo schema degli altri giocatori, nell'ordine: "+ playersscheme);
            sendMessageOut("Ecco qui il tavolo e il tuo schema:\n");
            sendMessageOut(greenCarpet.toString()+"\n");
            sendMessageOut(player.toString()+"\n");
            sendMessageOut("1)passa il turno\n2)inserisci dado\n3)usa carta utensile\n");
            value = inKeyboard.readLine();
            if(value.equals("1")){
                game.setGreenCarpet(greenCarpet);
                game.setPlayer(player, i);
                sendMessageOut("############################### IL TUO TURNO E' TERMINATO. ATTENDI. ###############################\n\n");
                return game;
            }else if(value.equals("2")){
                if(ruler.checkAvailable(greenCarpet, player.getScheme())) {
                    System.out.println("E' stato scelto il dado");
                    if (!usedDice) {
                        placedice(greenCarpet, player, i);
                        usedDice = true;
                        if (usedTool) {
                            game.setGreenCarpet(greenCarpet);
                            game.setPlayer(player, i);
                            sendMessageOut("############################### IL TUO TURNO E' TERMINATO. ATTENDI. ###############################\n\n");
                            return game;
                        }
                    }else
                        sendMessageOut("Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                }else
                    sendMessageOut("Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
            }else if(value.equals("3")){
                System.out.println("E' stata scelta la tool");
                flagTool = placeTool(greenCarpet, player, i, usedDice);
                if (flagTool==1) {     //used a toolcard which include dice placement
                    game.setGreenCarpet(greenCarpet);
                    game.setPlayer(player, i);
                    sendMessageOut("############################### IL TUO TURNO E' TERMINATO. ATTENDI. ###############################\n\n");
                    return game;
                }
                if(flagTool==2) {
                    if(!usedDice)
                        usedTool = true;
                    else{
                        game.setGreenCarpet(greenCarpet);
                        game.setPlayer(player, i);
                        sendMessageOut("############################### IL TUO TURNO E' TERMINATO. ATTENDI. ###############################\n\n");
                        return game;
                    }
                }
            }
        }

    }

    private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean usedDice) throws IOException {
        String message;
        sendMessageOut("Inserisci il numero della carta tool da usare.");
        message=inKeyboard.readLine();
        int choice=stringToInt(message);
        boolean toolok=false;
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        String goon="a";
        boolean exit=false;
        boolean tooldice=false;


        if(choice>0 && choice<13) {
            //rembember to check if the tool chosen is inside the greencarpet.
            switch (choice) {
                case 1:     //no placement
                    while(!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")){
                            sendMessageOut("Inserisci il numero del dado della riserva che vuoi utilizzare");
                            String vdice=inKeyboard.readLine();
                            sendMessageOut("Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
                            String dicechose=inKeyboard.readLine();
                            while(!(dicechose.equals("c")) && !(dicechose.equals("d"))){
                                outVideo.println("Scelta errata. Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
                                dicechose=inKeyboard.readLine();
                            }
                            if(dicechose.equals("c")) //increase
                                toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(vdice), 1);
                            else //decrease
                                toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(vdice), 2);
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;
                case 2:
                    while (!toolok) {        //used to have a correct use of the tool
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                            String row = inKeyboard.readLine();
                            sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                            String col = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova riga");
                            String newrow = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova colonna");
                            String newcol = inKeyboard.readLine();

                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(row), stringToInt(col), stringToInt(newrow), stringToInt(newcol));
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;
                case 3:
                    while (!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                            String row = inKeyboard.readLine();
                            sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                            String col = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova riga");
                            String newrow = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova colonna");
                            String newcol = inKeyboard.readLine();
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(row), stringToInt(col), stringToInt(newrow), stringToInt(newcol));
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;
                case 4:
                    while (!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            sendMessageOut("PRIMO DADO:\n");
                            sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                            String row = inKeyboard.readLine();
                            sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                            String col = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova riga");
                            String newrow = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova colonna");
                            String newcol = inKeyboard.readLine();
                            sendMessageOut("SECONDO DADO:\n");
                            sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                            String row2 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                            String col2 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova riga");
                            String newrow2 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova colonna");
                            String newcol2 = inKeyboard.readLine();
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(row), stringToInt(col), stringToInt(newrow), stringToInt(newcol), stringToInt(row2), stringToInt(col2), stringToInt(newrow2), stringToInt(newcol2));
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 5:
                    while(!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            sendMessageOut("Inserisci il numero del dado della riserva che vuoi utilizzare");
                            String vdice = inKeyboard.readLine();
                            sendMessageOut("Inserisci il round da cui vuoi prelevare il dado da scambiare");
                            String round = inKeyboard.readLine();
                            sendMessageOut("Inserisci la posizione del dado nel round (numero di riga)");
                            String dicepos = inKeyboard.readLine();
                            toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(vdice), stringToInt(round), stringToInt(dicepos));
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 6:
                    Ruler ruler = new Ruler();
                    boolean checkcorrdice=false;
                    Dice dice=null;
                    String row="";
                    String col="";
                    while(!toolok && !usedDice) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            sendMessageOut("Inserisci il numero del dado della riserva che vuoi utilizzare");
                            String ndice = inKeyboard.readLine();
                            dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(ndice),6,0);
                            if (dice != null) {
                                if (ruler.checkAvailableDice(dice, player.getScheme())) {
                                    while (!checkcorrdice) {
                                        sendMessageOut("Il dado è stato nuovamente lanciato. E' uscito: " + dice + ". Sei pregato di indicare dove piazzarlo.\nInserisci la riga.\n");
                                        row = inKeyboard.readLine();
                                        sendMessageOut("Ora inserisci la colonna.");
                                        col = inKeyboard.readLine();
                                        checkcorrdice = ruler.checkCorrectPlacement(stringToInt(row), stringToInt(col), dice, player.getScheme());
                                    }
                                    player.getScheme().setBoxes(dice, stringToInt(row), stringToInt(col));
                                    tooldice=true;
                                }
                                else
                                    greenCarpet.setDiceInStock(dice);
                                toolok = true;
                            } else
                                sendMessageOut("C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    if(usedDice){
                        sendMessageOut("Non puoi utilizzare questa carta tool. Hai già piazzato un dado!");
                        exit=true;
                    }
                    break;
                case 7:
                    /*
                    sendMessageOut("@TOOL-7");
                    while (!(message.equals("@TOOLUSED-7")) && !(message.equals("@TOOLEXIT"))) ;
                    */
                    break;
                case 8:
                    /*
                    sendMessageOut("@TOOL-manca");
                    while (!(message.equals("@TOOLUSED-9")) && !(message.equals("@TOOLEXIT"))) ;
                    break;
                    */
                case 9:
                    /*
                    if (!useddice) {
                        sendMessageOut("@TOOL-1");
                        while (!(message.equals("@TOOLUSED-1")) && !(message.equals("@TOOLEXIT"))) ;
                    } else
                        sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                    */
                    break;
                case 10:
                    while(!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            outVideo.println("Inserisci il numero del dado della riserva che vuoi utilizzare");
                            String ndice = inKeyboard.readLine();
                            toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(ndice), 0);
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 11:
                    /*
                    if (!useddice) {
                        sendMessageOut("@TOOL-8");
                        while (!(message.equals("@TOOLUSED-8")) && !(message.equals("@TOOLEXIT"))) ;
                    } else
                        sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                    */
                    break;
                case 12:
                    while (!toolok) {
                        goon="a";
                        while(!goon.equals("y") && !goon.equals("n")) {
                            sendMessageOut("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
                            goon = inKeyboard.readLine();
                        }
                        if(goon.equals("y")) {
                            String row2 = "";
                            String col2 = "";
                            String newrow2 = "";
                            String newcol2 = "";
                            sendMessageOut("INSERISCI IL NUMERO DI DADI CHE VUOI SPOSTARE (1 o 2):\n");
                            String ndice = inKeyboard.readLine();
                            sendMessageOut("Inserisci il numero del round da cui prendere il dado\n");
                            String round = inKeyboard.readLine();
                            sendMessageOut("Inserisci la posizione del dado nel round (numero di riga)\n");
                            String dicepos = inKeyboard.readLine();

                            sendMessageOut("PRIMO DADO:\n");
                            sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                            String row1 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                            String col1 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova riga");
                            String newrow1 = inKeyboard.readLine();
                            sendMessageOut("Inserisci la nuova colonna");
                            String newcol1 = inKeyboard.readLine();
                            if (ndice.equals("2")) {
                                sendMessageOut("SECONDO DADO:\n");
                                sendMessageOut("Inserisci la riga del dado che vuoi scegliere");
                                row2 = inKeyboard.readLine();
                                sendMessageOut("Inserisci la colonna del dado che vuoi scegliere");
                                col2 = inKeyboard.readLine();
                                sendMessageOut("Inserisci la nuova riga");
                                newrow2 = inKeyboard.readLine();
                                sendMessageOut("Inserisci la nuova colonna");
                                newcol2 = inKeyboard.readLine();
                            }
                            if (ndice.equals("2"))
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(ndice), stringToInt(row1), stringToInt(col1), stringToInt(newrow1), stringToInt(newcol1), stringToInt(row2), stringToInt(col2), stringToInt(newrow2), stringToInt(newcol2), stringToInt(round), stringToInt(dicepos));
                            else
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(ndice), stringToInt(row1), stringToInt(col1), stringToInt(newrow1), stringToInt(newcol1), 0, 0, 0, 0, stringToInt(round), stringToInt(dicepos));
                        }else{
                            toolok=true;
                            exit=true;
                        }

                    }
                    break;
            }
        }else{
            sendMessageOut("Hai inserito un valore sbagliato!");
        }

        if(exit)
            return 3;
        if(tooldice)
            return 1;
        else
            return 2;

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
        if(message.equals("0"))
            return 0;
        else if(message.equals("1"))
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
        else if(message.equals("10"))
            return 10;
        else if(message.equals("11"))
            return 11;
        else if(message.equals("12"))
            return 12;
        else
            return -1;
    }

}
