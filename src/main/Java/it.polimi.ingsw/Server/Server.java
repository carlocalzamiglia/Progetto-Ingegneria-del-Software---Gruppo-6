package Server;


import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Vector;

public class Server {
    public static void main(String[]args) throws RemoteException {
        Vector<User> Database=new Vector<User>(10,1);
        Thread t1;
        ServerRmi serverRmi=new ServerRmi(Database);
        t1=new Thread(serverRmi);
        t1.start();
    }
}
