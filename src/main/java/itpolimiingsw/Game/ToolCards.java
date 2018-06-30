package itpolimiingsw.Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

public class ToolCards implements Serializable {
    private String name;
    private Colour colour;
    private String description1;
    private String description2;
    private int serialNumber;
    private int cost;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public ToolCards(int serialNumber) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/JsonFile/toolCards.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        ArrayList<ToolCards> toolCardsFromJson = gson.fromJson(reader, new TypeToken<ArrayList<ToolCards>>() {
        }.getType());


        if (serialNumber < 0 || serialNumber > 12) {
            this.name = toolCardsFromJson.get(0).getName();
            this.colour = toolCardsFromJson.get(0).getColour();
            this.description1 = toolCardsFromJson.get(0).getDescription1();
            this.description2 = toolCardsFromJson.get(0).getDescription2();
            this.serialNumber=toolCardsFromJson.get(0).getSerialNumber();
            this.cost=toolCardsFromJson.get(0).getCost();

        } else {
            this.name = toolCardsFromJson.get(serialNumber).getName();
            this.colour = toolCardsFromJson.get(serialNumber).getColour();
            this.description1 = toolCardsFromJson.get(serialNumber).getDescription1();
            this.description2 = toolCardsFromJson.get(serialNumber).getDescription2();
            this.serialNumber=toolCardsFromJson.get(serialNumber).getSerialNumber();
            this.cost=toolCardsFromJson.get(serialNumber).getCost();
        }

    }


    //-----------------------------------------------Getters and setters------------------------------------------------
    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }
    public Colour getColour() {
        return colour;
    }
    public String getDescription1() {
        return description1;
    }
    public String getDescription2() {
        return description2;
    }
    public int getSerialNumber() {
        return serialNumber;
    }
    public int getCost(){
        return cost;
    }


    //-----------------------------------------------Print methods------------------------------------------------------
    @Override
    public String toString() {
        String s=new String();
        if(colour!=null)
            s=colour.escape()+"[]"+Colour.RESET;
        else
            s=s+"[?]";
        s=s+name+"\t"+serialNumber+"\tCosto:"+cost+"\n  "+description1+". ";
        if(description2!=null)
            s=s+description2+"\n";
        else
            s=s+"\n";
        return s;
    }
    public void dump(){
        System.out.println(this);
    }



}
