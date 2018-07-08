package itpolimiingsw.UserExperience;

import com.google.gson.Gson;
import itpolimiingsw.GameItems.Dice;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;

public class GUI implements ClientInterface {


    //----------------------------------------VAR---------------------
    GUIController launcher = new GUIController();
    private boolean c = false;
    public GUI() throws FileNotFoundException {
        Launcher threadl = new Launcher();
        threadl.start();
    }

    /**
     * Starts the GUI.
     */
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
        return dati;
    }

    @Override
    public void showMessage(String message) { }

    @Override
    public void showError(String message) {
        String[] messages = message.split("-");
        GUIController.showPopup(messages);
    }

    @Override
    public void showPlacementeError(String message) {
        String[] messages = message.split("-");
        GUIController.showPopupDicePlaceWrong(messages);
    }

    @Override
    public void showConnDiscPopup(String message) {
        GUIController.showConnDiscPopup(message);
    }

    @Override
    public void showToolTricks(String card, String message) {
        GUIController.showToolPopup(card, message);
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
        while(!GUIController.getLogin() && !c){
            try {
                sleep(200);
            }catch(InterruptedException e){}
        }
        if(c) {
            return "4";
        }

        GUIController.setLogin(false);
        String action = GUIController.handleTurnMenu();
        return action;
    }

    @Override
    public void endTurn() {
        GUIController.endTurn();
    }

    @Override
    public void endTurnMessage() {
        GUIController.chooseAction(7);
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
            GUIController.setLogin(false);
            return placedice;
        }
        GUIController.setLogin(false);
        placedice = GUIController.getplaceDice();
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
        int[] coord = GUIController.getToolCoord();
        return coord;
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
        }else if (numofdices==2){
            tmpcoord=tool4Messages();
        }else{
            allcoordinates[0]=99;
            return allcoordinates;
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
    public int tool11Messages(String dice) throws IOException, InterruptedException {
        Gson gson = new Gson();
        Dice dice1 = gson.fromJson(dice, Dice.class);
        return GUIController.getTool11(dice1.getItalianColour());
    }

    @Override
    public void timerOut(boolean end) {
        GUIController.timerOut(end);
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

    @Override
    public void sendTimer(int i) {
        if(i!=-1)
            GUIController.showTimer(i);
    }

    /**
     * Asks for coordinates at the gui.
     * @param num   number of coordinates requested
     * @return the array of coordinates
     * @throws IOException
     * @throws InterruptedException
     */
    public int[] handleCoordinates(int num) throws IOException, InterruptedException {
        int[] allcoordinates = new int[num];
        int[] oldcoordinates;
        for(int i=0; i<num/2; i++){
            oldcoordinates=chooseCoordinates();
            if(oldcoordinates[0]==99)
                return oldcoordinates;
            for(int j=0; j<2; j++)
                allcoordinates[(i*2)+j]=oldcoordinates[j];
        }

        return allcoordinates;
    }


}
