package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String character_Name, @RequestParam String password, Model model) {
        // プレイヤー名とパスワードが一致するか確認
        String sqlCheckLogin = "SELECT COUNT(*) FROM player_characters WHERE character_Name = ? AND player_pass = ?";
        int count = jdbcTemplate.queryForObject(sqlCheckLogin, Integer.class, character_Name, password);

        if (count > 0) {
            // ログイン成功
            return "field"; // ログイン後のリダイレクト先（例としてプレイヤーリスト画面）
        } else {
            // ログイン失敗時のエラーメッセージを設定してログイン画面を再表示
            model.addAttribute("errorMessage", "プレイヤー名またはパスワードが正しくありません。");
            return "login";
        }
    }
}
