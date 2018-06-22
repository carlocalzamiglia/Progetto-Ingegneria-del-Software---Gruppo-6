package it.polimi.ingsw.Game;

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


    //-----------------------------------------------Checks the serialnumber of a tool card-----------------------------
    public boolean checkSpecial(int serialNumber){
        if(serialNumber==1 || serialNumber==5 ||serialNumber==6 ||serialNumber==9 ||serialNumber==10 ||serialNumber==11){
            return true;
        }
        else
            return false;
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


    /*
    * public ToolCards (int serialNumber) {
        switch (serialNumber) {
            case 1:
                this.name = "Pinza Sgrossatrice";
                this.colour = Colour.ANSI_PURPLE;
                this.description1 = "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1";
                this.description2 = "Non puoi cambiare un 6 in 1 o un 1 in 6";
                this.serialNumber = 1;
                this.cost = 1;
                break;
            case 2:
                this.name = "Pennello per Eglomise";
                this.colour = Colour.ANSI_BLUE;
                this.description1 = "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore";
                this.description2 = "Devi rispettare tutte le altre restrizioni di piazzamento";
                this.serialNumber = 2;
                this.cost = 1;
                break;
            case 3:
                this.name = "Alesatore per lamina di rame";
                this.colour = Colour.ANSI_RED;
                this.description1 = "Muovi un qualsiasi dado nella tua vetrata ignorando le restizioni di valore";
                this.description2 = "Devi rispettare tutte le altre restrizioni di piazzamento";
                this.serialNumber = 3;
                this.cost = 1;
                break;
            case 4:
                this.name = "Lathekin";
                this.colour = Colour.ANSI_YELLOW;
                this.description1 = "Muovi esattamente due dadi rispettando tutte le restrizioni di piazzamento";
                this.description2 = null;
                this.serialNumber = 4;
                this.cost = 1;
                break;
            case 5:
                this.name = "Taglierina circolare";
                this.colour = Colour.ANSI_GREEN;
                this.description1 = "Dopo aver scelto un dado scambia quel dado con un dado sul tracciato dei Round";
                this.description2 = null;
                this.serialNumber = 5;
                this.cost = 1;
                break;
            case 6:
                this.name = "Pennello per Pasta Salda";
                this.colour = Colour.ANSI_PURPLE;
                this.description1 = "Dopo aver scelto un dado, tira nuovamente quel dado";
                this.description2 = "Se non puoi piazzarlo,riponilo nella Riserva";
                this.serialNumber = 6;
                this.cost = 1;
                break;
            case 7:
                this.name = "Martelletto";
                this.colour = Colour.ANSI_BLUE;
                this.description1 = "Tira nuovamente tutti i dadi della Riserva";
                this.description2 = "Questa carta può essere usata solo durante il tuo secondo turno, prima di scegliere il secondo dado";
                this.serialNumber = 7;
                this.cost = 1;
                break;
            case 8:
                this.name = "Tenaglia a Rotelle";
                this.colour = Colour.ANSI_RED;
                this.description1 = "Dopo il tuo primo turno scegli immediatamente un altro dado";
                this.description2 = "Salta il tuo secondo turno in questo round";
                this.serialNumber = 8;
                this.cost = 1;
                break;
            case 9:
                this.name = "Riga in Sughero";
                this.colour = Colour.ANSI_YELLOW;
                this.description1 = "Dopo aver scelto un dado piazzalo in una casella che non sia adiacente a un altro dado";
                this.description2 = "Devi rispettare tutte le restrizioni di piazzamento";
                this.serialNumber = 9;
                this.cost = 1;
                break;
            case 10:
                this.name = "Tampone Diamantato";
                this.colour = Colour.ANSI_GREEN;
                this.description1 = "Dopo aver scelto un dado, giralo sulla faccia opposta";
                this.description2 = "6 diventa 1, 5 diventa 2, 4 diventa 3 ecc.";
                this.serialNumber = 10;
                this.cost = 1;
                break;
            case 11:
                this.name = "Diluente per Pasta Salda";
                this.colour = Colour.ANSI_PURPLE;
                this.description1 = "Dopo aver scelto un dado, riponilo nel Sacchetto, poi pescane uno dal Sacchetto";
                this.description2 = "Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento";
                this.serialNumber = 11;
                this.cost = 1;
                break;
            case 12:
                this.name = "Taglierina Manuale";
                this.colour = Colour.ANSI_BLUE;
                this.description1 = "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round";
                this.description2 = "Devi rispettare tutte le restizioni di piazzamento";
                this.serialNumber = 12;
                this.cost = 1;
                break;
            case 13:
                this.name = "Strip Cutter";
                this.description1 = "Take one die from any player. Give them a die of matching Color or Value";
                this.description2 = "They may place it ignoring Color or Value Restrictions May only be used before Round 7";
                this.serialNumber = 13;
                this.cost = 1;
                break;
            default:
                break;
        }

    }*/
}
