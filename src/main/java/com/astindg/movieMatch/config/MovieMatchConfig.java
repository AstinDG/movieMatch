package com.astindg.movieMatch.config;

import com.astindg.movieMatch.domain.KeyboardsKeeper;
import com.astindg.movieMatch.domain.MessagesKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:movieMatch.properties")
public class MovieMatchConfig {
    @Autowired
    Environment environment;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MessagesKeeper getMessageKeeper(){
        return new MessagesKeeper(this.environment);
    }

    @Bean(initMethod = "initializeKeyboardsMap")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public KeyboardsKeeper getKeyboardsKeeper(){
        return new KeyboardsKeeper(this.environment);
    }


}
