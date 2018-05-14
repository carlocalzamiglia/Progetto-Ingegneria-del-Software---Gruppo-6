package Game;

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
        toolCards = new ToolCards[3];
    }
    public void setPublicGoals(PublicGoal p1, PublicGoal p2, PublicGoal p3){
        publicGoals[0]=p1;
        publicGoals[1]=p2;
        publicGoals[2]=p3;
    }

    public PublicGoal getPublicGoal(int i) {
        return publicGoals[i];
    }

    public void setToolCards(ToolCards t1,ToolCards t2,ToolCards t3){
        this.toolCards[0]=t1;
        this.toolCards[1]=t2;
        this.toolCards[2]=t3;
    }
    public void setStock(int numbers,DiceBucket diceBucket){
        for(int i=0; i<numbers;i++){
            Dice dice=diceBucket.educe();
            dice.roll();
            this.stock.add(dice);
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
    public Dice changeDiceFromRoundPath(int i, int round, Dice dice){
        Dice d=roundPath[i-1][round-1];
        roundPath[i-1][round-1]=dice;
        return d;
    }
    public Dice getDiceFromRoundPath(int i, int round){
        return roundPath[i-1][round-1];
    }
    public Dice getDiceFromStock(int i){
        Dice dice= stock.get(i-1);
        stock.remove(i-1);
        return dice;
    }
    public void setDiceInStock(Dice dice){
        this.stock.add(dice);
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public ToolCards getToolCard(int i) {
        return toolCards[i-1];
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
        for (int i=0;i<stock.size();i++)
            s=s+(i+1)+")"+stock.get(i).toString()+" ";
        s=s+"\n";
        ;
        s = s+"\n";
        for (int i = 0; i < 3; i++){
            s = s + "\n";
            s = s + publicGoals[i];
            s = s + "\n";
        }
        for (int i = 0; i < 3; i++) {
            s = s + toolCards[i];
            s = s + "\n";
        }
        return s;
    }
    public void dump(){
        System.out.println(this);
    }

}

