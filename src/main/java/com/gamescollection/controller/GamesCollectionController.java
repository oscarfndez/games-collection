package com.gamescollection.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
public class GamesCollectionController {

    // Endpoint para leer un blog por su ID
    @GetMapping("/readBlog/{id}")
    public String readBlog(@PathVariable UUID id) {
        return "Leyendo el blog con ID: " + id.toString();
    }
}