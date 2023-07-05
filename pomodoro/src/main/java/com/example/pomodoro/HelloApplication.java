package com.example.pomodoro;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private long algusAeg;
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane border = new BorderPane();

        //lisab borderpaneile regioonid
        border.setBottom(addHBoxAlumine());
        border.setCenter(keskOsa());
        border.setStyle("-fx-background-color: #EDCBD2;");

        Scene scene = new Scene(border, 280,440);
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

        Rectangle ruut = new Rectangle();
        ruut.setHeight(65);
        ruut.setWidth(65);

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

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.radiusProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(dropShadow.radiusProperty(), 25))
        );
        timeline.setAutoReverse(true);
        button.setOnMouseExited(event -> {
            timeline.stop();
            dropShadow.setRadius(0);
                }

        );
        timeline.play();
    }

    public GridPane keskOsa() {
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER); // Set alignment to center
        Circle ring = new Circle(100);

        Button ringNupp = new Button();
        ringNupp.setShape(ring);
        ringNupp.setPrefSize(200,200);
        ringNupp.setText("Alusta");
        ringNupp.setStyle("-fx-background-color: #80C4B7; -fx-border-color: #369a89; -fx-border-width: 2px;" +
                "-fx-font-size: 38px; -fx-text-fill: #1b6a63; -fx-font-family: Norasi; ");

        DropShadow dropShadow = new DropShadow(0, Color.web("#1b6a63"));
        ringNupp.setEffect(dropShadow);
        ringNupp.setOnMouseEntered(event ->
            animateGlowEffect(ringNupp)
        );

        ringNupp.setOnMouseClicked(e -> {
            taimer2(ringNupp);
        });



        grid.add(ringNupp, 0, 0);
        GridPane.setHalignment(ring, javafx.geometry.HPos.CENTER); // Align the circle within the cell

        return grid;
    }

    public void taimer2(Button nupp) {
        nupp.setOnAction(event -> {
            algusAeg = System.nanoTime();
            //nupp.setDisable(true); //et sekundeid midagi ei häiriks (teeb tumedaks)
            updateTimeElapsed(nupp);
        });
    }

    private void updateTimeElapsed(Button nupp) {
        int durationSec = 100;
        //timeline hakkab pidevalt actionit nupule saatma
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            long currentTime = System.nanoTime();
            //leiame ajavahe praeguse ja alguse vahel
            // ja teeme saadud nanosekundid sekunditeks
            long elapsedTime = Math.round((currentTime - algusAeg) / 1_000_000_000.0);

            long hours = elapsedTime / 3600;
            long minutes = (elapsedTime % 3600) / 60;
            long seconds = elapsedTime % 60; // % 60 tagab et sekundid jäävad 0..59 vahele
            //kui aeg tais saab

            if (elapsedTime >= durationSec) {
                nupp.setText("Aeg täis!");
                //nupp.setDisable(false); //ylevalpool on enable
            } else {
                String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                nupp.setText(timeText);

            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


    public static void main(String[] args) {
        launch();
    }
}