package com.AlbertoMrMekko.resat;

import javafx.scene.layout.BorderPane;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ResatConfig
{
    @Bean
    public BorderPane root()
    {
        return new BorderPane();
    }
}
