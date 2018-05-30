package it.polimi.ingsw.Server;


import java.io.Serializable;
import java.util.ArrayList;

public class DBUsers implements Serializable {
    private ArrayList<User> users;

    //--------------------------------------------constructor-----------------------------------------------------------
    public DBUsers(){
        users=new ArrayList<>();
    }

    //-------------------------------------------check user and password------------------------------------------------
    public int login(String nickname, String password){
        //just a temporary user
        User temp=new User(nickname,password);
        for (User u: users) {
            //nickname already exist
            if (u.getNickname().equals(nickname)) {
                //user already online
                if (u.isOnline()) {
                    return 3;
                }
                //user not online
                else {
                    //wrong password
                    if (!u.getPassword().equals(password)) {
                        return 2;
                    }//login correct
                    else {
                        u.setOnline(true);
                        return 1;
                    }
                }
            }


        }
        //new nickname
        users.add(temp);
        return 0;
    }

    //----------------------------------------check if a user is present------------------------------------------------
    public boolean isPresent(User user){
        int index=users.indexOf(user);
        if(index==-1)
            return false;
        else
            return true;
    }

    //-----------------------------------return the number of clients connected-----------------------------------------
    public int size(){
        return users.size();
    }

    //--------------------------------------return a user finding it by index-------------------------------------------
    public User getUser(int i){
        return users.get(i);
    }

    //--------------------------------------return a user finding it by name-------------------------------------------
    public User getUser(String nickname) {
        for (User u : users)
            if (u.getNickname().equals(nickname)) {
                u.getNickname().toString();
                return u;
            }
        System.out.println("bo");
        return null;
    }

    //---------------------------------------------print all users------------------------------------------------------
    public String toString(){
        String s= new String ();
        for (User u:users) {
            s=s+u.toString();
        }
        return s;
    }

    //------------------------------------------------dump of "this"----------------------------------------------------
    public void dump(){
        System.out.println(this);
    }




}

