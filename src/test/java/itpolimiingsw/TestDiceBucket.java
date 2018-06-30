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
    public void tDiceBucketAndScheme(){
        Bridge bridge=new Bridge(1);
        DiceBucket bag=new DiceBucket();
        Scheme scheme = new Scheme(1);
        bridge.setScheme(scheme);
        Dice diceArray[]=new Dice[6];
        for(int i=0;i<6;i++){
            diceArray[i]=bag.educe();
            diceArray[i].roll();
            System.out.print(i+1+") "+diceArray[i].toString()+" ");
        }
        System.out.println();
        System.out.println(bridge);
    }
    @org.junit.jupiter.api.Test
    public void testDice(){
        Dice d=new Dice(Colour.ANSI_YELLOW);
        d.roll();
    }
}
