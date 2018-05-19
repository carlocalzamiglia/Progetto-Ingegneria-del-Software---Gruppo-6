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
    private boolean alive = true;

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

            int check=DB.login(user, psw);
            while (check!=0 && check != 1) {
                if(check==2) {
                    out.println("2");
                    out.flush();
                }else if(check==3){
                    out.println("3");
                    out.flush();
                }
                user = in.nextLine();
                psw = in.nextLine();
            }
            out.println("0");
            out.flush();
            DB.getUser(user).setClientHandler(this);
            String nickname = in.nextLine();
            System.out.println(nickname + " loggato con connessione socket");
            new HandleDisconnection(nickname, this).start();
            new ListenFromClient(nickname).start();
            newUserMessage(nickname);
            sendMessageOut("Benvenuto, "+nickname+". Ora puoi giocare!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    class ListenFromClient extends Thread {
        String message;
        String nickname;

        public ListenFromClient(String nickname) {
            this.nickname = nickname;
        }

        public void run() {
            while (true) {
                try {
                    ins = new ObjectInputStream(socket.getInputStream());
                    // read the message form the input datastream
                    message = (String) ins.readObject();

                    if (message.equals("@LOGOUT")) {
                        DB.getUser(nickname).setOnline(false);
                        DB.getUser(nickname).setClientHandler(null);
                        System.out.println(nickname+ " si è appena disconnesso.");
                    } else if (message.equals("@ALIVE")) {
                        alive = !alive;
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
        if (DB.getUser(nickname).isOnline()) {
            try {
                checkalive = alive;
                sendMessageOut("@ALIVE");
                sleep(1000);
                if (checkalive != alive) {
                    return true;
                } else {
                    DB.getUser(nickname).setOnline(false);
                    DB.getUser(nickname).setClientHandler(null);
                    System.out.println(nickname + " ha probabilmente perso la connessione.");
                    return false;
                }

            } catch (IOException e) {       //PROBABILMENTE NON E' NECESSARIO.
                return false;
            }
        } else {
            return false;
        }

    }

    private synchronized void newUserMessage(String nickname) throws IOException {
        for(int i=0; i<DB.size();i++){
            if(!(DB.getUser(i).getNickname().equals(nickname))) {
                if (DB.getUser(i).getClientHandler() != null)
                    DB.getUser(i).getClientHandler().sendMessageOut(nickname+" ha appena effettuato il login ed è pronto a giocare.");
                else if (DB.getUser(i).getClient() != null)
                    DB.getUser(i).getClient().tell(nickname+" ha appena effettuato il login ed è pronto a giocare.");
            }
        }
    }



    public void sendMessageOut(String message) throws IOException {
        outs = new ObjectOutputStream(socket.getOutputStream());
        outs.writeObject(message);
    }

}
