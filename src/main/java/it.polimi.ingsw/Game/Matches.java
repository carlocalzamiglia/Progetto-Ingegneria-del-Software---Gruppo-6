package it.polimi.ingsw.Game;

import it.polimi.ingsw.Server.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Matches implements Serializable {
    private ArrayList<Game> matches;

    public Matches(){
        matches=new ArrayList<>();
    }
    public void addUser(User user) throws IOException {

        for (Game g:matches) {
            if (!g.getPlaying() && g.getPlaying()!=null) {
                g.addUser(user);
                return;
            }
            //if(g.getPlaying() && g==getGame(user.getNickname())){}

        }

        Game tmp=new Game(matches.size());
        tmp.addUser(user);
        matches.add(tmp);
    }
    public Game getGame(String nickname){
        for (Game g:matches) {
            for(User u:g.getUsers()){
                if(u.getNickname().equals(nickname))
                    return g;

            }
        }
        return null;
    }

    public int size(){
        return matches.size();
    }


}
