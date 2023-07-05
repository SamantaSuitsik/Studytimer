package com.example.pomodoro;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.animation.Animation.*;

public class Taimer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        int delaySeconds = 1; // delay in seconds
        int durationSeconds = 5; // duration in seconds

        Button button = new Button("Start Timer");
        button.setOnAction(new EventHandler<ActionEvent>() {
            int counter = 0;

            @Override
            public void handle(ActionEvent event) {
                counter++;
                System.out.println("Timer: " + counter + " seconds elapsed");

                if (counter >= durationSeconds) {
                    // Stop the timer after the specified duration
                    ((Button) event.getSource()).setText("Timer Finished");
                    ((Button) event.getSource()).setDisable(true);
                }
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(delaySeconds), event -> {
            button.fire(); // Trigger the button's action event
        }));
        timeline.setCycleCount(INDEFINITE); // Repeat indefinitely
        timeline.play();

        StackPane stackPane = new StackPane(button);
        Scene scene = new Scene(stackPane, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
