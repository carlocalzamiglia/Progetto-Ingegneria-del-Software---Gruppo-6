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
                    clientInt.showMessage("L'utente selezionato è già connesso.");
                else if(logged==2)
                    clientInt.showMessage("Password di " + username + " errata");
            }
        }
        catch(Exception e)
        {
            clientInt.showMessage("Exception: "+e);
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
        clientInt.showMessage(message);
    }


    //******************************************game methods***********************************************+
    @Override
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4) throws IOException, InterruptedException {
        TimerThread timerThread= new TimerThread(0);
        timerThread.start();
        int choose=clientInt.schemeMessages(scheme1, scheme2, scheme3, scheme4);
        if(choose==99) {
            clientInt.endTurn();
            Random random = new Random();
            return random.nextInt(4) + 1;
        }
        timerThread.interrupt();
        return choose;
    }

    //-------------------------------------------------------------------------------------------------------------------
    class TimerThread extends Thread implements Serializable {
        int time;
        //constructor

        public TimerThread(int time) {
            this.time = time;
        }
        @Override
        public void run(){
            while (time<20) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) { }
                time++;
            }clientInt.timerOut(true);
            return;
        }
        public int getTime() {
            return time;
        }
        public void setTime(){
            time=20;
        }
    }

    class HandleTurn extends Thread implements Serializable {
        GreenCarpet greenCarpet;
        Player player;
        int i;
        TimerThread timerThread;

        public HandleTurn(GreenCarpet greenCarpet, Player player, int i,TimerThread timerThread) {
            this.greenCarpet = greenCarpet;
            this.player = player;
            this.i = i;
            this.timerThread=timerThread;
        }

        public GreenCarpet getGreenCarpet() {
            return greenCarpet;
        }

        public Player getPlayer() {
            return player;
        }

        public int getI() {
            return i;
        }

        @Override
        public void run()  {
            clientInt.timerOut(false);
            Game game = new Game(0);
            boolean usedDice = false;
            boolean flag = true;
            int flagTool = 0;
            boolean usedTool = false;
            Ruler ruler = new Ruler();
            String value=null;
            String greencarpetjson = gson.toJson(greenCarpet);
            String playerjson = gson.toJson(player);
            clientInt.printCarpetFirst(greencarpetjson, playerjson);
            while (true) {
                try {
                    value = clientInt.handleTurnMenu();
                    if(value.equals("4")) {
                        timerThread.setTime();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (value.equals("1")) {
                    timerThread.setTime();
                    return;
                } else if (value.equals("2")) {//DICE
                    if (ruler.checkAvailable(greenCarpet, player.getScheme())) {
                        if (!usedDice) {
                            try {
                                placedice(greenCarpet, player, i);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            usedDice = true;
                            if (usedTool) {
                                timerThread.setTime();
                                return;
                            }
                        } else
                            clientInt.showMessage("Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                    } else
                        clientInt.showMessage("Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
                } else if (value.equals("3")) {
                    try {
                        flagTool = placeTool(greenCarpet, player, i, usedDice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flagTool == 1) {     //used a toolcard which include dice placement
                        timerThread.setTime();
                        return;
                    }
                    if (flagTool == 2) {
                        if (!usedDice)
                            usedTool = true;
                        else {
                            timerThread.setTime();
                            return;
                        }
                    }
                }
                if (timerThread.getTime()==20)
                    return;
            }
        }
    }

    private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean usedDice) throws IOException, InterruptedException {
        String message;
        boolean flag;
        int choice;
        do {
            choice=clientInt.chooseToolMessages();
            if(choice==0){
                return 1;
            }
            flag=greenCarpet.toolIsIn(choice);
            if(!flag)
                clientInt.showMessage("La carta utensile scelta non è presente sul tavolo da gioco");
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")){
                            int vdice=clientInt.chooseDice();
                            if(vdice==99)
                                return 1;
                            int selection=clientInt.tool1Messages();
                            if(selection==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool23Messages();
                            if(coordinates[0]==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool23Messages();
                            if(coordinates[0]==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int[] coordinates;
                            coordinates=clientInt.tool4Messages();
                            if(coordinates[0]==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int vdice = clientInt.chooseDice();
                            if(vdice==99)
                                return 1;
                            int[] dicepos = clientInt.chooseFromPath();
                            if(dicepos[0]==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int ndice = clientInt.chooseDice();
                            if(ndice==99)
                                return 1;
                            dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice,6,0);
                            if (dice != null) {
                                if (ruler.checkAvailableDice(dice, player.getScheme())) {
                                    String dicejson=gson.toJson(dice);
                                    while (!checkcorrdice) {
                                        coordinates=clientInt.tool6Messages(dicejson);
                                        if(coordinates[0]==99)
                                            return 1;
                                        checkcorrdice = ruler.checkCorrectPlacement(coordinates[0], coordinates[1], dice, player.getScheme());
                                    }
                                    player.getScheme().setBoxes(dice, coordinates[0], coordinates[1]);
                                    tooldice=true;
                                }
                                else
                                    greenCarpet.setDiceInStock(dice);
                                toolok = true;
                            } else
                                clientInt.showMessage("C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                        }else{
                            toolok=true;
                            exit=true;
                        }
                    }
                    if(usedDice){
                        clientInt.showMessage("Non puoi utilizzare questa carta tool. Hai già piazzato un dado!");
                        exit=true;
                    }
                    break;
                case 7:
                    while(!toolok) {
                        if(greenCarpet.getTurn()==2 && !usedDice) {
                            goon = clientInt.goOnTool();
                            if(goon.equals("0"))
                                return 1;
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
                            if(goon.equals("0"))
                                return 1;
                            if(goon.equals("y")){
                                int vdice=clientInt.chooseDice();
                                if(vdice==99)
                                    return 1;
                                int[] dicepos=clientInt.chooseCoordinates();
                                if(dicepos[0]==99)
                                    return 1;
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
                            if(goon.equals("0"))
                                return 1;
                            if (goon.equals("y")) {
                                int vdice = clientInt.chooseDice();
                                if(vdice==99)
                                    return 1;
                                int[] dicepos = clientInt.chooseCoordinates();
                                if(dicepos[0]==99)
                                    return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int ndice = clientInt.chooseDice();
                            if(ndice==99)
                                return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")){
                            int ndice = clientInt.chooseDice();
                            if(ndice==99)
                                return 1;
                            dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice, choice, 0);
                            if(dice!=null) {
                                while (!checkcorrdice) {
                                    dice.setFace("");
                                    String dicejson = gson.toJson(dice);
                                    int[] value = clientInt.tool11Messages(dicejson);
                                    if(value[0]==99)
                                        return 1;
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
                        if(goon.equals("0"))
                            return 1;
                        if(goon.equals("y")) {
                            int[] coordinates12 = clientInt.tool12Messages();
                            if(coordinates12[0]==99)
                                return 1;
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
            clientInt.showMessage("Hai inserito un valore sbagliato!");
        }
        String greenupd = gson.toJson(greenCarpet);
        String playerupd = gson.toJson(player);

        clientInt.printTool(greenupd, playerupd);
        if(exit)
            return 3;
        if(tooldice)
            return 1;
        else
            return 2;

    }


    private void placedice(GreenCarpet greenCarpet, Player player, int i) throws IOException, InterruptedException {   //i is player's number for "getplayer"
        Boolean checkdice = false;
        Ruler ruler = new Ruler();
        int[] dicecoord = new int[3];
        while (!checkdice) {
            dicecoord=clientInt.placeDiceMessages();
            if(dicecoord[0]==99)
                return;
            Dice dice = greenCarpet.checkDiceFromStock(dicecoord[0]);
            if(dice!=null) {
                checkdice = ruler.checkCorrectPlacement(dicecoord[1], dicecoord[2], dice, player.getScheme());
                if (!checkdice)
                    clientInt.showMessage("Il dado non può essere inserito");
            }else
                clientInt.showMessage("Hai scelto un dado non valido");

        }
        player.getScheme().setBoxes(greenCarpet.getDiceFromStock(dicecoord[0]), dicecoord[1], dicecoord[2]);
        String schemejson = gson.toJson(player.getScheme());
        clientInt.schemeUpdated(schemejson);
    }
    public Game endTurn(GreenCarpet greenCarpet, Player player, int i) throws InterruptedException, IOException {
        Game game =new Game(0);
        TimerThread timerThread=new TimerThread(0);
        timerThread.start();
        HandleTurn handleTurn=new HandleTurn( greenCarpet,  player,  i,timerThread);
        handleTurn.start();

        while (timerThread.getTime()<20){sleep(200);}
        game.setGreenCarpet(handleTurn.getGreenCarpet());
        game.setPlayer(handleTurn.getPlayer(), handleTurn.getI());
        clientInt.timerOut(true);
        sleep(300);
        clientInt.endTurn();
        return game;
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
