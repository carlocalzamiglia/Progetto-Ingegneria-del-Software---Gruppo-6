package itpolimiingsw.Game;

import itpolimiingsw.Server.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.Random;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Game implements Serializable {
    private ArrayList<User> users;
    private ArrayList<Player> player;
    private GreenCarpet greenCarpet;
    private Inventory inventory;
    private Boolean isPlaying;
    private int idGame;
    private int numUser;
    private int numUserOnline;
    Matches matches;

    public Game(int index, Matches matches){
        isPlaying=false;
        idGame=index;
        this.inventory = new Inventory() ;
        this.numUser=0;
        users=new ArrayList<>();
        player=new ArrayList<>();
        this.matches=matches;
    }
    public void addUser(User user){
        if (numUser != 4) {
            numUser++;
            numUserOnline++;
            this.users.add(user);
        }
        if(numUser==4){
            new GameStart().start();
        }
        if(numUser==2)
            new GameStartonTime().start();

    }

    class GameStart extends Thread{
        public void run(){
            try {
                startGame();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    class GameStartonTime extends Thread{
        public void run(){
            for(User u:users) {
                try {
                    u.getConnectionType().sendMessageOut("\nLa partita inizierà fra 10 secondi!");
                } catch (IOException | NullPointerException e) {
                    u.setOnline(false);
                    numUserOnline--;
                }
            }
            try {
                GameStartonTime.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            try {
                if(isPlaying==false && numUser>1)
                    startGame();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public Boolean getPlaying() {
        return isPlaying;
    }

    public GreenCarpet getGreenCarpet() {
        return greenCarpet;
    }
    public Player getPlayer(int index){
        return player.get(index);
    }

    public void startGame() throws IOException, InterruptedException {
        int time = 90;
        try {
            time = Integer.parseInt(readTime());
        } catch (NumberFormatException e) {
            System.out.println("Il tempo inserito nel timer non è numerico. Verrà impostato quello di default (90 secondi).");
        }
        if (time == 0) {
            System.out.println("Hai inserito un valore non accettabile per il timer. Verrà impostato quello di default (90 secondi).");
            time = 90;
        }
        isPlaying = true;
        this.greenCarpet = new GreenCarpet(numUser);
        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();
        //greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2),new ToolCards(3));
        PrivateGoal[] privateGoals = getRndPrivateGoals(numUser);
        Scheme[] schemes = getRndSchemes(numUser);
        Bridge[] bridges = getRndBridges(numUser);

        for (int i = 0; i < numUser; i++) {
            if(numUserOnline>1) {
                Player player = new Player(users.get(i).getNickname());
                player.setOnline(users.get(i).isOnline());
                player.setPrivateGoal(privateGoals[i]);

                try {
                    setScheme(schemes, bridges, i, time, player);
                } catch (IOException | NullPointerException e) {
                    users.get(i).setOnline(false);
                    player.setOnline(false);
                    numUserOnline--;
                }


                this.player.add(player);
            }else{
                //what we expect from the game?
            }
        }

        for (int j = 0; j < 10; j++) {
            greenCarpet.setRound(j);
            greenCarpet.setPlayer(player);
            for (int i = 0; i < numUser; i++) {
                player.get(i).setSecondTurn(true);
            }
            System.out.println("ROUND " + (j + 1));
            greenCarpet.setStock(numUser * 2 + 1);

            for (int i = 0; i < numUser; i++) {
                if (player.get(i).isOnline()) {
                    if(numUserOnline>1) {
                        try {
                            if (player.get(i).getScheme() == null) {
                                setScheme(schemes, bridges, i, time, player.get(i));
                            }
                            greenCarpet.setTurn(1);
                            turn(i, time);
                        } catch (IOException | NullPointerException e) {
                            setOffPlayer(i);
                        }
                    }else{
                        calculate_result(i);
                        return;
                    }
                }
                greenCarpet.setPlayer(player);
            }
            for (int i = numUser - 1; i >= 0; i--) {
                if (player.get(i).isOnline()) {
                    if(numUserOnline>1) {
                        try {
                            if (player.get(i).getScheme() == null) {
                                setScheme(schemes, bridges, i, time, player.get(i));
                            }
                            greenCarpet.setTurn(2);
                            if (player.get(i).getSecondTurn()) {
                                turn(i, time);
                            }
                        } catch (IOException | NullPointerException e) {
                            setOffPlayer(i);
                        }
                    }else{
                        calculate_result(i);
                        return;
                    }
                }
                greenCarpet.setPlayer(player);
            }
            greenCarpet.setRoundPath(j + 1);
            player.add(player.get(0));
            player.remove(0);
            users.add(users.get(0));
            users.remove(0);
        }
        if(numUserOnline>0)
            calculate_result(5);
        else
            System.out.println("Nessun giocatore è più in partita.");

    }

    public void calculate_result(int singleplayer) throws IOException, InterruptedException {       //AGGIUNGERE SHOWSCORE
        Calculator calculator = new Calculator(player, greenCarpet);
        //------------start calculating------------
        if(singleplayer==5) {
            for (int i = 0; i < numUser; i++)
                player.get(i).setPoints(calculator.calculate(i));       //set points to each player

            Player[] playerscore = new Player[numUser];
            for (int i = 0; i < numUser; i++)
                playerscore[i] = player.get(i);

            for (int i = 0; i < numUser; i++) {
                for (int j = i + 1; j < playerscore.length; j++) {
                    if (playerscore[i].getPoints() < playerscore[j].getPoints()) {
                        Player tmp = playerscore[i];
                        playerscore[i] = playerscore[j];
                        playerscore[j] = tmp;
                    }
                }
            }
            System.out.println("E' appena terminata la partita con il seguente risultato:\n");
            for (int i = 0; i < numUser; i++) {
                try {
                    users.get(i).getConnectionType().showScore(tableToString(playerscore));
                } catch (NullPointerException e) {
                }
            }

        }else{
            player.get(singleplayer).setPoints(calculator.calculate(singleplayer));
            try {
                String[] s=new String[1];
                s[0]="Sei l'unico giocatore all'interno della partita. Hai vinto con " + player.get(singleplayer).getPoints() + " punti!";
                users.get(singleplayer).getConnectionType().showScore(s);
            } catch(NullPointerException | ConnectException e) { }
        }

        matches.deleteGame(this);

    }


    private String[] getJsonSchemes(Scheme scheme, Scheme scheme1, Scheme scheme2, Scheme scheme3) {
        Gson gson = new GsonBuilder().create();
        String[] schemesjson = new String[4];
        schemesjson[0] = gson.toJson(scheme);
        schemesjson[1] = gson.toJson(scheme1);
        schemesjson[2] = gson.toJson(scheme2);
        schemesjson[3] = gson.toJson(scheme3);
        return schemesjson;
    }

    private void setScheme(Scheme[] schemes,Bridge[] bridges, int i, int time, Player player) throws IOException, InterruptedException {
        String[] schemesjson = getJsonSchemes(schemes[(i * 4)], schemes[(i * 4) + 1], schemes[(i * 4) + 2], schemes[(i * 4) + 3]);
        Gson gson = new GsonBuilder().create();
        String privategoaljson = gson.toJson(player.getPrivateGoal());
        int schemechose = users.get(i).getConnectionType().chooseScheme(schemesjson[0], schemesjson[1], schemesjson[2], schemesjson[3], privategoaljson, time);
        if (schemechose != 0) {
            player.setBridge(bridges[i]);
            player.setScheme(schemes[(i * 4) + schemechose - 1]);
            player.setMarkers();
        }
    }

    private void turn(int i, int time) throws IOException, InterruptedException{
        System.out.println("tocca a: " + users.get(i).getNickname());
        Game game = users.get(i).getConnectionType().endTurn(this.getGreenCarpet(), this.getPlayer(i), i, time);
        if(game!=null) {
            this.greenCarpet = game.greenCarpet;
            this.player.set(i, game.getPlayer(0));
        }
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Player> getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        String s=greenCarpet+"\n";
        for(Player p:player)
            s=s+p.toString()+"\n";
        return s;
    }

    public void dump(){
        System.out.println(this);
    }

    private void setOffPlayer(int i){
        users.get(i).setOnline(false);
        player.get(i).setOnline(false);
        playerDisconnect();
    }

    public void playerDisconnect(){
        numUserOnline--;
    }



    //-------------------------------Random methods for the initialization of the match---------------------------------
    private PrivateGoal[] getRndPrivateGoals(int numPlayer){
        Random rnd=new Random();
        PrivateGoal [] privateGoals=new PrivateGoal[numPlayer];
        int index[]=new int[4];
        index[0]=rnd.nextInt(4)+1;
        index[1]=rnd.nextInt(4)+1;
        index[2]=rnd.nextInt(4)+1;
        index[3]=rnd.nextInt(4)+1;
        while(index[0]==index[1] || index[1]==index[2] || index[0]==index[2] || index[0]==index[3] || index[1]==index[3] || index[3]==index[2] ){
            index[0]=rnd.nextInt(4)+1;
            index[1]=rnd.nextInt(4)+1;
            index[2]=rnd.nextInt(4)+1;
            index[3]=rnd.nextInt(4)+1;
        }
        for(int i=0;i<numPlayer;i++) {
            privateGoals[i] = new PrivateGoal(index[i]);
        }
        return privateGoals;
    }
    private Scheme[] getRndSchemes(int numPlayer){
        Random rnd=new Random();
        Scheme [] schemes=new Scheme[numPlayer*4];
        int index[]=new int[16];
        index[0]=rnd.nextInt(12)*2+1;
        index[2]=rnd.nextInt(12)*2+1;
        while(index[0]==index[2])
            index[2]=rnd.nextInt(12)*2+1;
        index[4]=rnd.nextInt(12)*2+1;
        while(index[4]==index[0] || index[4]==index[2])
            index[4]=rnd.nextInt(12)*2+1;
        index[6]=rnd.nextInt(12)*2+1;
        while(index[6]==index[0] || index[6]==index[2] || index[6]==index[4])
            index[6]=rnd.nextInt(12)*2+1;
        index[8]=rnd.nextInt(12)*2+1;
        while(index[8]==index[0] || index[8]==index[2] || index[8]==index[4] || index[8]==index[6])
            index[8]=rnd.nextInt(12)*2+1;
        index[10]=rnd.nextInt(12)*2+1;
        while(index[10]==index[0] || index[10]==index[2] || index[10]==index[4] || index[10]==index[6] || index[10]==index[8])
            index[10]=rnd.nextInt(12)*2+1;
        index[12]=rnd.nextInt(12)*2+1;
        while(index[12]==index[0] || index[12]==index[2] || index[12]==index[4] || index[12]==index[6]|| index[12]==index[8]|| index[12]==index[10])
            index[12]=rnd.nextInt(12)*2+1;
        index[14]=rnd.nextInt(12)*2+1;
        while(index[14]==index[0] || index[14]==index[2] || index[14]==index[4] || index[14]==index[6]|| index[14]==index[8]|| index[14]==index[10]|| index[14]==index[12])
            index[14]=rnd.nextInt(12)*2+1;


        index[1]=index[0]+1;
        index[3]=index[2]+1;
        index[5]=index[4]+1;
        index[7]=index[6]+1;
        index[9]=index[8]+1;
        index[11]=index[10]+1;
        index[13]=index[12]+1;
        index[15]=index[14]+1;
        for(int i=0;i<numPlayer*4;i++) {
            schemes[i] = new Scheme(index[i]);
        }
        return schemes;
    }
    private Bridge[] getRndBridges(int numPlayer) {
        Random rnd = new Random();
        Bridge[] bridges = new Bridge[numPlayer];
        int index[] = new int[4];
        index[0] = rnd.nextInt(4) + 1;
        index[1] = rnd.nextInt(4) + 1;
        index[2] = rnd.nextInt(4) + 1;
        index[3] = rnd.nextInt(4) + 1;
        while (index[0] == index[1] || index[1] == index[2] || index[0] == index[2] || index[0] == index[3] || index[1] == index[3] || index[3] == index[2]) {
            index[0] = rnd.nextInt(4) + 1;
            index[1] = rnd.nextInt(4) + 1;
            index[2] = rnd.nextInt(4) + 1;
            index[3] = rnd.nextInt(4) + 1;
        }
        for (int i = 0; i < numPlayer; i++) {
            bridges[i] = new Bridge(index[i]);
        }
        return bridges;
    }


    private String[] tableToString(Player[] playerscore){
        String[] s=new String[playerscore.length];
        for (int i=0;i<playerscore.length;i++){
            s[i]=(i + 1) + "°: " + playerscore[i].getNickname() + "\tPunteggio: " + playerscore[i].getPoints()+"\n";
        }
        return s;
    }
    public void setGreenCarpet(GreenCarpet greenCarpet) {
        this.greenCarpet = greenCarpet;
    }

    public void setPlayer(Player player,int i) {
        this.player.add(player);
    }

    private String readTime() throws IOException {
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/server_config.txt");
        BufferedReader b = new BufferedReader(f);
        String time;
        try {
            String useless=b.readLine();
            useless=b.readLine();
            time = (b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return time;
    }
}
