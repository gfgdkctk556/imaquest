package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BattleController {

    @GetMapping("/battle")
    public String showField(Model model) {
        // 任意のモデル属性があれば追加
        return "battle"; // field.htmlのパス
    }

    // 他のメソッドもここに追加
}
