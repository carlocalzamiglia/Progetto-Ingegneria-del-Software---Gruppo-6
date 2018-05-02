package it.polimi.ingsw;


import it.polimi.ingsw.Project.Game.Dice;
import it.polimi.ingsw.Project.Game.DiceBucket;

public class TestDiceBucket {
    @org.junit.jupiter.api.Test
    public void test(){

        DiceBucket bag= new DiceBucket();
        for(int i=0; i<22; i++){
            Dice d=bag.educe();
            d.roll();
            System.out.println(d);
        }
        System.out.println("ciao");
        bag.dump();

    }
}
