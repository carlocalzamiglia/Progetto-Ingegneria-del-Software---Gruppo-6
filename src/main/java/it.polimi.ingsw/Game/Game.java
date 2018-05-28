package it.polimi.ingsw.Game;

import it.polimi.ingsw.Server.User;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private ArrayList<User> users;
    private ArrayList<Player> player;
    private GreenCarpet greenCarpet;
    private Inventory inventory;
    private Boolean isPlaying;
    private int idGame;
    private int numUser;

    public Game(int index){
        isPlaying=false;
        idGame=index;
        this.inventory = new Inventory() ;
        this.numUser=0;
        users=new ArrayList<>();
        player=new ArrayList<>();
    }
    public void addUser(User user) {
        if (numUser != 4) {
            numUser = numUser + 1;
            this.users.add(user);
        }
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public GreenCarpet getGreenCarpet() {
        return greenCarpet;
    }
    public Player getPlayer(int index){
        return player.get(index);
    }

    public void startGame(){
        Scanner scanner=new Scanner(System.in);
        Ruler ruler=new Ruler();
        ToolCardsExecutor executor=new ToolCardsExecutor();
        isPlaying=true;
        this.greenCarpet=new GreenCarpet(numUser);
        DiceBucket diceBucket=inventory.getDiceBucket();
        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();

        PrivateGoal[] privateGoals=new PrivateGoal(1).getRndPrivateGoals(numUser);
        Scheme [] schemes=new Scheme(0).getRndSchemes(numUser);
        Bridge[] bridges=new Bridge(0).getRndBridges(numUser);

        for (int i=0;i<numUser;i++) {
            Player player=new Player(users.get(i).getNickname());
            player.setPrivateGoal(privateGoals[i]);
            System.out.println(users.get(i).getNickname()+" il tuo obbiettivo privato è:");
            privateGoals[i].dump();
            System.out.println("Scegli lo schema tra questi");
            schemes[(i*4)].dump();
            schemes[(i*4)+1].dump();
            schemes[(i*4)+2].dump();
            schemes[(i*4)+3].dump();
            int choose=scanner.nextInt();
            while(choose<=0 || choose>4){
                System.out.println("errore selezione\nScegli lo schema tra questi");
                choose=scanner.nextInt();
            }
            player.setBridge(bridges[i]);
            player.setScheme(schemes[(i*4)+choose-1]);
            player.setMarkers();

            this.player.add(player);
        }


        for(int j=0;j<10;j++) {
            System.out.println("ROUND "+(j+1));
            greenCarpet.setStock(numUser*2+1,diceBucket);

            for (int i = 0; i < numUser; i++) {
                boolean flagDice=false;
                boolean flagTool=false;
                boolean [] flagRound=new boolean[numUser];
                for (int indBol=0;indBol<numUser;indBol++)
                    flagRound[indBol]=false;

                greenCarpet.dump();
                System.out.println("ROUND "+(j+1)+" TURNO:"+(1));
                System.out.println("tocca a \n" + player.get(i));
                System.out.println("Cosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                int choose = scanner.nextInt();
                while (choose != 1 && choose != 2 && choose!=3) {
                    System.out.println("Errore scelta\nCosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                    choose = scanner.nextInt();
                }
                while(choose!=3) {
                    if (choose == 2 && !flagTool) {
                        System.out.println("che carta vuoi usare?");
                        int cards = scanner.nextInt();
                        while(!(cards>0 && cards<4)){
                            System.out.println("errore selezione scegli un altra carta?");
                            cards = scanner.nextInt();
                        }
                        if((new ToolCards(0).checkSpecial(greenCarpet.getToolCard(cards).getSerialNumber()) && flagDice) || greenCarpet.getToolCard(cards).getSerialNumber()==7)
                            System.out.println("non puoi usare questa carta");
                        else if(new ToolCards(0).checkSpecial(greenCarpet.getToolCard(cards).getSerialNumber())){
                            executor.executeToolCard(cards, player.get(i), greenCarpet, ruler, diceBucket);
                            player.get(i).dump();
                            flagTool = true;
                            flagDice=true;
                        }
                        else{
                            executor.executeToolCard(cards, player.get(i), greenCarpet, ruler, diceBucket);
                            player.get(i).dump();
                            flagTool = true;
                        }
                    }
                    if (choose == 1 && !flagDice) {
                        if (ruler.checkAvailable(greenCarpet, player.get(i).getScheme())) {
                            System.out.println("Quale dado vuoi posizionare?");
                            int dice = scanner.nextInt();
                            while(dice<=0 || dice >=greenCarpet.getStock().size()+1){
                                System.out.println("Posizione non valida\nQuale dado vuoi posizionare?");
                                dice = scanner.nextInt();
                            }
                            System.out.println("inserisci riga");
                            int row = scanner.nextInt();
                            System.out.println("inserisci colonna");
                            int col = scanner.nextInt();
                            boolean correct = ruler.checkCorrectPlacement(row, col, greenCarpet.checkDiceFromStock(dice), player.get(i).getScheme());
                            while (!correct) {
                                System.out.println("errore piazzamento.");
                                System.out.println("Quale dado vuoi posizionare?");
                                dice = scanner.nextInt();
                                System.out.println("inserisci riga");
                                row = scanner.nextInt();
                                System.out.println("inserisci colonna");
                                col = scanner.nextInt();
                                correct = ruler.checkCorrectPlacement(row, col, greenCarpet.checkDiceFromStock(dice), player.get(i).getScheme());
                            }
                            player.get(i).getScheme().setBoxes(greenCarpet.getDiceFromStock(dice), row, col);
                            player.get(i).dump();
                            flagDice = true;
                        } else {
                            System.out.println("non è possibile inserire alcun dado");
                            flagDice = true;
                        }
                    }
                    System.out.println("Cosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                    choose=scanner.nextInt();
                    while (choose != 1 && choose != 2 && choose!=3) {
                        System.out.println("Errore scelta\nCosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                        choose = scanner.nextInt();
                    }
                }
            }
            for (int i = numUser-1; i>=0; i--) {
                boolean flagDice=false;
                boolean flagTool=false;
                boolean [] flagRound=new boolean[numUser];
                for (int indBol=0;indBol<numUser;indBol++)
                    flagRound[indBol]=false;

                greenCarpet.dump();
                System.out.println("ROUND "+(j+1)+" TURNO:"+(2));
                System.out.println("tocca a \n" + player.get(i));
                System.out.println("Cosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                int choose = scanner.nextInt();
                while (choose != 1 && choose != 2 && choose!=3) {
                    System.out.println("Errore scelta\nCosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                    choose = scanner.nextInt();
                }
                while(choose!=3) {
                    if (choose == 2 && !flagTool) {
                        System.out.println("che carta vuoi usare?");
                        int cards = scanner.nextInt();
                        while(!(cards>0 && cards<4)){
                            System.out.println("errore selezione scegli un altra carta?");
                            cards = scanner.nextInt();
                        }
                        if((new ToolCards(0).checkSpecial(greenCarpet.getToolCard(cards).getSerialNumber())||greenCarpet.getToolCard(cards).getSerialNumber()==7) && flagDice)
                            System.out.println("non puoi usare questa carta");
                        else if(new ToolCards(0).checkSpecial(greenCarpet.getToolCard(cards).getSerialNumber())||greenCarpet.getToolCard(cards).getSerialNumber()==7){
                            executor.executeToolCard(cards, player.get(i), greenCarpet, ruler, diceBucket);
                            player.get(i).dump();
                            flagTool = true;
                            flagDice=true;
                        }
                        else{
                            executor.executeToolCard(cards, player.get(i), greenCarpet, ruler, diceBucket);
                            player.get(i).dump();
                            flagTool = true;
                        }
                    }
                    if (choose == 1 && !flagDice) {
                        if (ruler.checkAvailable(greenCarpet, player.get(i).getScheme())) {
                            System.out.println("Quale dado vuoi posizionare?");
                            int dice = scanner.nextInt();
                            System.out.println("inserisci riga");
                            int row = scanner.nextInt();
                            System.out.println("inserisci colonna");
                            int col = scanner.nextInt();
                            boolean correct = ruler.checkCorrectPlacement(row, col, greenCarpet.checkDiceFromStock(dice), player.get(i).getScheme());
                            while (!correct) {
                                System.out.println("errore piazzamento.");
                                System.out.println("Quale dado vuoi posizionare?");
                                dice = scanner.nextInt();
                                System.out.println("inserisci riga");
                                row = scanner.nextInt();
                                System.out.println("inserisci colonna");
                                col = scanner.nextInt();
                                correct = ruler.checkCorrectPlacement(row, col, greenCarpet.checkDiceFromStock(dice), player.get(i).getScheme());
                            }
                            player.get(i).getScheme().setBoxes(greenCarpet.getDiceFromStock(dice), row, col);
                            player.get(i).dump();
                            flagDice = true;
                        } else {
                            System.out.println("non è possibile inserire alcun dado");
                            flagDice = true;
                        }
                    }
                    System.out.println("Cosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                    choose=scanner.nextInt();
                    while (choose != 1 && choose != 2 && choose!=3) {
                        System.out.println("Errore scelta\nCosa vuoi fare:\n1)posiziona un dado\n2)usa una carta\n3)passa");
                        choose = scanner.nextInt();
                    }
                }
            }
            greenCarpet.setRoundPath(j+1);
            player.add(player.get(0));
            player.remove(0);
            users.add(users.get(0));
            users.remove(0);
        }
        greenCarpet.dump();
        for(int i=0;i<numUser;i++)
            player.get(i).dump();
        Calculator calculator=new Calculator(player,greenCarpet);
        for(int i=0;i<numUser;i++)
            System.out.println(player.get(i).getNickname()+" punteggio: "+calculator.calculate(i));
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        String s=greenCarpet+"\n";
        for(Player p:player)
            s=s+p.toString()+"\n";
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
