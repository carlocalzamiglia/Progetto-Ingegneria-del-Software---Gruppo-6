package itpolimiingsw.Server;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import itpolimiingsw.Game.*;

import itpolimiingsw.ServertoClientHandler.ServertoClient;

import javax.tools.Tool;
import java.io.Serializable;

import java.io.*;
import java.net.Socket;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Thread.sleep;


public class ServerSocketClientHandler implements Runnable,ServertoClient {
    private Socket socket;
    private DBUsers DB;
    private Matches matches;
    String message = "";
    String [] arrOfMsg;
    Gson gson = new GsonBuilder().create();


    //-------------------------------------------------constructor------------------------------------------------------
    public ServerSocketClientHandler(Socket socket, DBUsers DB, Matches matches) throws IOException {
        this.socket = socket;
        this.DB = DB;
        this.matches=matches;
    }

    //-----------------------------------------------connection method--------------------------------------------------
    @Override
    public void run() {
        try {

            Scanner in = new Scanner(socket.getInputStream());
            String user = in.nextLine();
            String psw = in.nextLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            int check=DB.login(user, psw);
            while (check!=0 && check != 1) {
                if(check==2) {
                    out.println("2");
                    out.flush();
                }else if(check==3){
                    out.println("3");
                    out.flush();
                }
                user = in.nextLine();
                psw = in.nextLine();
                check=DB.login(user, psw);
            }
            out.println("0");
            out.flush();
            DB.getUser(user).setClientHandler(this);
            String nickname = in.nextLine();
            if(check==1) {
                if(matches.getGame(user)!=null) {
                    matches.getGame(user).playerConnect();
                    if(matches.getPlayer(user)!=null)
                        matches.getPlayer(user).setOnline(true);
                    matches.getUser(user).setClientHandler(this);
                    matches.getUser(user).setOnline(true);
                    matches.getGame(user).reconnectUser();
                }else {
                    matches.addUser(DB.getUser(user));
                }
            }
            if(check==0){
                matches.addUser(DB.getUser(user));
            }

            new ListenFromClient(user).start();
            System.out.println(nickname + " loggato con connessione socket");
            newUserMessage(nickname, " ha appena effettuato il login ed è pronto a giocare.");
            sendMessageOut("Benvenuto, "+nickname+". La partita inizierà a breve!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------wait for messages from client--------------------------------------------
    class ListenFromClient extends Thread implements Serializable {
        String nickname;
        //constructor

        public ListenFromClient(String nickname) {
            this.nickname = nickname;
        }
        //HERE THE IMPLEMENTATION OF THE PROTOCOL
        public void run() {
            ObjectInputStream ins;
            while (true) {
                try {
                    String msg="";
                    ins = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    msg = (String) ins.readObject();
                    arrOfMsg = msg.split("-");
                    message=arrOfMsg[0];
                    System.out.println("il protocollo è: "+message);
                    if (message.equals("@LOGOUT")) {
                        DB.getUser(nickname).setOnline(false);
                        DB.getUser(nickname).setClientHandler(null);
                        System.out.println(nickname+ " si è appena disconnesso.");
                    }else if(message.equals("@RECONNECT")){
                        clientAlive(this.nickname);
                    }
                } catch (IOException e) {
                    try {
                        clientAlive(this.nickname);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //-----------------------------------------check if client is online yet--------------------------------------------
    public void clientAlive(String nickname) throws IOException, InterruptedException {
        //if (DB.getUser(nickname).isOnline()) {
         //       try {
         //           sendMessageOut("@ALIVE");
         //           return true;
         //       }catch(IOException e){
        DB.getUser(nickname).setOnline(false);
        DB.getUser(nickname).setClientHandler(null);
        if(matches.getGame(nickname)!=null) {
            matches.getGame(nickname).playerDisconnect();
            if (matches.getGame(nickname).getPlaying()) {
                matches.getUser(nickname).setOnline(false);
                if (matches.getPlayer(nickname) != null)
                    matches.getPlayer(nickname).setOnline(false);
            }
        }
        newUserMessage(nickname, " è uscito dalla partita.");
        System.out.println(nickname + " ha probabilmente perso la connessione.");
        message="@DEAD";
        return;
          //      }
        //} else {
        //    return false;
       // }

    }

    //-----------------------------------------new client connected message---------------------------------------------
    public void newUserMessage(String nickname, String message) throws IOException, InterruptedException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getConnectionType() != null) {
                    sleep(1000);
                    try {
                        DB.getUser(i).getConnectionType().sendConnDiscMessage(nickname + message);
                    }catch(RemoteException | SocketException e){}
                }
            }
        }
    }

    //--------------------------------------------send message to the user----------------------------------------------
    public void sendMessageOut(String message) throws IOException {
        try {
            ObjectOutputStream outs;
            outs = new ObjectOutputStream(socket.getOutputStream());
            outs.writeObject(message);
        }catch(IOException e){}
    }


    //just for rmi, we'll see.
    @Override
    public boolean aliveMessage() {
        return false;
    }


    //********************************************* all game methods ***************************************+

    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException {
        message="";
        sendMessageOut("@SCHEME-"+scheme1+"-"+scheme2+"-"+scheme3+"-"+scheme4+"-"+privategoal+"-"+time);
        while(!(message.equals("@SCHEME")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(300);}
        if(message.equals("@DEAD"))
            return 0;
        if(message.equals("@TIMEROUT")){
            message="";
            Random random =new Random();
            int scheme = random.nextInt(4)+1;
            return scheme;
        }
        return Integer.parseInt(arrOfMsg[1]);
    }


    public Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException {
        Game game =new Game(time, null);
        HandleTurn handleTurn=new HandleTurn( greenCarpet,  player,  i, time);
        handleTurn.run();
        game.setGreenCarpet(handleTurn.getGreenCarpet());
        game.setPlayer(handleTurn.getPlayer(), handleTurn.getI());
        if(message.equals("@DEAD"))
            return null;
        return game;
    }


    @Override
    public Boolean newMatch() throws IOException, InterruptedException {
        sendMessageOut("@ENDGAMEACTION");
        while (!(message.equals("@ENDGAMEACTION"))&& !message.equals("@DEAD")) {sleep(300);}
        if(!message.equals("@DEAD")) {
            if (arrOfMsg[1].equals("true"))
                return true;
            else {
                return false;
            }
        }else
            return false;
    }

    @Override
    public void showScore(String[] score) {

        String string=new String();
        for (int i=0;i<score.length;i++)
            string=string+score[i]+"_";

        System.out.println(string);
        try {
            sendMessageOut("@SHOWSCORE-"+string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendConnDiscMessage(String message) throws IOException {
        sendMessageOut("@CONNDISC-"+message);
    }


    class HandleTurn extends Thread implements Serializable {
        GreenCarpet greenCarpet;
        Player player;
        int i;
        int time;

        private HandleTurn(GreenCarpet greenCarpet, Player player, int i, int time) {
            this.greenCarpet = greenCarpet;
            this.player = player;
            this.i = i;
            this.time=time;
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
        public void run(){
            boolean usedDice=false;
            Game game= new Game(0, null);
            int flagTool=0;
            boolean usedTool=false;
            Ruler ruler = new Ruler();
            String greencarpetjson = gson.toJson(greenCarpet);
            String playerjson = gson.toJson(player);

            try {
                sendMessageOut("@YOURTURN-true-"+time);
                sendMessageOut("@PRINTALL-"+greencarpetjson+"-"+playerjson);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true) {
                try {
                    sendMessageOut("@CHOOSEACTION");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (!(message.equals("@ACTIONCHOSE")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("@DEAD")) {
                    game=null;
                    return;
                }

                if (message.equals("@TIMEROUT")) {
                    message = "";
                    arrOfMsg[0] = "";
                    return;
                }

                if (arrOfMsg[1].equals("1")) {//pass
                    arrOfMsg[1]="";
                    message="";
                    game.setGreenCarpet(greenCarpet);
                    game.setPlayer(player, i);
                    try {
                        sendMessageOut("@YOURTURN-false");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else if (arrOfMsg[1].equals("2")) { //dice
                    if(ruler.checkAvailable(greenCarpet, player.getScheme())) {
                        if (!usedDice) {
                            try {
                                placedice(greenCarpet, player);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(!message.equals("@TIMEROUT")) {
                                usedDice = true;
                                if (usedTool) {
                                    game.setGreenCarpet(greenCarpet);
                                    game.setPlayer(player, i);
                                    try {
                                        sendMessageOut("@YOURTURN-false");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                            }
                        }else {
                            try {
                                sendMessageOut("@ERROR-Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        try {
                            sendMessageOut("@ERROR-Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!message.equals("@TIMEROUT"))
                        message="";
                } else if (arrOfMsg[1].equals("3")) {   //tool
                    if(!usedTool) {
                        try {
                            flagTool = placeTool(greenCarpet, player, usedDice);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //IF METHOD RETURN TRUE I USED A "PLACE DICE" TOOL AND I RETURN.
                        if(flagTool==0 && !message.equals("@TIMEROUT")) {
                            message = "";
                            arrOfMsg[0] = "";
                            return;
                        }
                        if (flagTool==1 || (flagTool==2 && usedDice)) {     //used a toolcard which include dice placement
                            game.setGreenCarpet(greenCarpet);
                            game.setPlayer(player, i);
                            arrOfMsg[1]="";
                            message="";
                            try {
                                sendMessageOut("@YOURTURN-false");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        if(flagTool==2 && !usedDice) {   //just used a toolcard
                            usedTool = true;
                            message="";
                        }
                        if(flagTool==3){
                            usedTool = false;
                            message="";
                        }
                    }else {
                        try {
                            sendMessageOut("@ERROR-Hai già utilizzato una carta tool in questo giro!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!message.equals("@TIMEROUT"))
                        message="";
                }
                if (message.equals("@TIMEROUT")) {
                    message = "";
                    return;
                }
                message="";
            }
        }


        private void placedice(GreenCarpet greenCarpet, Player player) throws IOException, InterruptedException {   //i is player's number for "getplayer"
            Boolean checkdice = false;
            Ruler ruler = new Ruler();
            while (!checkdice) {
                sendMessageOut("@PLACEDICE");
                while (!(message.equals("@DICEPLACED")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(300);}
                if(message.equals("@DEAD") || message.equals("@TIMEROUT")) {
                    return;
                }
                Dice dice = greenCarpet.checkDiceFromStock(stringToInt(arrOfMsg[1]));
                if(dice!=null) {
                    checkdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), dice, player.getScheme());
                    if (!checkdice)
                        sendMessageOut("@ERROR-Il dado non può essere inserito");
                }else
                    sendMessageOut("@ERROR-Hai scelto un dado non valido");

                message="";
            }
            player.getScheme().setBoxes(greenCarpet.getDiceFromStock(stringToInt(arrOfMsg[1])), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
            String greencarpetupd = gson.toJson(greenCarpet);
            String playerupd = gson.toJson(player);

            sendMessageOut("@AFTERTOOLUPDATE-"+greencarpetupd+"-"+playerupd);
        }

        private int placeTool(GreenCarpet greenCarpet, Player player, boolean useddice) throws IOException, InterruptedException {
            ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
            boolean toolok=false;
            boolean flag;
            int choice;
            boolean tooldice=false; //if the tool cards include dice placement
            do {
                choice = useToolMethod();
                if(choice!=0) {
                    flag = greenCarpet.toolIsIn(choice);
                    if (!flag) {
                        sendMessageOut("@ERROR-La carta utensile scelta non è presente sul tavolo da gioco.");
                    }
                }else
                    return 0;

            }while(!flag);
            if(choice>0 && choice<13) {
                //rembember to check if the tool chosen is inside the greencarpet.
                switch (choice) {
                    case 1:     //no placement
                        toolok = toolOne(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 2:
                        toolok = toolTwo(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 3:
                        toolok = toolThree(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 4:
                        toolok = toolFour(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 5:
                        toolok = toolFive(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 6:
                        boolean[] res = toolSix(player, greenCarpet, toolCardsExecutor, useddice);
                        toolok = res[0];
                        tooldice = res[1];
                        break;
                    case 7:
                        toolok = toolSeven(player, greenCarpet, toolCardsExecutor, useddice);
                        break;
                    case 8:
                        toolok = toolEight(player, greenCarpet, toolCardsExecutor, useddice);
                        break;
                    case 9:
                        tooldice = toolNine(player, greenCarpet, toolCardsExecutor, useddice);
                        break;
                    case 10:
                        toolok = toolTen(player, greenCarpet, toolCardsExecutor);
                        break;
                    case 11:
                        res = toolEleven(player, greenCarpet, toolCardsExecutor, useddice);
                        toolok = res[0];
                        tooldice = res[1];
                        break;
                    case 12:
                        toolok = toolTwelve(player, greenCarpet, toolCardsExecutor);
                        break;
                }
            }else{
                sendMessageOut("@ERROR-Hai inserito un valore sbagliato!");
                message="";
            }
            String greencarpetupd = gson.toJson(greenCarpet);
            String playerupd = gson.toJson(player);
            if(!tooldice || !(useddice && toolok))
                sendMessageOut("@AFTERTOOLUPDATE-"+greencarpetupd+"-"+playerupd);
            if(tooldice)      //return 1 if the tool card include dice placement
                return 1;
            else
            if(toolok)      //used a tool without placement
                return 2;
            else            //no tool used
                return 3;
        }

        //------------------------------------------TOOL METHODS------------------------------------------------------------
        private int useToolMethod() throws IOException, InterruptedException {
            sendMessageOut("@USETOOL");
            while (!(message.equals("@TOOLUSED")) && !message.equals("@DEAD")&& !message.equals("@TIMEROUT")) { sleep(300); }
            if(message.equals("@DEAD")||message.equals("@TIMEROUT")){
                return 0;
            }
            int choice = stringToInt(arrOfMsg[1]);

            message="";
            return choice;
        }

        private boolean toolOne(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-4");
            while (!(message.equals("@TOOLUSED4")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                return false;
            boolean toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, 1, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
            if(!toolok) {
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            }
            message="";
            return toolok;
        }

        private boolean toolTwo(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-1");
            while (!(message.equals("@TOOLUSED1"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                return false;
            int[] coord=new int[4];
            for (int k=1; k<5;k++)
                coord[k-1]=stringToInt(arrOfMsg[k]);
            boolean toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, 2, coord);
            if(!toolok)
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            message = "";
            return toolok;
        }

        private boolean toolThree(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-1");
            while (!(message.equals("@TOOLUSED1"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
            if(message.equals("@DEAD") ||message.equals("@TIMEROUT"))
                return false;
            //reset coord
            int[] coord=new int[4];
            for (int k=1; k<5;k++)
                coord[k-1]=stringToInt(arrOfMsg[k]);
            boolean toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, 3, coord);
            if(!toolok)
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            message = "";
            return toolok;
        }

        private boolean toolFour(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-2");
            while (!(message.equals("@TOOLUSED2"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                return false;
            int[] coord1dice= new int[4];
            int[] coord2dice= new int[4];
            for (int k=0;k<arrOfMsg.length;k++) {
                if (k<4)
                    coord1dice[k] = stringToInt(arrOfMsg[k+1]);
                else if (k<8)
                    coord2dice[k % 4] = stringToInt(arrOfMsg[k+1]);
            }
            boolean toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, 4, coord1dice, coord2dice);
            if(!toolok)
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            message = "";
            return toolok;
        }

        private boolean toolFive(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            if(greenCarpet.checkEmptyRoundpath()) {
                sendMessageOut("@TOOL-5");
                while (!(message.equals("@TOOLUSED5")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {
                    sleep(200);
                }
                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                    return false;
                int[] dicePos = new int[2];
                dicePos[0] = stringToInt(arrOfMsg[2]);
                dicePos[1] = stringToInt(arrOfMsg[3]);
                boolean toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, 5, stringToInt(arrOfMsg[1]), dicePos);
                if (!toolok)
                    sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
                message = "";
                return toolok;
            }else{
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool. Il tracciato dei round è vuoto."); //TODO aggiungere ad RMI!!!!
                return false;
            }
        }

        private boolean[] toolSix(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, Boolean useddice) throws IOException, InterruptedException {
            Ruler ruler = new Ruler();
            boolean checkcorrdice=false;
            Dice dice=null;
            boolean[] ret = new boolean[2];
            ret[0]=false;       //used the tool
            ret[1]=false;       //placed dice
            if(!useddice) {
                sendMessageOut("@TOOL-6");
                while (!(message.equals("@TOOLUSED6"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                    return ret;
                dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]),6,0);
                int dicepos = stringToInt(arrOfMsg[1]);
                if(dice!=null) {
                    if (ruler.checkAvailableDice(dice, player.getScheme())) {
                        while (!checkcorrdice) {
                            String dicejson = gson.toJson(dice);
                            sendMessageOut("@TOOL-61-" + dicejson);
                            while (!(message.equals("@TOOLUSED61") && !message.equals("@TIMEROUT")) && !message.equals("@DEAD")) {
                                sleep(200);
                            }
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return ret;
                            checkcorrdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), dice, player.getScheme());
                            message = "";
                        }
                        greenCarpet.getDiceFromStock(dicepos);
                        player.getScheme().setBoxes(dice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
                        ret[0]=true;
                        ret[1]=true;
                    }
                    else
                        ret[0]=true;
                }else{
                    sendMessageOut("@ERROR-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                }
                message="";
            }else
                sendMessageOut("@ERROR-Non puoi utilizzare questa carta tool. Hai già piazzato un dad in questo turno.");
            message="";
            return ret;
        }

        private boolean toolSeven(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException {
            boolean toolok = false;
            if(greenCarpet.getTurn()==2 && !useddice) {
                sendMessageOut("@TOOL-7");
                while (!(message.equals("@TOOLUSED7"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")) { sleep(200); }
                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                    return false;
                toolok=toolCardsExecutor.changeDiceCard(player, greenCarpet, 7);
                if(!toolok)
                    sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            }else
                sendMessageOut("@ERROR-Questa toolcard non è attualmente utilizzabile.");
            message="";
            return toolok;
        }

        private boolean toolEight(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException {
            boolean toolok = false;
            if(greenCarpet.getTurn()==1 && useddice) {
                sendMessageOut("@TOOL-8");
                while (!(message.equals("@TOOLUSED8"))&& !message.equals("@DEAD")&& !message.equals("@TIMEROUT")){sleep(200) ;}
                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                    return false;
                toolok=toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), 8, stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
                if(!toolok)
                    sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            }else
                sendMessageOut("@ERROR-Questa toolcard non è attualmente utilizzabile.  Ricordati di piazzare un dado prima di utilizzarla!");
            message="";
            return toolok;
        }

        private boolean toolNine(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException {
            boolean toolok=false;
            if (!useddice) {
                sendMessageOut("@TOOL-8");
                while (!(message.equals("@TOOLUSED8"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")){ sleep(200);}
                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                    return false;
                toolok=toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), 9, stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
                if(!toolok)
                    sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
                message="";
            } else
                sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
            message="";
            if(toolok)
                return true;
            else
                return false;
        }

        private boolean toolTen(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-6");
            while (!(message.equals("@TOOLUSED6"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
            if(message.equals("@DEAD") || arrOfMsg[1].equals("@TIMEROUT"))
                return false;
            boolean toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, 10, stringToInt(arrOfMsg[1]), 0);
            if(!toolok)
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            message="";
            return toolok;
        }

        private boolean[] toolEleven(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor, boolean useddice) throws IOException, InterruptedException {
            Ruler ruler = new Ruler();
            boolean checkcorrdice=false;
            boolean tooldice = false;
            Dice dice=null;
            boolean[] ret = new boolean[2];
            ret[0]=false;       //used the tool
            ret[1]=false;       //placed dice
            if(!useddice) {
                sendMessageOut("@TOOL-6");
                while (!(message.equals("@TOOLUSED6"))&& !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                if(message.equals("@DEAD") || message.equals("@TIMEROUT")) {
                    return ret;
                }
                dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), 11, 0);
                int dicepos = stringToInt(arrOfMsg[1]);
                if(dice!=null) {
                    dice.setFace("");
                    String dicejson = gson.toJson(dice);
                    sendMessageOut("@TOOL-91-" + dicejson);
                    while (!(message.equals("@TOOLUSED91")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {
                        sleep(200);
                    }
                    if(message.equals("@DEAD") || message.equals("@TIMEROUT")) {
                        Random rnd = new Random();
                        int val = rnd.nextInt(6) + 1;
                        dice.setFace(ruler.intToString(val));
                        greenCarpet.getDiceFromStock(dicepos);
                        greenCarpet.setDiceInStock(dice);
                        ret[0]=true;
                        return ret;
                    }
                    dice.setFace(ruler.intToString(stringToInt(arrOfMsg[1])));
                    while (!checkcorrdice) {
                        sendMessageOut("@TOOL-92");
                        while (!(message.equals("@TOOLUSED92")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                        if(message.equals("@DEAD") || message.equals("@TIMEROUT")) {
                            ret[0]=true;
                            return ret;
                        }
                        message="";
                        if(ruler.checkAvailableDice(dice, player.getScheme())) {
                            checkcorrdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), dice, player.getScheme());
                            if(checkcorrdice) {
                                greenCarpet.getDiceFromStock(dicepos);
                                player.getScheme().setBoxes(dice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
                                ret[0]=true;
                                ret[1]=true;
                            }
                        }else {
                            checkcorrdice = true;
                            ret[0]=true;
                        }
                    }
                }else{
                    sendMessageOut("@ERROR-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                }
            }else
                sendMessageOut("@ERROR-Non puoi utilizzare questa carta tool. Hai già piazzato un dado in questo turno.");
            message="";
            return ret;
        }

        private boolean toolTwelve(Player player, GreenCarpet greenCarpet, ToolCardsExecutor toolCardsExecutor) throws IOException, InterruptedException {
            sendMessageOut("@TOOL-3");
            while (!(message.equals("@TOOLUSED3"))&& !message.equals("@DEAD")&& !message.equals("@TIMEROUT")) {sleep(300);}
            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                return false;
            int numOfDices = stringToInt(arrOfMsg[1]);
            int[] allcoordinates = new int[11];
            for (int k = 0; k < 11; k++) {
                if (k < 8)
                    allcoordinates[k] = stringToInt(arrOfMsg[k + 2]);
                else if (k == 8)
                    allcoordinates[k] = 0;
                else
                    allcoordinates[k] = stringToInt(arrOfMsg[k + 1]);
            }
            boolean toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, 12, numOfDices, allcoordinates);
            if(!toolok)
                sendMessageOut("@ERROR-Non è stato possibile utilizzare la tool.");
            message = "";
            return toolok;
        }
        //********************************************TOOL METHODS**********************************************************


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

}
