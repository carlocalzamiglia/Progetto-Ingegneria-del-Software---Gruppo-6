package itpolimiingsw.Client;

import java.io.IOException;

public class ClientSetup {

    /**
     * This method starts every client. From args we get the gui/cli and rmi/socket choice.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CLI cli;
        GUI gui;

        String view = args[0];
        String choice = args[1];
        int exportPort=0;

        if(choice.equals("socket")) {
            if(view.equals("cli")) {
                cli = new CLI();
                new ClientSocket(cli);
            }else{
                gui = new GUI();
                new ClientSocket(gui);
            }

        }
        else if(choice.equals("rmi")) {
            exportPort = Integer.parseInt(args[2]);
            if (view.equals("cli")) {
                cli = new CLI();
                new ClientRmi(cli, exportPort);
            } else {
                gui = new GUI();
                new ClientRmi(gui, exportPort);
            }
        }
    }
}
