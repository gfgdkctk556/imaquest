package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImaquestController {

    @GetMapping("/")
    public String home() {
        return "index"; // index.htmlのパス
    }

    @RequestMapping("/login")
    public String loginForm(Model model) {
        return "login"; // login.htmlのパス
    }

    @RequestMapping("/register")
    public String registerForm(Model model) {
        return "register"; // register.htmlのパス
    }
}
