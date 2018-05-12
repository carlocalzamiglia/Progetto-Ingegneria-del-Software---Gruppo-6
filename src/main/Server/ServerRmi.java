package Server;

import Client.ClientRmiInt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ServerRmi extends UnicastRemoteObject implements ServerRmiInt, Runnable{
    private Vector<User> users;

    public ServerRmi(Vector<User> users) throws RemoteException{
        super();
        this.users=users;

    }

    public void run(){


        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            ServerRmiInt server=new ServerRmi(users);
            try {
                Naming.rebind("rmi://localhost/1234",server);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            System.out.println("Server online! Attendo client.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean checkUser(String nickname) throws RemoteException{
        for (User u:users) {
            if(u.getNickname().equals(nickname))
                return false;
        }
        return true;
    }

    @Override
    public void login(String nickname) throws RemoteException{
        try {
            ClientRmiInt newClient = (ClientRmiInt) Naming.lookup("rmi://localhost/Client_" + nickname);
            users.addElement(new User(nickname,newClient));
            System.out.println(nickname+", si è connesso con successo al server.");
        } catch (NotBoundException | RemoteException |MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String nickname, String message) throws RemoteException {
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getNickname().equals(nickname))
                users.get(i).getUserclient().serverMessage(message);
        }
    }
}
