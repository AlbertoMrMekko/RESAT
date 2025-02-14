package com.AlbertoMrMekko.resat;

import com.AlbertoMrMekko.resat.view.ViewManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

@SpringBootApplication
public class ResatApplication extends Application
{
    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void init()
    {
        applicationContext = new SpringApplicationBuilder(ResatApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage)
    {
        ViewManager viewManager = applicationContext.getBean(ViewManager.class);
        viewManager.showSidebarView();

        BorderPane root = applicationContext.getBean(BorderPane.class);
        Scene scene = new Scene(root, 1125, 750);

        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        root.getStyleClass().add("background");

        primaryStage.setScene(scene);
        primaryStage.setTitle("RESAT");
        primaryStage.show();
    }

    @Override
    public void stop()
    {
        applicationContext.close();
        Platform.exit();
    }
}
