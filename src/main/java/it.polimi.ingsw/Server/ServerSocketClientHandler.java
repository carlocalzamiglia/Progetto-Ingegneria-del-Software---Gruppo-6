package it.polimi.ingsw.Server;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import it.polimi.ingsw.Client.ClientRmi;
import it.polimi.ingsw.Game.Game;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Matches;
import it.polimi.ingsw.Game.Ruler;
import it.polimi.ingsw.Game.Scheme;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.Colour;
import it.polimi.ingsw.Game.ToolCardsExecutor;
import it.polimi.ingsw.ServertoClientHandler.ServertoClient;

import java.io.Serializable;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static jdk.nashorn.internal.objects.NativeMath.random;

public class ServerSocketClientHandler implements Runnable,ServertoClient, Serializable {
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
                    matches.getPlayer(user).setOnline(true);
                    matches.getUser(user).setClientHandler(this);
                    matches.getUser(user).setOnline(true);
                }else {
                    matches.addUser(DB.getUser(user));
                }
            }
            if(check==0){
                matches.addUser(DB.getUser(user));
            }

            new ListenFromClient(user).start();
            System.out.println(nickname + " loggato con connessione socket");
            newUserMessage(nickname);
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
                    clientAlive(this.nickname);
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //-----------------------------------------check if client is online yet--------------------------------------------
    public void clientAlive(String nickname) {
        //if (DB.getUser(nickname).isOnline()) {
         //       try {
         //           sendMessageOut("@ALIVE");
         //           return true;
         //       }catch(IOException e){
        DB.getUser(nickname).setOnline(false);
        DB.getUser(nickname).setClientHandler(null);
        if(matches.getGame(nickname).getPlaying()) {
            matches.getUser(nickname).setOnline(false);
            matches.getGame(nickname).playerDisconnect();
            if(matches.getPlayer(nickname)!=null)
                matches.getPlayer(nickname).setOnline(false);
        }
        System.out.println(nickname + " ha probabilmente perso la connessione.");
        message="@DEAD";
        return;
          //      }
        //} else {
        //    return false;
       // }

    }

    //-----------------------------------------new client connected message---------------------------------------------
    public void newUserMessage(String nickname) throws IOException, InterruptedException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getConnectionType() != null) {
                    sleep(1000);
                    DB.getUser(i).getConnectionType().sendMessageOut(nickname + " ha appena effettuato il login ed è pronto a giocare.");
                }
            }
        }
    }

    //--------------------------------------------send message to the user----------------------------------------------
    public void sendMessageOut(String message) throws IOException {
        ObjectOutputStream outs;
        outs = new ObjectOutputStream(socket.getOutputStream());
        outs.writeObject(message);
    }


    //just for rmi, we'll see.
    @Override
    public boolean aliveMessage() {
        return false;
    }


    //********************************************* all game methods ***************************************+

    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, int time) throws IOException, InterruptedException {
        message="";
        sendMessageOut("@SCHEME-"+scheme1+"-"+scheme2+"-"+scheme3+"-"+scheme4+"-"+time);
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

    class HandleTurn extends Thread implements Serializable {
        GreenCarpet greenCarpet;
        Player player;
        int i;
        int time;

        public HandleTurn(GreenCarpet greenCarpet, Player player, int i, int time) {
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
            Game game= new Game(0);
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
                                placedice(greenCarpet, player, i);
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
                            flagTool = placeTool(greenCarpet, player, i, usedDice);
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
                        if (flagTool==1) {     //used a toolcard which include dice placement
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
                        if(flagTool==2) {
                            if(!usedDice) {
                                usedTool = true;
                                arrOfMsg[1] = "";
                            }else{
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
    }



    private void placedice(GreenCarpet greenCarpet, Player player, int i) throws IOException, InterruptedException {   //i is player's number for "getplayer"
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

    public Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException {
        Game game =new Game(time);
        HandleTurn handleTurn=new HandleTurn( greenCarpet,  player,  i, time);
        handleTurn.run();
        game.setGreenCarpet(handleTurn.getGreenCarpet());
        game.setPlayer(handleTurn.getPlayer(), handleTurn.getI());
        if(message.equals("@DEAD"))
            return null;
        return game;
    }


    private int placeTool(GreenCarpet greenCarpet, Player player, int i, boolean useddice) throws IOException, InterruptedException {
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        boolean toolok=false;
        boolean flag;
        int choice;
        boolean exit=false;     //catch the exit message
        boolean tooldice=false; //if the tool cards include dice placement
        while(!toolok) {                //run 'till the card is correct and used
            do {
                sendMessageOut("@USETOOL");
                while (!(message.equals("@TOOLUSED")) && !message.equals("@DEAD")&& !message.equals("@TIMEROUT")) { sleep(300); }
                if(message.equals("@DEAD")||message.equals("@TIMEROUT"))
                    return 0;
                choice = stringToInt(arrOfMsg[1]);
                flag=greenCarpet.toolIsIn(choice);
                if (!flag)
                    sendMessageOut("@ERROR-La carta utensile scelta non è presente sul tavolo da gioco.");
                message="";
            }while(!flag);
            if(choice>0 && choice<13) {
                //rembember to check if the tool chosen is inside the greencarpet.
                switch (choice) {
                    case 1:     //no placement
                        while(!toolok) {
                                sendMessageOut("@TOOL-4");
                                while (!(message.equals("@TOOLUSED4")) && !(message.equals("@TOOLEXIT"))  && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                                if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                    return 0;
                                if(!(message.equals("@TOOLEXIT"))) {
                                    toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
                                }else {
                                    exit = true;
                                    toolok = true;
                                }
                                message="";
                        }
                        break;
                    case 2:
                        while (!toolok) {        //used to have a correct use of the tool
                            sendMessageOut("@TOOL-1");
                            while (!(message.equals("@TOOLUSED1")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!(message.equals("@TOOLEXIT"))) {
                                int[] coord=new int[4];
                                for (int k=1; k<5;k++)
                                    coord[k-1]=stringToInt(arrOfMsg[k]);
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coord);
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message = "";
                        }
                        break;
                    case 3:
                        while (!toolok) {
                            sendMessageOut("@TOOL-1");
                            while (!(message.equals("@TOOLUSED1")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
                            if(message.equals("@DEAD") ||message.equals("@TIMEROUT"))
                                return 0;
                            if(!(message.equals("@TOOLEXIT"))) {
                                int[] coord=new int[4];
                                for (int k=1; k<5;k++)
                                    coord[k-1]=stringToInt(arrOfMsg[k]);
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coord);
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message = "";
                        }
                        break;
                    case 4:
                        while (!toolok) {
                            sendMessageOut("@TOOL-2");
                            while (!(message.equals("@TOOLUSED2")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {sleep(300);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!(message.equals("@TOOLEXIT"))) {
                                int[] coord1dice= new int[4];
                                int[] coord2dice= new int[4];
                                for (int k=0;k<arrOfMsg.length;k++) {
                                    if (k<4)
                                        coord1dice[k] = stringToInt(arrOfMsg[k+1]);
                                    else if (k<8)
                                        coord2dice[k % 4] = stringToInt(arrOfMsg[k+1]);
                                }
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, coord1dice, coord2dice);
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message = "";
                        }
                        break;
                    case 5:
                        while(!toolok) {
                            sendMessageOut("@TOOL-5");
                            while (!(message.equals("@TOOLUSED5")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!message.equals("@TOOLEXIT")) {
                                int[] dicePos= new int[2];
                                dicePos[0]=stringToInt(arrOfMsg[2]);
                                dicePos[1]=stringToInt(arrOfMsg[3]);
                                toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]),dicePos);
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message="";
                        }
                        break;
                    case 6:
                        Ruler ruler = new Ruler();
                        boolean checkplacement;
                        boolean checkcorrdice=false;
                        Dice dice=null;
                        while(!toolok && !useddice) {
                            sendMessageOut("@TOOL-6");
                            while (!(message.equals("@TOOLUSED6")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!message.equals("@TOOLEXIT")) {
                                dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]),choice,0);
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
                                                return 0;
                                            checkcorrdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), dice, player.getScheme());
                                            message = "";
                                        }
                                        greenCarpet.getDiceFromStock(dicepos);
                                        player.getScheme().setBoxes(dice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
                                        tooldice=true;
                                    }
                                    else
                                        greenCarpet.setDiceInStock(dice);
                                    toolok = true;
                                }else{
                                    sendMessageOut("@ERROR-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                                    toolok=true;
                                    exit=true;
                                }
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message="";
                        }
                        if(useddice){
                            sendMessageOut("@ERROR-Non puoi utilizzare questa carta tool. Hai già piazzato un dado!");
                            exit=true;
                            toolok=true;
                        }
                        message="";
                        break;
                    case 7:
                        while(!toolok) {
                            if(greenCarpet.getTurn()==2 && !useddice) {
                                sendMessageOut("@TOOL-7");
                                while (!(message.equals("@TOOLUSED7")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {
                                    sleep(200);
                                }
                                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                    return 0;
                                if (!message.equals("@TOOLEXIT")) {
                                    toolok=toolCardsExecutor.changeDiceCard(player, greenCarpet, choice);
                                } else {
                                    exit = true;
                                    toolok = true;
                                }
                                message = "";
                            }else{
                                sendMessageOut("@ERROR-Questa toolcard non è attualmente utilizzabile.");
                                toolok=true;
                                exit=true;
                            }
                            message="";
                        }
                        message="";
                        break;
                    case 8:
                        while(!toolok) {
                            if(greenCarpet.getTurn()==1 && useddice) {
                                sendMessageOut("@TOOL-8");
                                while (!(message.equals("@TOOLUSED8")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD")&& !message.equals("@TIMEROUT")){sleep(200) ;}
                                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                    return 0;
                                if(!message.equals("@TOOLEXIT")){
                                    toolok=toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), choice, stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
                                }else{
                                    toolok=true;
                                    exit=true;
                                }

                            }else {
                                sendMessageOut("@ERROR-Questa toolcard non è attualmente utilizzabile.  Ricordati di piazzare un dado prima di utilizzarla!");
                                toolok=true;
                                exit=true;
                            }
                            message="";
                        }
                        message="";
                        break;
                    case 9:
                        if (!useddice) {
                            while(!toolok) {
                                sendMessageOut("@TOOL-8");
                                while (!(message.equals("@TOOLUSED8")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){ sleep(200);}
                                System.out.println(message);
                                if (message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                    return 0;
                                if(!message.equals("@TOOLEXIT")){
                                    toolok=toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), choice, stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
                                }else{
                                    toolok=true;
                                    exit=true;
                                }
                                message="";
                            }

                        } else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        message="";
                        tooldice=true;
                        break;
                    case 10:
                        while(!toolok) {
                            sendMessageOut("@TOOL-6");
                            while (!(message.equals("@TOOLUSED6")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                            if(message.equals("@DEAD") || arrOfMsg[1].equals("@TIMEROUT"))
                                return 0;
                            if(!(message.equals("@TOOLEXIT")))
                                toolok = toolCardsExecutor.changeDiceCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), 0);
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message="";
                        }
                        message="";
                        break;
                    case 11:
                        ruler = new Ruler();
                        checkcorrdice=false;
                        dice=null;
                        while(!toolok && !useddice) {
                            sendMessageOut("@TOOL-6");
                            while (!(message.equals("@TOOLUSED6")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")){sleep(200);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!message.equals("@TOOLEXIT")) {
                                dice = toolCardsExecutor.usePlacementCard(player, greenCarpet, stringToInt(arrOfMsg[1]), choice, 0);
                                int dicepos = stringToInt(arrOfMsg[1]);
                                if(dice!=null) {
                                    while (!checkcorrdice) {
                                        dice.setFace("");
                                        String dicejson = gson.toJson(dice);
                                        sendMessageOut("@TOOL-91-" + dicejson);
                                        while (!(message.equals("@TOOLUSED91")) && !message.equals("@DEAD") && !message.equals("@TIMEROUT")) {
                                            sleep(200);
                                        }
                                        if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                            return 0;
                                        dice.setFace(ruler.intToString(stringToInt(arrOfMsg[3])));
                                        if(ruler.checkAvailableDice(dice, player.getScheme())) {
                                            checkcorrdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), dice, player.getScheme());
                                            if(checkcorrdice) {
                                                greenCarpet.getDiceFromStock(dicepos);
                                                player.getScheme().setBoxes(dice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]));
                                                tooldice = true;
                                            }
                                        }
                                        else {
                                            checkcorrdice = true;
                                            greenCarpet.setDiceInStock(dice);
                                        }
                                        message = "";
                                    }


                                    toolok = true;
                                }else{
                                    sendMessageOut("@ERROR-C'è stato un errore. Non è possibile utilizzare la carta selezionata. Potresti non avere più markers disponibili, o aver inserito un valore del dado errato.");
                                    toolok=true;
                                    exit=true;
                                }
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message="";
                        }
                        if(useddice){
                            sendMessageOut("@ERROR-Non puoi utilizzare questa carta tool. Hai già piazzato un dado!");
                            exit=true;
                            toolok=true;
                        }
                        message="";
                        break;
                    case 12:
                        while (!toolok) {
                            sendMessageOut("@TOOL-3");
                            while (!(message.equals("@TOOLUSED3")) && !(message.equals("@TOOLEXIT")) && !message.equals("@DEAD")&& !message.equals("@TIMEROUT")) {sleep(300);}
                            if(message.equals("@DEAD") || message.equals("@TIMEROUT"))
                                return 0;
                            if(!(message.equals("@TOOLEXIT"))) {
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
                                toolok = toolCardsExecutor.useMovementCard(player, greenCarpet, choice, numOfDices, allcoordinates);
                            }
                            else {
                                exit = true;
                                toolok = true;
                            }
                            message = "";
                        }
                        break;
                }
            }else{
                sendMessageOut("@ERROR-Hai inserito un valore sbagliato!");
                message="";
            }
        }
        String greencarpetupd = gson.toJson(greenCarpet);
        String playerupd = gson.toJson(player);
        sendMessageOut("@AFTERTOOLUPDATE-"+greencarpetupd+"-"+playerupd);
        if(exit)
            return 3;
        else
            if(tooldice)      //return 1 if the tool card include dice placement
                return 1;
            else
                return 2;
    }







    //----------------------------------------------------support methods-----------------------------------------------

    private boolean okTool(int i){
        if(i==1 || i==5 || i==6 || i==9 || i==10 || i==11)
            return true;
        else
            return false;
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
