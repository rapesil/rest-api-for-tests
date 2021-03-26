package com.peixoto.api.controllers;

import com.peixoto.api.models.HelloWorld;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity<HelloWorld> greeting() {
        HelloWorld hello = HelloWorld.builder().hello("Hello, World").build();
        return ResponseEntity.ok(hello);
    }

}
