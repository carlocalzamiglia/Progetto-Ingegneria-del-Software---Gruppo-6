package it.polimi.ingsw.Game;
import java.io.Serializable;
import java.util.*;

public class Dice implements Serializable {
    private String face;
    private Colour colour;

    public static final String[] faces = {
            "\u2680",
            "\u2681",
            "\u2682",
            "\u2683",
            "\u2684",
            "\u2685"
    };
    //-----------------------------------------------Constructor--------------------------------------------------------
    public Dice(Colour colour){
        this.colour=colour;
    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
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

    //-----------------------------------------------Print methods------------------------------------------------------
    public String toString (){
        String escape= this.colour.escape();
        return escape+"["+face+"]"+Colour.RESET;
    }
    public void dump (){
        System.out.println(this);
    }

    //-----------------------------------------------Method that sets a casual face to a dice---------------------------
    public void roll(){
        int count = faces.length;
        Random rand = new Random();
        int index = rand.nextInt(count);
        this.face = faces[index];
    }

    public String faceToNo(){
        String s = null;

        if (face.equals("\u2680")) {
            s = "1";
        }
        if (face.equals("\u2681")) {
            s = "2";
        }
        if (face.equals("\u2682")) {
            s = "3";
        }
        if (face.equals("\u2683")) {
            s = "4";
        }
        if (face.equals("\u2684")) {
            s = "5";
        }
        if (face.equals("\u2685")) {
            s = "6";
        }
        return s;
    }

}
