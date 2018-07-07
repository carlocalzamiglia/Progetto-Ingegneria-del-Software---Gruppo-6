package itpolimiingsw.Client;

import itpolimiingsw.Game.ToolCards;
import itpolimiingsw.ServertoClientHandler.ClientInterface;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

import java.net.SocketException;

import static java.lang.Thread.sleep;

public class ClientSocket {
    private int PORT;
    private String root;
    private String name;
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    ClientInterface clientInt;




    //GAME VARIABLES
    private boolean yourturn;

    //---------------------------------------------------launch execute-------------------------------------------------
    public ClientSocket(ClientInterface clientInt){
        this.clientInt=clientInt;

        try
        {
            execute("null", "null");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    //--------------------------------------launch connect, then login and then play------------------------------------
    private void execute(String nick, String pass)
    {
        String[] logindata;
        try
        {
            connect();
            logindata = login(nick, pass);
            play(logindata);
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
        BufferedReader inKeyboard;
        PrintWriter outVideo;
        try
        {
            root=leggiDaFile();
            String[] parts=root.split(":");
            try {
                PORT = Integer.parseInt(parts[1]);
            }catch (NumberFormatException e){
                clientInt.showError("Errore inserimento-Hai inserito un valore non numerico per la porta. Eseguo la disconessione.");
                System.exit(0);
            }
            root=parts[0];
            try {
                socket = new Socket(root, PORT);
            }catch(ConnectException e2){
                clientInt.showError("Server offline-Il server sembra essere offline. Chiudo il programma.");
                System.exit(0);
            }
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        }
        catch(Exception e)
        {
            clientInt.showError("Eccezione-Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                clientInt.showError("Eccezione-Socket not closed");
            }
        }
    }

    //---------------------------------------------------login part-----------------------------------------------------
    private String[] login(String nick, String pass)
    {
        String[] login = new String [2];
        String nickname = new String();
        String password = new String();
        try
        {
            String logged ="2";

            while(logged.equals("2") || logged.equals("3"))
            {
                //legge nickname dal client

                if(nick.equals("null")) {
                    login = clientInt.loginMessages();
                    nickname = login[0];
                    password = login[1];
                }else {
                    nickname = nick;
                    password = pass;
                }

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
                    clientInt.showError("Errore inserimento-Password di " + nickname + " errata");
                else if(logged.equals("3"))
                    clientInt.showError("Errore login-L'utente selezionato è già connesso. Deve esserci un errore!");
            }
            clientInt.loginOkMessage();
        }
        catch(Exception e)
        {
            clientInt.showError("Eccezione-Exception: "+e);
            try {
                socket.close();
            }
            catch(IOException ex) {
                clientInt.showError("Eccezione-Socket not closed");
            }
        }
        login[0]=nickname;
        login[1]=password;
        return login;
    }

    //----------------------------------------------------game part-----------------------------------------------------
    private void play(String[] logindata) throws IOException, InterruptedException {
        //for now we did't implement the complete protocol for the socket comunication but it will be implement in this while loop
        //this is for the client part
        new ListenFromServer(this, logindata).start();
        while(10>1){
           try {
               sleep(3000);
           }catch (InterruptedException e ){ }
        }
    }

    //---------------------------------------------class for server messages--------------------------------------------
    class TimerThreadSocket extends Thread  {
        int time;
        ListenFromServer listenFromServer;
        String[] logindata;
        ClientSocket clientSocket;
        int i;
        boolean flag; //gui scheme
        //constructor

        public TimerThreadSocket(int time,ListenFromServer listenFromServer, boolean flag) {
            this.time = time;
            this.listenFromServer=listenFromServer;
            this.logindata=listenFromServer.getLogindata();
            this.clientSocket=listenFromServer.getClientSocket();
            this.flag=flag;
        }
        @Override
        public void run() {
            try {
                i=0;
                while (i < time) {
                    sleep(1000);
                    i++;
                    clientInt.sendTimer(time-i);
                }
                try {
                    clientInt.timerOut(true);
                    sleep(300);
                    sendMessage("@TIMEROUT");
                } catch (IOException e) {
                    System.out.println("catchato eccezione 1");
                }
                if(flag) {
                    flag=false;
                    clientInt.endTurnMessage();
                }
                else
                    clientInt.endTurn();
                //listenFromServer.interrupt();
                //ListenFromServer newListen = new ListenFromServer(clientSocket, logindata);
                //newListen.start();
                return;
            }catch (InterruptedException e){
                System.out.println("timer terminato");
            }

        }
        public int getTime() {
            return i;
        }
        public void setTime(){
            i=90;
        }
    }


    class ListenFromServer extends Thread {
        ClientSocket clientSocket;
        String[] logindata;
        TimerThreadSocket timerThreadSocket;
        public ListenFromServer(ClientSocket clientSocket, String[] logindata){
            this.clientSocket=clientSocket;
            this.logindata = logindata;
        }
        public String[] getLogindata(){
            return this.logindata;
        }

        public ClientSocket getClientSocket() {
            return clientSocket;
        }

        public void run() {
            while(true) {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    //---------------------HERE THE IMPLEMENTATION OF THE PROTOCOL------------------------------
                    String msg = (String) in.readObject();
                    String [] arrOfStr = msg.split("-");

                    if(arrOfStr[0].equals("@SCHEME")) {
                        System.out.println("timer iniziato");
                        TimerThreadSocket timerThreadSocket=new TimerThreadSocket(Integer.parseInt(arrOfStr[6]),this, true);
                        timerThreadSocket.start();
                        int scheme=clientInt.schemeMessages(arrOfStr[1], arrOfStr[2],arrOfStr[3], arrOfStr[4], arrOfStr[5]);
                        if(scheme!=99){
                            sendMessage("@SCHEME-" + scheme);
                            timerThreadSocket.interrupt();
                        }

                    }

                    else if(arrOfStr[0].equals("@PRINTALL")) {
                        clientInt.printCarpetFirst(arrOfStr[1], arrOfStr[2]);
                    }

                    else if(arrOfStr[0].equals("@YOURTURN")) { //enables turn
                        if(arrOfStr[1].equals("true")) {
                            clientInt.timerOut(false);
                            System.out.println("timer iniziato");
                            timerThreadSocket = new TimerThreadSocket(Integer.parseInt(arrOfStr[2]), this, false);
                            timerThreadSocket.start();
                            yourturn = true;
                        }
                        else {
                            timerThreadSocket.interrupt();
                            clientInt.endTurn();
                            yourturn=false;
                        }
                    }
                    else if(arrOfStr[0].equals("@USETOOL")){
                        int tool = clientInt.chooseToolMessages();
                        if (tool!=0)
                             sendMessage("@TOOLUSED-"+tool);
                    }

                    else if(arrOfStr[0].equals("@PLACEDICE")){          //choose and place dice
                        if(yourturn==true) {
                            int[] coordinates;
                            coordinates=clientInt.placeDiceMessages();
                            if(coordinates[0]!=99)
                                sendMessage("@DICEPLACED-" + coordinates[0] + "-" + coordinates[1] + "-" + coordinates[2]);
                        }
                    }

                    else if(arrOfStr[0].equals("@AFTERTOOLUPDATE")){
                        clientInt.updateView(arrOfStr[1], arrOfStr[2]);
                    }

                    else if(arrOfStr[0].equals("@CHOOSEACTION")){
                        String action = clientInt.handleTurnMenu();
                        if (!action.equals("4"))
                           sendMessage("@ACTIONCHOSE-"+action);
                    }

                    else if(arrOfStr[0].equals("@ERROR")){
                        clientInt.showError("Errore-"+ arrOfStr[1]);
                    }
                    else if(arrOfStr[0].equals("@ERRORPLACEDICE")){
                        clientInt.showPlacementeError("Errore-"+ arrOfStr[1]);
                    }

                    else if(arrOfStr[0].equals("@SHOWSCORE")){
                        System.out.println("La partita è terminata. Stampo la classifica.");
                        String s=new String();
                        for (int i=1;i<arrOfStr.length;i++) {
                            if (i != arrOfStr.length - 1)
                                s = s + arrOfStr[i] + "-";
                            else
                                s = s + arrOfStr[i];
                        }
                        String[] scores = s.split("_");
                        clientInt.showScore(scores);
                    }

                    else if(arrOfStr[0].equals("@CONNDISC")) {
                        clientInt.showConnDiscPopup(arrOfStr[1]);
                    }

                    else if(arrOfStr[0].equals("@TOOL")){
                        //--------------------USE TOOL CARDS------------------------
                            if (arrOfStr[1].equals("1")) {
                                clientInt.showToolTricks(new ToolCards(2).getName(), new ToolCards(2).getUsagetricks());
                                int[] coordinates;
                                coordinates=clientInt.tool23Messages();
                                if (coordinates[0]!=99)
                                    sendMessage("@TOOLUSED1-" + coordinates[0] + "-" + coordinates[1] + "-" + coordinates[2] + "-" + coordinates[3]);
                            }
                            if (arrOfStr[1].equals("2")) {
                                clientInt.showToolTricks(new ToolCards(4).getName(), new ToolCards(4).getUsagetricks());
                                int[] coordinates;
                                coordinates=clientInt.tool4Messages();
                                if (coordinates[0]!=99)
                                    sendMessage("@TOOLUSED2-" + coordinates[0] + "-" + coordinates[1] + "-" + coordinates[2] + "-" + coordinates[3] + "-" + coordinates[4] + "-" + coordinates[5] + "-" + coordinates[6] + "-" + coordinates[7]);
                            }
                            if (arrOfStr[1].equals("3")) {
                                clientInt.showToolTricks(new ToolCards(3).getName(), new ToolCards(3).getUsagetricks());
                                int[] coordinates12 = clientInt.tool12Messages();
                                if (coordinates12[0]!=99)
                                    sendMessage("@TOOLUSED3-" + coordinates12[8] + "-" + coordinates12[0] + "-" + coordinates12[1] + "-" + coordinates12[2] + "-" + coordinates12[3] + "-" + coordinates12[4] + "-" + coordinates12[5] + "-" + coordinates12[6] + "-" + coordinates12[7] + "-" + coordinates12[9] + "-" + coordinates12[10]);
                            }
                            if(arrOfStr[1].equals("4")){
                                clientInt.showToolTricks(new ToolCards(1).getName(), new ToolCards(1).getUsagetricks());
                                int vdice=clientInt.chooseDice();
                                if (vdice!=99) {
                                    int dicechose = clientInt.tool1Messages();
                                    if (dicechose != 99)
                                        sendMessage("@TOOLUSED4-" + vdice + "-" + dicechose);
                                }
                            }
                            if(arrOfStr[1].equals("6")){
                                clientInt.showToolTricks(new ToolCards(6).getName(), new ToolCards(6).getUsagetricks());
                                int ndice = clientInt.chooseDice();
                                if (ndice!=99)
                                    sendMessage("@TOOLUSED6-"+ndice);

                            }
                            if(arrOfStr[1].equals("61")){
                                int[] coordinates = clientInt.tool6Messages(arrOfStr[2]);
                                if (coordinates[0]!=99)
                                    sendMessage("@TOOLUSED61-"+coordinates[0]+"-"+coordinates[1]);
                            }
                            if(arrOfStr[1].equals("5")){
                                clientInt.showToolTricks(new ToolCards(5).getName(), new ToolCards(5).getUsagetricks());
                                int vdice = clientInt.chooseDice();
                                if (vdice!=99) {
                                    int[] dicepos = clientInt.chooseFromPath();
                                    if (dicepos[0] != 99)
                                        sendMessage("@TOOLUSED5-" + vdice + "-" + dicepos[0] + "-" + dicepos[1]);
                                }
                            }
                            if(arrOfStr[1].equals("7")){
                                sendMessage("@TOOLUSED7-1");
                            }
                            if(arrOfStr[1].equals("8")){
                                clientInt.showToolTricks(new ToolCards(8).getName(), new ToolCards(8).getUsagetricks());
                                int vdice=clientInt.chooseDice();
                                if (vdice!=99) {
                                    int[] dicepos = clientInt.chooseCoordinates();
                                    if (dicepos[0] != 99)
                                        sendMessage("@TOOLUSED8-" + vdice + "-" + dicepos[0] + "-" + dicepos[1]);
                                }
                            }
                            if(arrOfStr[1].equals("91")){
                                int value = clientInt.tool11Messages(arrOfStr[2]);
                                if (value!=99)
                                    sendMessage("@TOOLUSED91-"+value);
                            }

                            if(arrOfStr[1].equals("92")){
                                int[] value = clientInt.chooseCoordinates();
                                if (value[0]!=99)
                                    sendMessage("@TOOLUSED92-"+value[0]+"-"+value[1]);
                            }

                    } else if(arrOfStr[0].equals("@ENDGAMEACTION")){
                        boolean choose;
                        choose = clientInt.newMatch();
                        if(choose){
                            sendMessage("@ENDGAMEACTION-true");
                        }else{
                            clientInt.exit();
                            sendMessage("@ENDGAMEACTION-false");
                        }
                    }

                    else {
                        System.out.println(msg);
                    }
                }
                catch (InterruptedException e) { }
                catch(IOException e) {
                    clientInt.showError("Errore di connessione-Ops, c'è stato un problema di connessione con il socket. Devo chiudere l'applicazione");
                    System.exit(0);
                }
                catch(ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    //---------------------------------------------send message to server-----------------------------------------------
    public void sendMessage(String message) throws IOException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
        }catch(IOException e){
            System.out.println("Ho catchato");
        }
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

