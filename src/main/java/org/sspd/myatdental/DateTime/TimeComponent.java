package org.sspd.myatdental.DateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeComponent {



    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

    public void initializeClock(Label clockbtn) {
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now(); // ✅ Use LocalDateTime
            clockbtn.setText(now.format(timeFormat)); // ✅ Works with both date and time
        }));
        clock.setCycleCount(Timeline.INDEFINITE); // loop forever
        clock.play();
    }
}
