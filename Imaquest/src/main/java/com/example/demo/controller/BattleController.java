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
    private final BattleSQLController battleSQLController;

    public BattleController(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
        this.jdbcTemplate = jdbcTemplate;
        this.battleSQLController = battleSQLController;
    }

    @GetMapping("/battle")
    public String startBattle(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer playerId = (Integer) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/login";
        }

        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        List<Map<String, Object>> enemies = getEnemies();

        model.addAttribute("playerInfo", playerInfo);
        model.addAttribute("enemies", enemies);

        if (request.getParameter("startBattle") != null) {
            model.addAttribute("battleStarted", true);
        }

        if (request.getParameter("playerAction") != null) {
            String playerAction = request.getParameter("playerAction");
            switch (playerAction) {
                case "attack":
                    performPlayerAttack(playerId, model);
                    break;
                case "magic":
                	
                    // 魔法の処理を追加
                    break;
                case "item":
                    // アイテムの処理を追加
                    break;
                default:
                    break;
            }
        }

        battleSQLController.processBattleRewards(playerId, 1, 1, 1);

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

    private void performPlayerAttack(int playerId, Model model) {
        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        List<Map<String, Object>> enemies = getEnemies();

        int playerAttack = (int) playerInfo.get("character_Attack");
        int enemyDefense = (int) enemies.get(0).get("enemy_defense");
        int playerDamage = calculateDamage(playerAttack, enemyDefense);

        Map<String, Object> enemy = enemies.get(0);
        int enemyHP = (int) enemy.get("enemy_hp");
        enemyHP -= playerDamage;

        if (enemyHP <= 0) {
            battleSQLController.processEnemyDefeat(playerId, enemy);
        }

        model.addAttribute("battleResultMessage", "プレイヤーが" + playerDamage + "ダメージを与えました。");
    }

    private int calculateDamage(int playerAttack, int enemyDefense) {
        Random random = new Random();
        int baseDamage = playerAttack - enemyDefense + random.nextInt(5);
        return Math.max(baseDamage, 1);
    }
}
