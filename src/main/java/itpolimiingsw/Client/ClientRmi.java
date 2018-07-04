package itpolimiingsw.Client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itpolimiingsw.Game.Game;
import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Ruler;
import itpolimiingsw.Game.Player;
import itpolimiingsw.Game.Dice;
import itpolimiingsw.Game.ToolCardsExecutor;

import itpolimiingsw.Server.ServerRmiClientHandlerInt;
import itpolimiingsw.ServertoClientHandler.ClientInterface;
import itpolimiingsw.ServertoClientHandler.ServertoClient;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

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
            System.out.println("aaException: "+e);
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
            System.out.println("bbException: " + e);
            e.printStackTrace();
        }
    }

    //------------------------------------------does the all RMI connection---------------------------------------------
    private void connect()     throws RemoteException{
        try
        {
            root=leggiDaFile();
            String[] parts=root.split(":");
            try {
                PORT = Integer.parseInt(parts[1]);
            }catch(NumberFormatException e){
                clientInt.showError("Errore inserimento-Hai inserito un valore non numerico per la porta. Eseguo la disconessione.");
                System.exit(0);
            }
            root=parts[0];
            Registry registry = LocateRegistry.getRegistry(root,PORT);
            server=(ServerRmiClientHandlerInt) registry.lookup("RMICONNECTION");
            new HandleServerConnectionForRmi(this).start();
        }
        catch(Exception e)
        {
            System.out.println("ccException: "+e);
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
                    clientInt.showError("Errore login-L'utente selezionato è già connesso. Deve esserci un errore!");
                else if(logged==2)
                    clientInt.showError("Errore inserimento-Password di " + nickname + " errata");
            }
            clientInt.loginOkMessage();
        }
        catch(Exception e)
        {
            clientInt.showError("Eccezione-ddException: "+e);
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
            String useless = b.readLine();
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
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException {
        TimerThread timerThread= new TimerThread(time);
        timerThread.start();
        int choose=clientInt.schemeMessages(scheme1, scheme2, scheme3, scheme4, privategoal);
        if(choose==99) {
            clientInt.endTurnMessage();
            Random random = new Random();
            return random.nextInt(4) + 1;
        }
        timerThread.interrupt();
        return choose;
    }

    //-------------------------------------------------------------------------------------------------------------------
    class TimerThread extends Thread implements Serializable {
        int time;
        int i;
        //constructor

        public TimerThread(int time) {
            this.time = time;
        }
        @Override
        public void run(){
            i=0;
            while (i<time) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) { }
                i++;
            }clientInt.timerOut(true);
            return;
        }
        public int getTime() {
            return i;
        }
        public void setTime(){
            i=time;
        }
        public int getMaxTime() {return time;}
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
            Game game = new Game(0, null);
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
                } catch (InterruptedException e) {return;}
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
                            } catch (InterruptedException e) { return;}
                            usedDice = true;
                            if (usedTool) {
                                timerThread.setTime();
                                return;
                            }
                        } else
                            clientInt.showError("Errore-Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                    } else
                        clientInt.showError("Errore-Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
                } else if (value.equals("3")) {
                    if(!usedTool) {
                        try {
                            flagTool = placeTool(greenCarpet, player, i, usedDice);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {return;}
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
                    }else
                        clientInt.showError("Scelta errata-Hai già utilizzato una carta tool in questo giro!");
                }
                if (timerThread.getTime()==timerThread.getMaxTime())
                    return;
            }
        }
    }

    private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean useddice) throws IOException, InterruptedException {
        String message;
        boolean flag;
        int choice;
        int realchoice=2;
        do {
            choice=clientInt.chooseToolMessages();
            if(choice==0){
                return 1;
            }
            flag=greenCarpet.toolIsIn(choice);
            if(!flag)
                clientInt.showError("Errore-La carta utensile scelta non è presente sul tavolo da gioco");
        }while (!flag);
        boolean toolok=false;
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        String goon;
        boolean exit=false;
        boolean tooldice=false;
        System.out.println("tool scelta: "+choice);
        if(choice>0 && choice<13) {
            //rembember to check if the tool chosen is inside the greencarpet.
            if(choice==3) {
                choice = 2;
                realchoice = 3;
            }
            else if(choice==2)
                realchoice=2;
            switch (choice) {
                case 1:     //no placement
                    int vdice=clientInt.chooseDice();
                    if(vdice==99)
                        return 1;
                    int selection=clientInt.tool1Messages();
                    if(selection==99)
                        return 1;
                    toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice,vdice, selection);
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }

                    break;


                case 2:     //used for tool 2 & 3
                    int[] coordinates;
                    coordinates=clientInt.tool23Messages();
                    if(coordinates[0]==99)
                        return 1;
                    toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, realchoice, coordinates);
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;

                case 4:
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
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;
                case 5:
                    vdice = clientInt.chooseDice();
                    if(vdice==99)
                        return 1;
                    int[] dicepos = clientInt.chooseFromPath();
                    if(dicepos[0]==99)
                        return 1;
                    toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, vdice, dicepos);
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;
                case 6:
                    Ruler ruler = new Ruler();
                    boolean checkcorrdice=false;
                    Dice dice=null;
                    coordinates = new int[2];
                    if(!useddice) {
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
                                greenCarpet.getDiceFromStock(ndice);
                                player.getScheme().setBoxes(dice, coordinates[0], coordinates[1]);
                                tooldice=true;
                            }
                        } else
                            clientInt.showError("Errore-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                    }else{
                        clientInt.showError("Errore-Non puoi utilizzare questa toolcard. Hai già piazzato un dado in questo turno.");
                    }
                    break;
                case 7:
                    if(greenCarpet.getTurn()==2 && !useddice)
                        toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice);
                    else
                        clientInt.showError("Errore-Questa toolcard non è attualmente utilizzabile.");
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;
                case 8:
                    if(greenCarpet.getTurn()==1 && useddice){
                        toolok = tool89method(player, greenCarpet, choice);
                    }else {
                        clientInt.showError("Errore-Questa toolcard non è attualmente utilizzabile. Ricordati di piazzare un dado prima di utilizzarla!");
                    }
                    if(toolok)
                        tooldice=true;
                    break;
                case 9:
                    //se 0 exit true e toolok true
                    //se 1 return 1
                    //se 2 ho usato la tool --> toolok=true e tooldice=true;
                    //se 3 --> toolok=false;
                    if(!useddice)
                        toolok = tool89method(player, greenCarpet, choice);
                    else
                        clientInt.showError("Errore-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                    if(toolok)
                        tooldice=true;
                    break;
                case 10:
                    int ndice = clientInt.chooseDice();
                    if(ndice==99)
                        return 1;
                    toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, ndice, 0);
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;
                case 11:
                    ruler = new Ruler();
                    checkcorrdice=false;
                    if(!useddice){
                        ndice = clientInt.chooseDice();
                        if(ndice==99)
                            return 1;
                        dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice, choice, 0);
                        if(dice!=null) {
                            dice.setFace("");
                            String dicejson = gson.toJson(dice);
                            int value = clientInt.tool11Messages(dicejson);
                            if(value==99) {
                                Random rnd = new Random();
                                int val = rnd.nextInt(6) + 1;
                                dice.setFace(ruler.intToString(val));
                                greenCarpet.getDiceFromStock(ndice);
                                greenCarpet.setDiceInStock(dice);
                                return 1;
                            }
                            dice.setFace(ruler.intToString(value));
                            while (!checkcorrdice) {
                                int[] coord = clientInt.chooseCoordinates();
                                if(ruler.checkAvailableDice(dice, player.getScheme())) {
                                    checkcorrdice = ruler.checkCorrectPlacement(coord[0], coord[1], dice, player.getScheme());
                                    if(checkcorrdice) {
                                        greenCarpet.getDiceFromStock(ndice);
                                        player.getScheme().setBoxes(dice, coord[0], coord[1]);
                                        tooldice = true;
                                    }
                                }
                                else
                                    checkcorrdice = true;
                            }
                        }else{
                            clientInt.showError("Errore-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                        }
                    }else
                        clientInt.showError("Errore-Non puoi utilizzare questa toolcard. Hai già piazzato un dado in questo turno.");
                    break;
                case 12:
                    int[] coordinates12 = clientInt.tool12Messages();
                    if(coordinates12[0]==99)
                        return 1;
                    int numOfDices=coordinates12[8];
                    toolok = toolCardsExecutor.useMovementCard(player, greenCarpet,choice, numOfDices, coordinates12);
                    if(!toolok){
                        clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
                    }
                    break;
            }
        }else{
            clientInt.showError("Errore-Hai inserito un valore sbagliato!");
        }
        String greenupd = gson.toJson(greenCarpet);
        String playerupd = gson.toJson(player);
        if(!tooldice || !(useddice && toolok))
            clientInt.updateView(greenupd, playerupd);
        if(tooldice)
            return 1;
        else
            return 2;

    }


    //-----------------------------------------TOOL METHODS-------

    private boolean tool89method(Player player, GreenCarpet greenCarpet, int choice) throws IOException, InterruptedException {
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        boolean toolok89;
        int vdice = clientInt.chooseDice();
        if (vdice == 99)
            return false;
        int[] dicepos = clientInt.chooseCoordinates();
        if (dicepos[0] == 99)
            return false;
        toolok89 = toolCardsExecutor.usePlacementCard(player, greenCarpet, vdice, choice, dicepos[0], dicepos[1]);
        if(!toolok89)
            clientInt.showError("Non è stato possibile utilizzare la tool.");
        return toolok89;
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
                    clientInt.showError("Errore-Il dado non può essere inserito");
            }else
                clientInt.showError("Errore-Hai scelto un dado non valido");

        }
        player.getScheme().setBoxes(greenCarpet.getDiceFromStock(dicecoord[0]), dicecoord[1], dicecoord[2]);
        String playerjson = gson.toJson(player);
        String greencarpetjson = gson.toJson(greenCarpet);
        clientInt.updateView(greencarpetjson,playerjson);
    }
    public Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException {
        Game game =new Game(0, null);
        TimerThread timerThread=new TimerThread(time);
        timerThread.start();
        HandleTurn handleTurn=new HandleTurn( greenCarpet,  player,  i,timerThread);
        handleTurn.start();
        while (timerThread.getTime()<time){sleep(200);}
        game.setGreenCarpet(handleTurn.getGreenCarpet());
        game.setPlayer(handleTurn.getPlayer(), handleTurn.getI());
        clientInt.timerOut(true);
        sleep(300);
        clientInt.endTurn();
        timerThread.interrupt();
        return game;
    }


    public boolean serverAlive() throws RemoteException{
        boolean alive;
        try {
            alive = server.serverConnected();
        }catch(RemoteException e){
            alive=false;
            clientInt.showError("Errore-Il server sembra essere offline. Chiudo il programma.");
            clientInt.exit();
        }
        return alive;
    }

    @Override
    public Boolean newMatch() throws IOException, InterruptedException {
        boolean check;
        check = clientInt.newMatch();
        if(check)
            return true;
        else{
            clientInt.exit();
            return false;
        }
    }

    @Override
    public void showScore(String[] score) {
        System.out.println("La partita è terminata. Stampo la classifica.");
        clientInt.showScore(score);
    }

    @Override
    public void sendConnDiscMessage(String message) throws RemoteException {
        clientInt.showConnDiscPopup(message);
    }
}
