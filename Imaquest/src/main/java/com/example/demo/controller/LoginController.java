package com.example.demo.controller;

import javax.servlet.http.HttpSession;

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
    public String login(
            @RequestParam String character_Name,
            @RequestParam String password,
            Model model,
            HttpSession session
    ) {
        String sqlCheckLogin = "SELECT COUNT(*) FROM player_characters WHERE character_Name = ? AND player_pass = ?";
        int count = jdbcTemplate.queryForObject(sqlCheckLogin, Integer.class, character_Name, password);
        if (count > 0) {
            String sessionId = session.getId();

            // プレイヤーIDを取得
            Integer playerId = getPlayerId(character_Name);
            
            // プレイヤーIDが取得できた場合、セッションに設定
            if (playerId != null) {
                session.setAttribute("playerId", playerId);
            } else {
                // エラーハンドリング: プレイヤーIDが取得できない場合の処理
                return "redirect:/ng";
            }

            String insertSql = "INSERT INTO session_data (session_id, character_name) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, sessionId, character_Name);

            // プレイヤー名をセッションに設定
            session.setAttribute("character_Name", character_Name);

            // Check the value of the first_login column
            String checkFirstLoginSql = "SELECT first_login FROM player_characters WHERE character_Name = ?";
            int firstLogin = jdbcTemplate.queryForObject(checkFirstLoginSql, Integer.class, character_Name);

            if (firstLogin == 1) {
                // If first_login is 1, display opening.html
                String updateFirstLoginSql = "UPDATE player_characters SET first_login = 5 WHERE character_Name = ?";
                jdbcTemplate.update(updateFirstLoginSql, character_Name);
                return "opening";
            } else {
                // If first_login is not 1, display field.html
                return "redirect:/field";
            }
        } else {
            return "redirect:/ng";
        }
    }

    // プレイヤー名からプレイヤーIDを取得するメソッド
    private Integer getPlayerId(String character_Name) {
        String sqlGetPlayerId = "SELECT player_id FROM player_characters WHERE character_Name = ?";
        return jdbcTemplate.queryForObject(sqlGetPlayerId, Integer.class, character_Name);
    }
}
