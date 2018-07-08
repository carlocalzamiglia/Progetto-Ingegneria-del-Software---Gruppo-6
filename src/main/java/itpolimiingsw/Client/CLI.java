package itpolimiingsw.Client;

import itpolimiingsw.Game.PrivateGoal;
import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Scheme;
import itpolimiingsw.Game.Player;
import itpolimiingsw.Game.Dice;
import itpolimiingsw.ServertoClientHandler.ClientInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static java.lang.Thread.sleep;

public class CLI implements ClientInterface {
    private Gson gson = new GsonBuilder().create();
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private boolean c = false;
    private boolean flag=false;


    @Override
    public String[] loginMessages() throws IOException {
        String[] logindata = new String[2];
        System.out.println("Inserire nickname:");
        logindata[0]=in.readLine();
        for(int j = 0; j<logindata[0].length(); j++){
            if(logindata[0].charAt(j)=='-' || logindata[0].charAt(j)=='_'){
                flag=true;
            }
        }
        while(logindata[0].length()==0) {logindata[0]=in.readLine();}
        char s = logindata[0].charAt(0);
        while (s == ' ' || flag) {
            System.out.println("Username non valido. Inserire username:");
            logindata[0] = in.readLine();
            while(logindata[0].length()==0) {logindata[0]=in.readLine();}
            s = logindata[0].charAt(0);
            flag=false;
            for(int j = 0; j< logindata[0].length(); j++){
                if(logindata[0].charAt(j)=='-'){
                    flag=true;
                }
            }
        }
        System.out.println("Inserire password:");
        logindata[1]=in.readLine();
        while(logindata[1].length()==0) {logindata[1]=in.readLine();}
        s = logindata[1].charAt(0);
        while (s == ' ') {
            System.out.println("Nickname non valido. Inserire nickname:");
            logindata[1] = in.readLine();
            while(logindata[1].length()==0) {logindata[1]=in.readLine();}
            s = logindata[1].charAt(0);
        }
        return logindata;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        String[] messages = message.split("-");
        System.out.println(messages[1]);
    }
    @Override
    public void showPlacementeError(String message){
        showError(message);
    }

    @Override
    public void showConnDiscPopup(String message) {
        System.out.println(message);
    }

    @Override
    public void showToolTricks(String tool, String message) { }

    @Override
    public int schemeMessages(String scheme1, String scheme2, String scheme3,String scheme4, String privategoal) throws IOException, InterruptedException {
        String message;
        Scheme scheme1class=gson.fromJson(scheme1, Scheme.class);
        Scheme scheme2class=gson.fromJson(scheme2, Scheme.class);
        Scheme scheme3class=gson.fromJson(scheme3, Scheme.class);
        Scheme scheme4class=gson.fromJson(scheme4, Scheme.class);
        PrivateGoal privateGoal=gson.fromJson(privategoal, PrivateGoal.class);

        try {
            do {
                System.out.println("Ecco il tuo obiettivo privato: "+privateGoal.toString());
                System.out.println("Scegli uno schema:\n" + scheme1class.toString() + "\n" + scheme2class.toString() + "\n" + scheme3class.toString() + "\n" + scheme4class.toString() + "\n");
                while(!in.ready() && !c) {sleep(200);}
                if (!c)
                  message = in.readLine();
                else
                    message="1";
                if(Integer.parseInt(message) <= 0 || Integer.parseInt(message) > 4)
                    this.showMessage("Hai inserito un valore errato!");
            } while (Integer.parseInt(message) <= 0 || Integer.parseInt(message) > 4);
        }catch (NumberFormatException e){
            this.showMessage("Hai inserito un valore errato!");
            return schemeMessages(scheme1, scheme2, scheme3, scheme4, privategoal);
        }
        if (c) {
            int i=99;
            return i;
        }
        System.out.println("Hai scelto lo schema "+message+". Ora attendi il tuo turno!");
        return Integer.parseInt(message);
    }


    //---------------------------------------PRINT METHODS--------------------------------------------------------------
    @Override
    public void printCarpetFirst(String greenCarpetjson, String playerjson) {
        System.out.println("\n\n*************************** E' IL TUO TURNO ***************************");
        System.out.println("Hai 90 secondi per terminare il tuo turno. Altrimenti il gioco passerà in automatico.");
        int i=0;
        GreenCarpet greenCarpet=gson.fromJson(greenCarpetjson, GreenCarpet.class);
        Player player=gson.fromJson(playerjson, Player.class);
        for(Player p: greenCarpet.getPlayer())
            if(p.getNickname().equals(player.getNickname()))
                i = greenCarpet.getPlayer().indexOf(p);
        System.out.println("Ecco lo schema degli altri giocatori:\n");
        System.out.println(greenCarpet.playersToString(i));
        System.out.println("Ecco qui il tavolo e il tuo schema:\n");
        System.out.println(greenCarpet.toString());
        System.out.println((greenCarpet.getRound()+1)+"° ROUND\t\t\t"+greenCarpet.getTurn()+"° TURNO\n");
        System.out.println(player.toString()+"\n");
    }



