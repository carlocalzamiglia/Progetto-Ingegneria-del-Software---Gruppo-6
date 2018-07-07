package itpolimiingsw.Client;

import com.google.gson.Gson;
import itpolimiingsw.Game.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;




import java.awt.*;
import java.awt.font.GraphicAttribute;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class GUIController extends Application {
    static Stage window;

    StackPane pane1,pane2,pane3,pane4,pane5;
    GridPane pane;
    Scene scene1, scene2,scene3,scene4,scene5;

    private static boolean dicePlaceable =false;
    private static boolean toolUsable = false;
    private static boolean endgamechoose;
    private static boolean newGame;
    private static String[] scorefinal;
    private static boolean login = false;
    private static String[] loginData = new String[2];
    private static int scenechoose = 0;
    private static String[] schemesjson = new String[4];
    private static int schemechose;
    private static String menuaction;
    private static String privateGoalJsonSt;
    private static String dicejsonSt;
    private GridPane myscheme=new GridPane();
    private static int[] placedice = new int[3];
    private static int toolchose;
    private static int current;
    private static String playerScheme;

    private static String greencarpetJsonSt;
    private static String playerJsonSt;

    private static boolean dicechoose = false;
    private static boolean toolchoose = false;


    private static String currentmessage;
    private static boolean newmessage=false;

    private static boolean c = false;


    private static boolean j = false;
    //TOOL VAR



    //tool2-3
    public static int[] dicetoolpos = new int[2];
    public static boolean toolCoord = false;
    public static boolean toolCoordDone = false;

    //tool5
    public static boolean dicePath = false;
    public static int[] pathVal = new int[2];



    static Button play;
    static Button playd;


    //SCENE 4 DECLARATION
    boolean isin = true;
    ImageView imageView2;
    AnchorPane im2=new AnchorPane();
    GridPane scheme = new GridPane();
    GridPane grcRoundTr=new GridPane();
    GridPane grcStock=new GridPane();
    HBox hBoxPubGoal=new HBox(40);
    VBox vboxScheme=new VBox(10);
    HBox hboxTool=new HBox(40);
    VBox vboxTool1=new VBox(5);
    VBox vboxTool2=new VBox(5);
    VBox vboxTool3=new VBox(5);


    HBox markers=new HBox(5);
    HBox hboxOpponentScheme=new HBox(10);
    VBox vBoxCardItems=new VBox(10);
    HBox hboxgrande=new HBox(20);
    VBox vboxGC=new VBox(20);
    VBox vboxButton=new VBox(20);
    static Label timerlab;

    //SCENE 4 END DECLARATION







    public GUIController() throws FileNotFoundException {

    }


    //----------------------------------------------------GETTER----------------------------------------------

    public static int getScheme() {
        return schemechose;
    }       //RITORNA LO SCHEMA SCELTO

    public static boolean getNewGame(){return newGame;}         //RITORNA SE SI HA SCELTO PER LA FINE DELLA PARTITA

    public static boolean getNewGameChosen() {return endgamechoose;}    //RITORNA LA SCELTA DI FINE PARTITA


    public static boolean getDiceChoose(){
        return dicechoose;
    }       //RITORNA SE HAI SCELTO IL DADO DELLA RISERVA

    public static int getDiceChosen(){                                  //RITORNA IL DADO SCELTO DALLA RISERVA
        dicechoose=false;
        return placedice[0];
    }



    public static int getTool() {                                   //RITORNA LA TOOL SCELTA
        setLogin(false);
        toolchoose=false;
        return toolchose;
    }

    public static boolean getToolChoose() {
        return toolchoose;
    }           //RITORNA SE HAI SCELTO LA TOOL DA USARE

    public static boolean getLogin() {
        return login;
    }

    public static String[] getLoginData() {                         //RITORNA I DATI DI LOGIN
        return loginData;
    }               //RITORNA I DATI DI LOGIN

    public static int[] getplaceDice() {
        return placedice;
    }               //RITORNA DADO SCELTO + NUOVA POSIZIONE


    public static int getTool1Val() {   //TOOL1 VALORE INCREMENTATO O DECREMENTATO
        AtomicInteger tool1res= new AtomicInteger();
        tool1res.set(0);
        Platform.runLater(() ->{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Pinza Sgrossatrice");
            alert.setContentText("Scegli un'opzione.");

            ButtonType buttonTypeOne = new ButtonType("Incrementa");
            ButtonType buttonTypeTwo = new ButtonType("Decrementa");
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                tool1res.set(1);
            } else if (result.get() == buttonTypeTwo) {
                tool1res.set(2);
            }
        });
        while(tool1res.get()==0 && !c){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        if(c) {
            return 99;
        }
        System.out.println("SCELTA: "+tool1res.get());
        return tool1res.get();
    }

    public static int getTool11(String colour) {
        AtomicBoolean flag= new AtomicBoolean(false);
        AtomicInteger tool11res= new AtomicInteger();
        tool11res.set(0);
        Platform.runLater(() ->{
            List<Integer> choices = new ArrayList<Integer>();
            choices.add(1);
            choices.add(2);
            choices.add(3);
            choices.add(4);
            choices.add(5);
            choices.add(6);


            ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(1, choices);
            dialog.setTitle("Diluente per Pasta Salda");
            dialog.setHeaderText("Il dado pescato è di colore: "+colour+".");
            dialog.setContentText("Scegli il nuovo valore:");
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent())
                tool11res.set(result.get());
            else
                flag.set(true);
        });
        while(tool11res.get()==0 && !flag.get() && !c){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        if(c)
            return 99;
        if(flag.get())
            return getTool11(colour);
        return tool11res.get();
    }

    public static int getndice12() {
        AtomicInteger tool12res= new AtomicInteger();
        tool12res.set(0);
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Taglierina Manuale");
            alert.setHeaderText("Quanti dadi vuoi spostare?");
            alert.setContentText("Scegli opzione:");

            ButtonType buttonTypeOne = new ButtonType("Uno");
            ButtonType buttonTypeTwo = new ButtonType("Due");


            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                tool12res.set(1);
            } else if (result.get() == buttonTypeTwo) {
                tool12res.set(2);
            } else {
                getndice12();
            }});


        while(tool12res.get()==0 && !c){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        if(c) {
            return 99;
        }
        return tool12res.get();
    }

    public static void showTool6(String dicejson){
        dicejsonSt=dicejson;
        scenechoose=6;
    }

    public static boolean getToolCoordDone(){
        return toolCoordDone;
    }

    public static int[] getToolCoord() {
        toolCoordDone =false;
        return dicetoolpos;
    }


    public static boolean getDicePath() {
        return dicePath;
    }

    public static int[] getPathVal() {
        setDicePath();
        return pathVal;
    }


    //----------------------------------------------------SETTER----------------------------------------------

    public static void setDicePath() {
        dicePath=false;
    }
    public static void setNewGameChosen(boolean choose){endgamechoose=choose;}
    public static void setLogin(boolean bool) {
        login=bool;
    }
    public static void setDiceChose(boolean chosedice) {
         dicePlaceable = chosedice;
    }

    public static void setToolCoord(){
        toolCoord =true;
        toolCoordDone = false;
    }



    public static void showTimer(int i) {
        Platform.runLater(() ->{
                play.setText("Gioca (" + i + ")");
                timerlab.setText("Timer: "+i);
        });
    }


    public void launchgui(){
        launch();
    }


    BackgroundFill myBF = new BackgroundFill(Color.rgb(0, 0, 0, 0), new CornerRadii(1),
            new Insets(0.0, 0.0, 0.0, 0.0));

    BackgroundImage myBI= new BackgroundImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/image/screen.jpg")),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    //final double initialSceneWidth = 2000;
    //final double initialSceneHeight = 1000;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=new Stage();
        window=primaryStage;
        window.setTitle("Sagrada");





        //object 1 layout
        Label username_l = new Label("Username");
        username_l.setPrefSize(300,50);
        username_l.setFont(new Font(40.0));
        TextField username_t = new TextField();
        username_t.setPrefSize(300,50);
        username_t.setFont(new Font(30.0));
        Label password_l = new Label("Password");
        password_l.setPrefSize(300,50);
        password_l.setFont(new Font(40.0));
        PasswordField password_t = new PasswordField();
        password_t.setPrefSize(300,50);
        password_t.setFont(new Font(30.0));
        Button login = new Button("LOGIN");
        login.setPrefSize(300,100);
        login.setFont(new Font(30.0));
        Image image1 = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/sagrada.jpg"));
        ImageView imageView = new ImageView(image1);
        imageView.setFitWidth(900);
        imageView.setFitHeight(300);
        //layout 1
        HBox usernameLayout = new HBox(10);
        usernameLayout.getChildren().addAll(username_l, username_t);
        usernameLayout.setAlignment(Pos.CENTER);
        HBox passwordLayout = new HBox(10);
        passwordLayout.getChildren().addAll(password_l, password_t);
        passwordLayout.setAlignment(Pos.CENTER);
        VBox loginLayout = new VBox(40);
        loginLayout.getChildren().addAll(imageView, usernameLayout, passwordLayout,login);
        loginLayout.setAlignment(Pos.CENTER);
        pane1 = new StackPane(loginLayout);
        pane1.setBackground(new Background(myBI));
        scene1 = new Scene(pane1);



        SceneThread sceneThread = new SceneThread(this);
        sceneThread.start();
        //MessageThread messageThread=new MessageThread(this);
        //messageThread.start();
        window.setScene(scene1);
        current=1;

        window.setResizable(false);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        window.show();



        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());




        login.setOnAction(event -> {
            loginAction(username_t.getText(), password_t.getText());
        });

        scene1.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                loginAction(username_t.getText(), password_t.getText());
            }
        });



    }

    private void loginAction(String username, String password){
        //prova connessione
        if(!username.isEmpty() && !password.isEmpty()){
            boolean flag = false;
            for(int j = 0; j<username.length(); j++){
                if(username.charAt(j)=='-'){
                    flag=true;
                }
            }
            if(username.charAt(0)!=' ' && !flag) {
                loginData[0] = username;
                loginData[1] = password;
                setLogin(true);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERRORE");
                alert.setContentText("Hai inserito dei caratteri non validi.");
                alert.showAndWait();
            }
        }
    }
    public ImageView imageToImageV(Image image,int h,int w){
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        return imageView;
    }

    public GridPane setSchemeChose(String js1,String js2,String js3,String js4) throws IOException {
        GridPane pane = new GridPane();
        Gson gson = new Gson();
        Scheme s1 = gson.fromJson(js1, Scheme.class);
        Scheme s2 = gson.fromJson(js2, Scheme.class);
        Scheme s3 = gson.fromJson(js3, Scheme.class);
        Scheme s4 = gson.fromJson(js4, Scheme.class);
        GridPane sc1 = setScheme(s1,350,280);
        GridPane sc2 = setScheme(s2,350,280);
        GridPane sc3 = setScheme(s3,350,280);
        GridPane sc4 = setScheme(s4,350,280);
        Label label1=new Label(s1.getName());
        label1.setFont(Font.font(null,FontWeight.BOLD,40));
        Label label2=new Label(s2.getName());
        label2.setFont(Font.font(null,FontWeight.BOLD,40));
        Label label3=new Label(s3.getName());
        label3.setFont(Font.font(null,FontWeight.BOLD,40));
        Label label4=new Label(s4.getName());
        label4.setFont(Font.font(null,FontWeight.BOLD,40));


        ToggleGroup group1=new ToggleGroup();
        RadioButton r1=new RadioButton("\tdifficolta: "+difficultyToStr(s1.getDifficulty()));
        r1.setFont(new Font(30));
        r1.setSelected(true);
        RadioButton r2=new RadioButton("\tdifficolta: "+difficultyToStr(s2.getDifficulty()));
        r2.setFont(new Font(30));
        RadioButton r3=new RadioButton("\tdifficolta: "+difficultyToStr(s3.getDifficulty()));
        r3.setFont(new Font(30));
        RadioButton r4=new RadioButton("\tdifficolta: "+difficultyToStr(s4.getDifficulty()));
        r4.setFont(new Font(30));

        r1.setToggleGroup(group1);
        r2.setToggleGroup(group1);
        r3.setToggleGroup(group1);
        r4.setToggleGroup(group1);

        pane.add(label1,0,0);
        pane.add(label2,1,0);
        pane.add(sc1,0,1);
        pane.add(sc2,1,1);
        pane.add(r1,0,2);
        pane.add(r2,1,2);
        pane.add(label3,0,3);
        pane.add(label4,1,3);
        pane.add(sc3,0,4);
        pane.add(sc4,1,4);
        pane.add(r3,0,5);
        pane.add(r4,1,5);
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane .setHgap(10);
        pane.setVgap(20);
        return pane;

    }
    public GridPane setScheme(Scheme scheme,double width,double height) throws IOException {

        GridPane gridPane = new GridPane();
        for(int i=0;i<4;i++){
            for(int j=0;j<5;j++){
                Canvas canvas = getDiceDraws(scheme.getBox(i, j), scheme.getBox(i, j).getAddedDice(), height/4, width/5, true);
                gridPane.add(canvas, j, i);
            }
        }
        gridPane.setMinSize(width, height);
        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }
    public HBox setMarkers(int num){
        HBox hBox = new HBox(5);
        for(int i=0; i<num; i++){
            hBox.getChildren().add(getMarkerDraws(50, 50));
        }
        return hBox;
    }

    public GridPane setStock(GreenCarpet gc) throws FileNotFoundException {
        GridPane stock=new GridPane();
        stock.setVgap(8.0);
        for(int i=0;i<gc.getStock().size();i++){
            Canvas canvas = getDiceDraws(null, gc.checkDiceFromStock(i+1), 55.0, 55.0, false);
            GraphicsContext gcc = canvas.getGraphicsContext2D();
            gcc.setFill(Color.TRANSPARENT);
            gcc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            stock.add(canvas,i,0);
        }
        return  stock;

    }public GridPane setRound(GreenCarpet gc) throws FileNotFoundException {
        GridPane roundTr=new GridPane();
        roundTr.setHgap(4.0);
        roundTr.setVgap(4.0);
        for(int i=0;i<gc.getnPlayers()*2+1;i++){
            for(int j=0;j<10;j++){
                Canvas canvas = new Canvas(40, 40);
                if(gc.getDiceFromRoundPath(i+1,j+1)!=null) {
                    canvas = getDiceDraws(null, gc.getDiceFromRoundPath(i+1, j+1), 38.0, 38.0, false);
                    GraphicsContext gcc = canvas.getGraphicsContext2D();
                    gcc.setFill(Color.TRANSPARENT);
                    gcc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                roundTr.add(canvas,j,i);
            }
        }
        return roundTr;

    }


    public Image valueToImage(String string) throws IOException {
        Image image=new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/restriction/dado"+string+".png"));
        return image;
    }
    public Image colorToImage(Colour colour) throws IOException {
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/restriction/dado"+colour.toString()+".png"));
        return image;
    }
    public Image diceToImage(Dice dice) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/dice/"+dice.faceToNo()+dice.getColour()+".png"));
        return image;
    }
    public Image numbToImage_PrG(int numb) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/privateGoal/"+toStr(numb)+".jpg"));
        return image;
    }
    public Image numbToImage_PuG(int numb) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/publicGoal/"+toStr(numb)+".jpg"));
        return image;
    }
    public Image numbToTool(int numb) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/ToolCard/"+toStr(numb)+".jpg"));
        return image;
    }


    public static void showSchemes(String scheme1, String scheme2, String scheme3, String scheme4,String privateGoalJson) {
        schemesjson[0]=scheme1;
        schemesjson[1]=scheme2;
        schemesjson[2]=scheme3;
        schemesjson[3]=scheme4;
        privateGoalJsonSt=privateGoalJson;
        scenechoose=1;
    }
    public static void updateGreenCarpet(String greencarpetJson, String playerJson){
        greencarpetJsonSt=greencarpetJson;
        playerJsonSt=playerJson;
        scenechoose=2;

    }


    public void setScene2(String messages, String title ){

        //object second layout
        Label match=new Label(title);
        Label message=new Label();
        message.setText(messages);
        match.setFont(Font.font(null, FontWeight.BOLD,80));
        message.setFont(Font.font(null,FontWeight.BOLD,50));
        //second layout
        VBox vBox=new VBox(match,message);
        vBox.setAlignment(Pos.CENTER);
        pane2=new StackPane(vBox);
        pane2.setBackground(new Background(myBI));
        try {
            scene2 = new Scene(pane2);
        }catch (IllegalArgumentException e){}
    }

    public void setScene3(GridPane pane,String privateGoalJson) throws FileNotFoundException {
        timerlab = new Label(); //FOR SCENE 4, TIMER
        //object third layout
        HBox hBox= new HBox(100);
        VBox vbox = new VBox(20);
        play= new Button();
        play.setText("Gioca");
        play.setPrefSize(300,100);

        play.setFont(new Font(30.0));
        Gson gson=new Gson();
        PrivateGoal privateGoal=gson.fromJson(privateGoalJson,PrivateGoal.class);
        ImageView imageView1=new ImageView(numbToImage_PrG(privateGoal.getSerialNumber()));
        AnchorPane im=new AnchorPane(imageView1);

        pane.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(im,play);
        vbox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(pane, vbox);
        hBox.setAlignment(Pos.CENTER);
        pane3 = new StackPane(hBox);
        pane3.setBackground(new Background(myBI));
        scene3 = new Scene(pane3);

        RadioButton r1=(RadioButton)pane.getChildren().get(4);
        RadioButton r2=(RadioButton)pane.getChildren().get(5);
        RadioButton r3=(RadioButton)pane.getChildren().get(10);
        RadioButton r4=(RadioButton)pane.getChildren().get(11);
        play.setOnAction(e -> {

            schemeChosenDone(r1, r2, r3, r4);
        });

        scene3.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                schemeChosenDone(r1, r2, r3, r4);
            }
        });

    }

    public void schemeChosenDone(RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4){
        if(r1.isSelected()){
            System.out.println("1");
            schemechose=1;
            myscheme.getChildren().add(pane.getChildren().get(2));
        }
        else if(r2.isSelected()){
            System.out.println("2");
            schemechose=2;
            myscheme.getChildren().add(pane.getChildren().get(3));
        }
        else if(r3.isSelected()){
            System.out.println("3");
            schemechose=3;
            myscheme.getChildren().add(pane.getChildren().get(8));
        }
        else if(r4.isSelected()){
            System.out.println("4");
            schemechose=4;
            myscheme.getChildren().add(pane.getChildren().get(9));
        }
        setScene2("", "ATTENDI IL TUO TURNO...");
        window.setScene(scene2);
        current=2;
        setLogin(true);
    }

    public void setScene4(String greenCarpetJson,String playerJson,int flag, boolean visible) throws IOException {
        pane2.getChildren().clear();
        pane3.getChildren().clear();

        Gson gson = new Gson();
        GreenCarpet gc = gson.fromJson(greenCarpetJson, GreenCarpet.class);
        int first = gc.getToolCard(1).getSerialNumber();
        int second = gc.getToolCard(2).getSerialNumber();
        int third = gc.getToolCard(3).getSerialNumber();
        Player player = gson.fromJson(playerJson, Player.class);
        Scheme[] schemes = new Scheme[3];
        String[] opname=new String[3];

        int k = 0;
        for (int i = 0; i < gc.getnPlayers(); i++) {
            if (!gc.getPlayer().get(i).getNickname().equals(player.getNickname())) {
                schemes[k] = gc.getPlayer().get(i).getScheme();
                opname[k]=gc.getPlayer().get(i).getNickname();
                k++;
            }
        }

        //clear
        //vboxTool1.getChildren().clear();
        //vboxTool2.getChildren().clear();
        //vboxTool3.getChildren().clear();
        //hboxTool.getChildren().clear();
        //hBoxPubGoal.getChildren().clear();
        vboxScheme.getChildren().clear();
        hboxOpponentScheme.getChildren().clear();
        vboxGC.getChildren().clear();
        vBoxCardItems.getChildren().clear();
        hboxgrande.getChildren().clear();
        vboxButton.getChildren().clear();

        Label cost1 = new Label("Costo: " + gc.getToolCard(1).getCost());
        Label cost2 = new Label("Costo: " + gc.getToolCard(2).getCost());
        Label cost3 = new Label("Costo: " + gc.getToolCard(3).getCost());

        cost1.setFont(Font.font(null, FontWeight.BOLD, 30));
        cost2.setFont(Font.font(null, FontWeight.BOLD, 30));
        cost3.setFont(Font.font(null, FontWeight.BOLD, 30));

        if(gc.getRound()==0 && isin){
            //IMAGES
            vboxTool1.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(1).getSerialNumber()), 320, 230), cost1);
            vboxTool2.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(2).getSerialNumber()), 320, 230), cost2);
            vboxTool3.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(3).getSerialNumber()), 320, 230), cost3);
            hboxTool.getChildren().addAll(vboxTool1, vboxTool2, vboxTool3);

            hBoxPubGoal.getChildren().addAll(imageToImageV(numbToImage_PuG(gc.getPublicGoal(0).getSerialNumber()), 320, 230), imageToImageV(numbToImage_PuG(gc.getPublicGoal(1).getSerialNumber()), 320, 230), imageToImageV(numbToImage_PuG(gc.getPublicGoal(2).getSerialNumber()), 320, 230));

            imageView2 = imageToImageV(numbToImage_PrG(player.getPrivateGoal().getSerialNumber()), 350, 250);
            im2 = new AnchorPane(imageView2);
            isin = false;
        }

        vboxTool1.getChildren().remove(1);
        vboxTool2.getChildren().remove(1);
        vboxTool3.getChildren().remove(1);
        vboxTool1.getChildren().add(1, cost1);
        vboxTool2.getChildren().add(1, cost2);
        vboxTool3.getChildren().add(1, cost3);




        playd = new Button("Piazza dado");
        playd.setFont(new Font(30));
        playd.setPrefSize(300, 50);
        playd.setVisible(visible);
        playd.setAlignment(Pos.CENTER);

        Button useTool = new Button("Usa carta");
        useTool.setFont(new Font(30));
        useTool.setPrefSize(300, 50);
        useTool.setAlignment(Pos.CENTER);
        useTool.setVisible(visible);

        Button pass = new Button("Passa");
        pass.setPrefSize(300, 50);
        pass.setFont(new Font(30));
        pass.setAlignment(Pos.CENTER);
        pass.setVisible(visible);

        vboxButton.getChildren().addAll(playd, useTool, pass);


        scheme.getChildren().clear();
        scheme = setScheme(player.getScheme(), 350, 280);
        markers.getChildren().clear();
        markers = setMarkers(player.getMarkers().size());


        grcRoundTr.getChildren().clear();
        grcStock.getChildren().clear();
        grcRoundTr = setRound(gc);
        grcStock = setStock(gc);


        Label name = new Label();
        name.setText(player.getScheme().getName() + "  " + player.getScheme().difficultyToString());
        name.setFont(Font.font(null, FontWeight.BOLD, 20));
        name.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));


        Label stock = new Label("RISERVA");
        stock.setFont(Font.font(null, FontWeight.BOLD, 30));
        Label roundTr = new Label("TRACCIATO DEI ROUND");
        roundTr.setFont(Font.font(null, FontWeight.BOLD, 30));
        timerlab = new Label();
        Label playerLabel = new Label();
        playerLabel.setText("Giocatore: "+player.getNickname());
        timerlab.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        playerLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        timerlab.setFont(Font.font(null, FontWeight.BOLD, 20));
        playerLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        Label roundTurnLabel = new Label();
        roundTurnLabel.setText("ROUND: "+(gc.getRound()+1)+ "  TURNO: "+gc.getTurn());
        roundTurnLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        roundTurnLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        vboxButton.prefHeightProperty().bind(vboxScheme.heightProperty());
        im2.prefHeightProperty().bind(vboxScheme.heightProperty());
        scheme.prefHeightProperty().bind(vboxScheme.heightProperty());
        vboxScheme.getChildren().addAll(timerlab, roundTurnLabel, playerLabel, scheme, name, markers, im2);


        for (int i = 0; i < gc.getnPlayers() - 1; i++) {
            VBox opponent = new VBox();
            Label opponentname = new Label(opname[i]);
            opponentname.setFont(Font.font(null, FontWeight.BOLD, 20));
            opponent.getChildren().addAll(setScheme(schemes[i], 250, 200), opponentname);
            hboxOpponentScheme.getChildren().add(opponent);

        }

        vBoxCardItems.getChildren().addAll(hboxTool, hBoxPubGoal, hboxOpponentScheme);




        vboxGC.getChildren().addAll(roundTr,grcRoundTr,stock,grcStock, vboxButton);


        vboxScheme.prefHeightProperty().bind(hboxgrande.heightProperty());
        hboxgrande.prefHeightProperty().bind(scene1.heightProperty());
        hboxgrande.prefWidthProperty().bind(scene1.widthProperty());
        hboxgrande.getChildren().setAll(vboxGC, vboxScheme, vBoxCardItems);
        pane4 = new StackPane(hboxgrande);
        pane4.setBackground(new Background(myBI));
        scene4 = new Scene(pane4);


        grcStock.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {                 //BOTTONI RISERVA
                @Override
                public void handle(MouseEvent event) {
                    if (dicePlaceable) {
                        Node source = (Node) event.getSource();
                        placedice[0] = GridPane.getColumnIndex(source) + 1;
                        dicechoose = true;
                        dicePlaceable = false;
                    }
                }
            });
        });

        grcRoundTr.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {                 //BOTTONI PATH
                @Override
                public void handle(MouseEvent event) {
                    Node source = (Node) event.getSource();
                    pathVal[1] = GridPane.getRowIndex(source) + 1;
                    pathVal[0] = GridPane.getColumnIndex(source) + 1;
                    dicePath = true;
                }
            });

        });

        scheme.getChildren().forEach(item2 -> {
            item2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (dicechoose) {
                        Node source2 = (Node) event.getSource();
                        placedice[1] = GridPane.getRowIndex(source2);
                        placedice[2] = GridPane.getColumnIndex(source2);
                        dicechoose = false;
                        dicePlaceable = false;
                        setLogin(true);
                    }
                    if (toolCoord) {
                        Node source2 = (Node) event.getSource();
                        dicetoolpos[0] = GridPane.getRowIndex(source2);
                        dicetoolpos[1] = GridPane.getColumnIndex(source2);
                        toolCoordDone = true;
                        toolCoord = false;
                    }
                }
            });
        });

        pass.setOnAction(e -> {
            menuaction = "1";
            setLogin(true);
        });
        playd.setOnAction(e -> {
            menuaction = "2";
            dicePlaceable = true;
            setLogin(true);
        });
        useTool.setOnAction(e -> {
            menuaction = "3";
            toolUsable = true;
            setLogin(true);
        });

        int finalFirst = first;
        vboxTool1.getChildren(). get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if (toolUsable) {
                    toolchose = finalFirst;
                    toolchoose = true;
                    toolUsable = false;
                }
            }
        });
        int finalSecond = second;
        vboxTool2.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if (toolUsable) {
                    toolchose = finalSecond;
                    toolchoose = true;
                    toolUsable = false;
                }
            }
        });
        int finalThird = third;
        vboxTool3.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if (toolUsable) {
                    toolchose = finalThird;
                    toolchoose = true;
                    toolUsable = false;
                }
            }
        });


    }

    public void setScene5(String [] score ){
        Label scoreL[]=new Label[score.length];
        VBox vBoxscene5=new VBox(40);
        Label labelTop=new Label("CLASSIFICA");
        labelTop.setFont(Font.font(null,FontWeight.BOLD,200));
        vBoxscene5.setAlignment(Pos.CENTER);
        vBoxscene5.getChildren().add(labelTop);
        HBox hBoxscene5=new HBox(40);
        Button replay=new Button("Gioca ancora");
        Button quit = new Button("LOGOUT");
        hBoxscene5.getChildren().addAll(replay,quit);
        for(int i=0;i<score.length;i++){
            scoreL[i]=new Label();
            scoreL[i].setFont(Font.font(null,FontWeight.BOLD,70-(i*10)));
            scoreL[i].setText(score[i]);
            vBoxscene5.getChildren().add(scoreL[i]);
        }
        vBoxscene5.getChildren().add(hBoxscene5);
        pane5 = new StackPane(vBoxscene5);
        pane5.setBackground(new Background(myBI));
        scene5 = new Scene(pane5);

        replay.setOnAction(e ->{
            endgamechoose=true;
            newGame=true;

            setScene2("", "MATCHMAKING...");
            current=2;
            Platform.runLater(() ->{
                window.setScene(scene2);

            });
        });
        quit.setOnAction(e ->{
            endgamechoose=true;
            newGame=false;
        });

    }

    public static void loginOK() {
        scenechoose = 3;
        current=2;
    }

    public static void showPopup(String[] message) {
        System.out.println(message[1]);
        Platform.runLater(() ->{
            dicechoose=false;
            setLogin(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText(message[0]);
            alert.setContentText(message[1]);
            alert.showAndWait();
        });

    }

    public static void showPopupDicePlaceWrong(String[] message) {
        System.out.println(message[1]);
        Platform.runLater(() ->{
            dicechoose=false;
            setLogin(false);
            dicePlaceable=true;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText(message[0]);
            alert.setContentText(message[1]);
            alert.show();

        });

    }

    public static void showConnDiscPopup(String message){
        Platform.runLater(() ->{
            setLogin(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText(message);
            alert.show();
        });
    }

    public static void showToolPopup(String tool, String message){
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Utilizzo della carta tool.");
            alert.setHeaderText(tool);
            alert.setContentText(message);
            alert.show();
        });
    }

    public static String handleTurnMenu() {
        return menuaction;
    }

    public static void chooseAction(int scene) {
        scenechoose=scene;
    }

    public static void endTurn() {
        scenechoose=2;
    }



    public static void updateView(String greencarpet, String playerJson) {
        playerJsonSt=playerJson;
        greencarpetJsonSt=greencarpet;
        scenechoose=4;
    }

    public static void showScore(String[] score) {
        scorefinal=score;
        scenechoose=5;
    }

    public static void showMessages(String messages) {
    }

    private String toStr(int numb){
        String string=new String();
        if(numb==1){
            string="one";
        }else if(numb==2){
            string="two";
        }else if(numb==3){
            string="three";
        }else if(numb==4){
            string="four";
        }else if(numb==5){
            string="five";
        }else if(numb==6){
            string="six";
        }else if(numb==7){
            string="seven";
        }else if(numb==8){
            string="eight";
        }else if(numb==9){
            string="nine";
        }else if(numb==10){
            string="ten";
        }else if(numb==11) {
            string = "eleven";
        }else if(numb==12) {
            string = "twelve";
        }
        else
            string="empty";
        return string;








    }

    private String difficultyToStr(int numb){
        String string=new String();
        for(int i=0;i<numb;i++){
            string=string+"*";
        }
        return string;
    }

    public static void timerOut(boolean end) {
        c = end;
    }


    //---------------------------------------------TOOL METHODS---------------------------------------------------------


    public class SceneThread extends Thread{
        private GUIController GUIController;
        public  SceneThread(GUIController GUIController){
            this.GUIController = GUIController;
        }
        @Override
        public void run() {
            while (10>0) {
                while(scenechoose==0){
                    try {
                        sleep(200);
                    } catch (InterruptedException e) { }
                }
                if(scenechoose==1) {  //TIME TO SHOW SCHEMES
                    System.out.println("a");

                    try {
                        pane = setSchemeChose(schemesjson[0], schemesjson[1], schemesjson[2], schemesjson[3]);
                    } catch (IOException e) {
                        System.out.println("catch beccata");
                    }
                    try {
                        GUIController.setScene3(pane,privateGoalJsonSt);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        Platform.runLater(()->{
                            window.setScene(scene3);
                            current=3;
                        });
                    } catch (NullPointerException e) {
                    }
                }
                else if(scenechoose==2){        //set scene 4 without buttons
                    System.out.println("tavolo aggiornato");
                    Platform.runLater(()->{
                        try {
                            GUIController.setScene4(greencarpetJsonSt,playerJsonSt,1, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        window.setScene(scene4);
                        current=4;
                    });
                }
                else if (scenechoose == 3){
                    GUIController.setScene2("", "MATCHMAKING...");
                    current=2;
                    Platform.runLater(() ->{
                        window.setScene(scene2);
                    });
                }
                else if(scenechoose==4){      //set scene 4 with buttons

                    Platform.runLater(()->{
                        try {
                            GUIController.setScene4(greencarpetJsonSt, playerJsonSt,1 ,true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        window.setScene(scene4);
                    });
                }
                else if(scenechoose==5){
                    Platform.runLater(() ->{
                        GUIController.setScene5(scorefinal);
                        window.setScene(scene5);
                        current=5;
                    });
                }
                else if(scenechoose==6){
                    Gson gson = new Gson();
                    Dice dice = gson.fromJson(dicejsonSt, Dice.class);
                    Platform.runLater(() ->{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Pennello per Pasta Salda");
                        alert.setHeaderText("Dado");
                        alert.setContentText("Il dado è stato lanciato, ora scegli dove piazzarlo.");
                        alert.getDialogPane().getChildren().add(getDiceDraws(null, dice, 50.0, 50.0, false));
                        alert.showAndWait();
                    });
                }
                else if(scenechoose==7){
                    System.out.println("aaaa");
                    GUIController.setScene2("", "ATTENDI IL TUO TURNO...");
                    Platform.runLater(()->{
                        window.setScene(scene2);
                    });
                }
                scenechoose = 0;
            }

        }
    }

    //METHODS FOR DICE's DRAWING
    private Canvas getDiceDraws(Box box, Dice dice,Double h,Double w, boolean flag) {
        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc =  canvas.getGraphicsContext2D();
        if(box!=null) {
            if (box.getAddedDice() == null) {
                if (box.getRestrictionColour() != null) {   //JUST COLOR
                    gc.setFill(box.getRestrictionColour().getfxColor());
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                } else if (box.getRestrictionValue() != null) {   //JUST VALUES
                    gc.setFill(Color.LIGHTGREY);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    diceDraw(gc, Integer.parseInt(box.getRestrictionValue()), canvas.getHeight(), canvas.getWidth());
                }
                else{
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
            }
        }
        if(dice!=null) {   //THE ALL DICE
            if(flag) {
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
            gc.setFill(dice.getColour().getfxColor());
            gc.fillRoundRect(canvas.getWidth()/24, canvas.getHeight()/24, canvas.getWidth()-(canvas.getWidth()/12), canvas.getHeight()-(canvas.getHeight()/12), canvas.getWidth()/4, canvas.getHeight()/4);
            diceDraw(gc, Integer.parseInt(dice.faceToNo()), canvas.getHeight(), canvas.getWidth());
        }
        return canvas;
    }

    private Canvas getMarkerDraws(double w, double h){
        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval((canvas.getWidth()-(2*canvas.getWidth()/3))/2, (canvas.getWidth()-2*(canvas.getWidth()/3))/2, 2*canvas.getWidth()/3, 2*canvas.getHeight()/3);
        return canvas;
    }

    private void diceDraw(GraphicsContext gc, int i, double h, double w){
        switch (i){
            case 1:
                gc.setFill(Color.BLACK);
                gc.fillOval((w-(w/5))/2, (h-(h/5))/2, w/5, h/5);
                break;
            case 2:
                gc.setFill(Color.BLACK);
                gc.fillOval(w/10, h/10, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*7, w/5, h/5);
                break;
            case 3:
                gc.setFill(Color.BLACK);
                gc.fillOval(w/10, h/10, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*7, w/5, h/5);
                gc.fillOval((w-(w/5))/2, (h-(h/5))/2, w/5, h/5);
                break;
            case 4:
                gc.setFill(Color.BLACK);
                gc.fillOval(w/10, h/10, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*7, w/5, h/5);
                gc.fillOval(w/10, (h/10)*7, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10), w/5, h/5);
                break;
            case 5:
                gc.setFill(Color.BLACK);
                gc.fillOval(w/10, h/10, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*7, w/5, h/5);
                gc.fillOval(w/10, (h/10)*7, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10), w/5, h/5);
                gc.fillOval((w-(w/5))/2, (h-(h/5))/2, w/5, h/5);
                break;
            case 6:
                gc.setFill(Color.BLACK);
                gc.fillOval(w/10, h/10, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*7, w/5, h/5);
                gc.fillOval(w/10, (h/10)*7, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10), w/5, h/5);
                gc.fillOval(w/10, (h/10)*4, w/5, h/5);
                gc.fillOval((w/10)*7, (h/10)*4, w/5, h/5);
                break;

        }
    }

}



