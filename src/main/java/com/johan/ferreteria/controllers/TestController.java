package com.johan.ferreteria.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController 
public class TestController {



    @GetMapping 
    String hi() {
        return "Hola";
    }

}
