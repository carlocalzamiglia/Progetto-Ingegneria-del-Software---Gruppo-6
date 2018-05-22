package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientRmiInt;

public class User {
    private String nickname;
    private String password;
    private Integer wins;
    private boolean online;
    public ClientRmiInt rmiClient;
    private ServerSocketClientHandler clientHandler;

    public User(String nickname, String password){
        this.nickname=nickname;
        this.password=password;
        this.online=true;
        this.wins=0;
    }

    public void setName(String name) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public void hasWin(){
        this.wins=this.wins+1;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public ClientRmiInt getClient() {
        return rmiClient;
    }

    public ServerSocketClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(ServerSocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setRmiClient(ClientRmiInt rmiClient) {
        this.rmiClient = rmiClient;
    }

    public boolean isOnline() {
        return online;
    }

    public Integer getWins() {
        return wins;
    }

    @Override
    public String toString() {
        String s=nickname+"\n"+wins+"\n"+"online: "+online+"\n"+"tipo di connessione: ";
        if(this.clientHandler==null)
            s=s+"rmi "+rmiClient+"\n";
        if(this.rmiClient==null)
            s=s+"socket "+clientHandler+"\n";
        return s;
    }

    public void dump(){
        System.out.println(this);
    }
}

