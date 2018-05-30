package it.polimi.ingsw.Game;

import java.io.Serializable;
import java.util.Random;

public class Bridge implements Serializable {
    private Colour colour;
    private int serialNumber;
    private Scheme scheme;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public Bridge (int serialNumber){
        switch (serialNumber){
            case 1:
                this.colour=Colour.ANSI_RED;
                this.serialNumber=1;
                break;
            case 2:
                this.colour=Colour.ANSI_PURPLE;
                this.serialNumber=2;
                break;
            case 3:
                this.colour=Colour.ANSI_BLUE;
                this.serialNumber=3;
                break;
            case 4:
                this.colour=Colour.ANSI_GREEN;
                this.serialNumber=4;
                break;
            default:
                break;
        }

    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public Colour getColour() {
        return colour;
    }
    public int getSerialNumber() {
        return serialNumber;
    }
    public Scheme getScheme() {
        return scheme;
    }
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public Bridge[] getRndBridges(int numPlayer) {
        Random rnd = new Random();
        Bridge[] bridges = new Bridge[numPlayer];
        int index[] = new int[4];
        index[0] = rnd.nextInt(4) + 1;
        index[1] = rnd.nextInt(4) + 1;
        index[2] = rnd.nextInt(4) + 1;
        index[3] = rnd.nextInt(4) + 1;
        while (index[0] == index[1] || index[1] == index[2] || index[0] == index[2] || index[0] == index[3] || index[1] == index[3] || index[3] == index[2]) {
            index[0] = rnd.nextInt(4) + 1;
            index[1] = rnd.nextInt(4) + 1;
            index[2] = rnd.nextInt(4) + 1;
            index[3] = rnd.nextInt(4) + 1;
        }
        for (int i = 0; i < numPlayer; i++) {
            bridges[i] = new Bridge(index[i]);
        }
        return bridges;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    @Override
    public String toString() {
        String s=new String();
        s=s+colour.escape()+"VETRATA"+Colour.RESET+"\n";
        if(scheme!=null)
            s=s+scheme.toString();
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
