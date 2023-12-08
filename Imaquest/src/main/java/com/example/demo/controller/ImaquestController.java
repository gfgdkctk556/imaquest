//package com.example.demo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class ImaquestController {
//
//    @GetMapping("/")
//    public String home() {
//        return "index"; // index.htmlのパス
//    }
//
//    @RequestMapping("/login")
//    public String loginForm(Model model) {
//        return "login"; // login.htmlのパス
//    }
//
//    @RequestMapping("/register")
//    public String registerForm(Model model) {
//        return "register"; // register.htmlのパス
//    }
//
//    // 新しく追加されたコントローラー
//    @RequestMapping("/demofield")
//    public String demoField(Model model) {
//        return "demofield"; // demofield.htmlのパス
//    }
//
//    @RequestMapping("/menu")
//    public String menu(Model model) {
//        return "menu"; // menu.htmlのパス
//    }
//
//    @RequestMapping("/mura")
//    public String mura(Model model) {
//        return "mura"; // mura.htmlのパス
//    }
//
//    @RequestMapping("/machi")
//    public String machi(Model model) {
//        return "machi"; // machi.htmlのパス
//    }
//
//    @RequestMapping("/battle")
//    public String battle(Model model) {
//        return "battle"; // battle.htmlのパス
//    }
//}
