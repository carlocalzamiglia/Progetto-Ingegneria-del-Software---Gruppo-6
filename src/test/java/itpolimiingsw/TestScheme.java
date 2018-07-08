package itpolimiingsw;

import itpolimiingsw.GameCards.Scheme;
import itpolimiingsw.GameItems.*;

public class TestScheme {
    @org.junit.jupiter.api.Test


    public void tScheme() {

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
        Markers markers=new Markers();
        int i=markers.getValue();
    }
    @org.junit.jupiter.api.Test
    public void testBoxes(){
        Colour colour=Colour.ANSI_YELLOW;
        Box box1=new Box(colour);
        Dice dice=new Dice(colour);
        dice.roll();
        Box box2=new Box(dice.faceToNo());
        Box box=new Box();
        box.setRestrictionColour(colour);
        box1.setRestrictionColour(null);
        String s="3";
        box1.setRestrictionValue(s);
        box.dump();
        box1.dump();
        box2.dump();
    }


}

