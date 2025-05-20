package org.sspd.myatdental.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.sspd.myatdental.Configuration.SpringContextHelper;

import java.io.IOException;

public class App extends Application {


    @Getter
    public static ApplicationContext context = SpringContextHelper.getContext();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/loginview/loginform.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
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