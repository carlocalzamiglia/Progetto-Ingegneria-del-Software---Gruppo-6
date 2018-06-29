package itpolimiingsw.ServertoClientHandler;


import itpolimiingsw.Game.Game;
import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServertoClient {
    int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException;
    void sendMessageOut(String message) throws RemoteException, IOException;
    boolean aliveMessage() throws RemoteException;
    Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException;
    Boolean newMatch() throws IOException, InterruptedException;
    void showScore(String[] score) throws RemoteException;
}
