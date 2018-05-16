package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocket implements Runnable {
    private final static int PORT=3001;
    private java.net.ServerSocket sc;
    public DBUsers DB;
    public ServerSocket(DBUsers DB){
        this.DB=DB;
    }
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
    public void connect() throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        sc = new java.net.ServerSocket(PORT);
        System.out.println("Server socket ready on port: " + PORT);
        while(10>1) {
            Socket socket = sc.accept();
            executor.submit(new ServerSocketClientHandler(socket, DB));
        }
    }
}

