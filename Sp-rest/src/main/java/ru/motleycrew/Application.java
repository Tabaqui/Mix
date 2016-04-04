package ru.motleycrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.motleycrew.config.JpaConfig;

/**
 * Created by vas on 02.04.16.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@EnableJpaRepositories(basePackages = {"ru.motleycrew"})
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(new Class<?>[] {Application.class, JpaConfig.class}, args);
    }
}
