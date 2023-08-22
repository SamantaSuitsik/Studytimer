package com.example.pomodoro;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;

import java.util.ArrayList;
//varvid: roosa - EDCBD2, heledaim roheline - 80C4B7, kesk roh - 369a89
//tumeroh - 1b6a63, tabbar must - 151516


public class HelloApplication extends Application {
    private boolean jookseb = false;
    private Taimer taimer = null;

    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();

        TextField kirjutuskast = new TextField("00");
        TextField kirjutuskast2 = new TextField("00");
        TextField kirjutuskast3 = new TextField("00");

        //lõpetamise nupp
        Button lopuNupp = new Button("Lõpeta");
        lopuNupp.setPadding(new Insets(1,5,1,5));
        lopuNupp.setStyle("-fx-background-color: #e6e6e6");
        lopuNupp.setVisible(false);//peidame alguses

        Scene scene = new Scene(border, 280,440);

        //lisab borderpaneile regioonid
        border.setBottom(addHBoxAlumine(lopuNupp,stage,scene)); //tõstab gridpanei kõrgemale!
        border.setCenter(keskOsa(kirjutuskast,kirjutuskast2,kirjutuskast3,lopuNupp));

        //lisame scene'ile css-i
        scene.getStylesheets().add(this.getClass().getResource("stylesheet.css").toExternalForm());
        //lisame kirjutuskastile css failist style'ingu
        kirjutuskast.getStyleClass().add("text-field");

