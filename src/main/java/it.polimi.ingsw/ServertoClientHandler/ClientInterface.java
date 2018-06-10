package it.polimi.ingsw.ServertoClientHandler;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Scheme;

import java.io.IOException;

public interface ClientInterface {
    String[] loginMessages() throws IOException;

    void showError(String message);

    int schemeMessages(String schemes) throws IOException;

    void printCarpetFirst(String greenCarpetjson, String playerjson);

    String handleTurnMenu() throws IOException;

    void endTurn();

    int[] placeDiceMessages() throws IOException;

    void schemeUpdated(String scheme);

    int chooseToolMessages() throws IOException;

    String goOnTool() throws IOException;

    int chooseDice() throws IOException;

    int[] chooseCoordinates() throws IOException;

    int[] chooseFromPath() throws IOException;

    int chooseValue() throws IOException;

    //ALL TOOL METHODS
    int tool1Messages() throws IOException;
    int[] tool23Messages() throws IOException;
    int[] tool4Messages() throws IOException;
    int[] tool12Messages() throws IOException;
    int[] tool6Messages(String dice) throws IOException;
    int[] tool11Messages(String dice) throws IOException;

}
