package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FieldController {

    @GetMapping("/field")
    public String showField(Model model) {
        // 任意のモデル属性があれば追加
        return "field"; // field.htmlのパス
    }

    // 他のメソッドもここに追加
}
