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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BattleController {
	
	private Map<String, Object> getRandomEnemy() {
	    List<Map<String, Object>> enemies = getEnemies();
	    Random random = new Random();
	    return enemies.get(random.nextInt(enemies.size()));
	}


    private static final String SELECT_PLAYER_INFO_QUERY = "SELECT * FROM player_characters WHERE player_id = ?";
    private static final String SELECT_ENEMIES_QUERY = "SELECT * FROM enemy_info";
    private static final String SELECT_PLAYER_SPELLS_QUERY = "SELECT s.* FROM player_skill ps JOIN magic_skills s ON ps.magic_skill_id = s.magic_skill_id WHERE ps.player_id = ?";
    private static final String SELECT_SPELL_DETAILS_QUERY = "SELECT * FROM magic_skills WHERE magic_skill_name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BattleSQLController battleSQLController;

    public BattleController(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
        this.jdbcTemplate = jdbcTemplate;
        this.battleSQLController = battleSQLController;
    }

    @GetMapping("/battle")
    public String startBattle(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam(name = "startBattle", required = false) String startBattle,
            @RequestParam(name = "playerAction", required = false) String playerAction,
            @RequestParam(name = "selectedSpellName", required = false) String selectedSpellName) {

        Integer playerId = (Integer) session.getAttribute("playerId");

        if (playerId == null) {
            return "redirect:/login";
        }

        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        List<Map<String, Object>> enemies = getEnemies();
        List<Map<String, Object>> spells = getPlayerSpells(playerId);

        // ランダムな敵を選択
        Map<String, Object> randomEnemy = getRandomEnemy(enemies);

        model.addAttribute("playerInfo", playerInfo);
        model.addAttribute("enemies", enemies);
        model.addAttribute("spells", spells);
        model.addAttribute("enemy", randomEnemy); // ランダムな敵情報を追加

        if (startBattle != null) {
            model.addAttribute("battleStarted", true);
        }

        if (playerAction != null) {
            switch (playerAction) {
                case "attack":
                    performPlayerAttack(playerId, model);
                    break;
                case "magic":
                    performPlayerMagic(playerId, selectedSpellName, model);
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
        return jdbcTemplate.queryForMap(SELECT_PLAYER_INFO_QUERY, playerId);
    }

    private List<Map<String, Object>> getEnemies() {
        return jdbcTemplate.queryForList(SELECT_ENEMIES_QUERY);
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
    
 // ランダムな敵を選択するメソッド
    private Map<String, Object> getRandomEnemy(List<Map<String, Object>> enemies) {
        Random random = new Random();
        int randomIndex = random.nextInt(enemies.size());
        return enemies.get(randomIndex);
    }


    private int calculateDamage(int playerAttack, int enemyDefense) {
        Random random = new Random();
        int baseDamage = playerAttack - enemyDefense + random.nextInt(5);
        return Math.max(baseDamage, 1);
    }

    private List<Map<String, Object>> getPlayerSpells(int playerId) {
        return jdbcTemplate.queryForList(SELECT_PLAYER_SPELLS_QUERY, playerId);
    }

    private Map<String, Object> getSpellDetails(String spellName) {
        return jdbcTemplate.queryForMap(SELECT_SPELL_DETAILS_QUERY, spellName);
    }


    private void performPlayerMagic(int playerId, String spellName, Model model) {
        // プレイヤー情報とモンスター情報を取得
        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        List<Map<String, Object>> enemies = getEnemies();
        Map<String, Object> enemy = enemies.get(0);

        // 選択された魔法の詳細情報を取得
        Map<String, Object> spellDetails = getSpellDetails(spellName);

        // 魔法攻撃力を取得
        int magicAttackPower = (int) spellDetails.get("magic_attack_power");

        // モンスターの体力を取得
        int enemyHP = (int) enemy.get("enemy_hp");

        // プレイヤーがモンスターに与えるダメージを計算
        int playerDamage = calculateMagicDamage(magicAttackPower);

        // モンスターの体力を減少させる
        enemyHP -= playerDamage;

        if (enemyHP <= 0) {
            // モンスターが倒れた場合の処理
            battleSQLController.processEnemyDefeat(playerId, enemy);
        }

        // バトル結果メッセージを表示
        model.addAttribute("battleResultMessage", "プレイヤーが" + spellName + "を使用し、モンスターに" + playerDamage + "ダメージを与えました。");
    }

    private int calculateMagicDamage(int magicAttackPower) {
        // 魔法ダメージの計算
        Random random = new Random();
        int baseDamage = magicAttackPower + random.nextInt(5);
        return Math.max(baseDamage, 1); // 最低ダメージを1に設定
    }

}
