package it.polimi.ingsw.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocket implements Runnable {
    private int PORT;
    private java.net.ServerSocket sc;
    public DBUsers DB;
    public ServerSocket(DBUsers DB){
        this.DB=DB;
    }

    //----------------------------------------launch the connection method----------------------------------------------
    @Override
    public void run() {
        ServerSocket ServerSocket = new ServerSocket(DB);
        try {
            ServerSocket.connect();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    //------------------------------------------wait for clients connection---------------------------------------------
    public void connect() throws IOException {
        PORT=leggiDaFile();
        ExecutorService executor = Executors.newCachedThreadPool();
        sc = new java.net.ServerSocket(PORT);
        System.out.println("Server socket ready on port: " + PORT);
        while(10>1) {
            Socket socket = sc.accept();
            executor.submit(new ServerSocketClientHandler(socket, DB));
        }
    }

    //---------------------------------------read connection properties from file---------------------------------------
    private int leggiDaFile() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/server_config.txt");

        BufferedReader b = new BufferedReader(f);
        int port;
        try {
            port = Integer.parseInt(b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return port;
    }
}

