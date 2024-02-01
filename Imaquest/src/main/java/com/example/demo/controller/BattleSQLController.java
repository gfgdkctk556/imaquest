package com.example.demo.controller;

//enemy_info テーブル:
//
//enemy_id: 敵のID
//enemy_name: 敵の名前
//enemy_image: 敵の画像
//enemy_level: 敵のレベル
//enemy_hp: 敵の体力
//enemy_attack: 敵の攻撃力
//enemy_defense: 敵の防御力
//enemy_gold_reward: 戦闘での報酬として得られるゴールド
//enemy_item_reward_id: 戦闘での報酬として得られるアイテムのID
//drop_rate: アイテムがドロップする確率
//enemy_exp_reward: 戦闘での経験値の報酬
//equipment テーブル:
//
//equipment_id: 装備のID
//equipment_name: 装備の名前
//effect: 装備の効果
//attack_power: 攻撃力の向上値
//defense_power: 防御力の向上値
//purchase_price: 購入価格
//sell_price: 売却価格
//magic_power: 魔法攻撃力の向上値
//magicpoint: MPの向上値
//healers テーブル:
//
//player_id: プレイヤーのID
//character_Name: キャラクターの名前
//character_Level: キャラクターのレベル
//character_Experience: キャラクターの経験値
//character_HP: キャラクターの体力
//character_MP: キャラクターのマジックポイント（MP）
//character_Attack: キャラクターの攻撃力
//character_Defense: キャラクターの防御力
//character_Image: キャラクターの画像
//char_type: キャラクターのタイプ（3＝回復士）
//healers_skill テーブル:
//
//player_id: プレイヤーのID
//magic_skill_id: 魔法スキルのID
//items テーブル:
//
//item_id: アイテムのID
//item_name: アイテムの名前
//item_effect: アイテムの効果
//purchase_price: 購入価格
//sell_price: 売却価格
//item_HP_heal: HP回復量
//item_MP_heal: MP回復量
//item_damage: 与えるダメージ量
//item_status_heal: ステータス回復量
//item_type: アイテムの種類（回復、攻撃など）
//item_attack: 攻撃力向上値
//levelup テーブル:
//
//Level: レベル
//RequiredExperience: 次のレベルに上がるために必要な経験値
//magic_skills テーブル:
//
//skill_id: 魔法スキルのID
//skill_name: 魔法スキルの名前
//magic_type: 魔法のタイプ（攻撃、回復など）
//magic_attack_power: 魔法の攻撃力
//effect: 魔法の効果
//mp_cost: 消費MP
//magic_type_heal: 魔法の回復量
//char_type: キャラクターのタイプ（1＝魔法使い）
//player_characters テーブル:
//
//player_id: プレイヤーのID
//player_name: プレイヤーの名前
//player_level: プレイヤーのレベル
//player_experience: プレイヤーの経験値
//player_hp: プレイヤーの体力
//player_mp: プレイヤーのマジックポイント（MP）
//player_attack: プレイヤーの攻撃力
//player_defense: プレイヤーの防御力
//player_gold: プレイヤーの所持金
//current_town_id: 現在の町のID
//character_image: プレイヤーの画像
//player_equipment テーブル:
//
//player_id: プレイヤーのID
//equipment_id: 装備のID
//equipped: 装備しているかどうか（1＝装備中）
//player_items テーブル:
//
//player_id: プレイヤーのID
//item_id: アイテムのID
//quantity: 所持しているアイテムの数量
//player_progress テーブル:
//
//player_id: プレイヤーのID
//current_enemy_id: 現在の敵のID
//current_story_id: 現在のストーリーのID
//player_skill テーブル:
//
//player_id: プレイヤーのID
//magic_skill_id: スキルのID
//session_data テーブル:
//
//session_id: セッションのID
//player_id: プレイヤーのID
//last_activity: 最終アクティビティのタイムスタンプ
//stories テーブル:
//
//story_id: ストーリーのID
//story_text: ストーリーのテキスト
//requirement_enemy_id: ストーリーの進行に必要な敵のID
//tanks テーブル:
//
//player_id: プレイヤーのID
//character_name: キャラクターの名前
//character_level: キャラクターのレベル
//character_experience: キャラクターの経験値
//character_hp: キャラクターの体力
//character_mp: キャラクターのマジックポイント（MP）
//character_attack: キャラクターの攻撃力
//character_defense: キャラクターの防御力
//character_image: キャラクターの画像
//char_type: キャラクターのタイプ（2＝タンク）
//tanks_skill テーブル:
//
//player_id: プレイヤーのID
//magic_skill_id: 魔法スキルのID
//wizards テーブル:
//
//player_id: プレイヤーのID
//character_name: キャラクターの名前
//character_level: キャラクターのレベル
//character_experience: キャラクターの経験値
//character_hp: キャラクターの体力
//character_mp: キャラクターのマジックポイント（MP）
//character_attack: キャラクターの攻撃力
//character_defense: キャラクターの防御力
//character_image: キャラクターの画像
//char_type: キャラクターのタイプ（1＝魔法使い）
import java.util.List;
import java.util.Map;
import java.util.Random;

