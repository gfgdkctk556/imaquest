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
    public String register(@RequestParam("character_Name") String character_Name, @RequestParam("password") String password, Model model) {
        // プレイヤー名が既に存在するか確認
        String sqlCheckDuplicate = "SELECT COUNT(*) FROM player_characters WHERE character_Name = ?";
        int count = jdbcTemplate.queryForObject(sqlCheckDuplicate, Integer.class, character_Name);

        if (count > 0) {
            // 既に同じ名前のプレイヤーが存在する場合、エラーメッセージを設定して登録画面を再表示
            model.addAttribute("errorMessage", "その名前は既に使用されています。別の名前を選択してください。");
            return "register";
        }

        // 新しいプレイヤーキャラクターエンティティを作成し、プレイヤーIDとパスワードを設定
        String sqlInsertPlayer = "INSERT INTO player_characters (character_Name, player_pass) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsertPlayer, character_Name, password);

        // アカウント作成時に同時に初期キャラクターを作成
        createInitialCharacter(character_Name);

        return "login"; // 登録後にログイン画面にリダイレクト
    }

    private void createInitialCharacter(String characterName) {
    	
    	// プレイヤーキャラクターの初期データを作成
        String sqlInsertPlayerCharacter = "INSERT INTO player_characters (character_Name, character_Level, character_HP, character_MP, character_Attack, character_Defense, character_Image, character_Experience) " +
                "VALUES (?, 1, 100, 50, 10, 10, 'player_image.png', 0)";
        jdbcTemplate.update(sqlInsertPlayerCharacter, characterName);

    	
        // タンクの初期キャラクターを作成
        String sqlInsertTank = "INSERT INTO tanks (character_Name, character_Level, character_HP, character_MP, character_Attack, character_Defense, character_Image, character_Experience) " +
                "VALUES (?, 1, 150, 30, 8, 15, 'tank_image.png', 0)";
        jdbcTemplate.update(sqlInsertTank,  "_Tank");

        // 回復士の初期キャラクターを作成
        String sqlInsertHealer = "INSERT INTO healers (character_Name, character_Level, character_HP, character_MP, character_Attack, character_Defense, character_Image, character_Experience) " +
                "VALUES (?, 1, 80, 60, 6, 10, 'healer_image.png', 0)";
        jdbcTemplate.update(sqlInsertHealer,  "_Healer");

        // 魔法使いの初期キャラクターを作成
        String sqlInsertWizard = "INSERT INTO wizards (character_Name, character_Level, character_HP, character_MP, character_Attack, character_Defense, character_Image, character_Experience) " +
                "VALUES (?, 1, 70, 80, 5, 8, 'wizard_image.png', 0)";
        jdbcTemplate.update(sqlInsertWizard,  "_Wizard");
    }
}
