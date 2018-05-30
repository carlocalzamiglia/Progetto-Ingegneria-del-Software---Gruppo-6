package it.polimi.ingsw.ServertoClientHandler;


import it.polimi.ingsw.Game.Game;

import java.io.IOException;
import java.rmi.RemoteException;

public interface ServertoClient {
    public int chooseScheme(String scheme1, String scheme2, String scheme3, String scheme4) throws IOException, InterruptedException;
    public void sendMessageOut(String message) throws RemoteException, IOException;
    public boolean aliveMessage();
    public void handleturn(Game game, int i) throws IOException, InterruptedException;
}
