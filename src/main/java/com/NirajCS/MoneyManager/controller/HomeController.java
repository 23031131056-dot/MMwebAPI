package com.NirajCS.MoneyManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/health","/status"})
public class HomeController 
{
    @RequestMapping("")
    public String healthCheck() {
        return "Money Manager Application is running!";
    }

}
