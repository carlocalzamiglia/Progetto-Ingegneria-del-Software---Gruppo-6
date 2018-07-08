package itpolimiingsw.Game;

import itpolimiingsw.Server.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Matches implements Serializable {
    private ArrayList<Game> matches;
    private boolean lock;

    public Matches(){
        matches=new ArrayList<>();
        lock=true;
    }

    /**
     * Add a specifid user into a match
     *
     * @param user the user to add at the match
     */
    public synchronized void addUser(User user){
        Boolean flag=false;
        while(!lock){
            try {
                wait();
            }catch(InterruptedException e){Thread.currentThread().interrupt();}
        }
        lock=false;
        if(!matches.isEmpty()){
            for (Game g:matches) {
                if (!g.getPlaying() && g.getPlaying() != null) {
                    flag=g.addUser(user);
                    lock=true;
                    notifyAll();
                    if(flag)
                        return;
                }
            }
        }

        Game tmp=new Game(this);
        tmp.addUser(user);
        matches.add(tmp);
        lock=true;
        notifyAll();
    }

    /**
     * search and return a Game where a player whit this nickname is playing
     *
     * @param nickname
     * @return the game where the player is playing
     */
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

    /**
     * method that search in match and return a User
     *
     * @param nickname
     * @return the user with this nickname
     */
    public User getUser(String nickname){
        for (Game g:matches) {
            for(User u:g.getUsers()){
                if(u.getNickname().equals(nickname))
                    return u;

            }
        }
        return null;
    }


    /**
     * method that search in match and return a Player
     *
     * @param nickname
     * @return the Player with this nickname
     */
    public Player getPlayer(String nickname){
        for (Game g:matches) {
            for(Player p:g.getPlayer()){
                if(p.getNickname().equals(nickname))
                    return p;

            }
        }
        return null;
    }


    /**
     *
     * @param game is the game tha
     */
    public void deleteGame(Game game) {
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
