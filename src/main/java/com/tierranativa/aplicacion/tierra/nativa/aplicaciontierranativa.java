package com.tierranativa.aplicacion.tierra.nativa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
@EnableAsync
public class

aplicaciontierranativa {

    public static void main(String[] args) {
        SpringApplication.run(aplicaciontierranativa.class, args);
    }
}