//imaquestデータベースの概要は以下の通りです。
//
//テーブル一覧:
//
//enemy_info: 敵の情報を保存するテーブル。
//equipment: 装備アイテムの情報を保存するテーブル。
//healers: 回復役のキャラクター情報を保存するテーブル。
//healers_skill: 回復役のスキル情報を保存するテーブル。
//items: アイテムの情報を保存するテーブル。
//levelup: レベルアップに必要な経験値を保存するテーブル。
//magic_skills: 魔法のスキル情報を保存するテーブル。
//player_characters: プレイヤーキャラクターの情報を保存するテーブル。
//player_equipment: プレイヤーの装備アイテム情報を保存するテーブル。
//player_items: プレイヤーのアイテム情報を保存するテーブル。
//player_progress: プレイヤーの進捗情報を保存するテーブル。
//player_skill: プレイヤーのスキル情報を保存するテーブル。
//session_data: ゲームセッションの情報を保存するテーブル。
//stories: ストーリーの情報を保存するテーブル。
//tanks: タンク役のキャラクター情報を保存するテーブル。
//tanks_skill: タンク役のスキル情報を保存するテーブル。
//wizards: 魔法使い役のキャラクター情報を保存するテーブル。

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BattleSQLController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BattleSQLController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setCurrentEnemy(HttpSession session) {
        List<Map<String, Object>> enemies = getEnemies();

        // ランダムに敵を選択
        Random random = new Random();
        int randomIndex = random.nextInt(enemies.size());
        Map<String, Object> randomEnemy = enemies.get(randomIndex);

        // セッションに選択した敵の情報を保存
        session.setAttribute("currentEnemy", randomEnemy);
    }

	public List<Map<String, Object>> getEnemies() {
		String selectSql = "SELECT * FROM enemy_info";
		return jdbcTemplate.queryForList(selectSql);
	}
   
    @Transactional
    public void processBattleRewards(int enemyId, int itemId, int magicSkillId, HttpSession session) {
        // セッションからプレイヤーIDを取得
        Integer playerId = (Integer) session.getAttribute("player_id");

        if (playerId == null) {
            // プレイヤーIDがnullの場合、エラー処理を行うか適切な対応を行う
            // 例えば、ログを出力してエラーを通知する、もしくはデフォルトのプレイヤーIDなどを使用するなど
            // ここでは簡単にログを出力する例を示します
            System.err.println("プレイヤーIDがセッションから取得できませんでした。");
            return;
        }

        dropGold(playerId, enemyId);
        dropItem(playerId, enemyId);
        gainExperience(playerId, enemyId);
        useItem(playerId, itemId);
        learnMagicSkill(playerId, magicSkillId);
    }
//ゴールドのドロップ
    private void dropGold(int playerId, int enemyId) {
        String selectPlayerGoldSql = "SELECT character_gold FROM player_characters WHERE player_id = ?";
        int currentGold = jdbcTemplate.queryForObject(selectPlayerGoldSql, Integer.class, playerId);

        String selectEnemyGoldSql = "SELECT enemy_gold_reward FROM enemy_info WHERE enemy_id = ?";
        int enemyGoldReward = jdbcTemplate.queryForObject(selectEnemyGoldSql, Integer.class, enemyId);

        String updateGoldSql = "UPDATE player_characters SET character_gold = ? WHERE player_id = ?";
        jdbcTemplate.update(updateGoldSql, currentGold + enemyGoldReward, playerId);
    }

    //アイテムのドロップ
    private void dropItem(int playerId, int enemyId) {
        String insertSql = "INSERT INTO player_items (player_id, item_id, item_quantity) " +
                "SELECT ?, enemy_info.enemy_item_reward_id, 1 " +
                "FROM enemy_info " +
                "WHERE enemy_id = ? AND RAND() <= enemy_info.drop_rate";

        String checkExistingSql = "SELECT COUNT(*) FROM player_items " +
                "WHERE player_id = ? AND item_id = (SELECT enemy_item_reward_id FROM enemy_info WHERE enemy_id = ?)";

        if (jdbcTemplate.queryForObject(checkExistingSql, Integer.class, playerId, enemyId) > 0) {
            String updateQuantitySql = "UPDATE player_items " +
                    "SET item_quantity = item_quantity + 1 " +
                    "WHERE player_id = ? AND item_id = (SELECT enemy_item_reward_id FROM enemy_info WHERE enemy_id = ?)";
            jdbcTemplate.update(updateQuantitySql, playerId, enemyId);
        } else {
            jdbcTemplate.update(insertSql, playerId, enemyId);
        }
    }
