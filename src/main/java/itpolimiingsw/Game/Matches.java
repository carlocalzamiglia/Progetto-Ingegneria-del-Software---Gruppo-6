package itpolimiingsw.Game;

import itpolimiingsw.Server.User;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class Matches implements Serializable {
    private ArrayList<Game> matches;
    private boolean lock;

    public Matches(){
        matches=new ArrayList<>();
        lock=true;
    }
    public synchronized void addUser(User user) throws IOException, InterruptedException {
        while(!lock){
            try {
                wait();
            }catch(InterruptedException e){Thread.currentThread().interrupt();}
        }
        lock=false;
        if(!matches.isEmpty()){
            for (Game g:matches) {
                if (!g.getPlaying() && g.getPlaying() != null) {
                    g.addUser(user);
                    lock=true;
                    notifyAll();
                    return;
                }
            }
        }

        Game tmp=new Game(matches.size(), this);
        tmp.addUser(user);
        matches.add(tmp);
        lock=true;
        notifyAll();
    }
    public Game getGame(String nickname){

        if(!matches.isEmpty()){
            for (Game g:matches) {
                for(User u:g.getUsers()){
                    if(u.getNickname().equals(nickname)) {
                        return g;
                    }
                }
            }
        }
        return null;
    }

    public int size(){
        return matches.size();
    }

    public User getUser(String nickname){
        for (Game g:matches) {
            for(User u:g.getUsers()){
                if(u.getNickname().equals(nickname))
                    return u;

            }
        }
        return null;
    }

    public Player getPlayer(String nickname){
        for (Game g:matches) {
            for(Player p:g.getPlayer()){
                if(p.getNickname().equals(nickname))
                    return p;

            }
        }
        return null;
    }


    public void deleteGame(Game game) throws IOException, InterruptedException {
        boolean result=false;
        for(User u: game.getUsers()){
            DeleteGameTh dg = new DeleteGameTh(u);
            dg.start();
        }
        matches.remove(game);
    }

    class DeleteGameTh extends Thread{
        User u;
        boolean result = false;
        public DeleteGameTh(User u){
            this.u=u;
        }
        public void run(){
            try {
                result=u.getConnectionType().newMatch();
                if(result){
                    addUser(u);
                }
            }catch(IOException | NullPointerException | InterruptedException e){}
        }
    }

}
