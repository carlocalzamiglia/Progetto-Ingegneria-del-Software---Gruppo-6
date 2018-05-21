package it.polimi.ingsw.Server;


import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) throws InterruptedException, RemoteException {

        final DBUsers DB=new DBUsers();
        Thread t1;
        Thread t2;
        ServerSocket serverSocket;
        ServerRmi serverRmi;
        serverSocket=new ServerSocket(DB);
        TimeUnit.SECONDS.sleep(1);
        serverRmi=new ServerRmi(DB);
        t1=new Thread(serverSocket);
        t2=new Thread(serverRmi);
        t1.start();
        t2.start();
        while (1>0){
            //loop that print the users DB
            TimeUnit.SECONDS.sleep(30);
            System.out.println("UTENTI CONNESSI");
            System.out.println("________________");
            DB.dump();
            System.out.println("________________");

        }
    }
}
