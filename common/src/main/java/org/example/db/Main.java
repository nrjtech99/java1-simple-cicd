package org.example.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    @Autowired
    private DBEntity dbEntity;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }



    // add command listener to print after application starts
//    @EventListener(ApplicationReadyEvent.class)
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        System.out.println("Application started successfully.");
//        System.out.println(dbEntity.getDBEntity());
//    }
}