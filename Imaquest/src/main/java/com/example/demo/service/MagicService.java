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
    private static final String SELECT_SPELL_DETAILS_QUERY = "SELECT * FROM magic_skills WHERE magic_skill_name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BattleSQLController battleSQLController;

    @Autowired
    public MagicService(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
        this.jdbcTemplate = jdbcTemplate;
        this.battleSQLController = battleSQLController;
    }

    public String performMagic(int playerId, String spellName, Model model, HttpSession session) {
        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        List<Map<String, Object>> enemies = getEnemies();
        Map<String, Object> enemy = enemies.get(0);

        Map<String, Object> spellDetails = getSpellDetails(spellName);
        int magicAttackPower = (int) spellDetails.get("magic_attack_power");
        int enemyHP = (int) enemy.get("enemy_hp");

        int playerDamage = calculateMagicDamage(magicAttackPower);
        enemyHP -= playerDamage;

        // セッションに敵のHP変化を保存
        session.setAttribute("enemyHPChange", playerDamage);

        if (enemyHP <= 0) {
            battleSQLController.processEnemyDefeat(playerId, enemy);
            String battleResultMessage = "プレイヤーが" + spellName + "を使用し、敵に" + playerDamage + "ダメージを与えて倒しました！";
            model.addAttribute("battleResultMessage", battleResultMessage);
            return getMagicServiceModel(playerId, spellName, playerDamage, session);
        } else {
            String battleResultMessage = "プレイヤーが" + spellName + "を使用し、敵に" + playerDamage + "ダメージを与えました！";
            model.addAttribute("battleResultMessage", battleResultMessage);
            return getMagicServiceModel(playerId, spellName, playerDamage, session);
        }
    }

    private String getMagicServiceModel(int playerId, String spellName, int damageDealt, HttpSession session) {
        // ここで魔法サービスのモデルを生成するロジックを実装
        // 例: "プレイヤーID: " + playerId + ", 魔法: " + spellName + ", ダメージ: " + damageDealt
        return "プレイヤーID: " + playerId + ", 魔法: " + spellName + ", ダメージ: " + damageDealt;
    }

    private Map<String, Object> getPlayerInfo(int playerId) {
        return jdbcTemplate.queryForMap(SELECT_PLAYER_INFO_QUERY, playerId);
    }

    private List<Map<String, Object>> getEnemies() {
        return jdbcTemplate.queryForList(SELECT_ENEMIES_QUERY);
    }

    private Map<String, Object> getSpellDetails(String spellName) {
        return jdbcTemplate.queryForMap(SELECT_SPELL_DETAILS_QUERY, spellName);
    }

    private int calculateMagicDamage(int magicAttackPower) {
        Random random = new Random();
        int baseDamage = magicAttackPower + random.nextInt(5);
        return Math.max(baseDamage, 1);
    }
}
