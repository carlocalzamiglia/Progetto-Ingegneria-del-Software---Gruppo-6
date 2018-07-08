package itpolimiingsw.GameCards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import itpolimiingsw.GameItems.Box;
import itpolimiingsw.GameItems.Dice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

public class Scheme implements Serializable {
    private Box boxes[][];
    private String name;
    private int difficulty;

    //-----------------------------------------------Constructor--------------------------------------------------------
    public Scheme(int serialNumber) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/JsonFile/schemes.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        ArrayList<Scheme> schemesFromJson = gson.fromJson(reader, new TypeToken<ArrayList<Scheme>>() {
        }.getType());


        if (serialNumber < 0 || serialNumber > 24) {
            this.boxes = schemesFromJson.get(0).getBoxes();
            this.name = schemesFromJson.get(0).getName();
            this.difficulty = schemesFromJson.get(0).getDifficulty();
        } else {
            this.boxes = schemesFromJson.get(serialNumber).getBoxes();
            this.name = schemesFromJson.get(serialNumber).getName();
            this.difficulty = schemesFromJson.get(serialNumber).getDifficulty();
        }

    }

    //-----------------------------------------------Getters and setters------------------------------------------------
    public String getName() {
        return name;
    }
    public int getDifficulty() {
        return this.difficulty;
    }
    public Box[][] getBoxes() {
        return boxes;
    }
    public Box getBox(int i, int j) {
        return boxes[i][j];
    }

    /**
     * Method that place a dice in the scheme
     *
     * @param dice the dice i want to add in the scheme
     * @param row the row of the scheme where i want to add the dice
     * @param column the coloum of the scheme where i want to add the dice
     */
    public void setBoxes(Dice dice, int row, int column) {
        this.boxes[row][column].setAddedDice(dice);
    }
    /**
     * Method that checks if a scheme is empty
     *
     * @return true if a scheme is empty, false otherwise
     */
    public boolean isEmpty() {
        boolean status = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.getBox(i, j).getAddedDice() != null)
                    status = false;
            }
        }
        return status;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    public String toString() {
        String s = new String();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                s = s.concat(boxes[i][j].toString());
            }
            s = s.concat("\n");
        }
        s = s.concat(name + " ");
        for (int i = 0; i < difficulty; i++) {
            s = s + "•";
        }
        return s;
    }
    public String difficultyToString(){
        String s=new String();
        for (int i = 0; i < difficulty; i++) {
            s = s + "•";
        }
        return s;
    }
    public void dump() {
        System.out.println(this);
    }


}