package itpolimiingsw;


import itpolimiingsw.Game.*;
import org.junit.jupiter.api.Test;

public class TestDiceBucket {
    @Test
    public void tDiceBucket(){

        DiceBucket bag= new DiceBucket();
        for(int i=0; i<22; i++){
            Dice d=bag.educe();
            d.roll();
            d.dump();
        }
        bag.dump();
        Dice dice=bag.educe();
        bag.insertDice(dice);

    }

    @org.junit.jupiter.api.Test
    public void testDice(){
        Dice d=new Dice(Colour.ANSI_YELLOW);
        d.roll();
    }

    @org.junit.jupiter.api.Test
    public void testDump(){
        DiceBucket diceBucket = new DiceBucket();
        diceBucket.dump();
    }
}