//アイテムの使用
    private void useItem(int playerId, int itemId) {
        String checkItemExistenceSql = "SELECT COUNT(*) FROM player_items WHERE player_id = ? AND item_id = ?";
        if (jdbcTemplate.queryForObject(checkItemExistenceSql, Integer.class, playerId, itemId) > 0) {
            String updateQuantitySql = "UPDATE player_items SET item_quantity = item_quantity - 1 " +
                    "WHERE player_id = ? AND item_id = ?";
            jdbcTemplate.update(updateQuantitySql, playerId, itemId);
        }
    }
//	魔法スキルの習得
    
    private void learnMagicSkill(int playerId, int magicSkillId) {
        String checkMagicSkillExistenceSql = "SELECT COUNT(*) FROM player_skill WHERE player_id = ? AND magic_skill_id = ?";
        if (jdbcTemplate.queryForObject(checkMagicSkillExistenceSql, Integer.class, playerId, magicSkillId) == 0) {
            String insertSql = "INSERT INTO player_skill (player_id, magic_skill_id) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, playerId, magicSkillId);
        }
    }
//    経験値の獲得
    private void gainExperience(int playerId, int enemyId) {
        String selectEnemyExpSql = "SELECT enemy_exp_reward FROM enemy_info WHERE enemy_id = ?";
        int enemyExpReward = jdbcTemplate.queryForObject(selectEnemyExpSql, Integer.class, enemyId);

        String selectPlayerExpSql = "SELECT character_Experience FROM player_characters WHERE player_id = ?";
        int currentExp = jdbcTemplate.queryForObject(selectPlayerExpSql, Integer.class, playerId);

        String updateExpSql = "UPDATE player_characters SET character_Experience = ? WHERE player_id = ?";
        jdbcTemplate.update(updateExpSql, currentExp + enemyExpReward, playerId);
    }
//    敵の撃破
    public void processEnemyDefeat(int playerId, Map<String, Object> enemy) {
        int enemyExpReward = (int) enemy.get("enemy_exp_reward");
        int enemyGoldReward = (int) enemy.get("enemy_gold_reward");

        // プレイヤーの経験値と所持金を取得
        String selectPlayerInfoSql = "SELECT character_Experience, character_gold FROM player_characters WHERE player_id = ?";
        Map<String, Object> playerInfo = jdbcTemplate.queryForMap(selectPlayerInfoSql, playerId);
        int currentExp = (int) playerInfo.get("character_Experience");
        int currentGold = (int) playerInfo.get("character_gold");

        // プレイヤーの経験値と所持金を更新
        String updatePlayerInfoSql = "UPDATE player_characters SET character_Experience = ?, character_gold = ? WHERE player_id = ?";
        jdbcTemplate.update(updatePlayerInfoSql, currentExp + enemyExpReward, currentGold + enemyGoldReward, playerId);

        // 以下、アイテムのドロップなどの処理も含めて実装
        // ...
    }

 // レベルアップ処理
    public void levelUp(int playerId) {
        // プレイヤーの現在の経験値とレベルを取得
        String selectPlayerInfoSql = "SELECT character_Experience, character_Level FROM player_characters WHERE player_id = ?";
        Map<String, Object> playerInfo = jdbcTemplate.queryForMap(selectPlayerInfoSql, playerId);
        int currentExp = (int) playerInfo.get("character_Experience");
        int currentLevel = (int) playerInfo.get("character_Level");

        // レベルアップに必要な経験値を取得
        String selectRequiredExpSql = "SELECT RequiredExperience FROM levelup WHERE Level = ?";
        int requiredExpForNextLevel = jdbcTemplate.queryForObject(selectRequiredExpSql, Integer.class, currentLevel + 1);

        // レベルアップ条件を満たしているかチェック
        if (currentExp >= requiredExpForNextLevel) {
            // レベルアップ処理
            int newLevel = currentLevel + 1;

            // プレイヤーのレベルを更新
            String updatePlayerLevelSql = "UPDATE player_characters SET character_Level = ? WHERE player_id = ?";
            jdbcTemplate.update(updatePlayerLevelSql, newLevel, playerId);

            // レベルアップメッセージなど、必要な処理を追加
            System.out.println("プレイヤーがレベルアップしました！ 新しいレベル: " + newLevel);
        	}
    	}
    }
