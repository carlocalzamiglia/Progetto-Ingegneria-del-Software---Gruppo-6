package it.polimi.ingsw.Client.Game;
import java.util.*;

public class Dice {
    private String face;
    private Colour colour;

    public static final String[] faces = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6"
    };

    public Dice(Colour colour){
        this.colour=colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFace() {
        return face;
    }
    public String toString (){
        String escape= this.colour.escape();
        return escape+"["+face+"]"+Colour.RESET;
    }
    public void dump (){
        System.out.println(this);
    }
    public void roll(){
        int count = faces.length;
        Random rand = new Random();
        int index = rand.nextInt(count);
        this.face = faces[index];
    }

}
