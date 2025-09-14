package com.example.prodavnicaracunara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class ProdavnicaRacunaraApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdavnicaRacunaraApplication.class, args);
    }
}