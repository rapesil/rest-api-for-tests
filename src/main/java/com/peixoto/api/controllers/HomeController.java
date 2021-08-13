package com.peixoto.api.controllers;

import com.peixoto.api.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String greeting() {
        return "API is up and running";
    }


}
