package it.polimi.ingsw.Project.Game;

import java.util.ArrayList;
import java.util.Random;

class DiceBucket {
    public ArrayList<Dice> diceArray;

    public DiceBucket (){
        diceArray=new ArrayList<Dice>();
        fill();
    }
    private void fill(){
        for(int i=0; i<90/5; i++){
            for (Colour c: Colour.values()){
                Dice d= new Dice(c);
                this.diceArray.add(d);
            }
        }
    }
    public void dump (){
        int i=1;
        for(Dice d: diceArray){
            System.out.print(i+": ");
            d.dump();
            i++;
        }
    }
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
