package itpolimiingsw.Server;


import itpolimiingsw.Client.ClientRmiInt;
import itpolimiingsw.Game.Matches;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ServerRmiClientHandler extends UnicastRemoteObject implements ServerRmiClientHandlerInt {
    private DBUsers DB ;
    private Matches matches;
    public ServerRmiClientHandler(DBUsers DB,Matches matches) throws RemoteException{
        this.DB=DB;
        this.matches=matches;
    }


    //---------------------------------------check if login is all right------------------------------------------------
    //@Override
    public int login(String nickname, String password) throws InterruptedException, RemoteException {
        return DB.login(nickname,password) ;
    }


    //--------------------------------add on user's array the RMI connection link---------------------------------------
    //@Override
    public void addRmi(ClientRmiInt client, String nickname) throws RemoteException{
        DB.getUser(nickname).setRmiClient(client);
        try {
            newUserMessage(nickname, " ha appena effettuato il login ed è pronto a giocare.");
            DB.getUser(nickname).getConnectionType().sendMessageOut("Benvenuto, "+nickname+". La partita inizierà a breve!");
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new HandleDisconnection(nickname, this).start();
    }

    //@Override
    public void addToMatches(String username) throws IOException, InterruptedException, RemoteException {
        matches.addUser(DB.getUser(username));
    }

    @Override
    public boolean reconnectUser(String username, ClientRmiInt client) throws RemoteException{
        try {
            if (matches.getGame(username) != null) {
                matches.getGame(username).playerConnect();
                if(matches.getPlayer(username)!=null)
                    matches.getPlayer(username).setOnline(true);
                matches.getUser(username).setRmiClient(client);
                matches.getUser(username).setOnline(true);
                matches.getGame(username).reconnectUser();
                return true;
            } else {
                return false;
            }
        }catch(NullPointerException e){ }
        return false;
    }



    //------------------------------------new user connected message on CLI---------------------------------------------
    @Override
    public void publish(String us) throws RemoteException{
        System.out.println(us+" loggato con connessione Rmi");
    }



    //--------------------------------------check if client is connected yet--------------------------------------------
    //@Override
    public boolean clientAlive(String nickname) throws IOException, InterruptedException, RemoteException {
        boolean flag;
        if(DB.getUser(nickname).isOnline()){
            try{
                flag=DB.getUser(nickname).getConnectionType().aliveMessage();
            }catch (ConnectException e){
                flag=false;
                DB.getUser(nickname).setOnline(false);
                DB.getUser(nickname).setRmiClient(null);
                if(matches.getGame(nickname)!=null) {
                    if(matches.getGame(nickname).getPlaying()) {
                        matches.getUser(nickname).setOnline(false);
                        if (matches.getPlayer(nickname) != null)
                            matches.getPlayer(nickname).setOnline(false);
                    }
                    matches.getGame(nickname).playerDisconnect();
                }
                newUserMessage(nickname, " è uscito dalla partita.");
                System.out.println(nickname+" ha perso la connessione ed è stato rimosso dal server");
            }
        }else
            flag=false;

        return flag;
    }

    @Override
    public boolean serverConnected() throws RemoteException{
        return true;
    }


    //--------------------------------------new client connected message------------------------------------------------
    @Override
    public void newUserMessage(String nickname, String message) throws IOException, RemoteException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getConnectionType() != null) {
                    try {
                        DB.getUser(i).getConnectionType().sendConnDiscMessage(nickname + message);
                    }catch(RemoteException | SocketException e) {}
                }
            }
        }
    }
}

