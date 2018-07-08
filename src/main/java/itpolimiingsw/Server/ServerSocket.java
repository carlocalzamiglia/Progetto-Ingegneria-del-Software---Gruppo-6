package itpolimiingsw.Server;

import itpolimiingsw.Game.Matches;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocket implements Runnable {
    private Matches matches;
    private DBUsers DB;
    java.net.ServerSocket sc;

    public ServerSocket(DBUsers DB,Matches matches){
        this.DB=DB;
        this.matches=matches;
    }

    /**
     * Launches the connection method.
     */
    @Override
    public void run() {
        ServerSocket ServerSocket = new ServerSocket(DB,matches);
        try {
            ServerSocket.connect();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Starts the socket connection, and waits for users to connect.
     * @throws IOException
     */
    private void connect() throws IOException {
        int PORT;
        PORT=readFromFile();
        ExecutorService executor = Executors.newCachedThreadPool();
        sc = new java.net.ServerSocket(PORT);
        System.out.println("Server socket ready on port: " + PORT);
        while(10>1) {
            Socket socket = sc.accept();
            executor.submit(new ServerSocketClientHandler(socket, DB, matches));
        }

    }

    /**
     * Read connection properties from the file.
     * @return the port chosen in the config file.
     * @throws IOException for the readline
     */
    private int readFromFile() throws IOException {
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

