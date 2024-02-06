package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountCreationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("character_Name") String character_Name,
                           @RequestParam("password") String password, Model model) {
        // プレイヤー名が既に存在するか確認
        String sqlCheckDuplicate = "SELECT COUNT(*) FROM player_characters WHERE character_Name = ?";
        int count = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, character_Name);

        if (count > 0) {
            // 既に同じ名前のプレイヤーが存在する場合、エラーメッセージを設定して登録画面を再表示
            model.addAttribute("errorMessage", "その名前は既に使用されています。別の名前を選択してください。");
            return "register";
        }

        // 新しいプレイヤーキャラクターエンティティを作成し、プレイヤーIDとパスワードを設定
        String sqlInsertPlayer = "INSERT INTO player_characters (character_Name, player_pass, char_type) VALUES (?, ?, 1)";
        jdbcTemplate.update(sqlInsertPlayer, character_Name, password);

        // アカウント作成時に同時に初期キャラクターを作成
        createInitialCharacter(character_Name);

        return "login"; // 登録後にログイン画面にリダイレクト
    }

    private void createInitialCharacter(String characterName) {
        // プレイヤーキャラクターの初期データを作成
        String sqlUpdatePlayerCharacter = "UPDATE player_characters " +
                "SET " +
                "character_Level = 1, " +
                "character_HP = 100, " +
                "character_MP = 50, " +
                "character_Attack = 10, " +
                "character_MagicAttack = 1, " +
                "character_Defense = 10, " +
                "character_Image = 'player_image.png', " +
                "character_Experience = 0 " +
                "WHERE character_Name = ?";

        jdbcTemplate.update(sqlUpdatePlayerCharacter, characterName);

       
    }
}