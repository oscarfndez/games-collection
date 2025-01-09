package com.ficticius.blog;

import org.springframework.boot.SpringApplication;

public class TestFicticiusBlogApplication {

    public static void main(String[] args) {
        SpringApplication.from(FicticiusBlogApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
