package itpolimiingsw.GameCards;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import itpolimiingsw.GameItems.Colour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

public class PrivateGoal implements Serializable {
    private String name;
    private String description;
    private int serialNumber;
    private Colour colour;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public PrivateGoal(int serialNumber) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/JsonFile/privateGoals.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        ArrayList<PrivateGoal> privateGoalsFromJson = gson.fromJson(reader, new TypeToken<ArrayList<PrivateGoal>>() {
        }.getType());
        if (serialNumber < 0 || serialNumber > 5) {
            this.name = privateGoalsFromJson.get(0).getName();
            this.description = privateGoalsFromJson.get(0).getDescription();
            this.serialNumber = privateGoalsFromJson.get(0).getSerialNumber();
            this.colour=privateGoalsFromJson.get(0).getColour();
        } else {
            this.name = privateGoalsFromJson.get(serialNumber).getName();
            this.description = privateGoalsFromJson.get(serialNumber).getDescription();
            this.serialNumber = privateGoalsFromJson.get(serialNumber).getSerialNumber();
            this.colour=privateGoalsFromJson.get(serialNumber).getColour();

        }

    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getSerialNumber() {
        return serialNumber;
    }
    public Colour getColour() {
        return colour;
    }


    //-----------------------------------------------Print methods------------------------------------------------------

    @Override
    public String toString() {
        String s;
        s=name.concat(" • Privata");
        s=s.concat("\n");
        s=s.concat(description);
        return s;
    }
    public void dump(){
        System.out.println(this);
    }

}