        stage.setTitle("Study timer");
        stage.setScene(scene);
        stage.show();
    }

    public VBox addHBoxAlumine(Button lopuNupp, Stage stage, Scene scene) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 0, 0, 0));
        vbox.setSpacing(40);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.setPrefSize(440,100);

        //tab bar - hetkel pole suurt kasutust
        HBox tabbar = new HBox();
        tabbar.setAlignment(Pos.CENTER);
        tabbar.setPadding(new Insets(1,1,1,1));
        tabbar.setMaxHeight(23);
        tabbar.setMinHeight(23);

        //aegade nupp
        Button aegadeNupp = new Button();
        aegadeNupp.setPadding(new Insets(0,0,10,0));//hitbox on vaiksem nupul
        aegadeNupp.getStyleClass().add("aegadeNupp");
        tabbar.getChildren().add(aegadeNupp);

        //nupp kolmnurga pildiks
        ImageView pilt = new ImageView(new Image(getClass().getResourceAsStream("kaunistusega2.png")));
        pilt.setFitHeight(34); //18
        pilt.setFitWidth(36); //20
        aegadeNupp.setGraphic(pilt);
        aegadeNupp.setOnMouseEntered(event -> {
            aegadeNupp.setText("Ajad");

        });
        aegadeNupp.setOnMouseExited(event ->
                aegadeNupp.setText(""));

        aegadeNupp.setOnMouseClicked(event -> {
            try {
                aegadeEkraan(stage,scene);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Polygon kolmnurk = new Polygon();
        kolmnurk.getPoints().addAll(
                0.0, 67.0,
                67.0, 67.0,
                33.4, 0.0);

        Rectangle ruut = new Rectangle(20,20);
        ruut.setHeight(30);
        ruut.setWidth(30);

        //nupud pole hetkel kasutusel!
        Button kolmnurkNupp = new Button();
        kolmnurkNupp.setShape(kolmnurk);
        kolmnurkNupp.setStyle("-fx-border-color: #d56b57; -fx-border-width: 1px; -fx-background-color: #E3856B; -fx-padding: 35 0 0 0; -fx-font-size: 13px; " +
                "-fx-text-fill: #8f3a28; -fx-font-family: 'Norasi'");
        kolmnurkNupp.setPrefSize(60,60);
        kolmnurkNupp.setText("Ajad");

        Button ruutNupp = new Button();
        ruutNupp.setShape(ruut);
        ruutNupp.setPrefSize(60,60);
        ruutNupp.setStyle("-fx-border-color: #d56b57; -fx-border-width: 1px; -fx-background-color: #E3856B; -fx-padding: 35 0 0 0; -fx-font-size: 13px; " +
                "-fx-text-fill: #8f3a28; -fx-font-family: Norasi");
        ruutNupp.setText("Muu");


        //ettevalmistused helendamiseks hoverides
        Color oranz = Color.web("#c15b44");
        Color valge = Color.web("#bababa");
        DropShadow dropShadow = new DropShadow(0, oranz);
        DropShadow dropShadow2 = new DropShadow(0, oranz);
        DropShadow shadowLopuNupule = new DropShadow(0,valge);
        ruutNupp.setEffect(dropShadow);
        kolmnurkNupp.setEffect(dropShadow2);
        lopuNupp.setEffect(shadowLopuNupule);

        ruutNupp.setOnMouseEntered(event -> animateGlowEffect(ruutNupp));
        kolmnurkNupp.setOnMouseEntered(event -> animateGlowEffect(kolmnurkNupp));
        lopuNupp.setOnMouseEntered(event -> animateGlowEffect(lopuNupp));

        vbox.getChildren().addAll(lopuNupp,tabbar);
        return vbox;
    }

    private void aegadeEkraan(Stage stage, Scene scene) throws Exception {
        //votame ajad Taimeri klassi abiga
        ArrayList<String> ajad = Taimer.loeFailist();
        System.out.println(ajad);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 10, 15, 0));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        vbox.setPrefSize(440,280);

        Button tagasiNupp = new Button();
        tagasiNupp.setPadding(new Insets(13,0,0,1));
        ImageView pilt = new ImageView(new Image(getClass().getResourceAsStream("kumernool.png")));
        pilt.setFitHeight(25);
        pilt.setFitWidth(15);
        tagasiNupp.setGraphic(pilt);

        Button clearNupp = new Button("-");

        //hbox selleks et tagasi nupp panna vasakule
        HBox hbox = new HBox();
        hbox.getChildren().addAll(tagasiNupp,vbox);

        //lisatakse ajad ekraanile
        for (int i = 0; i < ajad.size(); i++) {
            String aeg = ajad.get(i);
            Text ajaTekst = new Text(aeg);
            ajaTekst.setStyle("-fx-fill: #e6e6e6; -fx-font-family: Norasi;-fx-font-size: 20");
            vbox.getChildren().add(ajaTekst);
        }

        tagasiNupp.setOnMouseClicked(event -> {
            stage.setTitle("Study timer");
            stage.setScene(scene);
            stage.show();
        });

        //et saaks scrollida
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.getStyleClass().add("scroll");
        scroll.setContent(hbox);

        //uus stage ja scene
        //Stage stageAjad = new Stage();
        Scene sceneAjad = new Scene(scroll,280,440);
        //lisame scene'ile css-i
        sceneAjad.getStylesheets().add(this.getClass().getResource("stylesheet.css").toExternalForm());
        stage.setTitle("Ajad"); //kasutame algset stagei et uus leht ei avaneks
        stage.setScene(sceneAjad);
        stage.show();
    }

    public void animateGlowEffect(Button button) {
        DropShadow dropShadow = (DropShadow) button.getEffect();

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.radiusProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(dropShadow.radiusProperty(), 18))
        );
        timeline1.setAutoReverse(true);
        button.setOnMouseExited(event -> {
            timeline1.stop();
            dropShadow.setRadius(0);
                }

        );
        timeline1.play();
    }

    public GridPane keskOsa(TextField tunnid, TextField minutid, TextField sekundid, Button lopuNupp) {
        Text koolon = new Text(":");
        Text koolon2 = new Text(":");
        koolon.setFill(Paint.valueOf("#e6e6e6"));
        koolon2.setFill(Paint.valueOf("#e6e6e6"));
        koolon.getStyleClass().add("koolon");
        koolon2.getStyleClass().add("koolon");

        Label eesmark = new Label("Eesmärk");
        eesmark.getStyleClass().add("label");

        //gridpane ja selle joondamine
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);// Set alignment to center
        grid.setVgap(25);
        //lisaveerg algusesse, et ringNupp oleks sobival kohal ja piisavalt suur
        grid.getColumnConstraints().add(new ColumnConstraints(20));
        //grid.setGridLinesVisible(true);

        //tehakse sobivate suurustega veerud gridpaneis
        for (int i = 0; i < 7; i++) {
            if (i % 2 == 0)
                grid.getColumnConstraints().add(new ColumnConstraints(11));
            else
                grid.getColumnConstraints().add(new ColumnConstraints(47));
        }
        grid.getColumnConstraints().add(new ColumnConstraints(20));//lisaveerg  lõppu

        //et ringNupp oleks keskel tehakse stack pane
        StackPane stackpane = new StackPane(); //for overlapping
        stackpane.setAlignment(Pos.CENTER);

        //suure nupu tegemine
        Circle ring = new Circle(100);
        Button ringNupp = new Button();
        ringNupp.setShape(ring);
        ringNupp.setPrefSize(200, 200);
        ringNupp.setText("   Sea\neesmärk!");
        ringNupp.setStyle("-fx-background-color: #e6e6e6; -fx-border-color: #bababa; -fx-border-width: 2px;" +
                "-fx-font-size: 38px; -fx-text-fill: #1a1a1c; -fx-font-family: Norasi; ");
        //helenduse ettevalmistus hoverides
        DropShadow dropShadow = new DropShadow(0, Color.web("#B2B1B1A8"));
        ringNupp.setEffect(dropShadow);
        ringNupp.setOnMouseEntered(event ->
                animateGlowEffect(ringNupp)
        );

        ringNupp.setOnMouseClicked(e -> {
            System.out.println("Klikk HelloAPpis");
            taimeriKaivituseAlgus(ringNupp, lopuNupp, tunnid, minutid, sekundid, koolon, koolon2, eesmark);
        });

        lopuNupp.setOnMouseClicked(event1 -> {
            taimeriPeatamine(lopuNupp,tunnid,minutid,sekundid,koolon,koolon2,eesmark);
        });

        //paneme gridile objektid
        stackpane.getChildren().add(ringNupp);
        grid.add(stackpane, 0, 0, 9, 1);
        grid.add(tunnid, 2, 1);
        grid.add(minutid, 4, 1);
        grid.add(sekundid, 6, 1);
        grid.add(koolon, 3, 1);
        grid.add(koolon2, 5, 1);
        grid.add(eesmark, 4, 1, 3, 1);
        GridPane.setHalignment(eesmark, HPos.RIGHT);
        GridPane.setValignment(eesmark, VPos.BOTTOM);
        //GridPane.setHalignment(ring, javafx.geometry.HPos.CENTER); // Align the circle within the cell
        return grid;
    }

    public void taimeriKaivituseAlgus(Button ringNupp, Button lopuNupp, TextField tunnid, TextField minutid, TextField sekundid, Text koolon, Text koolon2, Label eesmark) {
        //kui taimer juba tehtud on ja see jookseb praegu (ei tee uut taimerit)
        //paneme pausile kaivita meetodi abiga
        if (jookseb)
            taimer.kaivita();

        else {
            //kui taimerit pole ja seega see ka ei jookse, siis loome selle
            //ringNupp.setText("Alusta");//jookseb yle siit prgu
            taimer = new Taimer(ringNupp, lopuNupp, tunnid, minutid, sekundid);
            taimer.kaivita();
            lopuNupp.setVisible(true);
            jookseb = true;

            //soovitud aega ei saa enam muuta ja eesmärgi värv muutub
            tunnid.setDisable(true);
            minutid.setDisable(true);
            sekundid.setDisable(true);
            koolon.setFill(Paint.valueOf("#815db8"));
            koolon2.setFill(Paint.valueOf("#815db8"));
            eesmark.setStyle("-fx-text-fill: #766896");
        }
    }

    private void taimeriPeatamine(Button lopuNupp, TextField tunnid, TextField minutid, TextField sekundid, Text koolon, Text koolon2, Label eesmark) {
        try {
            taimer.stop();
            jookseb = false;
            lopuNupp.setVisible(false);
            //eesmarki saab uuesti muuta
            tunnid.setDisable(false);
            minutid.setDisable(false);
            sekundid.setDisable(false);
            koolon.setFill(Paint.valueOf("#e6e6e6"));
            koolon2.setFill(Paint.valueOf("#e6e6e6"));
            eesmark.setStyle("-fx-text-fill: #e6e6e6");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}