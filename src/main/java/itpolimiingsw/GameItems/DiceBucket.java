package itpolimiingsw.GameItems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class DiceBucket implements Serializable {
    public ArrayList<Dice> diceArray;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public DiceBucket (){
        diceArray=new ArrayList<Dice>();
        fill();
    }

    /**
     *Method that fills the Dicebucket with 90 dices with 18 dice for each color
     */
    private void fill(){
        for(int i=0; i<90/5; i++){
            for (Colour c: Colour.values()){
                Dice d= new Dice(c);
                this.diceArray.add(d);
            }
        }
    }

    /**
     *Method that fills a single dice inside the dicebucket
     *@param dice is the dice to insert
     */
    public void insertDice(Dice dice){
        this.diceArray.add(dice);
    }

    /**
     *Method that educe a random coloured dice from the bucket
     *
     *@return the Dice with a random color
     */
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


    public void dump (){
        int i=1;
        for(Dice d: diceArray){
            System.out.print(i+": ");
            d.dump();
            i++;
        }
    }
}