    @Override
    public void updateView(String greenCarpetjson, String playerjson) {
        GreenCarpet greenCarpet=gson.fromJson(greenCarpetjson, GreenCarpet.class);
        Player player=gson.fromJson(playerjson, Player.class);
        System.out.println(greenCarpet.stockToString()+"\n"+player.toString());
    }



    @Override
    public String handleTurnMenu() throws IOException, InterruptedException {
        String value = new String();
        System.out.println("1)passa il turno\n2)inserisci dado\n3)usa carta utensile\n");
        while(!in.ready() && !c) {sleep(200);}
        if(!c)
            value = in.readLine();
        else
            value="1";
        while(!value.equals("1")&& !value.equals("2")&& !value.equals("3")){
            showMessage("Hai inserito un valore errato");
            while(!in.ready() && !c) {sleep(200);}
            if(!c)
                value = in.readLine();
            else
                value="1";
        }
        if(c)
            value="4";
        return value;
    }

    @Override
    public void endTurn() {
        System.out.println("############################### ATTENDI IL TUO TURNO... ###############################\n\n");
    }

    @Override
    public void endTurnMessage() {
        endTurn();
    }

    @Override
    public int[] placeDiceMessages() throws IOException, InterruptedException {
        int[] dicecoord = new int[3];
        int[] coordinates;
        dicecoord[0] = chooseDice();
        if (dicecoord[0]==99)
            return dicecoord;
        System.out.println("Inserire le coordinate di piazzamento.");
        coordinates=chooseCoordinates();
        if (coordinates[0]==99){
            return coordinates;
        }
        dicecoord[1] = coordinates[0];
        dicecoord[2] = coordinates[1];
        return dicecoord;
    }

    @Override
    public int chooseToolMessages() throws IOException, InterruptedException {
        int tool = 0;
        try{
            System.out.println("Inserisci il numero della carta tool da usare.");
            while(!in.ready() && !c) {sleep(200);}
            if (!c)
                tool=Integer.parseInt(in.readLine());
            else
                tool=0;
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseToolMessages();
        }
        return tool;
    }


    @Override
    public int chooseDice() throws IOException, InterruptedException {
        int dice=99;
        try{
            System.out.println("Scegli il dado dalla riserva.");
            while(!in.ready() && !c) {sleep(200);}
            if (!c)
                dice = Integer.parseInt(in.readLine());
            else {
                dice = 99;
            }
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseDice();
        }
        return dice;
    }

    @Override
    public int[] chooseCoordinates() throws IOException, InterruptedException {
        int[] coordinates = new int[2];
        try {
            System.out.println("Inserisci la riga.");
            while(!in.ready() && !c) {sleep(200);}
            if (!c) {
                coordinates[0] = Integer.parseInt(in.readLine());
            }else{
                coordinates[0]=99;
            return coordinates;
            }

            System.out.println("Inserisci la colonna.");
            while(!in.ready() && !c) {sleep(200);}
            if(!c)
                coordinates[1] = Integer.parseInt(in.readLine());
            else {
                coordinates[0] = 99;
                return coordinates;
            }
        }catch(NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseCoordinates();
        }
        return coordinates;
    }

    @Override
    public int[] chooseFromPath() throws IOException, InterruptedException {
        int[] coordinates = new int[2];
        try {
            System.out.println("Inserisci il numero del round.");
            while(!in.ready() && !c) {sleep(200);}
                if(!c)
                    coordinates[0] = Integer.parseInt(in.readLine());
                else
                    coordinates[0]=99;
            System.out.println("Inserisci la posizione del dado nel round (numero di riga).");
            while(!in.ready() && !c) {sleep(200);}
            if(!c)
                coordinates[1] = Integer.parseInt(in.readLine());
            else
                coordinates[0]=99;
        }catch(NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseFromPath();
        }
        return coordinates;
    }

