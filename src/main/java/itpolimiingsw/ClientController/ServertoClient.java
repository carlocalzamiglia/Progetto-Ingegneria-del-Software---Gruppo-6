package itpolimiingsw.ClientController;


import itpolimiingsw.GameController.Game;
import itpolimiingsw.GameTools.GreenCarpet;
import itpolimiingsw.GameTools.Player;
import java.io.IOException;
import java.rmi.RemoteException;

public interface ServertoClient {

    /**
     * It starts the choose scheme timer and forwards the schemes to the client interface
     * @param scheme1
     * @param scheme2
     * @param scheme3
     * @param scheme4
     * @param privategoal
     * @param time timer value.
     * @return an integer with the client choice value
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException when the timer ends.
     */
    int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException;

    /**
     * It forwars the @param to the client.
     * @param message is a string with an info message.
     * @throws IOException if there is a socket connection error.
     */
    void sendMessageOut(String message) throws IOException;

    /**
     * It is a ping method for the rmi client.
     * @return true
     * @throws RemoteException if the client is disconnected.
     */
    boolean aliveMessage() throws RemoteException;

    /**
     * It launches the turn timer and creates class handleturn.
     * @param greenCarpet game table.
     * @param player players info.
     * @param i
     * @param time timer duration.
     * @return
     * @throws InterruptedException if the timer ends.
     * @throws IOException if there is a socket connection error.
     */
    Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException;

    /**
     * Launches the Cli or Gui end match menù.
     * @return
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    Boolean newMatch() throws IOException, InterruptedException;

    /**
     * Launches the method that prints the score table.
     * @param score score table.
     * @throws RemoteException if there is a connection error.
     */
    void showScore(String[] score) throws RemoteException;

    /**
     * It launches the methods that print connection or disconnection messages.
     * @param message
     * @throws IOException if there is a socket connection error.
     */
    void sendConnDiscMessage(String message) throws IOException;
}
