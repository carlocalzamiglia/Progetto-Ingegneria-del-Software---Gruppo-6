package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientRmiInt;

public class User {
    private String nickname;
    private ClientRmiInt userclient;

    public User(String nickname, ClientRmiInt userclient){
        this.nickname=nickname;
        this.userclient=userclient;
    }
    public String getNickname() {
        return nickname;
    }
    public ClientRmiInt getUserclient() {
        return userclient;
    }
}
