package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public String showMenu(Model model) {
        // 任意のモデル属性があれば追加
        return "menu"; // menu.htmlのパス
    }

    // 他のメソッドもここに追加
}
