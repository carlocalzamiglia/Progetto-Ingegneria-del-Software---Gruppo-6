package itpolimiingsw;

import itpolimiingsw.Game.Colour;
import itpolimiingsw.Game.Dice;
import itpolimiingsw.Game.Scheme;

public class TestScheme {
    @org.junit.jupiter.api.Test


    public void tScheme() {

        /*Dice dice = new Dice(Colour.ANSI_BLUE);

        Scheme scheme[]=new Scheme[24];
        for(int i=1;i<=24;i++){
            scheme[i-1]=new Scheme(i);
            scheme[i-1].dump();
            System.out.println();
        }
        Dice d=new Dice(Colour.ANSI_BLUE);
        d.roll();
        scheme[0].setBoxes(d,1,0);
        scheme[0].dump();*/

        for(int i=0;i<30;i++) {
            System.out.println("numero: "+i);
            Scheme scheme = new Scheme(i);
            scheme.dump();
        }
    }

    @org.junit.jupiter.api.Test
    public void tDiceOnScheme() {
        Scheme scheme=new Scheme(1);
        boolean bool=scheme.isEmpty();
        System.out.println(bool);
        Dice d=new Dice(Colour.ANSI_BLUE);
        d.roll();
        scheme.setBoxes(d,1,0);
        scheme.dump();
        scheme.setBoxes(null,1,0);
        scheme.dump();
        bool=scheme.isEmpty();
        System.out.println(bool);
    }
}
