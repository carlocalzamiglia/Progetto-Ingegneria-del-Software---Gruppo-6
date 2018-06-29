package itpolimiingsw.Client;

import com.google.gson.Gson;
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
    public String[] loginMessages() throws IOException {
        while(!GUIController.getLogin()){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        GUIController.setLogin(false);
        //salviamo user e pass
        String[] dati = GUIController.getLoginData();
        System.out.println(dati[0]);
        return dati;
    }

    @Override
    public void showMessage(String message) {
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
        while(!GUIController.getLogin()){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        while(!GUIController.getLogin()){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        GUIController.setLogin(false);
        int[] placedice = GUIController.placeDice();
        System.out.println("dadoscelto: "+placedice[0]);
        return placedice;
    }


    @Override
    public int chooseToolMessages() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public String goOnTool() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public int chooseDice() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public int[] chooseCoordinates() throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int[] chooseFromPath() throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int chooseValue() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public int tool1Messages() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public int[] tool23Messages() throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int[] tool4Messages() throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int[] tool12Messages() throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int[] tool6Messages(String dice) throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public int[] tool11Messages(String dice) throws IOException, InterruptedException {
        return new int[0];
    }

    @Override
    public void timerOut(boolean end) {

    }

    @Override
    public boolean newMatch() throws IOException {
        GUIController.setNewGameChosen(false);
        while(GUIController.getNewGameChosen()){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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


}
