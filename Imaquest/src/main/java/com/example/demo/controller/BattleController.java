package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BattleController {

    private final JdbcTemplate jdbcTemplate;

    public BattleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/battle")
    public String startBattle(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int playerId = (int) session.getAttribute("playerId");

        // プレイヤーの情報を取得
        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        model.addAttribute("playerInfo", playerInfo);

        // 敵の情報を取得
        List<Map<String, Object>> enemies = getEnemies();
        model.addAttribute("enemies", enemies);

        // バトルの結果を格納するメッセージ
        String battleResultMessage = "";

        // バトルが実行されたかどうかを判定
        if (request.getParameter("startBattle") != null) {
            // バトル開始時の処理

            // ここで敵の行動やプレイヤーの行動を初期化
            // 例: enemies.get(0)が現在の敵として選択されるなど

            model.addAttribute("battleStarted", true);
        }

        //? プレイヤーの行動が選択された場合
        if (request.getParameter("playerAction") != null) {
            // プレイヤーの行動に応じた処理を実行
            // 例: 攻撃、魔法、アイテムの処理を分岐
            String playerAction = request.getParameter("playerAction");
            switch (playerAction) {
                case "attack":
                    // プレイヤーの攻撃処理
                    battleResultMessage = performPlayerAttack(playerInfo, enemies);
                    break;
                case "magic":
                    // プレイヤーの魔法処理
                    // 未実装
                    break;
                case "item":
                    // アイテム使用処理
                    // 未実装
                    break;
                default:
                    break;
            }
        }

        // ここで敵の行動処理などを追加

        model.addAttribute("battleResultMessage", battleResultMessage);

        return "battle";
    }

    private Map<String, Object> getPlayerInfo(int playerId) {
        String query = "SELECT * FROM player_characters WHERE player_id = ?";
        return jdbcTemplate.queryForMap(query, playerId);
    }

    private List<Map<String, Object>> getEnemies() {
        String query = "SELECT * FROM enemy_info";
        return jdbcTemplate.queryForList(query);
    }

    private String performPlayerAttack(Map<String, Object> playerInfo, List<Map<String, Object>> enemies) {
        // プレイヤーの攻撃処理

        // 仮のダメージ計算
        int playerDamage = calculateDamage((int) playerInfo.get("character_Attack"));

        // 例: 最初の敵にダメージを与える
        Map<String, Object> enemy = enemies.get(0);
        int enemyHP = (int) enemy.get("enemy_hp");
        enemyHP -= playerDamage;

        // 例: 敵のHPが0以下になった場合の処理
        if (enemyHP <= 0) {
            // ここで敵を倒した場合の処理などを行う
            // 例: 経験値の取得、ゴールドの取得など
        }

        // 敵のHPを更新
        updateEnemyHP((int) enemy.get("enemy_id"), enemyHP);

        return "プレイヤーが" + playerDamage + "ダメージを与えました。";
    }

    private int calculateDamage(int attackPower) {
        // 仮のダメージ計算
        Random random = new Random();
        return attackPower + random.nextInt(5);
    }

    private void updateEnemyHP(int enemyId, int newHP) {
        String query = "UPDATE enemy_info SET enemy_hp = ? WHERE enemy_id = ?";
        jdbcTemplate.update(query, newHP, enemyId);
    }

    private void updatePlayerXP(int playerId, int xp) {
        // 現在の経験値を取得
        int currentXP = getPlayerExperience(playerId);

        // 新しい経験値を計算してアップデート
        int newXP = currentXP + xp;
        String updateXPQuery = "UPDATE player_characters SET character_Experience = ? WHERE player_id = ?";
        jdbcTemplate.update(updateXPQuery, newXP, playerId);
    }

    private int getPlayerExperience(int playerId) {
        String query = "SELECT character_Experience FROM player_characters WHERE player_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, playerId);
    }


private void checkLevelUp(int playerId) {
    // 現在の経験値とレベルを取得
    String query = "SELECT character_Level, character_Experience FROM player_characters WHERE player_id = ?";
    Map<String, Object> playerStats = jdbcTemplate.queryForMap(query, playerId);
    int currentLevel = (int) playerStats.get("character_Level");
    int currentXP = (int) playerStats.get("character_Experience");

    // レベルアップに必要な経験値
    int requiredXP = calculateRequiredXP(currentLevel);

    if (currentXP >= requiredXP) {
        // レベルアップ
        int newLevel = currentLevel + 1;

        // ステータスのアップデート
        updatePlayerStats(playerId, newLevel);

        // メッセージ
        System.out.println("レベルアップしました！ 新しいレベル: " + newLevel);
    }
}

private int calculateRequiredXP(int currentLevel) {
    // 仮の計算式
    return currentLevel * 100;
}

private void updatePlayerStats(int playerId, int newLevel) {
    // レベルアップ時のステータスのアップデート
    // ここで必要なステータスのアップデート処理を実装

    // 仮の処理として、全てのステータスを1上げる
    String updateStatsQuery = "UPDATE player_characters SET character_Level = ?, character_HP = ?, character_Attack = ?, character_Defense = ? WHERE player_id = ?";
    jdbcTemplate.update(updateStatsQuery, newLevel, 1, 1, 1, playerId);
}

private void updatePlayerItems(int playerId, int itemId, int quantity) {
    // プレイヤーのアイテムをアップデート
    String query = "SELECT * FROM player_items WHERE player_id = ? AND item_id = ?";
    List<Map<String, Object>> existingItems = jdbcTemplate.queryForList(query, playerId, itemId);

    if (existingItems.isEmpty()) {
        // アイテムがまだない場合は新しく挿入
        String insertQuery = "INSERT INTO player_items (player_id, item_id, item_quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertQuery, playerId, itemId, quantity);
    } else {
        // アイテムが既にある場合は数量を更新
        String updateQuery = "UPDATE player_items SET item_quantity = ? WHERE player_id = ? AND item_id = ?";
        jdbcTemplate.update(updateQuery, quantity, playerId, itemId);
    }
}
}
