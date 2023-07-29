package com.example.pomodoro;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Taimer {
    private long algusAeg;
    private boolean jookseb;
    private String timeText;
    private Timeline timeline;
    private boolean oliPausil = false;
    private long elapsedTime;
    private long algusPausil;
    private long duration;
    private Button nupp;
    private Button lopuNupp;

    public Taimer(Button nupp, Button lopuNupp, TextField tunnid, TextField minutid, TextField sekundid) {
        this.duration = ajaKalkulaator(tunnid, minutid, sekundid);
        if (duration == -1) {
            //kui sisestus oli vale
            nupp.setText("  Vale\nsisestus");
            tunnid.setText("00");
            minutid.setText("00");
            sekundid.setText("00");

        }
        //kui eesmärki ei seatud, siis on see 2h
        else if (duration == 0) {
            tunnid.setText("02");
            duration = 4800;
        }

        this.nupp = nupp;
        this.lopuNupp = lopuNupp;
        this.algusAeg = System.currentTimeMillis();
        this.jookseb = false;
        this.timeText = "00:00:00";
        this.elapsedTime = 0;
        this.algusPausil = 0;

        //kui vajutatakse lopunuppu



    }

    public long ajaKalkulaator(TextField kirjutuskast, TextField kirjutuskast2, TextField kirjutuskast3) {
        //tehakse tundide, minutite ja sekundite kirjutuskastist aeg sekunditeks
        int sekundid = -1;
        //kasutatakse regexit et tuvastada kas sisend on numbriline
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (pattern.matcher(kirjutuskast.getText()).matches() &&
                pattern.matcher(kirjutuskast2.getText()).matches() &&
                pattern.matcher(kirjutuskast3.getText()).matches()) {
            System.out.println("kontroll korras");

            int tunnid = Integer.parseInt(kirjutuskast.getText());
            int minutid = Integer.parseInt(kirjutuskast2.getText());
            sekundid = Integer.parseInt(kirjutuskast3.getText());

            if (minutid > 59 || sekundid > 59) {
                //vale sisestus
                sekundid = -1;
            } else {
                //kui sisestus on õige - teisendus sekunditeks
                sekundid = sekundid + (minutid * 60) + (tunnid * 3600);
            }

        }
        return sekundid;

    }

    public void kaivita() {
        //teine kord ta votab ainult selle kliki
            if (jookseb) {
                //kui taimer parasjagu käib -> tuleb paus
                timeline.stop();
                algusPausil = System.currentTimeMillis();
                oliPausil = true;
                jookseb = false;
                System.out.println("paus");
            } else if (oliPausil) {
                //leiame kui kaua oli pausil
                long pausilAeg = System.currentTimeMillis() - algusPausil;
                //lisame algusajale pausil oldud aja, et timer algaks samast kohast
                algusAeg += pausilAeg;
                updateTimeElapsed(nupp);
                jookseb = true;
                oliPausil = false;
                //oliPausil on tõene ja hakkab jooksma
                System.out.println("votsin pausilt ara");
            } else {
                //kui taimer ei käi ega pole pausil
                jookseb = true;
                System.out.println("uus taimer laks kaima");
                algusAeg = System.currentTimeMillis();
                //nupp.setDisable(true); //et sekundeid midagi ei häiriks (teeb tumedaks)
                updateTimeElapsed(nupp);
            }
    }

    private void updateTimeElapsed(Button nupp) {
        long durationSec = duration + 1;

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

    public void stop() throws Exception {
        if (jookseb) {
            timeline.stop();
            System.out.println("TA TULI LABI SIIT");
            oliPausil = false;
            jookseb = false; //vaja, et tehtaks käivitades uus taimer
            kirjutaFaili();//salvestame aja
            TimeUnit.SECONDS.sleep(1);//et naha oma aega veel korraks
            nupp.setText("Alusta");

        }

    }

    private void kirjutaFaili() throws Exception {
        //kirjutab praeguse aja faili

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ajad.txt",true))) {
            writer.write(timeText + "\n");
        }
    }

    public static ArrayList<String> loeFailist() throws Exception {
        ArrayList<String> ajad = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("ajad.txt"), StandardCharsets.UTF_8))) {
            String aeg = reader.readLine();
            while (aeg != null) {
                //loeme ajad listi
                ajad.add(aeg);
                aeg = reader.readLine();
            }
        }
        return ajad;

    }

}
