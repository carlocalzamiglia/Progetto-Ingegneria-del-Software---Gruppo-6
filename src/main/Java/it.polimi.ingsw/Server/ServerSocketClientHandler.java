package it.polimi.ingsw.Server;

import javax.print.PrintException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.Thread.sleep;

public class ServerSocketClientHandler implements Runnable {
    private Socket socket;
    private DBUsers DB;
    private ObjectOutputStream outs;
    private ObjectInputStream ins;
    private ObjectInputStream inss;
    private boolean alive=true;

    public ServerSocketClientHandler(Socket socket, DBUsers DB) throws IOException {
        this.socket = socket;
        this.DB = DB;

    }

    @Override
    public void run() {
        try {

            Scanner in = new Scanner(socket.getInputStream());
            String user = in.nextLine();
            String psw = in.nextLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while (DB.login(user, psw) != 0 && DB.login(user, psw) != 1) {
                out.println(false);
                out.flush();
                user = in.nextLine();
                psw = in.nextLine();
            }
            out.println(true);
            out.flush();
            DB.getUser(user).setClientHandler(this);
            String nickname = in.nextLine();
            System.out.println(nickname + " loggato con connessione socket");
            new HandleDisconnection(nickname, this).start();
            new ListenFromClient(nickname).start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


   class ListenFromClient extends Thread {
        String message;
        String nickname;

        public ListenFromClient(String nickname){
            this.nickname=nickname;
        }
        public void run() {
            while (true) {
                try {
                    ins = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    message = (String) ins.readObject();
                    System.out.println(message);

                    if (message.equals("@LOGOUT")) {
                        DB.getUser(nickname).setOnline(false);
                        DB.getUser(nickname).setClientHandler(null);
                        System.out.println("chiudo il client");
                    }
                    else if(message.equals("@ALIVE")){
                        alive=!alive;
                    }
                } catch (IOException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean clientAlive(String nickname) throws ClassNotFoundException, InterruptedException {
        boolean checkalive;
        if(DB.getUser(nickname).isOnline()) {
            try {
                outs = new ObjectOutputStream(socket.getOutputStream());
                checkalive=alive;
                outs.writeObject("@ALIVE");
                sleep(1000);
                if(checkalive!=alive) {
                    return true;
                }
                else {
                    return false;
                }

            } catch (IOException e) {       //PROBABILMENTE NON E' NECESSARIO.
                DB.getUser(nickname).setOnline(false);
                DB.getUser(nickname).setClientHandler(null);
                System.out.println(nickname + " ha probabilmente perso la connessione.");
                return false;
            }
        }else{
            return false;
        }

    }

}
