package org.example;

import org.example.db.DBEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Main {

    @Autowired
    private DBEntity dbEntity;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }



    // add command listener to print after application starts
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Application started successfully !!!");
        System.out.println(dbEntity.getDBEntity());
    }
}