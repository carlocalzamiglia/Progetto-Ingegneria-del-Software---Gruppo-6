package it.polimi.ingsw.Game;

import java.util.ArrayList;
import java.util.Random;

public class DiceBucket {
    public ArrayList<Dice> diceArray;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public DiceBucket (){
        diceArray=new ArrayList<Dice>();
        fill();
    }

    //---------------------------------Method that fills the Dicebucket with 90 dices 18xcolor--------------------------
    private void fill(){
        for(int i=0; i<90/5; i++){
            for (Colour c: Colour.values()){
                Dice d= new Dice(c);
                this.diceArray.add(d);
            }
        }
    }


    public void insertDice(Dice dice){
        this.diceArray.add(dice);
    }

    public void dump (){
        int i=1;
        for(Dice d: diceArray){
            System.out.print(i+": ");
            d.dump();
            i++;
        }
    }

    //---------------------------------Method that drows a random coloured dice from the bucket-------------------------
    public Dice educe() {
        int count = diceArray.size();
        if (count == 0)
            return null;
        Random rand = new Random();
        int index = rand.nextInt(count);
        Dice d = diceArray.get(index);
        this.diceArray.remove(d);
        return d;
    }
}
