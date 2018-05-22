package it.polimi.ingsw.Game;

import it.polimi.ingsw.Server.User;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;

public class Game {
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
    }
    public void addUser(User user){
        if(numUser==4)
            return ;

        numUser = numUser + 1;
        this.users.add(user);
        return;
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

    public void startGame(){
        isPlaying=true;
        this.greenCarpet=new GreenCarpet(numUser);
        DiceBucket diceBucket=inventory.getDiceBucket();
        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();

        PrivateGoal[] privateGoals=new PrivateGoal(1).getRndPrivateGoals(numUser);


        for (int i=0;i<numUser;i++) {
            Player player=new Player(users.get(i).getNickname());
            player.setPrivateGoal(privateGoals[i]);


            this.player.add(player);
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
}
