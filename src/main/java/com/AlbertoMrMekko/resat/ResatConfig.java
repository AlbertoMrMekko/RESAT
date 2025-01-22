package com.AlbertoMrMekko.resat;

import javafx.scene.layout.BorderPane;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResatConfig
{
    @Bean
    public BorderPane root()
    {
        return new BorderPane();
    }
}
