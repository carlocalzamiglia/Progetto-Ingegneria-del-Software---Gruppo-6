package itpolimiingsw.Server;


import itpolimiingsw.Game.Matches;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerRmi implements Runnable{
    private DBUsers DB;
    private Matches matches;


    //-------------------------------------------------constructor------------------------------------------------------
    public ServerRmi(DBUsers DB,Matches matches){
        this.DB=DB;
        this.matches=matches;
    }

    /**
     * creates a new serverRmi object and then launches the connection method, which handle Connection and login parts.
     */
    public void run() {
        ServerRmi serverRmi = new ServerRmi(DB,matches);
        serverRmi.connect();
    }

    /**
     * execute the complete RMI connection part.
     */
    private void connect() {
        int PORT;
        try {
            PORT=readFromFile();
            java.rmi.registry.LocateRegistry.createRegistry(PORT);
            Registry registry = LocateRegistry.getRegistry(PORT);
            ServerRmiClientHandlerInt conn = new ServerRmiClientHandler(DB,matches);
            registry.rebind("RMICONNECTION", conn);
            System.out.println("Server rmi ready on port:"+PORT);
        }catch (Exception e) {
            System.out.println("Impossible to run the RMI server." );
        }
    }

    /**
     *
     * @return a int, which is the port, ridden in the "server_config" file.
     * @throws IOException in case readLine fails.
     * @throws NumberFormatException in case the port written in the file is not
     */
    private int readFromFile() throws IOException, NumberFormatException {
        System.out.println(System.getProperty("user.dir"));
        FileReader f=new FileReader(System.getProperty("user.dir")+"/src/main/resources/server_config.txt");

        BufferedReader b = new BufferedReader(f);
        int port;
        try {
            String useless = b.readLine();
            port = Integer.parseInt(b.readLine());
        }finally {
            b.close();
            f.close();
        }
        return port;
    }



}

