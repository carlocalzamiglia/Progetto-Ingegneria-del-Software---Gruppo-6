package itpolimiingsw.ServertoClientHandler;

import java.io.IOException;

public interface ClientInterface {
    /**
     * It asks the client to insert username and password.
     * @return an array of Strings with the username and the password.
     * @throws IOException if there is a socket connection error.
     */
    String[] loginMessages() throws IOException, InterruptedException;
    /**
     * It prints an info message.
     * @param message is the String that is going to be printed.
     */
    void showMessage(String message);

    /**
     * It prints errors messages.
     * @param message contains the error String.
     */
    void showError(String message);

    /**
     * It prints only the errors related to bad dice placement.
     * @param message contains the error String.
     */
    void showPlacementeError(String message);

    /**
     * It prints messages related to connection or disconnection of an user(Gui as a popup,Cli as a println).
     * @param message contain the String with the name of the user and the message.
     */
    void showConnDiscPopup(String message);

    /**
     * It prints the instruction of the related toolcard.
     * @param message1 contains the name of the toolcard.
     * @param message2 contains the instruction of the toolcard.
     */
    void showToolTricks(String message1, String message2);

    /**
     * It prints the private goal of the user and 4 schemes, then asks the client to choose one of them.
     * @param scheme1
     * @param scheme2
     * @param scheme3
     * @param scheme4
     * @param privategoal
     * @return an integer with the serial number of the scheme chose.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int schemeMessages(String scheme1, String scheme2, String scheme3,String scheme4, String privategoal) throws IOException, InterruptedException;

    /**
     * It prints the game table at the start of the turn.
     * @param greenCarpetjson game table in json extension.
     * @param playerjson players info in json extension.
     */
    void printCarpetFirst(String greenCarpetjson, String playerjson);

    /**
     * It prints the game table after every update.
     * @param greenCarpetjson game table json extension.
     * @param playerJson players info json extension.
     */
    void updateView(String greenCarpetjson, String playerJson);

    /**
     * It prints the game menu choices.
     * @return the value of the choice.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    String handleTurnMenu() throws IOException, InterruptedException;

    /**
     * It prints the "end turn" message.
     */
    void endTurn();

    /**
     * It prints a message after the scheme is chosen.
     */
    void endTurnMessage();

    /**
     * It asks the client to choose a dice from the stock and then the placement coordinates.
     * @return an integer array with the dice coordinates.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] placeDiceMessages() throws IOException, InterruptedException;

    /**
     * It asks the client for the serial number of the tool card he want to use.
     * @return an integer with the serial number of the tool card.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int chooseToolMessages() throws IOException, InterruptedException;

    /**
     * It asks the client the position of a stock's dice.
     * @return an integer that contains the position of the stock's dice.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int chooseDice() throws IOException, InterruptedException;

    /**
     * It asks the client to choose the coordinates of a box of his scheme.
     * @return an integer array with the column and row index.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] chooseCoordinates() throws IOException, InterruptedException;

    /**
     * It asks the client to choose a dice from the round path.
     * @return an integer array with the column and row index.
     * @throws IOException
     * @throws InterruptedException
     */
    int[] chooseFromPath() throws IOException, InterruptedException;

    /**
     * It asks the client to choose a value to be assigned to the dice.
     * @return an integer with the value to be assigned.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int chooseValue() throws IOException, InterruptedException;

    //ALL TOOL METHODS

    /**
     * It prints the client the tool1 choice menu and asks him to choose.
     * @return an integer with the choose value.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int tool1Messages() throws IOException, InterruptedException;

    /**
     * It asks the client to choose the coordinates of the dice to be moved and then the new coordinates of the dice.
     * @return an integer array with the old and new coordinates of the dice.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] tool23Messages() throws IOException, InterruptedException;

    /**
     * It asks the client the coordinates of 2 dices to be moved and the new coordinates of them.
     * @return an array with the old and new coordinates of the 2 dices.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] tool4Messages() throws IOException, InterruptedException;

    /**
     * It asks first the client to choose a dice from roundpath, then it prints the choice menu of tool 12 than launches
     * or tool23messages if the choice is 1 or tool4messages if the coice is 2.
     *
     * @return an integer array with the rounpath dice coordinates plus n coordinates like tool23messages or tool4messages.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] tool12Messages() throws IOException, InterruptedException;

    /**
     * It prints the rerolled dice of the tool6 and asks the client to place it.
     * @param dice in json extension.
     * @return an integer array with the placemente coordinates
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int[] tool6Messages(String dice) throws IOException, InterruptedException;

    /**
     * It prints the color of the new dice and launches choosevalue.
     * @param dice to be shown in json extension.
     * @return an integer with the value.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    int tool11Messages(String dice) throws IOException, InterruptedException;

    /**
     * It sets the timerout value.
     * @param end
     */
    void timerOut(boolean end);

    /**
     * It prints the end match menù.
     * @return true if 1 or false if 0.
     * @throws IOException if there is a socket connection error.
     * @throws InterruptedException
     */
    boolean newMatch() throws IOException, InterruptedException;

    /**
     * It closes the client.
     */
    void exit();

    /**
     * It tells if the login is validated.
     */
    void loginOkMessage();

    /**
     * It prints the score table.
     * @param score is an array of Strings with the score table.
     */
    void showScore(String[] score);

    /**
     * It prints the timer value(Only Gui).
     * @param i
     */
    void sendTimer(int i);
}
