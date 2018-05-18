package it.polimi.ingsw.Server;


import java.util.ArrayList;

public class DBUsers {
    private ArrayList<User> users;

    public DBUsers(){
        users=new ArrayList<>();
    }
    public int login(String nickname, String password){
        //crea un fittizio nuovo utente
        User temp=new User(nickname,password);
        //scanna tutti gli utenti del db
        for (User u: users) {
            //se il nickname è uguale
            if (u.getNickname().equals(nickname)) {
                //se il nickname è online vuol dire che esiste gia
                if (u.isOnline()) {
                    System.out.println("nickname già in uso");
                    return 3;
                }
                //se non è online controlla
                else {
                    //se la password è diversa il nickname esiste già ma la password è diversa
                    if (!u.getPassword().equals(password)) {
                        System.out.println("Password di " + nickname + " errata");
                        return 2;
                    }//altrimenti esegue l'accesso
                    else {
                        System.out.println("Utente nuovo inserito");
                        u.setOnline(true);
                        return 1;
                    }
                }
            }


        }
        //se non trova utenti con quel nickname crea un nuovo utente e lo aggiunge al db
        users.add(temp);
        System.out.println("Utente nuovo inserito");
        return 0;
    }
    public boolean isPresent(User user){
        int index=users.indexOf(user);
        if(index==-1)
            return false;
        else
            return true;
    }
    public int size(){
        return users.size();
    }
    public User getUser(int i){
        return users.get(i);
    }
    public User getUser(String nickname) {
        for (User u : users)
            if (u.getNickname().equals(nickname)) {
                u.getNickname().toString();
                return u;
            }
        System.out.println("bo");
        return null;
    }
    public String toString(){
        String s= new String ();
        for (User u:users) {
            s=s+u.toString();
        }
        return s;
    }
    public void dump(){
        System.out.println(this);
    }



}

