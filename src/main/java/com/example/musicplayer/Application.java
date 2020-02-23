package com.example.musicplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("\n" +
                "  __  __           _        _____  _                       \n" +
                " |  \\/  |         (_)      |  __ \\| |                      \n" +
                " | \\  / |_   _ ___ _  ___  | |__) | | __ _ _   _  ___ _ __ \n" +
                " | |\\/| | | | / __| |/ __| |  ___/| |/ _` | | | |/ _ \\ '__|\n" +
                " | |  | | |_| \\__ \\ | (__  | |    | | (_| | |_| |  __/ |   \n" +
                " |_|  |_|\\__,_|___/_|\\___| |_|    |_|\\__,_|\\__, |\\___|_|   \n" +
                "                                            __/ |          \n" +
                "                                           |___/           \n");
    }
}