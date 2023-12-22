package com.example.demo.controller;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class BattleSQLController {

    private final JdbcTemplate jdbcTemplate;

    public BattleSQLController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void processBattleRewards(int playerId, int enemyId, int itemId, int magicSkillId) {
        dropGold(playerId, enemyId);
        dropItem(playerId, enemyId);
        gainExperience(playerId, enemyId);
        useItem(playerId, itemId);
        learnMagicSkill(playerId, magicSkillId);
    }

    private void dropGold(int playerId, int enemyId) {
        // プレイヤーの現在のゴールドを取得
        String selectPlayerGoldSql = "SELECT character_gold FROM player_characters WHERE player_id = ?";
        int currentGold = jdbcTemplate.queryForObject(selectPlayerGoldSql, Integer.class, playerId);

        // 敵が持っているゴールドを取得
        String selectEnemyGoldSql = "SELECT enemy_gold_reward FROM enemy_info WHERE enemy_id = ?";
        int enemyGoldReward = jdbcTemplate.queryForObject(selectEnemyGoldSql, Integer.class, enemyId);

        // ゴールドを足し合わせて更新
        String updateGoldSql = "UPDATE player_characters SET character_gold = ? WHERE player_id = ?";
        jdbcTemplate.update(updateGoldSql, currentGold + enemyGoldReward, playerId);
    }

    private void dropItem(int playerId, int enemyId) {
        // アイテムのドロップ処理
        String insertSql = "INSERT INTO player_items (player_id, item_id, item_quantity) " +
                "SELECT ?, enemy_info.enemy_item_reward_id, 1 " +
                "FROM enemy_info " +
                "WHERE enemy_id = ? AND RAND() <= enemy_info.drop_rate";

        // アイテムが既に存在するか確認
        String checkExistingSql = "SELECT COUNT(*) FROM player_items " +
                "WHERE player_id = ? AND item_id = (SELECT enemy_item_reward_id FROM enemy_info WHERE enemy_id = ?)";

        // アイテムが存在すれば数量を増やす、存在しなければ新しいレコードを挿入
        if (jdbcTemplate.queryForObject(checkExistingSql, Integer.class, playerId, enemyId) > 0) {
            String updateQuantitySql = "UPDATE player_items " +
                    "SET item_quantity = item_quantity + 1 " +
                    "WHERE player_id = ? AND item_id = (SELECT enemy_item_reward_id FROM enemy_info WHERE enemy_id = ?)";
            jdbcTemplate.update(updateQuantitySql, playerId, enemyId);
        } else {
            jdbcTemplate.update(insertSql, playerId, enemyId);
        }
    }

    private void useItem(int playerId, int itemId) {
        // アイテムが存在するか確認
        String checkItemExistenceSql = "SELECT COUNT(*) FROM player_items WHERE player_id = ? AND item_id = ?";
        if (jdbcTemplate.queryForObject(checkItemExistenceSql, Integer.class, playerId, itemId) > 0) {
            // アイテムが存在する場合、数量を減らす
            String updateQuantitySql = "UPDATE player_items SET item_quantity = item_quantity - 1 " +
                    "WHERE player_id = ? AND item_id = ?";
            jdbcTemplate.update(updateQuantitySql, playerId, itemId);

            // ここにアイテム使用時の処理を追加
            // 例: 回復アイテムの場合、HPを回復するなど
        }
    }

    private void learnMagicSkill(int playerId, int magicSkillId) {
        // プレイヤーが既にその魔法を覚えているか確認
        String checkMagicSkillExistenceSql = "SELECT COUNT(*) FROM player_magic_skills " +
                "WHERE player_id = ? AND magic_skill_id = ?";
        if (jdbcTemplate.queryForObject(checkMagicSkillExistenceSql, Integer.class, playerId, magicSkillId) == 0) {
            // プレイヤーが魔法を覚えていない場合、覚える
            String insertSql = "INSERT INTO player_magic_skills (player_id, magic_skill_id) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, playerId, magicSkillId);

            // ここに魔法を覚えた際の処理を追加
        }
    }

    private void gainExperience(int playerId, int enemyId) {
        // 敵が持っている経験値を取得
        String selectEnemyExpSql = "SELECT enemy_exp_reward FROM enemy_info WHERE enemy_id = ?";
        int enemyExpReward = jdbcTemplate.queryForObject(selectEnemyExpSql, Integer.class, enemyId);

        // プレイヤーの現在の経験値を取得
        String selectPlayerExpSql = "SELECT character_Experience FROM player_characters WHERE player_id = ?";
        int currentExp = jdbcTemplate.queryForObject(selectPlayerExpSql, Integer.class, playerId);

        // プレイヤーの経験値を足し合わせて更新
        String updateExpSql = "UPDATE player_characters SET character_Experience = ? WHERE player_id = ?";
        jdbcTemplate.update(updateExpSql, currentExp + enemyExpReward, playerId);
        // レベルアップ処理も必要。
    }

   

    public void processEnemyDefeat(int playerId, Map<String, Object> enemy) {
        // ここに敵撃破時の処理を追加
    	


        // 経験値取得
        int enemyExpReward = (int) enemy.get("enemy_exp_reward");
        gainExperience(playerId, enemyExpReward);

        // ゴールド取得
        int enemyGoldReward = (int) enemy.get("enemy_gold_reward");
        dropGold(playerId, enemyGoldReward);

        // アイテム取得
        int enemyId = (int) enemy.get("enemy_id");
        dropItem(playerId, enemyId);
    }

}
