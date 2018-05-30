package it.polimi.ingsw.Game;

import it.polimi.ingsw.Server.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
        Scanner scanner=new Scanner(System.in);
        Ruler ruler=new Ruler();
        ToolCardsExecutor executor=new ToolCardsExecutor();
        isPlaying=true;
        this.greenCarpet=new GreenCarpet(numUser);
        DiceBucket diceBucket=inventory.getDiceBucket();
        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();

        PrivateGoal[] privateGoals=new PrivateGoal(1).getRndPrivateGoals(numUser);
        Scheme [] schemes=new Scheme(0).getRndSchemes(numUser);
        Bridge[] bridges=new Bridge(0).getRndBridges(numUser);

        for (int i=0;i<numUser;i++) {
            Player player=new Player(users.get(i).getNickname());
            player.setPrivateGoal(privateGoals[i]);

            users.get(i).getConnectionType().sendMessageOut("@ERROR-Il tuo obiettivo privato è: "+privateGoals[i]+"\n");

            int schemechose = users.get(i).getConnectionType().chooseScheme(schemes[(i*4)].toString(),schemes[(i*4)+1].toString(),schemes[(i*4)+2].toString(),schemes[(i*4)+3].toString());
            System.out.println("Schema scelto dal client: "+schemechose);
            player.setBridge(bridges[i]);
            player.setScheme(schemes[(i*4)+schemechose-1]);
            player.setMarkers();

            this.player.add(player);
        }

        for(int j=0;j<10;j++) {
            System.out.println("ROUND "+(j+1));
            greenCarpet.setStock(numUser*2+1,diceBucket);

            for (int i = 0; i < numUser; i++) {
                System.out.println("tocca a: "+users.get(i).getNickname());
                users.get(i).getConnectionType().handleturn(this.getGreenCarpet(), this.getPlayer(i), i, playersToString(i));
            }


            for (int i = numUser-1; i >=0; i--) {
                System.out.println("tocca a: "+users.get(i).getNickname());
                users.get(i).getConnectionType().handleturn(this.getGreenCarpet(), this.getPlayer(i), i, playersToString(i));
            }
            greenCarpet.setRoundPath(j+1);
            player.add(player.get(0));
            player.remove(0);
            users.add(users.get(0));
            users.remove(0);

        }
        greenCarpet.dump();
        for(int i=0;i<numUser;i++)
            player.get(i).dump();

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
            System.out.println((i + 1) + "°: " + playerscore[i].getNickname() + "Punteggio: " + playerscore[i].getPoints());
            users.get(i).getConnectionType().sendMessageOut("@ERROR-Ti sei posizionato "+(i+1)+"°, complimenti!");

        }

    }

    public ArrayList<User> getUsers() {
        return users;
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
}
