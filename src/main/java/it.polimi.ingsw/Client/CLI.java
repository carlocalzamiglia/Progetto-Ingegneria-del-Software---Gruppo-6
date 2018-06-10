package it.polimi.ingsw.Client;

import it.polimi.ingsw.Game.Game;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Matches;
import it.polimi.ingsw.Game.Ruler;
import it.polimi.ingsw.Game.Scheme;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.Colour;
import it.polimi.ingsw.Game.ToolCardsExecutor;
import it.polimi.ingsw.ServertoClientHandler.ClientInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CLI implements ClientInterface {
    Gson gson = new GsonBuilder().create();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public String[] loginMessages() throws IOException {
        String[] logindata = new String[2];
        System.out.println("Inserire nickname:");
        logindata[0]=in.readLine();

        System.out.println("Inserire password:");
        logindata[1]=in.readLine();
        return logindata;
    }

    @Override
    public void showError(String message) {
        System.out.println("ERRORE: "+message);
    }

    @Override
    public int schemeMessages(String schemes) throws IOException {
        String message;
        try {
            do {
                System.out.println("Scegli uno schema:\n" + schemes);
                message = in.readLine();
            } while (Integer.parseInt(message) <= 0 || Integer.parseInt(message) > 4);
        }catch (NumberFormatException e){
            this.showError("Hai inserito un valore errato!");
            return schemeMessages(schemes);
        }
        System.out.println("Hai scelto lo schema "+message+". Ora attendi il tuo turno!");
        return Integer.parseInt(message);
    }

    @Override
    public void printCarpetFirst(String greenCarpetjson, String playerjson) {
        System.out.println("\n\n*************************** E' IL TUO TURNO ***************************");
        //System.out.println("Ecco lo schema degli altri giocatori, nell'ordine: "+ playersscheme);

        GreenCarpet greenCarpet=gson.fromJson(greenCarpetjson, GreenCarpet.class);
        Player player=gson.fromJson(playerjson, Player.class);

        System.out.println("Ecco qui il tavolo e il tuo schema:\n");
        System.out.println(greenCarpet.toString());
        System.out.println((greenCarpet.getRound()+1)+"° ROUND\t\t\t"+greenCarpet.getTurn()+"° TURNO\n");
        System.out.println(player.toString()+"\n");
    }

    @Override
    public String handleTurnMenu() throws IOException {
        String value = new String();
        System.out.println("1)passa il turno\n2)inserisci dado\n3)usa carta utensile\n");
            value = in.readLine();
        while(!value.equals("1")&& !value.equals("2")&& !value.equals("3")){
            showError("Hai inserito un valore errato");
            value=in.readLine();
        }
        return value;
    }

    @Override
    public void endTurn() {
        System.out.println("############################### IL TUO TURNO E' TERMINATO. ATTENDI. ###############################\n\n");
    }

    @Override
    public int[] placeDiceMessages() throws IOException {
        int[] dicecoord = new int[3];
        int[] coordinates;
        dicecoord[0] = chooseDice();
        System.out.println("Inserire le coordinate di piazzamento.");
        coordinates=chooseCoordinates();
        dicecoord[1] = coordinates[0];
        dicecoord[2] = coordinates[1];
        return dicecoord;
    }

    @Override
    public void schemeUpdated(String schemejson) {
        Scheme scheme= gson.fromJson(schemejson, Scheme.class);
        System.out.println("Ecco lo schema aggiornato:\n"+scheme.toString());
    }

    @Override
    public int chooseToolMessages() throws IOException {
        int tool = 0;
        try{
            System.out.println("Inserisci il numero della carta tool da usare.");
            tool=Integer.parseInt(in.readLine());
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseToolMessages();
        }
        return tool;
    }

    @Override
    public String goOnTool() throws IOException {
        String goon="a";
        while(!goon.equals("y") && !goon.equals("n")) {
            System.out.println("Per utilizzare la carta tool inserisci 'y'. Per tornare al menù precedente inserisci 'n'");
            goon = in.readLine();
        }
        return goon;
    }

    @Override
    public int chooseDice() throws IOException {
        int dice;
        try{
            System.out.println("Scegli il dado dalla riserva.");
            dice=Integer.parseInt(in.readLine());
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseDice();
        }
        return dice;
    }

    @Override
    public int[] chooseCoordinates() throws IOException {
        int[] coordinates = new int[2];
        try {
            System.out.println("Inserisci la riga.");
            coordinates[0] = Integer.parseInt(in.readLine());
            System.out.println("Inserisci la colonna.");
            coordinates[1] = Integer.parseInt(in.readLine());
        }catch(NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseCoordinates();
        }
        return coordinates;
    }

    @Override
    public int[] chooseFromPath() throws IOException {
        int[] coordinates = new int[2];
        try {
            System.out.println("Inserisci il numero del round.");
            coordinates[0] = Integer.parseInt(in.readLine());
            System.out.println("Inserisci la posizione del dado nel round (numero di riga).");
            coordinates[1] = Integer.parseInt(in.readLine());
        }catch(NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseCoordinates();
        }
        return coordinates;
    }

    @Override
    public int chooseValue() throws IOException {
        int dice;
        try{
            System.out.println("Scegli il valore da assegnare al dado.");
            dice = Integer.parseInt(in.readLine());
            while (dice<1 || dice>6) {
                System.out.println("Hai inserito un valore errato. Scegli il valore da assegnare al dado.");
                dice = Integer.parseInt(in.readLine());
            }
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseValue();
        }
        return dice;
    }

    @Override
    public int tool1Messages() throws IOException {
        String dicechose;
        System.out.println("Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
        dicechose=in.readLine();
        while(!(dicechose.equals("c")) && !(dicechose.equals("d"))){
            System.out.println("Scelta errata. Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
            dicechose=in.readLine();
        }
        if(dicechose.equals("c"))
            return 1;
        else
            return 2;
    }

    @Override
    public int[] tool23Messages() throws IOException {
        int[] allcoordinates = new int[4];
        int[] coordinates;
        System.out.println("Inserire le coordinate del dado da spostare.");
        coordinates=chooseCoordinates();
        allcoordinates[0]=coordinates[0];
        allcoordinates[1]=coordinates[1];
        System.out.println("Inserire le nuove coordinate del dado.");
        coordinates=chooseCoordinates();
        allcoordinates[2]=coordinates[0];
        allcoordinates[3]=coordinates[1];
        return allcoordinates;
    }

    @Override
    public int[] tool4Messages() throws IOException {
        int[] allcoordinates = new int[8];
        int[] coordinates;
        System.out.println("PRIMO DADO:\n");
        coordinates=tool23Messages();
        allcoordinates[0]=coordinates[0];
        allcoordinates[1]=coordinates[1];
        allcoordinates[2]=coordinates[2];
        allcoordinates[3]=coordinates[3];
        System.out.println("SECONDO DADO:\n");
        coordinates=tool23Messages();
        allcoordinates[4]=coordinates[0];
        allcoordinates[5]=coordinates[1];
        allcoordinates[6]=coordinates[2];
        allcoordinates[7]=coordinates[3];
        return allcoordinates;
    }

    @Override
    public int[] tool12Messages() throws IOException {
        int[] allcoordinates = new int[11];
        int[] round= chooseFromPath();
        int[] tmp;
        int choice;
        try{
            do{
                System.out.println("Inserisci il numero di dadi da spostare (1 o 2).");
                choice=Integer.parseInt(in.readLine());
            }while(choice!=1 && choice!=2);
        }catch (NumberFormatException e){
            System.out.println("Errore nell'inserimento.");
            return tool12Messages();
        }
        if(choice==1){
            tmp=tool23Messages();
            for (int k=4;k<8;k++)
                allcoordinates[k]=0;
        }else
            tmp =tool4Messages();
        for (int k=0;k<tmp.length;k++)
            allcoordinates[k]=tmp[k];
        allcoordinates[8]=choice;
        allcoordinates[9]=round[0];
        allcoordinates[10]=round[1];
       return allcoordinates;
    }

    @Override
    public int[] tool6Messages(String dicejson) throws IOException {
        Dice dice = gson.fromJson(dicejson, Dice.class);
        System.out.println("Il dado è stato nuovamente lanciato. E' uscito: "+dice+". Sei pregato di indicare dove piazzarlo\n");
        return chooseCoordinates();
    }

    @Override
    public int[] tool11Messages(String dicejson) throws IOException {


        int[] coordinates = new int[3];
        int[] tmp;
        Dice dice = gson.fromJson(dicejson, Dice.class);
        System.out.println("Il colore del dado estratto è: "+dice+". Sei pregato di scegliere il valore.\n");
        coordinates[2]=chooseValue();
        tmp=chooseCoordinates();
        coordinates[0]=tmp[0];
        coordinates[1]=tmp[1];
        return coordinates;
    }
}
