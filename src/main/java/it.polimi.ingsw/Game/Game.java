package it.polimi.ingsw.Game;

import it.polimi.ingsw.Server.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

public class Game implements Serializable {
    private ArrayList<User> users;
    private ArrayList<Player> player;
    private GreenCarpet greenCarpet;
    private Inventory inventory;
    private Boolean isPlaying;
    private int idGame;
    private int numUser;

    public Game(int index){
        isPlaying=false;
        idGame=index;
        this.inventory = new Inventory() ;
        this.numUser=0;
        users=new ArrayList<>();
        player=new ArrayList<>();
    }
    public void addUser(User user) throws IOException {
        if (numUser != 4) {
            numUser = numUser + 1;
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
                e.printStackTrace();
            }
        }
    }
    class GameStartonTime extends Thread{
        public void run(){
            for(User u:users) {
                try {
                    u.getConnectionType().sendMessageOut("\nLa partita inizierà fra 10 secondi!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                GameStartonTime.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if(isPlaying==false && numUser>1)
                    startGame();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        isPlaying=true;
        this.greenCarpet=new GreenCarpet(numUser);
        greenCarpet.setRndPublicGoals();
        //greenCarpet.setRndToolCards();
        greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2),new ToolCards(3));
        PrivateGoal[] privateGoals=getRndPrivateGoals(numUser);
        Scheme [] schemes=getRndSchemes(numUser);
        Bridge[] bridges=getRndBridges(numUser);

        for (int i=0;i<numUser;i++) {
            Player player=new Player(users.get(i).getNickname());
            player.setOnline(true);
            player.setPrivateGoal(privateGoals[i]);

            users.get(i).getConnectionType().sendMessageOut("Il tuo obiettivo privato è: "+privateGoals[i]+"\n");

            int schemechose = users.get(i).getConnectionType().chooseScheme(schemes[(i*4)].toString(),schemes[(i*4)+1].toString(),schemes[(i*4)+2].toString(),schemes[(i*4)+3].toString());
           // System.out.println("Schema scelto dal client: "+schemechose);
            player.setBridge(bridges[i]);
            player.setScheme(schemes[(i*4)+schemechose-1]);
            player.setMarkers();

            this.player.add(player);
        }

        for(int j=0;j<10;j++) {
            for(int i=0;i<numUser;i++){
                player.get(i).setSecondTurn(true);
            }
            System.out.println("ROUND "+(j+1));
            greenCarpet.setStock(numUser*2+1);

            for (int i = 0; i < numUser; i++) {
                if(player.get(i).isOnline()) {
                    int turn = 1;
                    System.out.println("tocca a: " + users.get(i).getNickname());
                    Game game = users.get(i).getConnectionType().handleturn(this.getGreenCarpet(), this.getPlayer(i), i, playersToString(i), turn, j);
                    this.greenCarpet = game.greenCarpet;
                    this.player.set(i, game.getPlayer(0));
                }
            }
            for (int i = numUser-1; i >=0; i--) {
                if(player.get(i).isOnline()) {
                    int turn = 2;
                    if (player.get(i).getSecondTurn()) {
                        System.out.println("tocca a: " + users.get(i).getNickname());
                        Game game = users.get(i).getConnectionType().handleturn(this.getGreenCarpet(), this.getPlayer(i), i, playersToString(i), turn, j);
                        this.greenCarpet = game.greenCarpet;
                        this.player.set(i, game.getPlayer(0));
                    }
                }
            }
            greenCarpet.setRoundPath(j+1);
            player.add(player.get(0));
            player.remove(0);
            users.add(users.get(0));
            users.remove(0);

        }
        for(int i=0;i<numUser;i++)
            users.get(i).getConnectionType().sendMessageOut(greenCarpet.toString()+playersToString(i)+player.get(i).toString());

        //------------start calculating------------
        Calculator calculator=new Calculator(player,greenCarpet);
        for(int i=0;i<numUser;i++)
            player.get(i).setPoints(calculator.calculate(i));       //set points to each player

        Player [] playerscore = new Player[numUser];
        for(int i=0;i<numUser;i++)
            playerscore[i]=player.get(i);

        for(int i=0;i<numUser;i++){
            for(int j=i+1; j<playerscore.length;j++){
                if(playerscore[i].getPoints()<playerscore[j].getPoints()){
                    Player tmp=playerscore[i];
                    playerscore[i]=playerscore[j];
                    playerscore[j]=tmp;
                }
            }
        }
        System.out.println("E' appena terminata la partita con il seguente risultato:\n");
        for (int i=0;i<numUser;i++) {
            users.get(i).getConnectionType().sendMessageOut(tableToString(playerscore));
        }
        for (int i=0;i<numUser;i++)
            for(int j=0; j<users.size();j++)
                if(users.get(j).getNickname().equals(playerscore[i].getNickname()))
                    users.get(j).getConnectionType().sendMessageOut("Ti sei posizionato "+(i+1)+"°, complimenti!");


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


    //-------------------------------Random methods for the initialization of the match---------------------------------
    public PrivateGoal[] getRndPrivateGoals(int numPlayer){
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
    public Scheme[] getRndSchemes(int numPlayer){
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
    public Bridge[] getRndBridges(int numPlayer) {
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


    public String playersToString(int user){
        String s = new String();
        for(int i=0; i<player.size(); i++){
            if(i!=user) {
                if (user == (player.size() - 1) && i == (player.size() - 2))
                    s = s.concat(player.get(i).getNickname());
                else if(i == player.size() - 1)
                    s = s.concat(player.get(i).getNickname());
                else
                    s=s.concat(player.get(i).getNickname()+", ");
            }
        }
        s=s.concat("\n");
        for(int row=0; row<4; row++){
            for(int play=0; play<player.size();play++){
                if(play!=user) {
                    for (int col = 0; col < 5; col++) {
                        s = s.concat(player.get(play).getScheme().getBox(row, col).toString());
                    }
                    s = s.concat("\t\t");
                }
            }
            s=s.concat("\n");
        }
        return s;
    }
    public String tableToString(Player[] playerscore){
        String s=new String();
        s=s+"CLASSIFICA FINALE \n";
        for (int i=0;i<playerscore.length;i++){
            s=s+(i + 1) + "°: " + playerscore[i].getNickname() + "\tPunteggio: " + playerscore[i].getPoints()+"\n";
        }
        return s;
    }
    public void setGreenCarpet(GreenCarpet greenCarpet) {
        this.greenCarpet = greenCarpet;
    }

    public void setPlayer(Player player,int i) {
        this.player.add(player);
    }
}
