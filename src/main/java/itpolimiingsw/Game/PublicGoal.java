package itpolimiingsw.Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

public class PublicGoal implements Serializable {
    private String name;
    private String description;
    private String points;
    private int serialNumber;


    //-----------------------------------------------Constructor--------------------------------------------------------

    public PublicGoal (int serialNumber){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/main/resources/JsonFile/publicGoals.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        ArrayList<PublicGoal> publicGoalsFromJson = gson.fromJson(reader, new TypeToken<ArrayList<PublicGoal>>() {
        }.getType());
        if(serialNumber<0 || serialNumber>10){
            this.name= publicGoalsFromJson.get(0).getName();
            this.description= publicGoalsFromJson.get(0).getDescription();
            this.points=publicGoalsFromJson.get(0).getPoints();
            this.serialNumber= publicGoalsFromJson.get(0).getSerialNumber();
        }
        else{
            this.name= publicGoalsFromJson.get(serialNumber).getName();
            this.description= publicGoalsFromJson.get(serialNumber).getDescription();
            this.points=publicGoalsFromJson.get(serialNumber).getPoints();
            this.serialNumber= publicGoalsFromJson.get(serialNumber).getSerialNumber();
        }
    }

    //-----------------------------------------------Getter-------------------------------------------------------------
    public int getSerialNumber() {
        return serialNumber;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getPoints() {
        return points;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    public String toString() {
        String s="punti: "+points+"\t"+serialNumber+" "+name+"\tObbiettivo: "+description+"\n";
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
