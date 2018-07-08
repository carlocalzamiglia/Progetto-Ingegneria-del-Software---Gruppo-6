package itpolimiingsw.Server;


import java.util.ArrayList;

public class DBUsers {
    private ArrayList<User> users;
    private static boolean synch;

    //--------------------------------------------constructor-----------------------------------------------------------
    public DBUsers(){
        users=new ArrayList<>();
        synch=false;
    }


    /**
     * The method check for new user's username and password
     * @param nickname
     * @param password
     * @return 0 in case of a new User, 1 in case of a old (offline) User, 0 in case of wrong password, 3 in case the User is already online.
     * @throws InterruptedException
     */
    public synchronized int login(String nickname, String password) throws InterruptedException {
        while(synch){
            wait();
        }
        synch=true;
        //just a temporary user
        User temp=new User(nickname,password);
        for (User u: users) {
            //nickname already exist
            if (u.getNickname().equals(nickname)) {
                //user already online
                if (u.isOnline()) {
                    synch=false;
                    notifyAll();
                    return 3;
                }
                //user not online
                else {
                    //wrong password
                    if (!u.getPassword().equals(password)) {
                        synch=false;
                        notifyAll();
                        return 2;
                    }//login correct
                    else {
                        u.setOnline(true);
                        synch=false;
                        notifyAll();
                        return 1;
                    }
                }
            }


        }
        //new nickname
        users.add(temp);
        synch=false;
        notifyAll();
        return 0;
    }


    /**
     *
     * @return number of users connected.
     */
    public int size(){
        return users.size();
    }


    /**
     *
     * @param i index of the user
     * @return a user, finding him by index, or null.
     */
    public User getUser(int i){
        return users.get(i);
    }

    /**
     *
     * @param nickname
     * @return a user, finding him by name, or null.
     */
    public User getUser(String nickname) {
        for (User u : users)
            if (u.getNickname().equals(nickname)) {
                return u;
            }
        return null;
    }

    /**
     *
     * @return a print of all users.
     */
    public String toString(){
        String s= new String ();
        for (User u:users) {
            s=s+u.toString();
        }
        return s;
    }

    /**
     * Just a dump of the object "DBUsers"
     */
    public void dump(){
        System.out.println(this);
    }




}

