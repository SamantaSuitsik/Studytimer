package com.example.pomodoro;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.animation.Animation.*;

public class Taimer {
    private long algusAeg;
    private boolean jookseb;
    private String timeText;
    private Timeline timeline;
    private boolean oliPausil = false;
    private long elapsedTime;
    private long algusPausil;
    private String duration;

    public Taimer(TextField kirjutuskast) {
        duration = kirjutuskast.getText();
        if (duration.equals("Eesmärk (sekundites)"))
            duration = "10";
        this.algusAeg = System.currentTimeMillis();
        this.jookseb = false;
        this.timeText = "00:00:00";
        this.elapsedTime = 0;
        this.algusPausil = 0;

    }

    public void kaivita(Button nupp) {
        taimeriALgus(nupp);
    }

    private void taimeriALgus(Button nupp) {
        nupp.setOnAction(event -> {
            if (jookseb) {
                //kui taimer parasjagu käib -> tuleb paus

                timeline.stop();
                algusPausil = System.currentTimeMillis();
                oliPausil = true;
                jookseb = false;
                System.out.println("paus");
            }
            else if (oliPausil) {
                //leiame kui kaua oli pausil
                long pausilAeg = System.currentTimeMillis() - algusPausil;
                //lisame algusajale pausil oldud aja, et timer algaks samast kohast
                algusAeg += pausilAeg;
                updateTimeElapsed(nupp);
                jookseb = true;
                oliPausil = false;
                //oliPausil on tõene ja hakkab jooksma
                System.out.println("votsin pausilt ara");
            }
            else {
                //kui taimer ei käi ega pole pausil
                jookseb = true;
                System.out.println("tere");
                algusAeg = System.currentTimeMillis();
                //nupp.setDisable(true); //et sekundeid midagi ei häiriks (teeb tumedaks)
                updateTimeElapsed(nupp);
            }
        });

    }


    private void updateTimeElapsed(Button nupp) {
        int durationSec = Integer.parseInt(duration) + 1;

        //timeline hakkab 0.1 seki tagant eventi handlima
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            long currentTime = System.currentTimeMillis();
            //leiame ajavahe praeguse ja alguse vahel
            // ja teeme saadud nanosekundid sekunditeks
            elapsedTime = Math.round((currentTime - algusAeg) / 1000.0);

            long hours = elapsedTime / 3600;
            long minutes = (elapsedTime % 3600) / 60;
            long seconds = elapsedTime % 60; // % 60 tagab et sekundid jäävad 0..59 vahele

            //kui aeg tais saab
            if (elapsedTime >= durationSec) {
                nupp.setText("Aeg täis!\n" + timeText);
                timeline.stop();
                //nupp.setDisable(false); //ylevalpool on enable
            }
            //kui aeg pole tais
            else {
                timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                nupp.setText(timeText);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

}
