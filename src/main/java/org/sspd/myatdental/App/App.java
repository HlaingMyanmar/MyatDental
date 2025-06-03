package org.sspd.myatdental.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.sspd.myatdental.Configuration.SpringContextHelper;

import java.io.IOException;

public class App extends Application {

    private double xOffset = 0;
    private double yOffset = 0;


    @Getter
    public static ApplicationContext context = SpringContextHelper.getContext();

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/loginview/loginform.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Scene scene = new Scene(fxmlLoader.load());

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });


        stage.setTitle("Login Form");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void init() throws Exception {
        context = SpringContextHelper.getContext();
    }

    public static void main(String[] args) {
        launch();
    }
}