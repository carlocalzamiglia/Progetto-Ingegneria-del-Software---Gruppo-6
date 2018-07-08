package itpolimiingsw.Server;

import itpolimiingsw.Client.ClientRmiInt;
import itpolimiingsw.ServertoClientHandler.ServertoClient;

public class User {
    private String nickname;
    private String password;
    private boolean online;
    private ServertoClient connectionType;

    public User(String nickname, String password){
        this.nickname=nickname;
        this.password=password;
        this.online=true;
    }

    /**
     * Set the name of the user
     * @param name
     */
    public void setName(String name) {
        this.nickname = name;
    }

    /**
     * Set the user online or offline
     * @param online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    /**
     *
     * @return the connection type for communication (RMI or Socket)
     */
    public ServertoClient getConnectionType() {
        return connectionType;
    }


    public void setClientHandler(ServerSocketClientHandler clientHandler) {
        this.connectionType = clientHandler;
    }

    public void setRmiClient(ClientRmiInt rmiClient) {
        this.connectionType = rmiClient;
    }

    /**
     *
     * @return if a user is online or not.
     */
    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        String s=nickname+"\n"+"\n"+"online: "+online+"\n"+"tipo di connessione: ";
        if(this.getConnectionType()!=null)
            s=s+"Connessione: "+getConnectionType()+"\n";
        else
            s=s+"nessuna\n";
        return s;
    }

    public void dump(){
        System.out.println(this);
    }
}

