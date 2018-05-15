package Server;

import Client.ClientRmi;
import Client.ClientRmiInt;
import org.omg.PortableServer.POAPackage.ObjectNotActive;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.Registry;
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
                Naming.rebind("rmi://127.0.0.1/1234",server);
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
    public void login(String nickname) throws RemoteException {
        try {
            ClientRmiInt newClient = (ClientRmiInt) Naming.lookup("rmi://localhost/Client_" + nickname);
            users.addElement(new User(nickname,newClient));
            System.out.println(nickname+" si è connesso con successo al server.");
            sendMessage(nickname, "Ora sei connesso con successo al server, benvenuto!");
            new HandleDisconnection(nickname, this).start();

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

    public void manageDisconnection(String nickname) throws RemoteException{
        System.out.println(nickname +" si è appena disconnesso.");
        for (User i:users) {
            if(i.getNickname().equals(nickname)) {
                i.getUserclient().serverMessage("Disconnessione avvenuta con successo");
                users.remove(i);
                break;
            }
        }
    }

    public boolean clientAlive(String nickname) throws RemoteException{
        boolean flag=false;
        boolean trovato=false;
        for (User i:users) {
            if(i.getNickname().equals(nickname)) {
                try {
                    trovato=true;
                    flag = i.getUserclient().aliveMessage();
                }catch (ConnectException | NoSuchObjectException e){
                    flag=false;
                    users.remove(i);
                    System.out.println(nickname+" ha perso la connessione ed è stato rimosso dal server");
                    if(users.size()==0)
                        System.out.println("Nessun utente è connesso al server");
                    break;
                }
            }


        }
        if(trovato!=false)
            return flag;
        else{
            System.out.println("Utente non trovato");
            return trovato;
        }
    }


}
