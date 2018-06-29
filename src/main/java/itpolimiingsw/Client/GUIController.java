package itpolimiingsw.Client;

import com.google.gson.Gson;
import itpolimiingsw.Game.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class GUIController extends Application {
    static Stage window;

    StackPane pane1, pane2,pane3,pane4;
    GridPane pane;
    Scene scene1, scene2,scene3,scene4;
    HBox hBox= new HBox(40);
    VBox vbox = new VBox(10);
    private static boolean dicePlaceable =false;


    private static boolean login = false;
    private static String[] loginData = new String[2];
    private static int scenechoose = 0;
    private static String[] schemesjson = new String[4];
    private static int schemechose;
    private static String menuaction;
    private static String privateGoalJsonSt;
    private GridPane myscheme=new GridPane();
    private static int[] placedice = new int[3];

    private static String playerScheme;

    private static String greencarpetJsonSt;
    private static String playerJsonSt;

    private static boolean dicechoose = false;

    public GUIController() throws FileNotFoundException {

    }

    public static int getScheme() {
        return schemechose;
    }


    public void launchgui(){
        launch();
    }
    public static boolean getLogin() {
        return login;
    }

    public static String[] getLoginData() {
        return loginData;
    }


    public static void setLogin(boolean bool) {
        login=bool;
    }


    BackgroundFill myBF = new BackgroundFill(Color.rgb(0, 0, 0, 0), new CornerRadii(1),
            new Insets(0.0, 0.0, 0.0, 0.0));

    BackgroundImage myBI= new BackgroundImage(new Image(new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/image/image.jpg")),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

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












        login.setOnAction(event -> {
            //prova connessione
            System.out.println("nome: " + username_t.getText());
            System.out.println("password: " + password_t.getText());
            loginData[0]=username_t.getText();
            loginData[1]=password_t.getText();
            setLogin(true);

            /*
            if (username_t.getText().equals("CESNA")) {
                window.setScene(scene2);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERRORE LOGIN");
                alert.setHeaderText("Utente già presente");
                alert.setContentText("scegliere un altro username");

                alert.showAndWait();
            }*/
        });
        /*play.setOnAction(e -> {
            RadioButton r1=(RadioButton)pane.getChildren().get(2);
            RadioButton r2=(RadioButton)pane.getChildren().get(3);
            RadioButton r3=(RadioButton)pane.getChildren().get(6);
            RadioButton r4=(RadioButton)pane.getChildren().get(7);
            if(r1.isSelected()){
                System.out.println("1");
                schemechose=1;
                myscheme.getChildren().add(pane.getChildren().get(0));
                window.setScene(scene4);
            }
            else if(r2.isSelected()){
                System.out.println("2");
                schemechose=2;
                myscheme.getChildren().add(pane.getChildren().get(1));
                window.setScene(scene4);
            }
            else if(r3.isSelected()){
                System.out.println("3");
                schemechose=3;
                myscheme.getChildren().add(pane.getChildren().get(4));
                window.setScene(scene4);
            }
            else if(r4.isSelected()){
                System.out.println("4");
                schemechose=4;
                myscheme.getChildren().add(pane.getChildren().get(5));
                window.setScene(scene4);
            }
            setLogin(true);
        });*/

        SceneThread sceneThread = new SceneThread(this);
        sceneThread.start();
        window.setScene(scene1);
        window.setMaximized(true);
        window.show();


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



        ToggleGroup group1=new ToggleGroup();
        RadioButton r1=new RadioButton(s1.getName());
        r1.setFont(new Font(20));
        r1.setSelected(true);
        RadioButton r2=new RadioButton(s2.getName());
        r2.setFont(new Font(20));
        RadioButton r3=new RadioButton(s3.getName());
        r3.setFont(new Font(20));
        RadioButton r4=new RadioButton(s4.getName());
        r4.setFont(new Font(20));

        r1.setToggleGroup(group1);
        r2.setToggleGroup(group1);
        r3.setToggleGroup(group1);
        r4.setToggleGroup(group1);
        pane.add(sc1,0,0);
        pane.add(sc2,1,0);
        pane.add(r1,0,1);
        pane.add(r2,1,1);
        pane.add(sc3,0,2);
        pane.add(sc4,1,2);
        pane.add(r3,0,3);
        pane.add(r4,1,3);
        pane.setAlignment(Pos.CENTER_RIGHT);
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
        System.out.println("scene a 1");
    }
    public static void updateGreenCarpet(String greencarpetJson, String playerJson){
        greencarpetJsonSt=greencarpetJson;
        playerJsonSt=playerJson;
        scenechoose=2;

    }


    public void setScene2(){
        //object second layout
        Label match=new Label("MATCHMAKING...");
        match.setFont(new Font(80));
        //second layout
        VBox vBox=new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(match);
        pane2=new StackPane(vBox);
        pane2.setBackground(new Background(myBI));
        scene2=new Scene(pane2);


    }

    public void setScene3(GridPane pane,String privateGoalJson) throws FileNotFoundException {
        //object third layout


        Button play = new Button("Gioca");
        play.setPrefSize(300,100);

        play.setFont(new Font(30.0));
        Gson gson=new Gson();
        PrivateGoal privateGoal=gson.fromJson(privateGoalJson,PrivateGoal.class);
        ImageView imageView1=new ImageView(numbToImage_PrG(privateGoal.getSerialNumber()));
        AnchorPane im=new AnchorPane(imageView1);
        //im.prefWidthProperty().bind(vbox.widthProperty());
        //im.prefHeightProperty().bind(vbox.heightProperty());


        pane.setAlignment(Pos.CENTER);
        pane.prefWidthProperty().bind(hBox.widthProperty());
        pane.prefHeightProperty().bind(hBox.heightProperty());
        vbox.getChildren().addAll(im,play);
        //vbox.prefWidthProperty().bind(hBox.widthProperty());
        //vbox.prefHeightProperty().bind(hBox.heightProperty());
        vbox.setAlignment(Pos.CENTER);

        play.setOnAction(e -> {
            RadioButton r1=(RadioButton)pane.getChildren().get(2);
            RadioButton r2=(RadioButton)pane.getChildren().get(3);
            RadioButton r3=(RadioButton)pane.getChildren().get(6);
            RadioButton r4=(RadioButton)pane.getChildren().get(7);

            if(r1.isSelected()){
                System.out.println("1");
                schemechose=1;
                myscheme.getChildren().add(pane.getChildren().get(0));
            }
            else if(r2.isSelected()){
                System.out.println("2");
                schemechose=2;
                myscheme.getChildren().add(pane.getChildren().get(1));
            }
            else if(r3.isSelected()){
                System.out.println("3");
                schemechose=3;
                myscheme.getChildren().add(pane.getChildren().get(4));
            }
            else if(r4.isSelected()){
                System.out.println("4");
                schemechose=4;
                myscheme.getChildren().add(pane.getChildren().get(5));
            }
            String string1="first";
            String string2="first";
            try {
                setScene4(string1,string2,0,false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            window.setScene(scene4);
            window.setMaximized(true);
            setLogin(true);
        });

    }

    public void setScene4(String greenCarpetJson,String playerJson,int flag, boolean visible) throws IOException {
        Scheme empty = new Scheme(0);
        GreenCarpet gc = new GreenCarpet(0);
        Player player = new Player("tmp");
        if (flag == 1) {
                Gson gson = new Gson();
                gc = gson.fromJson(greenCarpetJson, GreenCarpet.class);
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
        HBox hBox1=new HBox(40);
        if(flag==1) {
            scheme=setScheme(player.getScheme(),60,380,275);
            hBox1.getChildren().addAll(imageToImageV(numbToTool(gc.getToolCard(1).getSerialNumber()), 350, 250), imageToImageV(numbToTool(gc.getToolCard(2).getSerialNumber()), 350, 250), imageToImageV(numbToTool(gc.getToolCard(3).getSerialNumber()), 350, 250));
            hBox2.getChildren().addAll(imageToImageV(numbToImage_PuG(gc.getPublicGoal(0).getSerialNumber()), 350, 250),imageToImageV(numbToImage_PuG(gc.getPublicGoal(1).getSerialNumber()), 350, 250),imageToImageV(numbToImage_PuG(gc.getPublicGoal(2).getSerialNumber()), 350, 250));
            grc=setStockAndRound(gc);
            imageView2=new ImageView(numbToImage_PrG(player.getPrivateGoal().getSerialNumber()));
            im2=new AnchorPane(imageView2);
        }
        else{
            scheme=myscheme;
            hBox1.getChildren().addAll(imageToImageV(numbToTool(0), 350, 250), imageToImageV(numbToTool(0), 350, 250), imageToImageV(numbToTool(0), 350, 250));
            hBox2.getChildren().addAll(imageToImageV(numbToImage_PuG(0), 350, 250),imageToImageV(numbToImage_PuG(0), 350, 250),imageToImageV(numbToImage_PuG(0), 350, 250));
        }
        vbox2.getChildren().addAll(im2,scheme);
        vbox2.setAlignment(Pos.CENTER);
        HBox hbox3=new HBox(10);
        hbox3.getChildren().setAll(setScheme(schemes[0],35,200,140),setScheme(schemes[1],35,200,140),setScheme(schemes[2],35,200,140));
        VBox vBox1=new VBox(10,hBox1,hBox2,hbox3);
        HBox hboxgrande=new HBox(20);
        Button play=new Button("Posiziona dado");
        play.setVisible(visible);
        Button useTool=new Button("Usa carta");
        useTool.setVisible(visible);
        Button pass=new Button("Passa");
        pass.setVisible(visible);
        HBox hbox4=new HBox(20,play,useTool,pass);
        VBox vbox5=new VBox(20,grc,hbox4);
        hboxgrande.getChildren().setAll(vbox5,vbox2,vBox1);
        pane4 = new StackPane(hboxgrande);
        pane4.setBackground(new Background(myBI));
        scene4 = new Scene(pane4);
        if(flag==1) {
            GridPane tmp = (GridPane) grc.getChildren().get(1);
            tmp.getChildren().forEach(item -> {
                item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(dicePlaceable) {
                            Node source = (Node) event.getSource();
                            dicechoose = true;
                            placedice[0] = GridPane.getRowIndex(source) + 1;
                        }
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

    }

    public static void loginOK() {
        scenechoose = 3;
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

    public static String handleTurnMenu() {
        return menuaction;
    }

    public static void chooseAction(int scene) {
        scenechoose=scene;
    }

    public static void endTurn() {
        scenechoose=2;
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int[] placeDice() {
        return placedice;
    }

    public static void updateView(String greencarpet, String playerJson) {
        playerJsonSt=playerJson;
        greencarpetJsonSt=greencarpet;
        scenechoose=4;
    }

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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(scenechoose==1) {  //TIME TO SHOW SCHEMES
                    System.out.println("a");

                    try {
                        pane = setSchemeChose(schemesjson[0], schemesjson[1], schemesjson[2], schemesjson[3]);
                    } catch (IOException e) {
                        System.out.println("catch beccata");
                    }
                    System.out.println("droga");
                    try {
                        GUIController.setScene3(pane,privateGoalJsonSt);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    hBox.getChildren().addAll(pane, vbox);
                    pane3 = new StackPane(hBox);
                    pane3.setBackground(new Background(myBI));
                    scene3 = new Scene(pane3);
                    try {
                        Platform.runLater(()->{
                            window.setScene(scene3);
                            window.setMaximized(true);
                            window.setResizable(true);

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
                        window.setMaximized(true);
                    });
                }
                else if (scenechoose == 3){
                    GUIController.setScene2();
                    Platform.runLater(() ->{
                        window.setScene(scene2);
                        window.setMaximized(true);
                        window.setResizable(true);
                    });
                }
                else if(scenechoose==4){      //set scene 4 with buttons
                    try {
                        GUIController.setScene4(greencarpetJsonSt, playerJsonSt,1 ,true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } Platform.runLater(()->{
                        window.setScene(scene4);
                        window.setMaximized(true);
                    });
                }
                scenechoose = 0;
            }

        }
    }
    public String toStr(int numb){
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
}



