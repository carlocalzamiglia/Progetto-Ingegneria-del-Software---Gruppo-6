package itpolimiingsw;


import itpolimiingsw.GameItems.Box;
import itpolimiingsw.GameItems.Colour;
import itpolimiingsw.GameItems.Dice;
import itpolimiingsw.GameCards.Scheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBox {
    @Test
    public void testRestrictionValueTrue(){
        Box box = new Box("4");
        box.setRestrictionValue("4");
        assertEquals("4", box.getRestrictionValue());
    }


    @Test
    public void testRestrictionColorTrue(){
        Colour colour = Colour.ANSI_RED;
        Box box = new Box(colour);
        box.setRestrictionColour(Colour.ANSI_RED);
        assertEquals(Colour.ANSI_RED, box.getRestrictionColour());
    }

    @Test
    public void testGetAddedDiceTrue(){
        Dice dice = new Dice(Colour.ANSI_RED);
        Scheme scheme = new Scheme(1);
        scheme.setBoxes(dice, 0, 0);
        Box box = scheme.getBox(0, 0);
        box.dump();
        box.toString();
        assertEquals(dice, box.getAddedDice());
    }

}
