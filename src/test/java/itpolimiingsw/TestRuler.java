package itpolimiingsw;


import itpolimiingsw.Game.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRuler {
    @Test
    public void  testPlacementIncorrect(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(false, ruler.checkCorrectPlacement(0, 0, dice, scheme));
    }

    @Test
    public void testPlacementCorrect(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2683");
        Dice dice1 = new Dice(Colour.ANSI_GREEN);
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(true, ruler.checkCorrectPlacement(0, 0, dice, scheme));
        scheme.setBoxes(dice, 0, 0);
        assertEquals(true, ruler.checkCorrectPlacement(1, 1, dice1, scheme));
    }

    @Test
    public void  testPlacementInCenter(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(false, ruler.checkCorrectPlacement(1, 1, dice, scheme));
    }

    @Test
    public void testAvailableTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");

        Dice dice1 = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2681");

        Dice dice2 = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2682");

        Scheme scheme = new Scheme(1);

        Ruler ruler = new Ruler();

        GreenCarpet greenCarpet = new GreenCarpet(1);

        greenCarpet.setStock(1);
        greenCarpet.setDiceInStock(dice);
        greenCarpet.setDiceInStock(dice1);
        greenCarpet.setDiceInStock(dice2);
        assertEquals(true, ruler.checkAvailable(greenCarpet, scheme));
    }

    @Test
    public void testAvailableFalse(){
        Scheme scheme = new Scheme(1);

        Ruler ruler = new Ruler();

        GreenCarpet greenCarpet = new GreenCarpet(1);

        greenCarpet.setStock(0);

        assertEquals(false, ruler.checkAvailable(greenCarpet, scheme));
    }

    @Test
    public void TestAvailableDiceTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(true, ruler.checkAvailableDice(dice, scheme));
    }

    @Test
    public void  testPlacementIncorrectBox(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(false, ruler.checkCorrectPlacement(0, 4, dice, scheme));
    }

    @Test
    public void  testNeighborsTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(true, ruler.checkEmptyNeighbors(0, 0, scheme));
    }

    @Test
    public void  testNeighborsFalse(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        scheme.setBoxes(dice1, 0, 1);
        assertEquals(false, ruler.checkEmptyNeighbors(0, 0, scheme));
    }
    @Test
    public void  testDiagonallyNeighborsTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(true, ruler.checkCorrectPlacement(1, 1,dice, scheme));
    }
    @Test
    public void  testDiagonallyNeighborsFalse(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(false, ruler.checkEmptyNeighbors(1, 1, scheme));
    }

    @Test
    public void  testNeighborsFaceFalse(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice1.setFace("\u2684");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        scheme.dump();
        assertEquals(false, ruler.checkNeighborsFaces(0, 1, dice1, scheme));
    }

    @Test
    public void  testNeighborsFaceTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice1.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(true, ruler.checkNeighborsFaces(0, 1, dice1, scheme));
    }

    @Test
    public void  testNeighborsColourFalse(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_BLUE);
        dice1.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(false, ruler.checkNeighborsColours(0, 1, dice1, scheme));
    }

    @Test
    public void  testNeighborsColourTrue(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice1.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        assertEquals(true, ruler.checkNeighborsColours(0, 1, dice1, scheme));
    }

    @Test
    public void  testDiceNumberMulti(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice1.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        scheme.setBoxes(dice, 0, 0);
        scheme.setBoxes(dice1, 0, 1);
        assertEquals(2, ruler.schemeCount(scheme));
    }

    @Test
    public void  testDiceNumberZero(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Dice dice1 = new Dice(Colour.ANSI_YELLOW);
        dice1.setFace("\u2683");
        Scheme scheme = new Scheme(1);
        Ruler ruler = new Ruler();
        assertEquals(0, ruler.schemeCount(scheme));
    }

    @Test
    public void  stringToIntTest(){
        Dice dice = new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2684");
        Ruler ruler = new Ruler();
        assertEquals(5, ruler.stringtoInt(dice.getFace()));
    }

    @Test
    public void  intToString(){
        Ruler ruler = new Ruler();
        assertEquals("\u2684", ruler.intToString(5));
    }
}
