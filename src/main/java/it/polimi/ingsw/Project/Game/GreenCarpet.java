package it.polimi.ingsw.Project.Game;

import java.util.ArrayList;

public class GreenCarpet {
    private ArrayList<Dice> stock;
    private Dice[][] roundPath;
    private PublicGoal[] publicGoals;
    private ToolCards[] toolCards;
    private int nPlayers;

    public GreenCarpet(int nPlayers){
        this.nPlayers = nPlayers;
        int i=nPlayers*2+1;
        stock= new ArrayList<Dice>(i);
        roundPath= new Dice[i][9];
        publicGoals = new PublicGoal[3];
        toolCards = new ToolCards[12];
    }
    public void setPublicGoals(PublicGoal p1, PublicGoal p2, PublicGoal p3){
        publicGoals[0]=p1;
        publicGoals[1]=p2;
        publicGoals[2]=p3;
    }
    public void setToolCards(ToolCards[] toolCards){
        this.toolCards=toolCards;
    }
    public void setStock(Dice[] stock){
        for(int i=0; i<stock.length;i++){
            this.stock.add(stock[i]);
        }
    }
    public ArrayList<Dice> getStock(){
        return stock;
    }

    public void setRoundPath(int round,ArrayList<Dice> dices){
        for(int i=0; i<dices.size();i++){
            this.roundPath[i][round-1]=dices.get(i);
        }
    }
    //public Dice getDiceFromRoundPath(int i, int round, Dice dice)  da implementare una volta prese in considerazione tutte le tool cards)

    public Dice getDiceFromStock(int i){
        Dice dice= stock.get(i);
        stock.remove(i);
        return dice;
    }
    public void setDiceInStock(Dice dice){
        this.stock.add(dice);
    }

    @Override
    public String toString() {
        String s = new String();
        for(int i=0;i<9;i++){
            s=s+"[" + (i+1) +"°] ";
        }
        s=s+"\n";
        for (int i = 0; i < nPlayers * 2 + 1; i++) {
            for (int j = 0; j < 9; j++) {
                if (roundPath[i][j] == null)
                    s = s + "[  ] ";
                else
                    s = s + roundPath[i][j] + "  ";
            }
            s = s+"\n";
        }
        s = s+stock.toString();
        s = s+"\n";
        for (int i = 0; i < 3; i++){
            s = s + publicGoals[i];
            s = s + "\n";
        }
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
    public String toolCardsToString(){
        String s= new String();
        for (int i = 0; i < 12; i++){
            s = s+toolCards[i];
            s = s + "\n";
        }
        return s;
    }
}

