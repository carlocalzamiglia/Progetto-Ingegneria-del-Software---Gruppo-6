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
import javafx.scene.control.*;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    //TOOL VAR


    //tool2-3
    public static int[] dicetoolpos = new int[2];
    public static boolean toolCoord = false;
    public static boolean toolCoordDone = false;

    //tool5
    public static boolean dicePath = false;
    public static int[] pathVal = new int[2];





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



    //getter tool
    public static String goOn() {   //SCEGLI SE CONTINUARE CON L'UTILIZZO DELLA TOOL
        AtomicInteger tool1res= new AtomicInteger();
        tool1res.set(0);
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sei sicuro di voler proseguire?");
            alert.setContentText("Scegli un'opzione.");

            ButtonType buttonTypeOne = new ButtonType("Continua");
            ButtonType buttonTypeTwo = new ButtonType("Torna al menù");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            boolean tool1=true;
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                tool1res.set(1);
            } else if (result.get() == buttonTypeTwo) {
                tool1res.set(2);
            }
        });
        while(tool1res.get()==0){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        if(tool1res.get()==1)
            return "y";
        else
            return "n";
    }


    public static int getTool1Val() {   //TOOL1 VALORE INCREMENTATO O DECREMENTATO
        AtomicInteger tool1res= new AtomicInteger();
        tool1res.set(0);
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Pinza Sgrossatrice");
            alert.setContentText("Scegli un'opzione.");

            ButtonType buttonTypeOne = new ButtonType("Incrementa");
            ButtonType buttonTypeTwo = new ButtonType("Decrementa");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            boolean tool1=true;
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                tool1res.set(1);
            } else if (result.get() == buttonTypeTwo) {
                tool1res.set(2);
            }
        });
        while(tool1res.get()==0){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        System.out.println("SCELTA: "+tool1res.get());
        return tool1res.get();
    }

    public static int getTool11(String colour) {
        AtomicBoolean flag= new AtomicBoolean(false);
        AtomicInteger tool11res= new AtomicInteger();
        tool11res.set(0);
        System.out.println("a");
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
        while(tool11res.get()==0 && !flag.get()){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
        }
        if(flag.get())
            return getTool11(colour);
        return tool11res.get();
    }

    public static int getndice12() {
        AtomicInteger tool12res= new AtomicInteger();
        tool12res.set(0);
        Platform.runLater(() ->{Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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


        while(tool12res.get()==0){
            try {
                sleep(200);
            } catch (InterruptedException e) { }
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
        MessageThread messageThread=new MessageThread(this);
        messageThread.start();
        window.setScene(scene1);
        current=1;
        window.show();



        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());



        login.setOnAction(event -> {
            //prova connessione
            if(!username_t.getText().trim().isEmpty() && !password_t.getText().trim().isEmpty()){
                loginData[0] = username_t.getText();
                loginData[1] = password_t.getText();
                setLogin(true);
            }
        });

        scene1.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                if(!username_t.getText().trim().isEmpty() && !password_t.getText().trim().isEmpty()){
                    loginData[0]=username_t.getText();
                    loginData[1]=password_t.getText();
                    setLogin(true);
                }
            }
        });



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
        GridPane sc1 = setScheme(s1,50,350,250);
        GridPane sc2 = setScheme(s2,50,350,250);
        GridPane sc3 = setScheme(s3,50,350,250);
        GridPane sc4 = setScheme(s4,50,350,250);
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
    public GridPane setScheme(Scheme scheme,int sizeIm,int width,int height) throws IOException {

        GridPane gridPane = new GridPane();

        for(int i=0;i<4;i++){
            for(int j=0;j<5;j++){
                Button b = new Button();
                ImageView testImageView = new ImageView();
                if(scheme.getBox(i,j).getAddedDice()==null){
                    if(scheme.getBox(i,j).getRestrictionColour()!=null){
                        testImageView = new ImageView(colorToImage(scheme.getBox(i,j).getRestrictionColour()));
                    }
                    else if(scheme.getBox(i,j).getRestrictionValue()!=null){
                        testImageView = new ImageView(valueToImage(scheme.getBox(i,j).getRestrictionValue()));
                    }
                }
                else{
                    testImageView=new ImageView(diceToImage(scheme.getBox(i,j).getAddedDice()));
                }
                testImageView.setFitWidth(sizeIm);
                testImageView.setFitHeight(sizeIm);
                b.setGraphic(testImageView);
                gridPane.add(b,j,i);
            }
        }
        gridPane.setMinSize(width, height);
        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }
    public HBox setDifficulty(int num) throws FileNotFoundException {
        HBox pane=new HBox(5);
        for(int i=0;i<num;i++){
            ImageView imageV =imageToImageV (new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/image/markers.png")),50,50);
            pane.getChildren().add(imageV);
        }
        return pane;
    }


    public GridPane setStockAndRound(GreenCarpet gc) throws FileNotFoundException {
        GridPane roundTr=new GridPane();
        GridPane stock=new GridPane();
        GridPane grc=new GridPane();
        for(int i=0;i<gc.getStock().size();i++){
            Button b=new Button();
            ImageView imageView = new ImageView(diceToImage(gc.checkDiceFromStock(i+1)));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            b.setBackground(new Background(myBF));
            b.setGraphic(imageView);
            stock.add(b,0,i);
        }
        for(int i=0;i<gc.getnPlayers()*2+1;i++){
            for(int j=0;j<10;j++){
                Button b=new Button();
                ImageView imageView = new ImageView();
                if(gc.getDiceFromRoundPath(i+1,j+1)!=null) {
                    imageView = new ImageView(diceToImage(gc.getDiceFromRoundPath(i+1,j+1)));
                }
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                b.setBackground(new Background(myBF));
                b.setGraphic(imageView);
                roundTr.add(b,j,i);
            }
        }
        grc.add(roundTr,0,0);
        grc.add(stock,0,1);
        return  grc;

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


    public void setScene2(String messages ){

        //object second layout
        Label match=new Label("MATCHMAKING...");
        Label message=new Label();
        message.setText(messages);
        match.setFont(Font.font(null, FontWeight.BOLD,80));
        message.setFont(Font.font(null,FontWeight.BOLD,50));
        //second layout
        VBox vBox=new VBox(match,message);
        vBox.setAlignment(Pos.CENTER);
        pane2=new StackPane(vBox);
        pane2.setBackground(new Background(myBI));
        scene2=new Scene(pane2);
    }

    public void setScene3(GridPane pane,String privateGoalJson) throws FileNotFoundException {
        //object third layout

        HBox hBox= new HBox(100);
        VBox vbox = new VBox(20);

        Button play = new Button("Gioca");
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

        play.setOnAction(e -> {
            RadioButton r1=(RadioButton)pane.getChildren().get(4);
            RadioButton r2=(RadioButton)pane.getChildren().get(5);
            RadioButton r3=(RadioButton)pane.getChildren().get(10);
            RadioButton r4=(RadioButton)pane.getChildren().get(11);

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
            String string1="first";
            String string2="first";
            try {
                setScene4(string1,string2,0,false);
            } catch (IOException e1) { }
            window.setScene(scene4);
            current=4;
            setLogin(true);
        });

    }

    public void setScene4(String greenCarpetJson,String playerJson,int flag, boolean visible) throws IOException {
        Scheme empty = new Scheme(0);
        GreenCarpet gc = new GreenCarpet(0);
        int first=0;
        int second=0;
        int third=0;
        Player player = new Player("tmp");
        if (flag == 1) {
                Gson gson = new Gson();
                gc = gson.fromJson(greenCarpetJson, GreenCarpet.class);
                first=gc.getToolCard(1).getSerialNumber();
                second=gc.getToolCard(2).getSerialNumber();
                third=gc.getToolCard(3).getSerialNumber();
                player = gson.fromJson(playerJson, Player.class);
        }
        int k=0;
        Scheme [] schemes=new Scheme[3];
        for(int i=0;i<3;i++)
            schemes[i]=empty;
        if(flag==1){
            for(int i=0;i<gc.getnPlayers();i++){
                if(!gc.getPlayer().get(i).getNickname().equals(player.getNickname())){
                    schemes[k]=gc.getPlayer().get(i).getScheme();
                    k++;
                }
            }
        }
        //layout4
        ImageView imageView2;
        AnchorPane im2=new AnchorPane();
        GridPane scheme;
        GridPane grc=new GridPane();
        HBox hBox2=new HBox(40);
        VBox vbox2=new VBox(10);
        HBox hboxTool=new HBox(40);
        VBox vboxTool1=new VBox(5);
        VBox vboxTool2=new VBox(5);
        VBox vboxTool3=new VBox(5);

        Button play=new Button("Piazza dado");
        play.setFont(new Font(30));
        play.setPrefSize(300,50);
        play.setVisible(visible);
        play.setAlignment(Pos.CENTER);
        Button useTool=new Button("Usa carta");
        useTool.setFont(new Font(30));
        useTool.setPrefSize(300,50);
        useTool.setAlignment(Pos.CENTER);
        useTool.setVisible(visible);
        Button pass=new Button("Passa");
        pass.setPrefSize(300,50);
        pass.setFont(new Font(30));
        pass.setAlignment(Pos.CENTER);
        pass.setVisible(visible);
        VBox vbox4=new VBox(20,play,useTool,pass);
        Label name=new Label();
        Label cost1=new Label();
        Label cost2=new Label();
        Label cost3=new Label();
        HBox markers=new HBox(5);
        if(flag==1) {
            scheme=setScheme(player.getScheme(),60,300,240);
            markers=setDifficulty(player.getMarkers().size());
            cost1.setText("Costo: "+gc.getToolCard(1).getCost());
            cost1.setFont(Font.font(null,FontWeight.BOLD,30));
            cost2.setText("Costo: "+gc.getToolCard(2).getCost());
            cost2.setFont(Font.font(null,FontWeight.BOLD,30));
            cost3.setText("Costo: "+gc.getToolCard(3).getCost());
            cost3.setFont(Font.font(null,FontWeight.BOLD,30));
            vboxTool1.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(1).getSerialNumber()), 350, 250),cost1);
            vboxTool2.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(2).getSerialNumber()), 350, 250),cost2);
            vboxTool3.getChildren().addAll( imageToImageV(numbToTool(gc.getToolCard(3).getSerialNumber()), 350, 250),cost3);
            hboxTool.getChildren().addAll(vboxTool1,vboxTool2,vboxTool3);
            hBox2.getChildren().addAll(imageToImageV(numbToImage_PuG(gc.getPublicGoal(0).getSerialNumber()), 350, 250),imageToImageV(numbToImage_PuG(gc.getPublicGoal(1).getSerialNumber()), 350, 250),imageToImageV(numbToImage_PuG(gc.getPublicGoal(2).getSerialNumber()), 350, 250));
            grc=setStockAndRound(gc);
            imageView2=imageToImageV(numbToImage_PrG(player.getPrivateGoal().getSerialNumber()),350, 250);
            im2=new AnchorPane(imageView2);
            name.setText(player.getScheme().getName()+"\tDifficoltà "+player.getScheme().getDifficulty());
            name.setFont(Font.font(null,FontWeight.BOLD,35));
        }
        else{
            if(myscheme!=null)
                scheme=myscheme;
            else
                scheme=setScheme(empty,60,300,240);
            vboxTool1.getChildren().clear();
            vboxTool2.getChildren().clear();
            vboxTool3.getChildren().clear();
            vboxTool1.getChildren().addAll(imageToImageV(numbToTool(0), 350, 250),cost1);
            vboxTool2.getChildren().addAll(imageToImageV(numbToTool(0), 350, 250),cost2);
            vboxTool3.getChildren().addAll( imageToImageV(numbToTool(0), 350, 250),cost3);
            hboxTool.getChildren().clear();
            hboxTool.getChildren().addAll(vboxTool1,vboxTool2,vboxTool3);
            hBox2.getChildren().clear();
            hBox2.getChildren().addAll(imageToImageV(numbToImage_PuG(0), 350, 250),imageToImageV(numbToImage_PuG(0), 350, 250),imageToImageV(numbToImage_PuG(0), 350, 250));
        }

        vbox4.prefHeightProperty().bind(vbox2.heightProperty());
        im2.prefHeightProperty().bind(vbox2.heightProperty());
        scheme.prefHeightProperty().bind(vbox2.heightProperty());

        vbox2.getChildren().clear();
        vbox2.getChildren().addAll(vbox4,scheme,name,im2);

        HBox hbox3=new HBox(10);
        hbox3.getChildren().clear();
        hbox3.getChildren().setAll(setScheme(schemes[0],35,200,140),setScheme(schemes[1],35,200,140),setScheme(schemes[2],35,200,140));
        VBox vBox1=new VBox(10);
        vBox1.getChildren().clear();
        vBox1.getChildren().addAll(hboxTool, hBox2, hbox3);
        HBox hboxgrande=new HBox(20);

        VBox vbox5=new VBox(20);
        vbox5.getChildren().clear();
        vbox5.getChildren().addAll(grc,markers);

        //set vbox 2 heigh prop.

        vbox2.prefHeightProperty().bind(hboxgrande.heightProperty());
        hboxgrande.prefHeightProperty().bind(scene1.heightProperty());
        hboxgrande.prefWidthProperty().bind(scene1.widthProperty());
        hboxgrande.getChildren().clear();
        hboxgrande.getChildren().setAll(vbox5,vbox2,vBox1);
        pane4 = new StackPane(hboxgrande);
        pane4.setBackground(new Background(myBI));
        scene4 = new Scene(pane4);
        if(flag==1) {
            GridPane tmp = (GridPane) grc.getChildren().get(1);     //STOCK
            tmp.getChildren().forEach(item -> {
                item.setOnMouseClicked(new EventHandler<MouseEvent>() {                 //BOTTONI RISERVA
                    @Override
                    public void handle(MouseEvent event) {
                        if(dicePlaceable) {
                            Node source = (Node) event.getSource();
                            placedice[0] = GridPane.getRowIndex(source) + 1;
                            dicechoose = true;
                        }
                    }
                });
            });


            GridPane tmp2 = (GridPane) grc.getChildren().get(0);
            tmp2.getChildren().forEach(item -> {
                item.setOnMouseClicked(new EventHandler<MouseEvent>() {                 //BOTTONI PATH
                    @Override
                    public void handle(MouseEvent event) {
                        Node source = (Node) event.getSource();
                        pathVal[1] = GridPane.getRowIndex(source) + 1;
                        pathVal[0] = GridPane.getColumnIndex(source) + 1;
                        dicePath=true;
                        System.out.println("path:"+pathVal[0]+".."+pathVal[1]);
                    }
                });

            });


            scheme.getChildren().forEach(item2 -> {
                item2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(dicechoose) {
                            Node source2 = (Node) event.getSource();
                            placedice[1]=GridPane.getRowIndex(source2);
                            placedice[2]=GridPane.getColumnIndex(source2);
                            System.out.println("dado:"+placedice[0]+"   riga:"+placedice[1]+"   col:"+placedice[2]);
                            dicechoose=false;
                            dicePlaceable=false;
                            setLogin(true);
                        }
                        if(toolCoord){
                            Node source2 = (Node) event.getSource();
                            dicetoolpos[0]=GridPane.getRowIndex(source2);
                            dicetoolpos[1]=GridPane.getColumnIndex(source2);
                            toolCoordDone =true;
                            toolCoord =false;
                            System.out.println("primo dado");
                        }
                    }
                });
            });

        }
        pass.setOnAction(e ->{
            menuaction="1";
            setLogin(true);
        });
        play.setOnAction(e ->{
            menuaction = "2";
            dicePlaceable=true;
            setLogin(true);
        });

        useTool.setOnAction(e -> {
            menuaction="3";
            toolUsable=true;
            setLogin(true);
        });

        int finalFirst = first;
        vboxTool1.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if(toolUsable) {
                    toolchose = finalFirst;
                    toolchoose = true;
                    toolUsable=false;
                }
            }
        });
        int finalSecond = second;
        vboxTool2.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if(toolUsable) {
                    toolchose = finalSecond;
                    toolchoose = true;
                    toolUsable=false;
                }
            }
        });
        int finalThird = third;
        vboxTool3.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {               //SCELTA TOOL
            @Override
            public void handle(MouseEvent event) {
                if(toolUsable) {
                    toolchose = finalThird;
                    toolchoose = true;
                    toolUsable=false;
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
            scoreL[i].setFont(Font.font(null,FontWeight.BOLD,100-(i*25)));
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

            setScene2("");
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
        Platform.runLater(() ->{
            dicechoose=false;
            dicePlaceable=true;
            setLogin(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText(message[0]);
            alert.setContentText(message[1]);
            alert.showAndWait();
        });

    }

    public static void showConnDiscPopup(String message){
        Platform.runLater(() ->{
            setLogin(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText(message);
            alert.showAndWait();
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
        currentmessage=messages;
        System.out.println("ho impostato new message"+current+messages);
        newmessage=true;
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



    public class MessageThread extends Thread {
        private GUIController GUIController;
        private MessageThread(GUIController GUIController) {
            this.GUIController = GUIController;
        }

        @Override
        public void run() {
            while (10 > 0) {
                while (!newmessage) {
                    try {
                        sleep(200);
                    } catch (InterruptedException e) { }
                }
                if (current==2) {
                    System.out.println("sono nel thread\t"+currentmessage);
                    Platform.runLater(()->{
                        GUIController.setScene2(currentmessage);
                        current=2;
                        window.setScene(scene2);
                    });
                    System.out.println("ho fatto la run later");

                }
                newmessage=false;
            }
        }
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
                    try {
                        GUIController.setScene4(greencarpetJsonSt,playerJsonSt,1, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("tavolo aggiornato");
                    Platform.runLater(()->{
                        window.setScene(scene4);
                        current=4;
                    });
                }
                else if (scenechoose == 3){
                    GUIController.setScene2("");
                    current=2;
                    Platform.runLater(() ->{
                        window.setScene(scene2);
                    });
                }
                else if(scenechoose==4){      //set scene 4 with buttons
                    try {
                        GUIController.setScene4(greencarpetJsonSt, playerJsonSt,1 ,true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } Platform.runLater(()->{
                        window.setScene(scene4);
                    });
                }
                else if(scenechoose==5){
                    GUIController.setScene5(scorefinal);
                    Platform.runLater(() ->{
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
                        alert.setContentText("Ecco il tuo dado rilanciato: "+dice.toString()+"Scegli dove piazzarlo.");
                        try {
                            alert.getDialogPane().getChildren().add(imageToImageV(diceToImage(dice),50,50));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        alert.showAndWait();
                    });
                }
                else if(scenechoose==7){
                    System.out.println("aaaa");
                    try {
                        GUIController.setScene4("first", "first",0 ,false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } Platform.runLater(()->{
                        window.setScene(scene4);
                    });
                }
                scenechoose = 0;
            }

        }
    }

}



