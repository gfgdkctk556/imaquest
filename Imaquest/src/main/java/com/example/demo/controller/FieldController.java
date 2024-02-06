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

        // プレイヤーの位置情報を取得
        Integer playerRow = (Integer) session.getAttribute("playerRow");
        Integer playerCol = (Integer) session.getAttribute("playerCol");

        // 他にも必要な情報があればセッションから取得
        //セッションがなければ縦0，横0にセッションを追加
		if (playerRow == null) {
			playerRow = 0;
			session.setAttribute("playerRow", playerRow);
		}
		if (playerCol == null) {
			playerCol = 0;
            session.setAttribute("playerCol", playerCol);
        }
        // モデルにプレイヤー名、プレイヤーID、プレイヤーの位置情報を追加
        model.addAttribute("playerName", playerName);
        model.addAttribute("playerId", playerId);
        model.addAttribute("playerRow", playerRow);
        model.addAttribute("playerCol", playerCol);

        // 他の画面に表示したい情報があればモデルに追加

        return "field"; // field.htmlのパス
    }
}
