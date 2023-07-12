package com.example.pomodoro;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;


public class HelloApplication extends Application {
    private boolean jookseb = false;
    private Taimer taimer;

    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();

        //lisab borderpaneile regioonid
        border.setBottom(addHBoxAlumine());
        border.setCenter(keskOsa());
        border.setStyle("-fx-background-color: #EDCBD2;");

        Scene scene = new Scene(border, 280,440);
        //scene.getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        //lisame scene'ile css-i
        scene.getStylesheets().add(this.getClass().getResource("stylesheet.css").toExternalForm());
        stage.setTitle("Study timer");
        stage.setScene(scene);
        stage.show();
    }

    public HBox addHBoxAlumine() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(1, 1, 25, 1));
        hbox.setSpacing(110);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefSize(440,100);

        Polygon kolmnurk = new Polygon();
        kolmnurk.getPoints().addAll(
                0.0, 67.0,
                67.0, 67.0,
                33.4, 0.0);

        Rectangle ruut = new Rectangle(20,20);
        ruut.setHeight(30);
        ruut.setWidth(30);

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

        Color oranz = Color.web("#c15b44");

        //ettevalmistused helendamiseks
        DropShadow dropShadow = new DropShadow(0, oranz);
        DropShadow dropShadow2 = new DropShadow(0, oranz);
        ruutNupp.setEffect(dropShadow);
        kolmnurkNupp.setEffect(dropShadow2);

        ruutNupp.setOnMouseEntered(event -> animateGlowEffect(ruutNupp));
        kolmnurkNupp.setOnMouseEntered(event -> animateGlowEffect(kolmnurkNupp));

        hbox.getChildren().addAll(kolmnurkNupp,ruutNupp);

        return hbox;
    }

    public void animateGlowEffect(Button button) {
        DropShadow dropShadow = (DropShadow) button.getEffect();

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.radiusProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(dropShadow.radiusProperty(), 25))
        );
        timeline1.setAutoReverse(true);
        button.setOnMouseExited(event -> {
            timeline1.stop();
            dropShadow.setRadius(0);
                }

        );
        timeline1.play();
    }

    public GridPane keskOsa() {
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);// Set alignment to center
        grid.setVgap(10);
        Circle ring = new Circle(100);

        Button ringNupp = new Button();
        ringNupp.setShape(ring);
        ringNupp.setPrefSize(200,200);
        ringNupp.setText("Alusta");
        ringNupp.setStyle("-fx-background-color: #80C4B7; -fx-border-color: #369a89; -fx-border-width: 2px;" +
                "-fx-font-size: 38px; -fx-text-fill: #1b6a63; -fx-font-family: Norasi; ");

        TextField kirjutuskast = new TextField("Eesmärk (sekundites)");
        kirjutuskast.setLayoutY(55);
        //lisame kirjutuskastile css failist style'ingu
        kirjutuskast.getStyleClass().add("text-field");


        //kirjutuskast.getStylesheets().add("/home/samanta/projektid/Studytimer/pomodoro/src/main/resources/com/example/pomodoro/stylesheet.css");


        DropShadow dropShadow = new DropShadow(0, Color.web("#1b6a63"));
        ringNupp.setEffect(dropShadow);
        ringNupp.setOnMouseEntered(event ->
            animateGlowEffect(ringNupp)
        );

        ringNupp.setOnMouseClicked(e -> {
            //kui taimer juba tehtud on ja see jookseb praegu (ei tee uut taimerit)
            if (jookseb)
                taimer.kaivita(ringNupp);
            else {
                //kui taimerit pole ja seega see ka ei jookse, siis loome selle
                jookseb = true;
                Taimer taimer = new Taimer(kirjutuskast);
                taimer.kaivita(ringNupp);
            }
        });
        grid.add(ringNupp, 0, 0);
        grid.add(kirjutuskast,0,2);
        //GridPane.setHalignment(ring, javafx.geometry.HPos.CENTER); // Align the circle within the cell
        return grid;
    }


    public static void main(String[] args) {
        launch();
    }
}