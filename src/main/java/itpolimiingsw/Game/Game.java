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

import static java.lang.Thread.sleep;

public class Game implements Serializable {
    private ArrayList<User> users;
    private ArrayList<Player> player;
    private GreenCarpet greenCarpet;
    private Boolean isPlaying;
    private int numUser;
    private int numUserOnline;
    private Matches matches;

    public Game(Matches matches){
        isPlaying=false;
        this.numUser=0;
        users=new ArrayList<>();
        player=new ArrayList<>();
        this.matches=matches;
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
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Player> getPlayer() {
        return player;
    }
    public void setGreenCarpet(GreenCarpet greenCarpet) {
        this.greenCarpet = greenCarpet;
    }

    public void setPlayer(Player player) {
        this.player.add(player);
    }
    public void playerConnect() { numUserOnline++; }


    /**
     * add the user in the game
     *
     * @param user to add
     */
    public boolean addUser(User user){
        Boolean flag= false;
        if (numUser < 4) {
            numUser++;
            numUserOnline++;
            this.users.add(user);
            flag= true;
        }
        if(numUserOnline==4){
            if(!isPlaying)
                new GameStart().start();
        }
        if(numUserOnline==2)
            new GameStartonTime().start();

        return flag;
    }

    /**
     * menage the game for client reconnection
     */
    public void reconnectUser(){
        if(numUserOnline==4){
            if(!isPlaying)
                new GameStart().start();
        }
        if(numUserOnline==2)
            new GameStartonTime().start();
    }

    /**
     * methods that manage the entire game setting
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void startGame() throws IOException, InterruptedException {
        isPlaying = true;
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

        this.greenCarpet = new GreenCarpet(numUser);
        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();
        //greenCarpet.setToolCards(new ToolCards(6),new ToolCards(11),new ToolCards(12));
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
                }
                this.player.add(player);
            }else{
                calculate_result(0);
                return;
            }
            if(numUserOnline<=1) {
                calculate_result(0);
                return;
            }
        }

        for (int j = 0; j < 10; j++) {
            greenCarpet.setRound(j);
            greenCarpet.setPlayer(player);
            for (int i = 0; i < player.size(); i++) {
                player.get(i).setSecondTurn(true);
            }
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

    /**
     * method that create a new string in json from four scheme
     *
     * @param scheme  first scheme
     * @param scheme1 second scheme
     * @param scheme2 third scheme
     * @param scheme3 fourth scheme
     * @return a string in json with the scheme
     */
    private String[] getJsonSchemes(Scheme scheme, Scheme scheme1, Scheme scheme2, Scheme scheme3) {
        Gson gson = new GsonBuilder().create();
        String[] schemesjson = new String[4];
        schemesjson[0] = gson.toJson(scheme);
        schemesjson[1] = gson.toJson(scheme1);
        schemesjson[2] = gson.toJson(scheme2);
        schemesjson[3] = gson.toJson(scheme3);
        return schemesjson;
    }

    /**
     * metods that assign at one player in the game the scheme chosen
     *
     * @param schemes array of schemes
     * @param bridges array of bridges
     * @param i index of the player in the array of players
     * @param time time of the timer
     * @param player Player appointed
     * @throws IOException
     * @throws InterruptedException
     */
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


    /**
     * method that handle every turn in the game
     *
     * @param i index of the player
     * @param time of the timer
     * @throws IOException
     * @throws InterruptedException
     */
    private void turn(int i, int time) throws IOException, InterruptedException{
        Game game = users.get(i).getConnectionType().endTurn(this.getGreenCarpet(), this.getPlayer(i), i, time);
        if(game!=null) {
            this.greenCarpet = game.greenCarpet;
            this.player.set(i, game.getPlayer(0));
        }
    }

    /**
     * Calculate the end score of the game and send to every player the score
     *
     * @param singleplayer flag of how many players
     * @throws IOException
     * @throws InterruptedException
     */
    private void calculate_result(int singleplayer) throws IOException, InterruptedException {       //AGGIUNGERE SHOWSCORE
        Calculator calculator = new Calculator(player, greenCarpet);
        //------------start calculating------------
        sleep(2000);
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
            int score = 0;
            for(int i = 0; i<users.size(); i++) {
                score = 0;
                if (users.get(i).isOnline()) {
                    try {
                        player.get(i).setPoints(calculator.calculate(singleplayer));
                        score = player.get(i).getPoints();
                    } catch (NullPointerException | IndexOutOfBoundsException e) {score = 0;}
                    try {
                        String[] s = new String[1];
                        s[0] = "Sei l'unico giocatore all'interno della partita._Hai vinto con " + score + " punti!";
                        users.get(i).getConnectionType().showScore(s);
                    } catch (NullPointerException | ConnectException e) {
                    }
                }
            }
        }

        matches.deleteGame(this);

    }

