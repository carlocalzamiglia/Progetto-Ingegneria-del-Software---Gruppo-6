package itpolimiingsw.Game;

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
        setColour(colour);
    }
    public String[] getFaces(){
        return faces;
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


    /**
     * Method that sets a casual face to a dice
     */
    public void roll(){
        int count = faces.length;
        Random rand = new Random();
        int index = rand.nextInt(count);
        setFace(faces[index]);
    }

    /**
     * this method return the string of the numer of the dice
     *
     * @return the string with the number of the dice's face
     */
    public String faceToNo(){
        String s = null;
        for (int i=0;i<faces.length;i++)
            if (face.equals(faces[i]))
                s=""+(i+1)+"";
        return s;
    }

    /**
     * this method return the color written in the italian language
     * @return the string with the italian colour
     */
    public String getItalianColour(){
        if(this.colour.equals(Colour.ANSI_BLUE))
            return "BLU";
        else if(this.colour.equals(Colour.ANSI_YELLOW))
            return "GIALLO";
        else if(this.colour.equals(Colour.ANSI_GREEN))
            return "VERDE";
        else if(this.colour.equals(Colour.ANSI_RED))
            return "ROSSO";
        else
            return "VIOLA";
    }

}