    @Override
    public int chooseValue() throws IOException, InterruptedException {
        int dice;
        try{
            System.out.println("Scegli il valore da assegnare al dado.");
            while(!in.ready() && !c) {sleep(200); }
            if(!c)
                dice = Integer.parseInt(in.readLine());
            else {
                return 99;
            }

            while (dice<1 || dice>6) {
                System.out.println("Hai inserito un valore errato. Scegli il valore da assegnare al dado.");
                while(!in.ready() && !c) {sleep(200);}
                if (!c)
                    dice = Integer.parseInt(in.readLine());
                else
                    return 99;
            }
        }catch (NumberFormatException e){
            System.out.println("Inserisci un valore corretto!");
            return chooseValue();
        }
        return dice;
    }
    //---------------------------------------------------Tool messages methods------------------------------------------
    @Override
    public int tool1Messages() throws IOException, InterruptedException {
        String dicechose;
        System.out.println("Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
        while(!in.ready() && !c) {sleep(200);}
        if(!c)
            dicechose=in.readLine();
        else
            return 99;
        while(!(dicechose.equals("c")) && !(dicechose.equals("d"))){
            System.out.println("Scelta errata. Inserisci 'c' se vuoi incrementarlo, 'd' se vuoi decrementarlo");
            while(!in.ready() && !c) {sleep(200);}
                if(!c)
                    dicechose=in.readLine();
                else{
                    return 99;
                }
        }
        if(dicechose.equals("c"))
            return 1;
        else
            return 2;
    }

    @Override
    public int[] tool23Messages() throws IOException, InterruptedException {
        int[] allcoordinates = new int[4];
        int[] coordinates;
        System.out.println("Inserire le coordinate del dado da spostare.");
        coordinates=chooseCoordinates();
        if (coordinates[0]!=99) {
            allcoordinates[0] = coordinates[0];
            allcoordinates[1] = coordinates[1];
        }else {
            allcoordinates[0] = 99;
            return allcoordinates;
        }
        System.out.println("Inserire le nuove coordinate del dado.");
        coordinates=chooseCoordinates();
        if (coordinates[0]!=99) {
            allcoordinates[2] = coordinates[0];
            allcoordinates[3] = coordinates[1];
        }else{
            allcoordinates[0]=99;
            return allcoordinates;
        }
        return allcoordinates;
    }

    @Override
    public int[] tool4Messages() throws IOException, InterruptedException {
        int[] allcoordinates = new int[8];
        int[] coordinates;
        System.out.println("PRIMO DADO:\n");
        coordinates=tool23Messages();
        if (coordinates[0]!=99) {
            allcoordinates[0] = coordinates[0];
            allcoordinates[1] = coordinates[1];
            allcoordinates[2] = coordinates[2];
            allcoordinates[3] = coordinates[3];
        }
        else{
            allcoordinates[0]=99;
            return allcoordinates;
        }
        System.out.println("SECONDO DADO:\n");
        coordinates=tool23Messages();
        if (coordinates[0]!=99) {
            allcoordinates[4] = coordinates[0];
            allcoordinates[5] = coordinates[1];
            allcoordinates[6] = coordinates[2];
            allcoordinates[7] = coordinates[3];
        }else
            allcoordinates[0]=99;
        return allcoordinates;
    }

    @Override
    public int[] tool12Messages() throws IOException, InterruptedException {
        int[] allcoordinates = new int[11];
        int[] round= chooseFromPath();
        if (round[0]==99)
            return round;
        int[] tmp;
        int choice;
        try{
            do{
                System.out.println("Inserisci il numero di dadi da spostare (1 o 2).");
                while(!in.ready() && !c) {sleep(200);}
                if(!c)
                    choice=Integer.parseInt(in.readLine());
                else{
                    allcoordinates[0]=99;
                    return allcoordinates;
                }
            }while(choice!=1 && choice!=2);
        }catch (NumberFormatException e){
            System.out.println("Errore nell'inserimento.");
            return tool12Messages();
        }
        if(choice==1){
            tmp=tool23Messages();
            if (tmp[0]==99) {
                allcoordinates[0] = 99;
                return allcoordinates;
            }
            for (int k=4;k<8;k++)
                allcoordinates[k]=0;
        }else
            tmp =tool4Messages();
        if (tmp[0]==99)
            return tmp;
        for (int k=0;k<tmp.length;k++)
            allcoordinates[k]=tmp[k];
        allcoordinates[8]=choice;
        allcoordinates[9]=round[0];
        allcoordinates[10]=round[1];
       return allcoordinates;
    }

    @Override
    public int[] tool6Messages(String dicejson) throws IOException, InterruptedException {
        Dice dice = gson.fromJson(dicejson, Dice.class);
        System.out.println("Il dado è stato nuovamente lanciato. E' uscito: "+dice+". Sei pregato di indicare dove piazzarlo\n");
        return chooseCoordinates();
    }

    @Override
    public int tool11Messages(String dicejson) throws IOException, InterruptedException {
        int face;
        Dice dice = gson.fromJson(dicejson, Dice.class);
        System.out.println("Il colore del dado estratto è: "+dice+". Sei pregato di scegliere il valore.\n");
        face=chooseValue();
        if (face==99) {
            return face;
        }
        return face;
    }

    @Override
    public void timerOut(boolean end){
        c=end;
    }

    @Override
    public boolean newMatch() throws IOException {
        String value;
        System.out.println("Inserisci '1' se vuoi giocare una nuova partita, '0' se vuoi uscire.");
        value = in.readLine();
        while(!value.equals("1")&& !value.equals("0")){
            showMessage("Hai inserito un valore errato");
            value=in.readLine();
        }
        if(value.equals("1")){
            System.out.println("Sei stato inserito in una nuova partita. Attendi altri giocatori.");
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void exit(){
        System.exit(0);
    }

    /**
     * Only Gui Usage.
     */
    @Override
    public void loginOkMessage() { }

    @Override
    public void showScore(String[] score) {
        System.out.println("Il gioco è terminato.\n CLASSIFICA:\n");
        for(int i=0; i<score.length; i++){
            System.out.println(score[i]);
        }
    }

    /**
     * Only Gui usage.
     * @param i
     */
    @Override
    public void sendTimer(int i) {

    }


}
