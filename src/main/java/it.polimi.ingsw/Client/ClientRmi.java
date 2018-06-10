package it.polimi.ingsw.Client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import it.polimi.ingsw.ServertoClientHandler.ClientInterface;
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
    ClientInterface clientInt;
    Gson gson = new GsonBuilder().create();

    //-----------------------------------------launch execute method---------------------------------------------
    public ClientRmi(ClientInterface clientInt)         throws RemoteException{
        super();
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
        String logindata[] = new String[2];
        try
        {
            int logged=3;

            while(logged!=0 && logged !=1)
            {
                logindata = clientInt.loginMessages();
                String username=logindata[0];
                logged=server.login(username, logindata[1]);
                if(logged==0) {
                    this.nickname=username;
                    server.addRmi(this, username);
                    server.publish(username);
                    server.addToMatches(username);
                }else if(logged==1){
                    this.nickname=username;
                    server.addRmi(this, username);
                    server.publish(username);
                    if(!(server.reconnectUser(username, this)))
                        server.addToMatches(username);
                }

                else if(logged==3)
                    clientInt.showError("L'utente selezionato è già connesso.");
                else if(logged==2)
                    clientInt.showError("Password di " + username + " errata");
            }
        }
        catch(Exception e)
        {
            clientInt.showError("Exception: "+e);
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
        clientInt.showError(message);
    }


    //******************************************game methods***********************************************+
    @Override
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4) throws IOException, InterruptedException {
        return clientInt.schemeMessages(scheme1+"\n"+scheme2+"\n"+scheme3+"\n"+scheme4+"\n");
    }


    @Override
    public Game handleturn(GreenCarpet greenCarpet, Player player, int i, String playersscheme) throws IOException, InterruptedException {
        Game game = new Game(0);
        boolean usedDice=false;
        int flagTool=0;
        boolean usedTool=false;
        Ruler ruler = new Ruler();
        String value;
        String greencarpetjson=gson.toJson(greenCarpet);
        String playerjson=gson.toJson(player);
        clientInt.printCarpetFirst(greencarpetjson, playerjson);
        while(true){
            value=clientInt.handleTurnMenu();
            if(value.equals("1")){
                game.setGreenCarpet(greenCarpet);
                game.setPlayer(player, i);
                clientInt.endTurn();
                return game;
            }else if(value.equals("2")){//DICE
                if(ruler.checkAvailable(greenCarpet, player.getScheme())) {
                    if (!usedDice) {
                        placedice(greenCarpet, player, i);
                        usedDice = true;
                        if (usedTool) {
                            game.setGreenCarpet(greenCarpet);
                            game.setPlayer(player, i);
                            clientInt.endTurn();
                            return game;
                        }
                    }else
                        clientInt.showError("Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                }else
                    clientInt.showError("Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
            }else if(value.equals("3")){
                flagTool = placeTool(greenCarpet, player, i, usedDice);
                if (flagTool==1) {     //used a toolcard which include dice placement
                    game.setGreenCarpet(greenCarpet);
                    game.setPlayer(player, i);
                    clientInt.endTurn();
                    return game;
                }
                if(flagTool==2) {
                    if(!usedDice)
                        usedTool = true;
                    else{
                        game.setGreenCarpet(greenCarpet);
                        game.setPlayer(player, i);
                        clientInt.endTurn();
                        return game;
                    }
                }
            }
        }
    }

    private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean usedDice) throws IOException {
        String message;
        boolean flag;
        int choice;
        do {
            choice=clientInt.chooseToolMessages();
            flag=greenCarpet.toolIsIn(choice);
            if(!flag)
                clientInt.showError("La carta utensile scelta non è presente sul tavolo da gioco");
        }while (!flag);
        boolean toolok=false;
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        String goon;
        boolean exit=false;
        boolean tooldice=false;
        if(choice>0 && choice<13) {
            //rembember to check if the tool chosen is inside the greencarpet.
            switch (choice) {
                case 1:     //no placement
                    while(!toolok) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")){
                            int vdice=clientInt.chooseDice();
                            int selection=clientInt.tool1Messages();
                            toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, vdice, selection);
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;


                case 2:
                    while (!toolok) {        //used to have a correct use of the tool
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool23Messages();
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coordinates);
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;
                case 3:
                    while (!toolok) {        //used to have a correct use of the tool
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool23Messages();
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coordinates);
                        }else {
                            exit = true;
                            toolok = true;
                        }
                    }
                    break;
                case 4:
                    while (!toolok) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool4Messages();
                            int[] coord1dice= new int[4];
                            int[] coord2dice= new int[4];
                            for (int k=0;k<coordinates.length;k++) {
                                if (k<4)
                                    coord1dice[k] = coordinates[k];
                                else
                                    coord2dice[k % 4] = coordinates[k];
                            }
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coord1dice,coord2dice);
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 5:
                    while(!toolok) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int vdice = clientInt.chooseDice();
                            int[] dicepos = clientInt.chooseFromPath();
                            toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, vdice, dicepos);
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
                    int[] coordinates = new int[2];
                    while(!toolok && !usedDice) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int ndice = clientInt.chooseDice();
                            dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice,6,0);
                            if (dice != null) {
                                if (ruler.checkAvailableDice(dice, player.getScheme())) {
                                    String dicejson=gson.toJson(dice);
                                    while (!checkcorrdice) {
                                        coordinates=clientInt.tool6Messages(dicejson);
                                        checkcorrdice = ruler.checkCorrectPlacement(coordinates[0], coordinates[1], dice, player.getScheme());
                                    }
                                    player.getScheme().setBoxes(dice, coordinates[0], coordinates[1]);
                                    tooldice=true;
                                }
                                else
                                    greenCarpet.setDiceInStock(dice);
                                toolok = true;
                            } else
                                clientInt.showError("C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    if(usedDice){
                        clientInt.showError("Non puoi utilizzare questa carta tool. Hai già piazzato un dado!");
                        exit=true;
                    }
                    break;
                case 7:
                    while(!toolok) {
                        if(greenCarpet.getTurn()==2 && !usedDice) {
                            goon = clientInt.goOnTool();
                            if (goon.equals("y")) {
                                toolok=toolCardsExecutor.changeDiceCard(player, greenCarpet, choice);
                            } else {
                                toolok = true;
                                exit = true;
                            }
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 8:
                    while(!toolok) {
                        if(greenCarpet.getTurn()==1 && usedDice){
                            goon=clientInt.goOnTool();
                            if(goon.equals("y")){
                                int vdice=clientInt.chooseDice();
                                int[] dicepos=clientInt.chooseCoordinates();
                                toolok=toolCardsExecutor.usePlacementCard(player, greenCarpet, vdice, choice, dicepos[0], dicepos[1]);
                            }else{
                                toolok = true;
                                exit = true;
                            }
                        }else {
                            sendMessageOut("@ERROR-Questa toolcard non è attualmente utilizzabile. Ricordati di piazzare un dado prima di utilizzarla!");
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 9:
                    if(!usedDice) {
                        while (!toolok) {
                            goon = clientInt.goOnTool();
                            if (goon.equals("y")) {
                                int vdice = clientInt.chooseDice();
                                int[] dicepos = clientInt.chooseCoordinates();
                                toolok = toolCardsExecutor.usePlacementCard(player, greenCarpet, vdice, choice, dicepos[0], dicepos[1]);
                            } else {
                                toolok = true;
                                exit = true;
                            }
                        }
                    }else
                        sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                    break;
                case 10:
                    while(!toolok) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int ndice = clientInt.chooseDice();
                            toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, ndice, 0);
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    break;
                case 11:
                    ruler = new Ruler();
                    checkcorrdice=false;
                    while(!toolok && !usedDice){
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")){
                            int ndice = clientInt.chooseDice();
                            dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice, choice, 0);
                            if(dice!=null) {
                                while (!checkcorrdice) {
                                    dice.setFace("");
                                    String dicejson = gson.toJson(dice);
                                    int[] value = clientInt.tool11Messages(dicejson);
                                    dice.setFace(ruler.intToString(value[2]));
                                    if(ruler.checkAvailableDice(dice, player.getScheme())) {
                                        checkcorrdice = ruler.checkCorrectPlacement(value[0], value[1], dice, player.getScheme());
                                        if(checkcorrdice) {
                                            player.getScheme().setBoxes(dice, value[0], value[1]);
                                            tooldice = true;
                                        }
                                    }
                                    else {
                                        checkcorrdice = true;
                                        greenCarpet.setDiceInStock(dice);
                                    }
                                }
                                toolok = true;
                            }else{
                                sendMessageOut("@ERROR-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                                toolok=true;
                                exit=true;
                            }
                        }else{
                            toolok = true;
                            exit = true;
                        }
                    }
                    break;
                case 12:
                    while (!toolok) {
                        goon=clientInt.goOnTool();
                        if(goon.equals("y")) {
                            int[] coordinates12 = clientInt.tool12Messages();
                            int numOfDices=coordinates12[8];
                            toolok = toolCardsExecutor.useMovementCard(player, greenCarpet,choice, numOfDices, coordinates12);
                        }else{
                            toolok=true;
                            exit=true;
                        }

                    }
                    break;
            }
        }else{
            clientInt.showError("Hai inserito un valore sbagliato!");
        }
        String schemejson = gson.toJson(player.getScheme());
        clientInt.schemeUpdated(schemejson);
        if(exit)
            return 3;
        if(tooldice)
            return 1;
        else
            return 2;

    }


    private void placedice(GreenCarpet greenCarpet, Player player, int i) throws IOException {   //i is player's number for "getplayer"
        Boolean checkdice = false;
        Ruler ruler = new Ruler();
        int[] dicecoord = new int[3];
        while (!checkdice) {
            dicecoord=clientInt.placeDiceMessages();
            Dice dice = greenCarpet.checkDiceFromStock(dicecoord[0]);
            if(dice!=null) {
                checkdice = ruler.checkCorrectPlacement(dicecoord[1], dicecoord[2], dice, player.getScheme());
                if (!checkdice)
                    clientInt.showError("Il dado non può essere inserito");
            }else
                clientInt.showError("Hai scelto un dado non valido");

        }
        player.getScheme().setBoxes(greenCarpet.getDiceFromStock(dicecoord[0]), dicecoord[1], dicecoord[2]);
        String schemejson = gson.toJson(player.getScheme());
        clientInt.schemeUpdated(schemejson);
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
