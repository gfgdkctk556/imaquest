package com.example.demo.controller;

import java.util.Map;

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
                // プレイヤーの全情報を取得
            	Map<String, Object> playerInfo = getPlayerInfo(playerId);
                session.setAttribute("playerId", playerId);
                session.setAttribute("character_Name", playerInfo.get("character_Name"));
                session.setAttribute("character_Level", playerInfo.get("character_Level"));
                session.setAttribute("character_Experience", playerInfo.get("character_Experience"));
                session.setAttribute("character_HP", playerInfo.get("character_HP"));
                session.setAttribute("character_MP", playerInfo.get("character_MP"));
                session.setAttribute("character_Attack", playerInfo.get("character_Attack"));
                session.setAttribute("character_MagicAttack", playerInfo.get("character_MagicAttack"));
                session.setAttribute("character_Defense", playerInfo.get("character_Defense"));
                session.setAttribute("character_Image", playerInfo.get("character_Image"));
                session.setAttribute("first_login", playerInfo.get("first_login"));
                session.setAttribute("character_gold", playerInfo.get("character_gold"));
                session.setAttribute("char_type", playerInfo.get("char_type"));
                
                // 他のプレイヤー情報も必要に応じてセッションに追加
                
                //マックスmpのセッション
                session.setAttribute("maxMP", playerInfo.get("character_MP"));
                
                System.out.println(playerInfo.get("character_HP"));
            } else {
                // エラーハンドリング: プレイヤーIDが取得できない場合の処理
                return "redirect:/ng";
            }

            String insertSql = "INSERT INTO session_data (session_id, character_name) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, sessionId, character_Name);

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

    // プレイヤーIDからプレイヤー情報を取得するメソッド
    private Map<String, Object> getPlayerInfo(int playerId) {
        String selectPlayerInfoSql = "SELECT * FROM player_characters WHERE player_id = ?";
        return jdbcTemplate.queryForMap(selectPlayerInfoSql, playerId);
    }
}
