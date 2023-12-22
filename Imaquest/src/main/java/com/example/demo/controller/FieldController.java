package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FieldController {

    @GetMapping("/field")
    public String showField(Model model, HttpServletRequest request) {
        // HttpSessionを取得
        HttpSession session = request.getSession();

        // セッションからプレイヤー名とプレイヤーIDを取得
        String playerName = (String) session.getAttribute("character_Name");
        Integer playerId = (Integer) session.getAttribute("playerId");

        // モデルにプレイヤー名とプレイヤーIDを追加
        model.addAttribute("playerName", playerName);
        model.addAttribute("playerId", playerId);

        // 任意のモデル属性があれば追加
        return "field"; // field.htmlのパス
    }
}
