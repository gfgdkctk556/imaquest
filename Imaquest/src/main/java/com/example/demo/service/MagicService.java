package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.controller.BattleSQLController;

@Service
public class MagicService {

    private static final String SELECT_PLAYER_INFO_QUERY = "SELECT * FROM player_characters WHERE player_id = ?";
    private static final String SELECT_ENEMIES_QUERY = "SELECT * FROM enemy_info";

    private final JdbcTemplate jdbcTemplate;
    private final BattleSQLController battleSQLController;

    @Autowired
    public MagicService(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
        this.jdbcTemplate = jdbcTemplate;
        this.battleSQLController = battleSQLController;
    }

    // 魔法処理
    //プレイヤーIDと、選択された魔法名、モデル、セッションを引数に取る
    //魔法ボタンが押されたら、Controllerでは、このメソッドを呼び出す
    public String performMagic(int playerId, String selectedSpellName, Model model, HttpSession session) {
        // プレイヤーの情報を取得
        Map<String, Object> playerInfo = getPlayerInfo(playerId);

        // 敵の情報を取得
        List<Map<String, Object>> enemies = getEnemies();
        // セッションから敵の情報を取得
        Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
        // プレイヤーの魔法攻撃力と敵の魔法防御力を取得
        int playerMagicAttack = (int) playerInfo.get("character_MagicAttack");
        int enemyMagicDefense = (int) enemies.get(0).get("enemy_magic_defense");
        // ダメージを計算
        int playerMagicDamage = calculateMagicDamage(playerMagicAttack, enemyMagicDefense);
        // バトル結果メッセージ
        String battleResultMessage = null;

        // セッションからプレイヤーおよびモンスターのHPを取得
        Integer playerHP = (Integer) session.getAttribute("character_HP");
        if (playerHP == null) {
            // "playerHP"がnullの場合の処理
            model.addAttribute("errorMessage", "プレイヤーのHP情報が取得できませんでした。");
            return "error";
        }

        // 新しい変数にモンスターのHPを格納
        int monsterHP = (int) currentEnemy.get("enemy_hp");

        // モンスターのHPを減算
        monsterHP -= playerMagicDamage;

        if (monsterHP <= 0) {
            // モンスターのHPが0以下の場合の処理
            String monsterName = (String) currentEnemy.get("enemy_name");
            battleResultMessage = "プレイヤーが" + monsterName + "に" + playerMagicDamage + "ダメージを与えました！";
            model.addAttribute("battleResultMessage", battleResultMessage);
            System.out.println("monsterHP: " + monsterHP);
        } else {
            // モンスターのHPが0以下でない場合の処理
            String monsterName = (String) currentEnemy.get("enemy_name");
            battleResultMessage = "プレイヤーが" + monsterName + "に" + playerMagicDamage + "ダメージを与えました！";
            model.addAttribute("battleResultMessage", battleResultMessage);
            System.out.println("monsterHP: " + monsterHP);

            // 新しい変数にモンスターのHPを格納
            currentEnemy.put("enemy_hp", monsterHP);

            // バトルメッセージをモデルに追加
            model.addAttribute("battleResultMessage", battleResultMessage);
        }

        return "battle";
    }
//プレイヤーの情報を取得
    private Map<String, Object> getPlayerInfo(int playerId) {
        return jdbcTemplate.queryForMap(SELECT_PLAYER_INFO_QUERY, playerId);
    }
//敵の情報を取得
    private List<Map<String, Object>> getEnemies() {
        return jdbcTemplate.queryForList(SELECT_ENEMIES_QUERY);
    }
//魔法ダメージの計算
    private int calculateMagicDamage(int playerMagicAttack, int enemyMagicDefense) {
        Random random = new Random();
        int baseMagicDamage = playerMagicAttack - enemyMagicDefense + random.nextInt(5);
        return Math.max(baseMagicDamage, 1);
    }
}
