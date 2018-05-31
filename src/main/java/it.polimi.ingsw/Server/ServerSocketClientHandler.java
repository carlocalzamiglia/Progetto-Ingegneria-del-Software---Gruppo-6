package it.polimi.ingsw.Server;



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
import java.util.Random;
import java.util.Scanner;

import static java.lang.Thread.sleep;
import static jdk.nashorn.internal.objects.NativeMath.random;

public class ServerSocketClientHandler implements Runnable,ServertoClient, Serializable {
    private Socket socket;
    private DBUsers DB;
    private Matches matches;
    //private ObjectOutputStream outs;
    //private ObjectInputStream ins;
    String message = "";
    String [] arrOfMsg;



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
            System.out.println(nickname + " loggato con connessione socket");
            new HandleDisconnection(nickname, this).start();
            new ListenFromClient(nickname).start();

            matches.addUser(DB.getUser(user));

            newUserMessage(nickname);
            sendMessageOut("Benvenuto, "+nickname+". Ora puoi giocare!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
                    ins = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    String msg = (String) ins.readObject();
                    System.out.println(msg);
                    arrOfMsg = msg.split("-");
                    message=arrOfMsg[0];
                    System.out.println("il protocollo è: "+message);
                    if (message.equals("@LOGOUT")) {
                        DB.getUser(nickname).setOnline(false);
                        DB.getUser(nickname).setClientHandler(null);
                        System.out.println(nickname+ " si è appena disconnesso.");
                    }
                } catch (IOException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //-----------------------------------------check if client is online yet--------------------------------------------
    public boolean clientAlive(String nickname) throws ClassNotFoundException, InterruptedException {
        if (DB.getUser(nickname).isOnline()) {
                try {
                    sendMessageOut("@ALIVE");
                    return true;
                }catch(IOException e){
                    DB.getUser(nickname).setOnline(false);
                    DB.getUser(nickname).setClientHandler(null);
                    System.out.println(nickname + " ha probabilmente perso la connessione.");
                    return false;
                }
        } else {
            return false;
        }

    }

    //-----------------------------------------new client connected message---------------------------------------------
    private synchronized void newUserMessage(String nickname) throws IOException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getConnectionType() != null)
                    DB.getUser(i).getConnectionType().sendMessageOut(nickname+" ha appena effettuato il login ed è pronto a giocare.");
                else if (DB.getUser(i).getConnectionType() != null)
                    DB.getUser(i).getConnectionType().sendMessageOut(nickname+" ha appena effettuato il login ed è pronto a giocare.");
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
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4) throws IOException, InterruptedException {
        sendMessageOut("@SCHEME-Scegli uno schema:\n"+scheme1+"\n"+scheme2+"\n"+scheme3+"\n"+scheme4);
        while(!(message.equals("@SCHEME"))){sleep(300);}
        System.out.println("schema scelto: "+arrOfMsg[1]);
        return stringToInt(arrOfMsg[1]);
    }

    public void test() throws IOException, InterruptedException {
        Boolean checkdice = false;
        Random random = new Random();
        Ruler ruler = new Ruler();
        Scheme scheme = new Scheme(9);
        sendMessageOut("@SCHEME-"+scheme.toString());
        while(!(message.equals("@SCHEME"))){}
        Dice dice = new Dice(Colour.ANSI_RED);
        while (checkdice==false) {
            dice.roll();
            checkdice = ruler.checkCorrectPlacement(0, 1, dice, scheme);
            if (checkdice == true)
                scheme.setBoxes(dice, 0, 1);
            else
                sendMessageOut("NO");
        }
        sendMessageOut("@SCHEME-"+scheme.toString());
        while(!(message.equals("@SCHEME"))){}
        System.out.println("SCHEMA RICEVUTO");
    }

    public Game handleturn(GreenCarpet greenCarpet, Player player, int i, String playersscheme) throws IOException, InterruptedException {
        boolean usedDice=false;
        Game game= new Game(0);
        boolean flagTool=false;
        boolean usedTool=false;
        Ruler ruler = new Ruler();
        while(true) {
            sendMessageOut("@ERROR-Ecco lo schema degli altri giocatori, nell'ordine: "+ playersscheme);
            sendMessageOut("@ERROR-Ecco qui il tavolo e il tuo schema:\n");
            sendMessageOut("@GC-"+greenCarpet.toString()+"\n");
            sendMessageOut("@PLAYER-"+player.toString()+"\n");
            sendMessageOut("@ERROR-1)passa il turno\n2)inserisci dado\n3)usa carta utensile\n");
            sendMessageOut("@CHOOSEACTION");
            while (!(message.equals("@ACTIONCHOSE"))){sleep(300);}
            if (arrOfMsg[1].equals("1")) {//pass
                System.out.println("E' stato scelto il passo");
                arrOfMsg[1]="";
                message="";
                game.setGreenCarpet(greenCarpet);
                game.setPlayer(player, i);
                return game;
            } else if (arrOfMsg[1].equals("2")) { //dice
                if(ruler.checkAvailable(greenCarpet, player.getScheme())) {
                    System.out.println("E' stato scelto il dado");
                    if (!usedDice) {
                        placedice(greenCarpet, player, i);
                        usedDice = true;
                        if (usedTool) {
                            sendMessageOut("@YOURTURN-false");
                            game.setGreenCarpet(greenCarpet);
                            game.setPlayer(player, i);
                            return game;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado per questo turno. Puoi passare o utilizzare una carta tool (che non preveda il piazzamento di un dado).");
                    }
                }else
                    sendMessageOut("@ERROR-Non è possibile inserire alcun dado. Passa il turno o utilizza una carta tool.");
                arrOfMsg[1]="";
                message="";
            } else if (arrOfMsg[1].equals("3")) {   //tool
                if(!usedTool)
                System.out.println("E' stata scelta la tool");
                flagTool = placeTool(greenCarpet, player, i, usedDice);
                //IF METHOD RETURN TRUE I USED A "PLACE DICE" TOOL AND I RETURN.
                if (flagTool) {
                    sendMessageOut("@YOURTURN-false");
                    game.setGreenCarpet(greenCarpet);
                    game.setPlayer(player, i);
                    return game;
                }else
                    sendMessageOut("@ERROR-Hai già utilizzato una carta tool in questo giro!");
                usedTool = true;
                arrOfMsg[1]="";
                message="";
            }
        }
    }

    private void placedice( GreenCarpet greenCarpet, Player player, int i) throws IOException, InterruptedException {   //i is player's number for "getplayer"
        Boolean checkdice = false;
        Random random = new Random();
        Ruler ruler = new Ruler();
        while (!checkdice) {
            sendMessageOut("@YOURTURN-true");
            sendMessageOut("@PLACEDICE");
            while (!(message.equals("@DICEPLACED"))){sleep(300);}
            Dice dice = greenCarpet.checkDiceFromStock(stringToInt(arrOfMsg[1]));
            if(dice!=null) {
                checkdice = ruler.checkCorrectPlacement(stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), dice, player.getScheme());
                System.out.println("checkdice ha detto: " + checkdice);
                if (!checkdice)
                    sendMessageOut("@ERROR-Il dado non può essere inserito");
            }else
                sendMessageOut("@ERROR-Hai scelto un dado non valido");

            message="";
        }
        player.getScheme().setBoxes(greenCarpet.getDiceFromStock(stringToInt(arrOfMsg[1])), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]));
        sendMessageOut("Ecco lo schema aggiornato:\n"+player.getScheme().toString());
    }


    private boolean placeTool(GreenCarpet greenCarpet, Player player, int i, boolean useddice) throws IOException, InterruptedException {
        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        boolean toolok=false;
            sendMessageOut("@YOURTURN-true");
            while(!toolok) {                //run 'till the card is correct and used
                sendMessageOut("@USETOOL");
                while (!(message.equals("@TOOLUSED"))){sleep(300);}
                int choice = stringToInt(arrOfMsg[1]);

                //rembember to check if the tool chosen is inside the greencarpet.


                switch (choice) {
                    case 1:
                        if (!useddice) {
                            sendMessageOut("@TOOL-4");
                            while (!(message.equals("@TOOLUSED4"))) ;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        break;
                    case 2:
                        while(!toolok) {        //used to have a correct use of the tool
                            sendMessageOut("@TOOL-1");
                            while (!(message.equals("@TOOLUSED1"))){sleep(300);}
                            toolok=toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), stringToInt(arrOfMsg[4]));
                        }
                        break;
                    case 3:
                        while(!toolok) {
                            sendMessageOut("@TOOL-1");
                            while (!(message.equals("@TOOLUSED1"))){sleep(300);}
                            toolok=toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), stringToInt(arrOfMsg[4]));
                        }
                        break;
                    case 4:
                        while(!toolok) {
                            sendMessageOut("@TOOL-2");
                            while (!(message.equals("@TOOLUSED2"))) {sleep(300);}
                            toolok=toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), stringToInt(arrOfMsg[4]), stringToInt(arrOfMsg[5]), stringToInt(arrOfMsg[6]), stringToInt(arrOfMsg[7]), stringToInt(arrOfMsg[8]));
                        }
                        break;
                    case 5:
                        if (!useddice) {
                            sendMessageOut("@TOOL-5");
                            while (!(message.equals("@TOOLUSED-5"))) ;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        break;
                    case 6:
                        if (!useddice) {
                            sendMessageOut("@TOOL-6");
                            while (!(message.equals("@TOOLUSED-6"))) ;
                        }
                        break;
                    case 7:
                        sendMessageOut("@TOOL-7");
                        while (!(message.equals("@TOOLUSED-7"))) ;
                        break;
                    case 8:
                        sendMessageOut("@TOOL-manca");
                        while (!(message.equals("@TOOLUSED-9"))) ;
                        break;
                    case 9:
                        if (!useddice) {
                            sendMessageOut("@TOOL-1");
                            while (!(message.equals("@TOOLUSED-1"))) ;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        break;
                    case 10:
                        if (!useddice) {
                            sendMessageOut("@TOOL-6");
                            while (!(message.equals("@TOOLUSED-6"))) ;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        break;
                    case 11:
                        if (!useddice) {
                            sendMessageOut("@TOOL-8");
                            while (!(message.equals("@TOOLUSED-8"))) ;
                        }else
                            sendMessageOut("@ERROR-Hai già piazzato un dado in questo turno, non puoi usare una tool card che preveda di piazzarne uno nuovo.");
                        break;
                    case 12:
                        while(!toolok) {
                            sendMessageOut("@TOOL-3");
                            while (!(message.equals("@TOOLUSED3"))) {sleep(300);}
                            if(arrOfMsg[1].equals("2"))
                                toolok=toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), stringToInt(arrOfMsg[4]), stringToInt(arrOfMsg[5]), stringToInt(arrOfMsg[6]), stringToInt(arrOfMsg[7]), stringToInt(arrOfMsg[8]), stringToInt(arrOfMsg[9]), stringToInt(arrOfMsg[10]), stringToInt(arrOfMsg[11]));
                            else
                                toolok=toolCardsExecutor.useMovementCard(player, greenCarpet, choice, stringToInt(arrOfMsg[1]), stringToInt(arrOfMsg[2]), stringToInt(arrOfMsg[3]), stringToInt(arrOfMsg[4]), stringToInt(arrOfMsg[5]), stringToInt(arrOfMsg[6]), stringToInt(arrOfMsg[7]));
                        }
                        break;
                }
            }
        return false;           //REMEMBER TO CHANGE THIS (USE THE TYPE-TOOL METHOD)
    }







    //----------------------------------------------------support methods-----------------------------------------------

    private boolean okTool(int i){
        if(i==1 || i==5 || i==6 || i==9 || i==10 || i==11)
            return true;
        else
            return false;
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
