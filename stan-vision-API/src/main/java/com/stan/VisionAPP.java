package com.stan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VisionAPP {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(VisionAPP.class, args);
    }

}
