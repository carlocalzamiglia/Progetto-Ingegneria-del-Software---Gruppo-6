package itpolimiingsw.Client;

import com.google.gson.Gson;
import itpolimiingsw.Game.Dice;
import itpolimiingsw.ServertoClientHandler.ClientInterface;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class GUI implements ClientInterface {


    //----------------------------------------VAR---------------------
    private boolean login = false;
    private String user = "";
    private String pass = "";
    Gson gson = new Gson();
    GUIController launcher = new GUIController();
    boolean c = false;

    public GUI() throws FileNotFoundException {
        Launcher threadl = new Launcher();
        threadl.start();
    }

    public class Launcher extends Thread{

        @Override
        public void run() {
            launcher.launchgui();

        }
    }
    //-------------------------------------GAME METHODS------------------
    @Override
    public String[] loginMessages() throws IOException, InterruptedException {
        while(!GUIController.getLogin()){
            sleep(200);
        }
        GUIController.setLogin(false);
        //salviamo user e pass
        String[] dati = GUIController.getLoginData();
        System.out.println(dati[0]);
        return dati;
    }

    @Override
    public void showMessage(String message) {
        String[] messages = message.split("-");
        GUIController.showMessages(messages);
    }

    @Override
    public void showError(String message) {
        String[] messages = message.split("-");
        GUIController.showPopup(messages);
    }

    @Override
    public int schemeMessages(String scheme1, String scheme2, String scheme3, String scheme4, String privategoljson) throws IOException, InterruptedException {
        //need method to create GUI schemes.
        GUIController.setLogin(false);
        GUIController.showSchemes(scheme1, scheme2, scheme3, scheme4,privategoljson);
        while(!GUIController.getLogin() && !c){
            sleep(200);
        }
        if (c) {
            int i=99;
            return i;
        }
        GUIController.setLogin(false);
        int scheme = GUIController.getScheme();
        return scheme;
    }

    @Override
    public void printCarpetFirst(String greenCarpetjson, String playerjson) {
        GUIController.updateGreenCarpet(greenCarpetjson,playerjson);
    }

    @Override
    public void updateView(String greenCarpetjson, String playerJson) {
        GUIController.updateView(greenCarpetjson, playerJson);
    }

    @Override
    public String handleTurnMenu() throws IOException, InterruptedException {
        GUIController.setLogin(false);
        GUIController.chooseAction(4);
        while(!GUIController.getLogin()){
            sleep(200);
        }
        if(c)
            return "4";

        GUIController.setLogin(false);
        String action = GUIController.handleTurnMenu();
        System.out.println("AZIONE:"+action);
        return action;
    }

    @Override
    public void endTurn() {
        GUIController.endTurn();
    }

    @Override
    public int[] placeDiceMessages() throws IOException, InterruptedException {
        GUIController.setLogin(false);
        GUIController.chooseAction(2);
        int[] placedice = new int [3];
        while(!GUIController.getLogin() && !c){
            sleep(200);
        }

        if(c){
            placedice[0]=99;
            return placedice;
        }
        GUIController.setLogin(false);
        placedice = GUIController.getplaceDice();
        System.out.println("dadoscelto: "+placedice[0]);
        return placedice;
    }


    @Override
    public int chooseToolMessages() throws IOException, InterruptedException {
        GUIController.setLogin(false);
        GUIController.chooseAction(2);
        while(!GUIController.getToolChoose() && !c){
            sleep(200);

        }
        if(c)
            return 0;

        GUIController.setLogin(false);
        int toolnumber = GUIController.getTool();
        return toolnumber;
    }

    @Override
    public String goOnTool() throws IOException, InterruptedException {
        return GUIController.goOn();
    }

    @Override
    public int chooseDice() throws IOException, InterruptedException {      //DONE
        GUIController.setDiceChose(true);
        while(!GUIController.getDiceChoose() &&!c){
            sleep(200);
        }
        if(c)
            return 99;
        GUIController.setDiceChose(false);
        return GUIController.getDiceChosen();
    }

    @Override
    public int[] chooseCoordinates() throws IOException, InterruptedException {     //NEVER USED
        GUIController.setToolCoord();
        while(!GUIController.getToolCoordDone() && !c){
            sleep(200);
        }

        if(c){
            int[] ret = new int[2];
            ret[0]=99;
            ret[1]=0;
            return ret;
        }
        return GUIController.getToolCoord();
    }

    @Override
    public int[] chooseFromPath() throws IOException, InterruptedException {
        GUIController.setDicePath();
        while(!GUIController.getDicePath() && !c){
            sleep(200);
        }
        if(c) {
            int[] ret = new int[1];
            ret[0] = 99;
            return ret;
        }
        return GUIController.getPathVal();
    }

    @Override
    public int chooseValue() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public int tool1Messages() throws IOException, InterruptedException {
        return GUIController.getTool1Val();
    }

    @Override
    public int[] tool23Messages() throws IOException, InterruptedException {
        return handleCoordinates(4);
    }

    @Override
    public int[] tool4Messages() throws IOException, InterruptedException {
        return handleCoordinates(8);
    }

    @Override
    public int[] tool12Messages() throws IOException, InterruptedException {
        int[] allcoordinates = new int[11];
        int[] tmpcoord;
        for(int i=0; i<11;i++)
            allcoordinates[i]=0;
        int[] path = new int[2];
        path = chooseFromPath();
        int numofdices= GUIController.getndice12();
        if(numofdices==1){
            tmpcoord=tool23Messages();
        }else{
            tmpcoord=tool4Messages();
        }
        for(int j=0; j<tmpcoord.length; j++)
            allcoordinates[j]=tmpcoord[j];
        allcoordinates[8]=numofdices;
        allcoordinates[9]=path[0];
        allcoordinates[10]=path[1];
        return allcoordinates;
    }

    @Override
    public int[] tool6Messages(String dice) throws IOException, InterruptedException {
        GUIController.showTool6(dice);
        return chooseCoordinates();
    }

    @Override
    public int[] tool11Messages(String dice) throws IOException, InterruptedException {
        Gson gson = new Gson();
        Dice dice1 = gson.fromJson(dice, Dice.class);
        int[] tool11 = new int[3];
        tool11[2] = GUIController.getTool11(dice1.getItalianColour());
        int[] tmp = chooseCoordinates();
        tool11[0]=tmp[0];
        tool11[1]=tmp[1];
        return tool11;
    }

    @Override
    public void timerOut(boolean end) {
        c=end;
    }

    @Override
    public boolean newMatch() throws IOException, InterruptedException {
        GUIController.setNewGameChosen(false);
        while(!GUIController.getNewGameChosen()){
            sleep(200);
        }
        GUIController.setNewGameChosen(false);
        return GUIController.getNewGame();
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public void loginOkMessage() {
        GUIController.loginOK();
    }

    @Override
    public void showScore(String[] score) {
        GUIController.showScore(score);
    }

    public int[] handleCoordinates(int num) throws IOException, InterruptedException {
        int[] allcoordinates = new int[num];
        int[] oldcoordinates;
        for(int i=0; i<num/2; i++){
            oldcoordinates=chooseCoordinates();
            for(int j=0; j<2; j++)
                allcoordinates[(i*2)+j]=oldcoordinates[j];
        }
        return allcoordinates;
    }


}
