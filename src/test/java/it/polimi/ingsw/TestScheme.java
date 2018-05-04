package it.polimi.ingsw;
import it.polimi.ingsw.Project.Game.Colour;
import it.polimi.ingsw.Project.Game.Dice;
import it.polimi.ingsw.Project.Game.Box;
import it.polimi.ingsw.Project.Game.Scheme;

public class TestScheme {
    @org.junit.jupiter.api.Test


    public void test() {

        Scheme scheme[]=new Scheme[24];
        for(int i=1;i<=24;i++){
            scheme[i-1]=new Scheme(i);
            scheme[i-1].dump();
            System.out.println();
        }
        Dice d=new Dice(Colour.ANSI_BLUE);
        d.roll();
        Box box = new Box();
        box.setAddedDice(d);
        scheme[0].setBoxes(box,1,0);
        scheme[0].dump();



    }
}

