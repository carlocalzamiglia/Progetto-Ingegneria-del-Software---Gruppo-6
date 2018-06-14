package it.polimi.ingsw.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GreenCarpet implements Serializable {
    private ArrayList<Dice> stock;
    private Dice[][] roundPath;
    private PublicGoal[] publicGoals;
    private ToolCards[] toolCards;
    private int nPlayers;
    private DiceBucket diceBucket;
    private int turn;
    private int round;
    private ArrayList<Player> player;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public GreenCarpet(int nPlayers){
        this.nPlayers = nPlayers;
        int i=nPlayers*2+1;
        stock= new ArrayList<Dice>(i);
        roundPath= new Dice[i][10];
        publicGoals = new PublicGoal[3];
        toolCards = new ToolCards[3];
        diceBucket=new DiceBucket();
    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public void setPublicGoals(PublicGoal p1, PublicGoal p2, PublicGoal p3){
        publicGoals[0]=p1;
        publicGoals[1]=p2;
        publicGoals[2]=p3;
    }

    public void setRndPublicGoals(){
        Random rnd=new Random();
        int index1=rnd.nextInt(9)+1;
        int index2=rnd.nextInt(9)+1;
        int index3=rnd.nextInt(9)+1;
        while(index1==index2 || index2==index3 || index1==index3){
            index1=rnd.nextInt(9)+1;
            index2=rnd.nextInt(9)+1;
            index3=rnd.nextInt(9)+1;
        }
        publicGoals[0]=new PublicGoal(index1);
        publicGoals[1]=new PublicGoal(index2);
        publicGoals[2]=new PublicGoal(index3);
    }

    public PublicGoal getPublicGoal(int i) {
        return publicGoals[i];
    }
    public void setToolCards(ToolCards t1,ToolCards t2,ToolCards t3){
        this.toolCards[0]=t1;
        this.toolCards[1]=t2;
        this.toolCards[2]=t3;
    }
    public void setRndToolCards(){
        Random rnd=new Random();
        int index1=rnd.nextInt(11)+1;
        int index2=rnd.nextInt(11)+1;
        int index3=rnd.nextInt(11)+1;
        while(index1==index2 || index2==index3 || index1==index3){
            index1=rnd.nextInt(11)+1;
            index2=rnd.nextInt(11)+1;
            index3=rnd.nextInt(11)+1;
        }
        toolCards[0]=new ToolCards(index1);
        toolCards[1]=new ToolCards(index2);
        toolCards[2]=new ToolCards(index3);
    }
    public ToolCards[] getToolCards(){
        return toolCards;
    }
    public void setStock(int numbers){
        for(int i=0; i<numbers;i++){
            Dice dice=diceBucket.educe();
            dice.roll();
            this.stock.add(dice);
        }
    }
    public ArrayList<Dice> getStock(){
        return stock;
    }
    public void setRoundPath(int round){
        for(int i=0; stock.size()>0;i++){
            this.roundPath[i][round-1]=getDiceFromStock(1);
        }

    }

    public void setPlayer(ArrayList<Player> player) {
        this.player = player;
    }

    public ArrayList<Player> getPlayer() {
        return player;
    }

    public void changeDiceFromRoundPath(int i, int round, Dice dice){
        Dice d=roundPath[i-1][round-1];
        roundPath[i-1][round-1]=dice;
        setDiceInStock(d);
    }
    public Dice getDiceFromRoundPath(int i, int round){
        return roundPath[i-1][round-1];
    }
    public Dice getDiceFromStock(int i){
        Dice dice= stock.get(i-1);
        stock.remove(i-1);
        return dice;
    }
    public Dice checkDiceFromStock(int i){
        Dice dice = null;
        if(i>0 && i<=stock.size())
            dice = stock.get(i - 1);
        return dice;
    }
    public void setDiceInStock(Dice dice){
        this.stock.add(dice);
    }
    public ToolCards getToolCard(int i) {
        return toolCards[i-1];
    }
    public int getnPlayers() {
        return nPlayers;
    }
    public DiceBucket getDiceBucket() {
        return diceBucket;
    }

    public void reRollStock(){
        for(int i=0;i<stock.size();i++){
            stock.get(i).roll();
        }
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getRound() {
        return round;
    }

    public int getTurn() {
        return turn;
    }

    //--------------------------------------Returns true if the roundpath isn't empty-----------------------------------
    public boolean checkEmptyRoundpath(){
        boolean bool=false;
        for (int i=0; i<(getnPlayers()*2+1)&& !bool;i++)
            for (int j=0;j<10 && !bool;j++)
                if (roundPath[i][j]!=null)
                    bool=true;
        return bool;
    }

    //--------------------------------------Returns true if the Tool Card is in The Green Carpet------------------------
    public boolean toolIsIn(int serialnumber){
        boolean bool=false;
        for(int j=0; j<getToolCards().length && !bool;j++) {
            if (getToolCards()[j].getSerialNumber() == serialnumber)
                return true;
            else
                bool=false;
        }
        return bool;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
       @Override
    public String toString() {
           String s = new String();
           s = "--OBBIETTIVI PUBBLICI--\n";
           for(int i=0;i<3;i++)
               s=s+(i+1)+")\t\t"+publicGoals[i];
           s = s + "--CARTE UTENSILI--\n";
           for (int i = 0; i < 3; i++) {
               s = s + toolCards[i];
           }
           s = s + "--TRACCIATO DEI ROUND--\n";
           for (int i = 0; i < 10; i++) {
               s = s + "[" + (i + 1) + "°] ";
           }
           s = s + "\n";
           for (int i = 0; i < nPlayers * 2 + 1; i++) {
               for (int j = 0; j < 10; j++) {
                   if (roundPath[i][j] == null)
                       s = s + "[  ] ";
                   else
                       s = s + roundPath[i][j] + "  ";
               }
               s = s + "\n";
           }
           s = s + "--RISERVA--\n";
           for (int i = 0; i < stock.size(); i++)
               s = s + (i + 1) + ")" + stock.get(i).toString() + " ";
           s = s + "\n";

           return s;
       }
    public void dump(){
        System.out.println(this);
    }

    public String stockToString(){
        String s = new String();
        s = s + "--RISERVA--\n";
        for (int i = 0; i < stock.size(); i++)
            s = s + (i + 1) + ")" + stock.get(i).toString() + " ";
        s = s + "\n";
        return s;
    }

    public String toolcardToString(){
        String s = new String();
        s = s + "--CARTE UTENSILI--\n";
        for (int i = 0; i < 3; i++) {
            s = s + toolCards[i];
        }
        return s;
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
                        if(player.get(play).getScheme()!=null)
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

