package it.polimi.ingsw.ServertoClientHandler;


import it.polimi.ingsw.Game.Game;
import it.polimi.ingsw.Game.GreenCarpet;
import it.polimi.ingsw.Game.Player;

import java.io.IOException;
import java.rmi.RemoteException;

public interface ServertoClient {
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4, String privategoal, int time) throws IOException, InterruptedException;
    public void sendMessageOut(String message) throws RemoteException, IOException;
    public boolean aliveMessage() throws RemoteException;
    public Game endTurn(GreenCarpet greenCarpet, Player player, int i, int time) throws InterruptedException, IOException;
    public Boolean newMatch() throws IOException, InterruptedException;
}