    /**
     *method that hanle the disconnection of a player
     *
     * @param i index of the player
     */
    private void setOffPlayer(int i){
        users.get(i).setOnline(false);
        player.get(i).setOnline(false);
        playerDisconnect();
    }

    /**
     *method that hanle the disconnection of a player, if the player is the last one delete the game
     */
    public void playerDisconnect(){
        numUserOnline--;
        if(numUserOnline==0)
            matches.deleteGame(this);
    }




    /**
     * method that return a random array of private goals
     *
     * @param numPlayer number of player in the game
     * @return a random array of Private Goal
     */
    private PrivateGoal[] getRndPrivateGoals(int numPlayer){
        Random rnd=new Random();
        PrivateGoal [] privateGoals=new PrivateGoal[numPlayer];
        int[] index=new int[4];
        index[0]=rnd.nextInt(5)+1;
        index[1]=rnd.nextInt(5)+1;
        index[2]=rnd.nextInt(5)+1;
        index[3]=rnd.nextInt(5)+1;
        while(index[0]==index[1] || index[1]==index[2] || index[0]==index[2] || index[0]==index[3] || index[1]==index[3] || index[3]==index[2] ){
            index[0]=rnd.nextInt(5)+1;
            index[1]=rnd.nextInt(5)+1;
            index[2]=rnd.nextInt(5)+1;
            index[3]=rnd.nextInt(5)+1;
        }
        for(int i=0;i<numPlayer;i++) {
            privateGoals[i] = new PrivateGoal(index[i]);
        }
        return privateGoals;
    }

    /**
     * method that return a random array of schemes
     *
     * @param numPlayer number of player in the game
     * @return a random array of scheme
     */
    private Scheme[] getRndSchemes(int numPlayer){
        Random rnd=new Random();
        Scheme [] schemes=new Scheme[numPlayer*4];
        int[] index=new int[16];
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

    /**
     * method that return a random array of bridge
     *
     * @param numPlayer number of player in the game
     * @return a random array of bridge
     */
    private Bridge[] getRndBridges(int numPlayer) {
        Random rnd = new Random();
        Bridge[] bridges = new Bridge[numPlayer];
        int[] index = new int[4];
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

    /**
     * method that return string of the score of every player
     *
     * @param playerscore array of players
     * @return string of score
     */
    private String[] tableToString(Player[] playerscore){
        String[] s=new String[playerscore.length];
        for (int i=0;i<playerscore.length;i++){
            s[i]=(i + 1) + "°: " + playerscore[i].getNickname() + "\tPunteggio: " + playerscore[i].getPoints()+"\n";
        }
        return s;
    }


    /**
     * read the configuration of the timer in the file sever_config.txt
     *
     * @return the time of the timer
     * @throws IOException
     */
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
        int time;
        public void run(){
            try{
                time = (Integer.parseInt(readTime())/3)*1000;
            }catch(IOException | NumberFormatException e){time = 60;}
            for(User u:users) {
                try {
                    u.getConnectionType().sendMessageOut("\nLa partita inizierà fra "+time/1000+" secondi!");
                } catch (IOException | NullPointerException e) {
                    u.setOnline(false);
                }
            }
            try {
                sleep(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if(numUserOnline>1) {
                try {
                    if (!isPlaying)
                        startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }else
                System.out.println("Non posso avviare la partita!");
        }
    }
}
