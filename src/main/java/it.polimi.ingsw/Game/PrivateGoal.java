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

public class PrivateGoal implements Serializable {
    private String name;
    private String description;
    private int serialNumber;


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
        } else {
            this.name = privateGoalsFromJson.get(serialNumber).getName();
            this.description = privateGoalsFromJson.get(serialNumber).getDescription();
            this.serialNumber = privateGoalsFromJson.get(serialNumber).getSerialNumber();
        }

    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getSerialNumber() {
        return serialNumber;
    }



    //-----------------------------------------------Print methods------------------------------------------------------

    @Override
    public String toString() {
        String s=new String();
        s=name.concat(" • Privata");
        s=s.concat("\n");
        s=s.concat(description);
        return s;
    }
    public void dump(){
        System.out.println(this);
    }


    //-----------------------------------------------old constructor----------------------------------------------------

    /*public PrivateGoal (int serialNumber) {
        switch (serialNumber) {
            case 1:
                this.name = "Sfumature Rosse";
                this.description = "Somma dei valori su tutti i dadi rossi";
                this.serialNumber = 1;
                break;
            case 2:
                this.name = "Sfumature Gialle";
                this.description = "Somma dei valori su tutti i dadi gialli";
                this.serialNumber = 2;
                break;
            case 3:
                this.name = "Sfumature Verdi";
                this.description = "Somma dei valori su tutti i dadi verdi";
                this.serialNumber = 3;
                break;
            case 4:
                this.name = "Sfumature Blu";
                this.description = "Somma dei valori su tutti i dadi blu";
                this.serialNumber = 4;
                break;
            case 5:
                this.name = "Sfumature Viola";
                this.description = "Somma dei valori su tutti i dadi viola";
                this.serialNumber = 5;
                break;
            default:
                break;

        }

    }*/
}
