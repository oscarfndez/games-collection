package com.gamescollection;

import org.springframework.boot.SpringApplication;

public class TestGamesCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.from(GamesCollectionApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
