package itpolimiingsw.Game;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.lang.*;

public enum Colour implements Serializable {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[36m"),
    ANSI_PURPLE("\u001B[35m");
    static final String RESET= "\u001B[0m";
    private String escape;


    Colour(String escape) {
        this.escape = escape;
    }
    public String escape() {
        return escape;
    }

    /**
     * this method get the paint color of the dice
     *
     * @return the Paint color of the dice
     */
    public Color getfxColor(){
        if(this.equals(ANSI_RED))
            return Color.RED;
        else if(this.equals(ANSI_GREEN))
            return Color.FORESTGREEN;
        else if(this.equals(ANSI_YELLOW))
            return Color.rgb(249, 235, 39);
        else if(this.equals(ANSI_BLUE))
            return Color.DODGERBLUE;
        else if(this.equals(ANSI_PURPLE))
            return Color.MEDIUMORCHID;
        return Color.RED;
    }
}
