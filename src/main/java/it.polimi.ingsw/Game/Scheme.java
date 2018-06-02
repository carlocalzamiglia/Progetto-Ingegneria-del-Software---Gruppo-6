package it.polimi.ingsw.Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Scheme implements Serializable {
    private Box boxes[][];
    private String name;
    private int difficulty;
    private Boolean used;



    //-----------------------------------------------Constructor--------------------------------------------------------
    public Scheme (int serialNumber){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/main/resources/JsonFile/schemes.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        ArrayList<Scheme> schemesFromJson = gson.fromJson(reader, new TypeToken<ArrayList<Scheme>>() {
        }.getType());
        if(serialNumber<0 || serialNumber>24){
            this.boxes= schemesFromJson.get(0).getBoxes();
            this.name= schemesFromJson.get(0).getName();
            this.difficulty= schemesFromJson.get(0).getDifficulty();
        }
        else{
            this.boxes= schemesFromJson.get(serialNumber).getBoxes();
            this.name= schemesFromJson.get(serialNumber).getName();
            this.difficulty= schemesFromJson.get(serialNumber).getDifficulty();
        }

    }

    //-----------------------------------------------Getters and setters------------------------------------------------
    public String getName() {
        return name;
    }
    public int getDifficulty(){
        return this.difficulty;
    }
    public Box[][] getBoxes() {
        return boxes;
    }
    public Box getBox(int i, int j) {
        return boxes[i][j];
    }


    //-----------------------------------------------Method that place a dice in the scheme-----------------------------
    public void setBoxes(Dice dice,int row,int column) {
        this.boxes[row][column].setAddedDice(dice);
    }

    //-----------------------------------------------Method that checks if a scheme is empty----------------------------
    public boolean isEmpty(){
        boolean status= true;
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 5; j++) {
                if (this.getBox(i, j).getAddedDice() != null)
                    status= false;
            }
        }
        return status;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    public String toString() {
        String s=new String();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 5; j++) {
                s=s.concat(boxes[i][j].toString());
            }
            s=s.concat("\n");
        }
        s=s.concat(name+" ");
        for(int i=0;i<difficulty;i++){
            s=s+"•";
        }
        return s;
    }
    public void dump(){
        System.out.println(this);
    }



    //-----------------------------------------------old costructor------------------------------------------------------
    /*public Scheme (int i){
        boxes=new Box[4][5];
        switch (i) {
            case 1:
                boxes[0][0] = new Box("4");
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("2");
                boxes[0][3] = new Box("5");
                boxes[0][4] = new Box(Colour.ANSI_GREEN);
                boxes[1][0] = new Box();
                boxes[1][1] = new Box();
                boxes[1][2] = new Box("6");
                boxes[1][3] = new Box(Colour.ANSI_GREEN);
                boxes[1][4] = new Box("2");
                boxes[2][0] = new Box();
                boxes[2][1] = new Box("3");
                boxes[2][2] = new Box(Colour.ANSI_GREEN);
                boxes[2][3] = new Box("4");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box("5");
                boxes[3][1] = new Box(Colour.ANSI_GREEN);
                boxes[3][2] = new Box("1");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Virtus";
                this.difficulty=5;
                break;
            case 2:
                boxes[0][0] = new Box(Colour.ANSI_YELLOW);
                boxes[0][1] = new Box(Colour.ANSI_BLUE);
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("1");
                boxes[1][0] = new Box(Colour.ANSI_GREEN);
                boxes[1][1] = new Box();
                boxes[1][2] = new Box("5");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box("4");
                boxes[2][0] = new Box("3");
                boxes[2][1] = new Box();
                boxes[2][2] = new Box(Colour.ANSI_RED);
                boxes[2][3] = new Box();
                boxes[2][4] = new Box(Colour.ANSI_GREEN);
                boxes[3][0] = new Box("2");
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box(Colour.ANSI_BLUE);
                boxes[3][4] = new Box(Colour.ANSI_YELLOW);
                this.name="Kaleidoscopie Dream";
                this.difficulty=4;
                break;
            case 3:
                boxes[0][0] = new Box(Colour.ANSI_YELLOW);
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("6");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("1");
                boxes[1][2] = new Box("5");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box("2");
                boxes[2][0] = new Box("3");
                boxes[2][1] = new Box(Colour.ANSI_YELLOW);
                boxes[2][2] = new Box(Colour.ANSI_RED);
                boxes[2][3] = new Box(Colour.ANSI_PURPLE);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box("4");
                boxes[3][3] = new Box("3");
                boxes[3][4] = new Box(Colour.ANSI_RED);
                this.name="Via Lux";
                this.difficulty=4;
                break;
            case 4:
                boxes[0][0] = new Box("5");
                boxes[0][1] = new Box(Colour.ANSI_GREEN);
                boxes[0][2] = new Box(Colour.ANSI_BLUE);
                boxes[0][3] = new Box(Colour.ANSI_PURPLE);
                boxes[0][4] = new Box("2");
                boxes[1][0] = new Box(Colour.ANSI_PURPLE);
                boxes[1][1] = new Box();
                boxes[1][2] = new Box();
                boxes[1][3] = new Box();
                boxes[1][4] = new Box(Colour.ANSI_YELLOW);
                boxes[2][0] = new Box(Colour.ANSI_YELLOW);
                boxes[2][1] = new Box();
                boxes[2][2] = new Box("6");
                boxes[2][3] = new Box();
                boxes[2][4] = new Box(Colour.ANSI_PURPLE);
                boxes[3][0] = new Box("1");
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box(Colour.ANSI_GREEN);
                boxes[3][4] = new Box("4");
                this.name="Aurorae Magnificus";
                this.difficulty=5;
                break;
            case 5:
                boxes[0][0] = new Box(Colour.ANSI_BLUE);
                boxes[0][1] = new Box("6");
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box(Colour.ANSI_YELLOW);
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("3");
                boxes[1][2] = new Box(Colour.ANSI_BLUE);
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box("5");
                boxes[2][2] = new Box("6");
                boxes[2][3] = new Box("2");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box("4");
                boxes[3][2] = new Box();
                boxes[3][3] = new Box("1");
                boxes[3][4] = new Box(Colour.ANSI_GREEN);
                this.name="Bellesguard";
                this.difficulty=3;
                break;
            case 6:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box(Colour.ANSI_BLUE);
                boxes[0][2] = new Box("2");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box(Colour.ANSI_YELLOW);
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("4");
                boxes[1][2] = new Box();
                boxes[1][3] = new Box(Colour.ANSI_RED);
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box("5");
                boxes[2][3] = new Box(Colour.ANSI_YELLOW);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box(Colour.ANSI_GREEN);
                boxes[3][1] = new Box("3");
                boxes[3][2] = new Box();
                boxes[3][3] = new Box();
                boxes[3][4] = new Box(Colour.ANSI_PURPLE);
                this.name="Sun Catcher";
                this.difficulty=3;
                break;
            case 7:
                boxes[0][0] = new Box("2");
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("5");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("1");
                boxes[1][0] = new Box(Colour.ANSI_YELLOW);
                boxes[1][1] = new Box("6");
                boxes[1][2] = new Box(Colour.ANSI_PURPLE);
                boxes[1][3] = new Box("2");
                boxes[1][4] = new Box(Colour.ANSI_RED);
                boxes[2][0] = new Box();
                boxes[2][1] = new Box(Colour.ANSI_BLUE);
                boxes[2][2] = new Box("4");
                boxes[2][3] = new Box(Colour.ANSI_GREEN);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box("3");
                boxes[3][2] = new Box();
                boxes[3][3] = new Box("5");
                boxes[3][4] = new Box();
                this.name="Symphony of Light";
                this.difficulty=6;
                break;
            case 8:
                boxes[0][0] = new Box(Colour.ANSI_PURPLE);
                boxes[0][1] = new Box("6");
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("3");
                boxes[1][0] = new Box("5");
                boxes[1][1] = new Box(Colour.ANSI_PURPLE);
                boxes[1][2] = new Box("3");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box("2");
                boxes[2][2] = new Box(Colour.ANSI_PURPLE);
                boxes[2][3] = new Box("1");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box("1");
                boxes[3][2] = new Box("5");
                boxes[3][3] = new Box(Colour.ANSI_PURPLE);
                boxes[3][4] = new Box("4");
                this.name="Firmitas";
                this.difficulty=5;
                break;
            case 9:
                boxes[0][0] = new Box("1");
                boxes[0][1] = new Box(Colour.ANSI_RED);
                boxes[0][2] = new Box("3");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("6");
                boxes[1][0] = new Box("5");
                boxes[1][1] = new Box("4");
                boxes[1][2] = new Box(Colour.ANSI_RED);
                boxes[1][3] = new Box("2");
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box("5");
                boxes[2][3] = new Box(Colour.ANSI_RED);
                boxes[2][4] = new Box("1");
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box("3");
                boxes[3][4] = new Box(Colour.ANSI_RED);
                this.name="Industria";
                this.difficulty=5;
                break;
            case 10:
                boxes[0][0] = new Box(Colour.ANSI_RED);
                boxes[0][1] = new Box();
                boxes[0][2] = new Box(Colour.ANSI_BLUE);
                boxes[0][3] = new Box();
                boxes[0][4] = new Box(Colour.ANSI_YELLOW);
                boxes[1][0] = new Box("4");
                boxes[1][1] = new Box(Colour.ANSI_PURPLE);
                boxes[1][2] = new Box("3");
                boxes[1][3] = new Box(Colour.ANSI_GREEN);
                boxes[1][4] = new Box("2");
                boxes[2][0] = new Box();
                boxes[2][1] = new Box("1");
                boxes[2][2] = new Box();
                boxes[2][3] = new Box("5");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box("6");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Aurora Sagradis";
                this.difficulty=4;
                break;
            case 11:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("6");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("5");
                boxes[1][2] = new Box(Colour.ANSI_BLUE);
                boxes[1][3] = new Box("4");
                boxes[1][4] = new Box();
                boxes[2][0] = new Box("3");
                boxes[2][1] = new Box(Colour.ANSI_GREEN);
                boxes[2][2] = new Box(Colour.ANSI_YELLOW);
                boxes[2][3] = new Box(Colour.ANSI_PURPLE);
                boxes[2][4] = new Box("2");
                boxes[3][0] = new Box("1");
                boxes[3][1] = new Box("4");
                boxes[3][2] = new Box(Colour.ANSI_RED);
                boxes[3][3] = new Box("5");
                boxes[3][4] = new Box("3");
                this.name="Batllo";
                this.difficulty=5;
                break;
            case 12:
                boxes[0][0] = new Box("6");
                boxes[0][1] = new Box(Colour.ANSI_PURPLE);
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("5");
                boxes[1][0] = new Box("5");
                boxes[1][1] = new Box();
                boxes[1][2] = new Box(Colour.ANSI_PURPLE);
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box(Colour.ANSI_RED);
                boxes[2][1] = new Box("6");
                boxes[2][2] = new Box();
                boxes[2][3] = new Box(Colour.ANSI_PURPLE);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box(Colour.ANSI_YELLOW);
                boxes[3][1] = new Box(Colour.ANSI_RED);
                boxes[3][2] = new Box("5");
                boxes[3][3] = new Box("4");
                boxes[3][4] = new Box("3");
                this.name="Shadow Thief";
                this.difficulty=5;
                break;
            case 13:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box("4");
                boxes[0][2] = new Box();
                boxes[0][3] = new Box(Colour.ANSI_YELLOW);
                boxes[0][4] = new Box("6");
                boxes[1][0] = new Box(Colour.ANSI_RED);
                boxes[1][1] = new Box();
                boxes[1][2] = new Box("2");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box(Colour.ANSI_RED);
                boxes[2][3] = new Box(Colour.ANSI_PURPLE);
                boxes[2][4] = new Box("1");
                boxes[3][0] = new Box(Colour.ANSI_BLUE);
                boxes[3][1] = new Box(Colour.ANSI_YELLOW);
                boxes[3][2] = new Box();
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Fractal Drops";
                this.difficulty=3;
                break;
            case 14:
                boxes[0][0] = new Box("1");
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("3");
                boxes[0][3] = new Box(Colour.ANSI_BLUE);
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("2");
                boxes[1][2] = new Box(Colour.ANSI_BLUE);
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box("6");
                boxes[2][1] = new Box(Colour.ANSI_BLUE);
                boxes[2][2] = new Box();
                boxes[2][3] = new Box("4");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box(Colour.ANSI_BLUE);
                boxes[3][1] = new Box("5");
                boxes[3][2] = new Box("2");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box("1");
                this.name="Gravitas";
                this.difficulty=5;
                break;
            case 15:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box(Colour.ANSI_GREEN);
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box("2");
                boxes[1][1] = new Box(Colour.ANSI_YELLOW);
                boxes[1][2] = new Box("5");
                boxes[1][3] = new Box(Colour.ANSI_BLUE);
                boxes[1][4] = new Box("1");
                boxes[2][0] = new Box();
                boxes[2][1] = new Box(Colour.ANSI_RED);
                boxes[2][2] = new Box("3");
                boxes[2][3] = new Box(Colour.ANSI_PURPLE);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box("1");
                boxes[3][1] = new Box();
                boxes[3][2] = new Box("6");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box("4");
                this.name="Chromatic Splendor";
                this.difficulty=4;
                break;
            case 16:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box("1");
                boxes[0][2] = new Box(Colour.ANSI_GREEN);
                boxes[0][3] = new Box(Colour.ANSI_PURPLE);
                boxes[0][4] = new Box("4");
                boxes[1][0] = new Box("6");
                boxes[1][1] = new Box(Colour.ANSI_PURPLE);
                boxes[1][2] = new Box("2");
                boxes[1][3] = new Box("5");
                boxes[1][4] = new Box(Colour.ANSI_GREEN);
                boxes[2][0] = new Box("1");
                boxes[2][1] = new Box(Colour.ANSI_GREEN);
                boxes[2][2] = new Box("5");
                boxes[2][3] = new Box("3");
                boxes[2][4] = new Box(Colour.ANSI_PURPLE);
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Lux Astram";
                this.difficulty=5;
                break;
            case 17:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box(Colour.ANSI_RED);
                boxes[0][3] = new Box("5");
                boxes[0][4] = new Box();
                boxes[1][0] = new Box(Colour.ANSI_PURPLE);
                boxes[1][1] = new Box("4");
                boxes[1][2] = new Box();
                boxes[1][3] = new Box(Colour.ANSI_GREEN);
                boxes[1][4] = new Box("3");
                boxes[2][0] = new Box("6");
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box(Colour.ANSI_BLUE);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box(Colour.ANSI_YELLOW);
                boxes[3][2] = new Box("2");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Luz Celestial";
                this.difficulty=3;
                break;
            case 18:
                boxes[0][0] = new Box("3");
                boxes[0][1] = new Box("4");
                boxes[0][2] = new Box("1");
                boxes[0][3] = new Box("5");
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("6");
                boxes[1][2] = new Box("2");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box(Colour.ANSI_YELLOW);
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box(Colour.ANSI_YELLOW);
                boxes[2][4] = new Box(Colour.ANSI_RED);
                boxes[3][0] = new Box("5");
                boxes[3][1] = new Box();
                boxes[3][2] = new Box(Colour.ANSI_YELLOW);
                boxes[3][3] = new Box(Colour.ANSI_RED);
                boxes[3][4] = new Box("6");
                this.name="Firelight";
                this.difficulty=5;
                break;
            case 19:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box();
                boxes[0][3] = new Box(Colour.ANSI_RED);
                boxes[0][4] = new Box("5");
                boxes[1][0] = new Box();
                boxes[1][1] = new Box();
                boxes[1][2] = new Box(Colour.ANSI_PURPLE);
                boxes[1][3] = new Box("4");
                boxes[1][4] = new Box(Colour.ANSI_BLUE);
                boxes[2][0] = new Box();
                boxes[2][1] = new Box(Colour.ANSI_BLUE);
                boxes[2][2] = new Box("3");
                boxes[2][3] = new Box(Colour.ANSI_YELLOW);
                boxes[2][4] = new Box("6");
                boxes[3][0] = new Box(Colour.ANSI_YELLOW);
                boxes[3][1] = new Box("2");
                boxes[3][2] = new Box(Colour.ANSI_GREEN);
                boxes[3][3] = new Box("1");
                boxes[3][4] = new Box(Colour.ANSI_RED);
                this.name="Ripples of Light";
                this.difficulty=5;
                break;
            case 20:
                boxes[0][0] = new Box("6");
                boxes[0][1] = new Box(Colour.ANSI_BLUE);
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("1");
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("5");
                boxes[1][2] = new Box(Colour.ANSI_BLUE);
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box("4");
                boxes[2][1] = new Box(Colour.ANSI_RED);
                boxes[2][2] = new Box("2");
                boxes[2][3] = new Box(Colour.ANSI_BLUE);
                boxes[2][4] = new Box();
                boxes[3][0] = new Box(Colour.ANSI_GREEN);
                boxes[3][1] = new Box("6");
                boxes[3][2] = new Box(Colour.ANSI_YELLOW);
                boxes[3][3] = new Box("3");
                boxes[3][4] = new Box(Colour.ANSI_PURPLE);
                this.name="Water of Life";
                this.difficulty=6;
                break;
            case 21:
                boxes[0][0] = new Box(Colour.ANSI_YELLOW);
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("2");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("6");
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("4");
                boxes[1][2] = new Box();
                boxes[1][3] = new Box("5");
                boxes[1][4] = new Box(Colour.ANSI_YELLOW);
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box(Colour.ANSI_YELLOW);
                boxes[2][4] = new Box("5");
                boxes[3][0] = new Box("1");
                boxes[3][1] = new Box("2");
                boxes[3][2] = new Box(Colour.ANSI_YELLOW);
                boxes[3][3] = new Box("3");
                boxes[3][4] = new Box();
                this.name="Comitas";
                this.difficulty=5;
                break;
            case 22:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("1");
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box("1");
                boxes[1][1] = new Box(Colour.ANSI_GREEN);
                boxes[1][2] = new Box("3");
                boxes[1][3] = new Box(Colour.ANSI_BLUE);
                boxes[1][4] = new Box("2");
                boxes[2][0] = new Box(Colour.ANSI_BLUE);
                boxes[2][1] = new Box("5");
                boxes[2][2] = new Box("4");
                boxes[2][3] = new Box("6");
                boxes[2][4] = new Box(Colour.ANSI_GREEN);
                boxes[3][0] = new Box();
                boxes[3][1] = new Box(Colour.ANSI_BLUE);
                boxes[3][2] = new Box("5");
                boxes[3][3] = new Box(Colour.ANSI_GREEN);
                boxes[3][4] = new Box();
                this.name="Lux Mundi";
                this.difficulty=6;
                break;
            case 23:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box(Colour.ANSI_BLUE);
                boxes[0][2] = new Box(Colour.ANSI_RED);
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box("4");
                boxes[1][2] = new Box("5");
                boxes[1][3] = new Box();
                boxes[1][4] = new Box(Colour.ANSI_BLUE);
                boxes[2][0] = new Box(Colour.ANSI_BLUE);
                boxes[2][1] = new Box("2");
                boxes[2][2] = new Box();
                boxes[2][3] = new Box(Colour.ANSI_RED);
                boxes[2][4] = new Box("5");
                boxes[3][0] = new Box("6");
                boxes[3][1] = new Box(Colour.ANSI_RED);
                boxes[3][2] = new Box("3");
                boxes[3][3] = new Box("1");
                boxes[3][4] = new Box();
                this.name="Fulgor del Cielo";
                this.difficulty=5;
                break;
            case 24:
                boxes[0][0] = new Box("1");
                boxes[0][1] = new Box(Colour.ANSI_PURPLE);
                boxes[0][2] = new Box(Colour.ANSI_YELLOW);
                boxes[0][3] = new Box();
                boxes[0][4] = new Box("4");
                boxes[1][0] = new Box(Colour.ANSI_PURPLE);
                boxes[1][1] = new Box(Colour.ANSI_YELLOW);
                boxes[1][2] = new Box();
                boxes[1][3] = new Box();
                boxes[1][4] = new Box("6");
                boxes[2][0] = new Box(Colour.ANSI_YELLOW);
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box("5");
                boxes[2][4] = new Box("3");
                boxes[3][0] = new Box();
                boxes[3][1] = new Box("5");
                boxes[3][2] = new Box("4");
                boxes[3][3] = new Box("2");
                boxes[3][4] = new Box("1");
                this.name="Sun's Glory";
                this.difficulty=6;
                break;
            case 0:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box();
                boxes[1][2] = new Box();
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box();
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.name="Empty";
                this.difficulty=0;
                break;
            default:
                break;
        }

    }*/

}
