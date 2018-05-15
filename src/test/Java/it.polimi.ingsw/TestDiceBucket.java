package it.polimi.ingsw;


import it.polimi.ingsw.Game.Bridge;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.DiceBucket;
import it.polimi.ingsw.Game.Scheme;

public class TestDiceBucket {
    @org.junit.jupiter.api.Test
    public void tDiceBucket(){

        DiceBucket bag= new DiceBucket();
        for(int i=0; i<22; i++){
            Dice d=bag.educe();
            d.roll();
            System.out.println(d);
        }
        System.out.println("ciao");
        bag.dump();

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
            System.out.print(i+1+") "+diceArray[i]+" ");
        }
        System.out.println();
        System.out.println(bridge);



    }
}
