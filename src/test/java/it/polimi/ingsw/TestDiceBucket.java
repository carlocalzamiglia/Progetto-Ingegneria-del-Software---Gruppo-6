package it.polimi.ingsw;

import it.polimi.ingsw.Client.Game.*;


import it.polimi.ingsw.Client.Game.Colour;
import it.polimi.ingsw.Client.Game.Dice;
import it.polimi.ingsw.Client.Game.DiceBucket;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
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
