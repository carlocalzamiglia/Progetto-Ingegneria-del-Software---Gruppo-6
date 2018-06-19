package it.polimi.ingsw.ServertoClientHandler;

import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Scheme;

import java.io.IOException;

public interface ClientInterface {
    String[] loginMessages() throws IOException;

    void showMessage(String message);

    int schemeMessages(String scheme1, String scheme2, String scheme3,String scheme4) throws IOException, InterruptedException;

    void printCarpetFirst(String greenCarpetjson, String playerjson);

    void printTool(String greenCarpetjson, String playerJson);

    String handleTurnMenu() throws IOException, InterruptedException;

    void endTurn();

    int[] placeDiceMessages() throws IOException, InterruptedException;

    void schemeUpdated(String scheme);

    int chooseToolMessages() throws IOException, InterruptedException;

    String goOnTool() throws IOException, InterruptedException;

    int chooseDice() throws IOException, InterruptedException;

    int[] chooseCoordinates() throws IOException, InterruptedException;

    int[] chooseFromPath() throws IOException, InterruptedException;

    int chooseValue() throws IOException, InterruptedException;

    //ALL TOOL METHODS
    int tool1Messages() throws IOException, InterruptedException;
    int[] tool23Messages() throws IOException, InterruptedException;
    int[] tool4Messages() throws IOException, InterruptedException;
    int[] tool12Messages() throws IOException, InterruptedException;
    int[] tool6Messages(String dice) throws IOException, InterruptedException;
    int[] tool11Messages(String dice) throws IOException, InterruptedException;


    void timerOut(boolean end);

}
