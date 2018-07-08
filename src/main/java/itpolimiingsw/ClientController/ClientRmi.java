package itpolimiingsw.ClientController;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itpolimiingsw.GameController.Game;
import itpolimiingsw.RMIDisconnectionHandler.HandleServerDisconnection;
import itpolimiingsw.GameTools.*;
import itpolimiingsw.GameItems.Dice;
import itpolimiingsw.GameCards.ToolCards;
import itpolimiingsw.Server.ServerRmiClientHandlerInt;
import itpolimiingsw.UserExperience.ClientInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import static java.lang.Thread.sleep;


public class ClientRmi implements ClientRmiInt, ServertoClient {

    private ServerRmiClientHandlerInt server;
    private String nickname;
    private ClientInterface clientInt;
    private Gson gson = new GsonBuilder().create();
    private int PORT;

    //-----------------------------------------launch execute method---------------------------------------------
    public ClientRmi(ClientInterface clientInt) throws RemoteException {
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

    /**
     * Launches connect method + login method.
     */
    private void execute() throws RemoteException{
        try
        {
            connect();
            login();
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * It creates the rmi connection with the server
     */
    private void connect() throws RemoteException {
        String root;
        try
        {
            root=readFromFile();
            String[] parts=root.split(":");
            try {
                PORT = Integer.parseInt(parts[1]);
            }catch(NumberFormatException e){
                clientInt.showError("Errore inserimento-Hai inserito un valore non numerico per la porta. Eseguo la disconessione.");
                System.exit(0);
            }
            root=parts[0];
            System.setProperty("java.rmi.server.hostname", parts[2]);
            Registry registry = LocateRegistry.getRegistry(root,PORT);
            server=(ServerRmiClientHandlerInt) registry.lookup("RMICONNECTION");
            UnicastRemoteObject.exportObject(this, Integer.parseInt(parts[3]));
            new HandleServerDisconnection(this).start();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    /**
     * Launches the login methods. If login is ok it add the user to the database on the server.
     */
    private void login() throws RemoteException {
        String[] logindata;
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
            clientInt.showError("Eccezione-Exception: "+e);
        }
    }


    @Override
    public boolean aliveMessage() throws RemoteException{
        return true;
    }

    /**
     * It reads from a file the connection informations.
     * @return the String with the informations.
     * @throws IOException for the readline method.
     */
    private String readFromFile() throws IOException {
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

    @Override
    public void sendMessageOut(String message) throws RemoteException{
        clientInt.showMessage(message);
    }


    //******************************************game methods***********************************************


    @Override
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException, RemoteException {
        TimerThread timerThread= new TimerThread(time);
        timerThread.start();
        int choose=clientInt.schemeMessages(scheme1, scheme2, scheme3, scheme4, privategoal);
        if(choose==99) {
            clientInt.endTurnMessage();
            Random random = new Random();
            return random.nextInt(4) + 1;
        }
        timerThread.setTimeScheme();
        return choose;
    }

    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Handle timer class.
     */
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
                } catch (InterruptedException e) {return; }
                i++;
                clientInt.sendTimer(time-i);
            }
            clientInt.timerOut(true);
            return;
        }
        public int getTime() {
            return i;
        }
        public void setTime(){
            this.interrupt();
        }
        public void setTimeScheme(){
            i=time;
        }
        public int getMaxTime() {return time;}
    }

    class HandleTurn implements Serializable {
        GreenCarpet greenCarpet;
        Player player;
        int i;
        TimerThread timerThread;
        Boolean usedDice = false;
        boolean usedTool = false;

        public HandleTurn(GreenCarpet greenCarpet, Player player, int i,TimerThread timerThread) {
            this.greenCarpet = greenCarpet;
            this.player = player;
            this.i = i;
            this.timerThread=timerThread;
        }

        public GreenCarpet getGreenCarpet() {
            return greenCarpet;
        }

        public void setUsedDice(){
            usedDice=false;
            usedTool = false;
        }

        public Player getPlayer() {
            return player;
        }

        public int getI() {
            return i;
        }


        public void run()  {
            clientInt.timerOut(false);
            usedDice = false;
            usedTool = false;
            int flagTool=0;
            Ruler ruler = new Ruler();
            String value=null;
            String greencarpetjson = gson.toJson(greenCarpet);
            String playerjson = gson.toJson(player);
            clientInt.printCarpetFirst(greencarpetjson, playerjson);
            int k = timerThread.getMaxTime();
            while (true) {
                try {
                    value = clientInt.handleTurnMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(value.equals("4")) {
                    timerThread.setTime();
                    return;
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
                        if (flagTool==1 || (flagTool==2 && usedDice)) {     //used a toolcard which include dice placement
                            timerThread.setTime();
                            return;
                        }
                        if(flagTool==2 && !usedDice)
                            usedTool = true;
                        if(flagTool==3)
                            usedTool = false;
                    }else
                        clientInt.showError("Scelta errata-Hai già utilizzato una carta tool in questo giro!");
                }
                if (timerThread.getTime()==k)
                    return;
            }
        }

        private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean useddice) throws IOException, InterruptedException {
            boolean[] res = new boolean[2];
            res[0]=false;
            res[1]=false;
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
            boolean tooldice=false;
            if(choice>0 && choice<13) {
                if(choice==3) {
                    choice = 2;
                    realchoice = 3;
                }
                else if(choice==2)
                    realchoice=2;
                switch (choice) {
                    case 1:     //no placement
                        clientInt.showToolTricks(new ToolCards(1).getName(), new ToolCards(1).getUsagetricks());
                        toolok = tool1(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 2:     //used for tool 2 & 3
                        clientInt.showToolTricks(new ToolCards(realchoice).getName(), new ToolCards(realchoice).getUsagetricks());
                        toolok = tool23(player, greenCarpet, toolCardsExecutor, realchoice);
                        break;
                    case 4:
                        clientInt.showToolTricks(new ToolCards(4).getName(), new ToolCards(4).getUsagetricks());
                        toolok = tool4(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 5:
                        clientInt.showToolTricks(new ToolCards(5).getName(), new ToolCards(5).getUsagetricks());
                        toolok = tool5(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 6:
                        clientInt.showToolTricks(new ToolCards(6).getName(), new ToolCards(6).getUsagetricks());
                        res = tool6(player, greenCarpet, toolCardsExecutor, useddice);
                        toolok=res[0];
                        tooldice=res[1];
                        break;
                    case 7:
                        clientInt.showToolTricks(new ToolCards(7).getName(), new ToolCards(7).getUsagetricks());
                        toolok = tool7(player, greenCarpet, toolCardsExecutor, useddice);
                        break;
                    case 8:
                        clientInt.showToolTricks(new ToolCards(8).getName(), new ToolCards(8).getUsagetricks());
                        if(greenCarpet.getTurn()==1 && useddice){
                            toolok = tool89method(player, greenCarpet, choice);
                        }else {
                            clientInt.showError("Errore-Questa toolcard non è attualmente utilizzabile. Ricordati di piazzare un dado prima di utilizzarla!");
                        }
                        if(toolok)
                            tooldice=true;
                        break;
                    case 9:
                        clientInt.showToolTricks(new ToolCards(9).getName(), new ToolCards(9).getUsagetricks());
                        if(!useddice)
                            toolok = tool89method(player, greenCarpet, choice);
                        else
                            clientInt.showError("Errore-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        if(toolok)
                            tooldice=true;
                        break;
                    case 10:
                        clientInt.showToolTricks(new ToolCards(10).getName(), new ToolCards(10).getUsagetricks());
                        toolok = tool10(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 11:
                        clientInt.showToolTricks(new ToolCards(11).getName(), new ToolCards(11).getUsagetricks());
                        res = tool11(player, greenCarpet, toolCardsExecutor, useddice);
                        toolok=res[0];
                        tooldice=res[1];
                        break;
                    case 12:
                        clientInt.showToolTricks(new ToolCards(12).getName(), new ToolCards(12).getUsagetricks());
                        toolok = tool12(player, greenCarpet, toolCardsExecutor);
                        break;
                }
            }else{
                clientInt.showError("Errore-Hai inserito un valore sbagliato!");
            }
            if(!toolok)
                clientInt.showError("Errore-Non è stato possibile utilizzare la tool.");
            String greenupd = gson.toJson(greenCarpet);
            String playerupd = gson.toJson(player);
            if(!tooldice || !(useddice && toolok))
                clientInt.updateView(greenupd, playerupd);
            if(tooldice)      //return 1 if the tool card include dice placement
                return 1;
            else
            if(toolok)      //used a tool without placement
                return 2;
            else            //no tool used
                return 3;

        }

        private boolean tool1(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException{
            int vdice=clientInt.chooseDice();
            if(vdice==99)
                return false;
            int selection=clientInt.tool1Messages();
            if(selection==99)
                return false;
            return toolCardsExecutor.changeDiceCard(player, greenCarpet, 1,vdice, selection);
        }

        private boolean tool23(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, int realchoice) throws IOException, InterruptedException {
            int[] coordinates;
            if(player.getScheme().isEmpty())
                return false;
            coordinates=clientInt.tool23Messages();
            if(coordinates[0]==99)
                return false;
            return toolCardsExecutor.useMovementCard(player, greenCarpet, realchoice, coordinates);
        }

        private boolean tool4(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException{
            Ruler ruler = new Ruler();
            if(ruler.schemeCount(player.getScheme())<2)
                return false;
            int[] coordinates = clientInt.tool4Messages();
            if (coordinates[0] == 99)
                return false;
            int[] coord1dice = new int[4];
            int[] coord2dice = new int[4];
            for (int k = 0; k < coordinates.length; k++) {
                if (k < 4)
                    coord1dice[k] = coordinates[k];
                else
                    coord2dice[k % 4] = coordinates[k];
            }
            return toolCardsExecutor.useMovementCard(player, greenCarpet, 4, coord1dice, coord2dice);
        }

        private boolean tool5(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            if(!greenCarpet.checkEmptyRoundpath())
                return false;
            int vdice = clientInt.chooseDice();
            if (vdice == 99)
                return false;
            int[] dicepos = clientInt.chooseFromPath();
            if (dicepos[0] == 99)
                return false;
            return toolCardsExecutor.changeDiceCard(player, greenCarpet, 5, vdice, dicepos);
        }

        private boolean[] tool6(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException{
            Ruler ruler = new Ruler();
            boolean checkcorrdice=false;
            Dice dice;
            int [] coordinates = new int[2];
            boolean[] ret = new boolean[2];
            ret[0]=false;       //used the tool
            ret[1]=false;       //placed dice
            if(!useddice) {
                int ndice = clientInt.chooseDice();
                if(ndice==99)
                    return ret;
                dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice,6,0);
                if (dice != null) {
                    if (ruler.checkAvailableDice(dice, player.getScheme())) {
                        String dicejson=gson.toJson(dice);
                        while (!checkcorrdice) {
                            coordinates=clientInt.tool6Messages(dicejson);
                            if(coordinates[0]==99)
                                return ret;
                            checkcorrdice = ruler.checkCorrectPlacement(coordinates[0], coordinates[1], dice, player.getScheme());
                            if(!checkcorrdice)
                                clientInt.showError("Errore-Non è possibile inserire il dado in questa posizione, scegline una nuova.");
                        }
                        greenCarpet.getDiceFromStock(ndice);
                        player.getScheme().setBoxes(dice, coordinates[0], coordinates[1]);
                        ret[0]=true;
                        ret[1]=true;
                    }
                } else
                    clientInt.showError("Errore-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
            }else{
                clientInt.showError("Errore-Non puoi utilizzare questa toolcard. Hai già piazzato un dado in questo turno.");
            }
            return ret;
        }

        private boolean tool7(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice)  {
            boolean toolok =  false;
            if(greenCarpet.getTurn()==2 && !useddice)
                toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, 7);
            else
                clientInt.showError("Errore-Questa toolcard non è attualmente utilizzabile.");
            return toolok;
        }

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
            return toolok89;
        }

        private boolean tool10(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            int ndice = clientInt.chooseDice();
            if(ndice==99)
                return false;
            return toolCardsExecutor.changeDiceCard(player, greenCarpet, 10, ndice, 0);
        }

        private boolean[] tool11(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException{
            Ruler ruler = new Ruler();
            boolean checkcorrdice=false;
            boolean[] ret = new boolean[2];
            ret[0]=false;       //used the tool
            ret[1]=false;       //placed dice
            if(!useddice){
                int ndice = clientInt.chooseDice();
                if(ndice==99)
                    return ret;
                Dice dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, ndice, 11, 0);
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
                        ret[0]=true;
                        return ret;
                    }
                    dice.setFace(ruler.intToString(value));
                    while (!checkcorrdice) {
                        int[] coord = clientInt.chooseCoordinates();
                        if(ruler.checkAvailableDice(dice, player.getScheme())) {
                            checkcorrdice = ruler.checkCorrectPlacement(coord[0], coord[1], dice, player.getScheme());
                            if(checkcorrdice) {
                                greenCarpet.getDiceFromStock(ndice);
                                player.getScheme().setBoxes(dice, coord[0], coord[1]);
                                ret[0]=true;
                                ret[1]=true;
                            }else
                                clientInt.showError("Errore-Non è possibile inserire il dado in questa posizione, scegline una nuova.");
                        }
                        else{
                            checkcorrdice = true;
                            ret[0]=true;
                        }
                    }
                }else{
                    clientInt.showError("Errore-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                }
            }else
                clientInt.showError("Errore-Non puoi utilizzare questa toolcard. Hai già piazzato un dado in questo turno.");

            return ret;
        }

        private boolean tool12(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            if(player.getScheme().isEmpty() || !greenCarpet.checkEmptyRoundpath())
                return false;
            int[] coordinates12 = clientInt.tool12Messages();
            if(coordinates12[0]==99)
                return false;
            int numOfDices=coordinates12[8];
            return toolCardsExecutor.useMovementCard(player, greenCarpet,12, numOfDices, coordinates12);
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
                        clientInt.showPlacementeError("Errore-Il dado non può essere inserito");
                }else
                    clientInt.showError("Errore-Hai scelto un dado non valido");

            }
            player.getScheme().setBoxes(greenCarpet.getDiceFromStock(dicecoord[0]), dicecoord[1], dicecoord[2]);
            String playerjson = gson.toJson(player);
            String greencarpetjson = gson.toJson(greenCarpet);
            clientInt.updateView(greencarpetjson,playerjson);
        }
    }

    //-----------------------------------------TOOL METHODS-------------------------------------------------------------
    @Override
    public Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException, RemoteException {
        Game game =new Game( null);
        TimerThread timerThread=new TimerThread(time);
        timerThread.start();
        HandleTurn handleTurn=new HandleTurn( greenCarpet,  player,  i,timerThread);
        handleTurn.run();
        game.setGreenCarpet(handleTurn.getGreenCarpet());
        game.setPlayer(handleTurn.getPlayer());
        handleTurn.setUsedDice();
        sleep(300);
        clientInt.endTurn();
        timerThread.interrupt();
        return game;
    }

    @Override
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
    public Boolean newMatch() throws IOException, InterruptedException, RemoteException {
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
    public void showScore(String[] score) throws RemoteException {
        clientInt.showScore(score);
    }

    @Override
    public void sendConnDiscMessage(String message) throws RemoteException {
        clientInt.showConnDiscPopup(message);
    }
}
