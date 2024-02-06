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
   
    
//ゴールドのドロップ処理
	//セッション内のモンスターの情報はMap<String, Integer>型で保存されている
	public void dropGold(int playerId, Map<String, Integer> currentEnemy) {
		int enemyGoldReward = currentEnemy.get("enemy_gold_reward");

		// プレイヤーの現在のゴールドを取得
		String selectPlayerGoldSql = "SELECT character_gold FROM player_characters WHERE player_id = ?";
		int currentGold = jdbcTemplate.queryForObject(selectPlayerGoldSql, Integer.class, playerId);

		// 敵とプレイヤーのゴールドを合算してDBを更新
		String updatePlayerGoldSql = "UPDATE player_characters SET character_gold = ? WHERE player_id = ?";
		jdbcTemplate.update(updatePlayerGoldSql, currentGold + enemyGoldReward, playerId);
	}
	
	
	// ドロップ率を取得
	//セッション内のモンスターの情報はMap<String, Integer>型で保存されている
	public void dropItem(int playerId, Map<String, Integer> currentEnemy) {
		int enemyId = currentEnemy.get("enemy_id");
		String selectDropRateSql = "SELECT drop_rate FROM enemy_info WHERE enemy_id = ?";
		int dropRate = jdbcTemplate.queryForObject(selectDropRateSql, Integer.class, enemyId);

		// ドロップ率に基づいてアイテムがドロップするか判定
		if (dropRate >= new Random().nextInt(100)) {
			// アイテムIDを取得
			String selectItemRewardIdSql = "SELECT enemy_item_reward_id FROM enemy_info WHERE enemy_id = ?";
			int itemRewardId = jdbcTemplate.queryForObject(selectItemRewardIdSql, Integer.class, enemyId);

			
			//アイテムがドロップする、場合、プレイヤーアイテムテーブルにアイテムを追加
			addItemToPlayerInventory(playerId, itemRewardId);
			
		}
		//アイテムがドロップしない場合

	}
	
	//プレイヤーアイテムテーブルにアイテムが存在するか確認
private void addItemToPlayerInventory(int playerId, int itemRewardId) {
		// TODO 自動生成されたメソッド・スタブ
	
	
	//存在しない場合、アイテムを追加
	    String checkItemExistenceSql = "SELECT COUNT(*) FROM player_items WHERE player_id = ? AND item_id = ?";
		if (jdbcTemplate.queryForObject(checkItemExistenceSql, Integer.class, playerId, itemRewardId) == 0) {
			String insertSql = "INSERT INTO player_items (player_id, item_id, item_quantity) VALUES (?, ?, 1)";
			jdbcTemplate.update(insertSql, playerId, itemRewardId);
			//存在する場合、アイテムの数量を増やす
		} else {
			String updateSql = "UPDATE player_items SET item_quantity = item_quantity + 1 WHERE player_id = ? AND item_id = ?";
			jdbcTemplate.update(updateSql, playerId, itemRewardId);
		}
		
	}

//アイテムの使用
    
    public void useItem(int playerId, int itemId) {
    }
        
        //プレイヤーの覚えている魔法を取得する
        //まずセッションに入っているプレイヤーidを取得
        //その後、そのプレイヤーidを使って、プレイヤースキルテーブルから魔法スキルを取得
        //プレイヤースキルテーブルから魔法スキルidを取得するためのSQL文を用意
       //魔法スキルidを使って、魔法スキルテーブルから魔法スキルを取得するためのSQL文を用意
        //SQL文を使って、魔法スキルを取得
        //取得した魔法スキルをリストに追加
        //リストを返す
    //これをコントローラーで呼び出し、htmlへ表示をする
    
		public List<Map<String, Object>> getMagicSkills(int playerId) {
			String selectPlayerSkillsSql = "SELECT magic_skill_id FROM player_skill WHERE player_id = ?";
			List<Integer> magicSkillIds = jdbcTemplate.queryForList(selectPlayerSkillsSql, Integer.class, playerId);
			return jdbcTemplate.queryForList("SELECT * FROM magic_skills WHERE skill_id IN (?)", magicSkillIds);
		}
		
		
    
       
        private void learnMagicSkill(int playerId, int magicSkillId) {
            String checkMagicSkillExistenceSql = "SELECT COUNT(*) FROM player_skill WHERE player_id = ? AND magic_skill_id = ?";
            if (jdbcTemplate.queryForObject(checkMagicSkillExistenceSql, Integer.class, playerId, magicSkillId) == 0) {
                String insertSql = "INSERT INTO player_skill (player_id, magic_skill_id) VALUES (?, ?)";
                jdbcTemplate.update(insertSql, playerId, magicSkillId);
            }
        }
        
        
        
        
    
//    経験値の獲得
    private void gainExperience(int playerId, int enemyId) {
    	//敵の経験値報酬を取得
        String selectEnemyExpSql = "SELECT enemy_exp_reward FROM enemy_info WHERE enemy_id = ?";
        int enemyExpReward = jdbcTemplate.queryForObject(selectEnemyExpSql, Integer.class, enemyId);
//プレイヤーの現在の経験値を取得
        String selectPlayerExpSql = "SELECT character_Experience FROM player_characters WHERE player_id = ?";
        int currentExp = jdbcTemplate.queryForObject(selectPlayerExpSql, Integer.class, playerId);
//敵とプレイヤーの経験値を合算してdbを更新
        String updateExpSql = "UPDATE player_characters SET character_Experience = ? WHERE player_id = ?";
        jdbcTemplate.update(updateExpSql, currentExp + enemyExpReward, playerId);
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
            
            //レベルアップしたとき能力値を上昇させる
            //元々の能力値をすべて取得し、元々の能力値を+10して更新
            
            String selectPlayerStatusSql = "SELECT character_HP, character_MP, character_Attack, character_Defense FROM player_characters WHERE player_id = ?";
            Map<String, Object> playerStatus = jdbcTemplate.queryForMap(selectPlayerStatusSql, playerId);
            	int currentHP = (int) playerStatus.get("character_HP");
            	int currentMP = (int) playerStatus.get("character_MP");
            	int currentAttack = (int) playerStatus.get("character_Attack");
            	int currentDefense = (int) playerStatus.get("character_Defense");
            	String updatePlayerStatusSql = "UPDATE player_characters SET character_HP = ?, character_MP = ?, character_Attack = ?, character_Defense = ? WHERE player_id = ?";
            	jdbcTemplate.update(updatePlayerStatusSql, currentHP + 10, currentMP + 10, currentAttack + 10, currentDefense + 10, playerId);
            	
            
            

            // プレイヤーのレベルを更新
            String updatePlayerLevelSql = "UPDATE player_characters SET character_Level = ? WHERE player_id = ?";
            jdbcTemplate.update(updatePlayerLevelSql, newLevel, playerId);

            // レベルアップメッセージなど、必要な処理を追加
            System.out.println("プレイヤーがレベルアップしました！ 新しいレベル: " + newLevel);
        	}
        //レベルアップしないとき
        System.out.println("currentExp: " + currentExp+"レベルアップはしません");
    	}
//////////////////////////////敵の撃破時の処理をまとめたもの
    //これを戦闘終了時に読み込めばすべての処理が行われる
    public void processEnemyDefeat(int playerId, Map<String, Integer> currentEnemy,HttpSession session) {
        // モンスターの撃破処理
    	int enemyExpReward = (int) currentEnemy.get("enemy_exp_reward");
        int enemyGoldReward = (int) currentEnemy.get("enemy_gold_reward");

        // プレイヤーの経験値と所持金を取得
        String selectPlayerInfoSql = "SELECT character_Experience, character_gold FROM player_characters WHERE player_id = ?";
        Map<String, Object> playerInfo = jdbcTemplate.queryForMap(selectPlayerInfoSql, playerId);
        int currentExp = (int) playerInfo.get("character_Experience");
        int currentGold = (int) playerInfo.get("character_gold");
       
        
        // プレイヤーの経験値と所持金を更新
        String updatePlayerInfoSql = "UPDATE player_characters SET character_Experience = ?, character_gold = ? WHERE player_id = ?";
        jdbcTemplate.update(updatePlayerInfoSql, currentExp + enemyExpReward, currentGold + enemyGoldReward, playerId);
//コンソールに表示
        System.out.println("プレイヤーが" + currentEnemy.get("enemy_name") + "を倒しました！ 経験値: " + enemyExpReward + " 所持金: " + enemyGoldReward);
        //所持金が〇〇になりました
        System.out.println("所持金が" + currentGold + "になりました");
        
        
        // アイテムのドロップを呼び出す
        int enemyItemRewardId = (int) currentEnemy.get("enemy_item_reward_id");
        addItemToPlayerInventory(playerId, enemyItemRewardId);

        // レベルアップ処理を行う
        levelUp(playerId);
        
     // バトルメッセージの生成
        String battleMessage = generateBattleMessage(playerId, currentEnemy, session);

        // バトルメッセージをセッションに格納
        session.setAttribute("battleMessage", battleMessage);

        
    }

    // バトルメッセージ生成メソッド
    //項目ごとに違う箱を用意
    //BattleControllerでの戦闘終了時に呼び出され、バトルメッセージを生成する。その後コントローラーでの処理になる	
    public String generateBattleMessage(Integer playerId, Map<String, Integer> currentEnemy, HttpSession session) {
        // バトルメッセージの生成ロジックを実装
        StringBuilder message = new StringBuilder();
        message.append(session.getAttribute("character_Name")).append("は").append(currentEnemy.get("enemy_name")).append("を倒しました！");

        // 経験値の取得
        Integer experiencePoints = (Integer) currentEnemy.get("enemy_exp_reward");
        message.append(" 経験値").append(experiencePoints).append("を手に入れました！");

        // ゴールドの取得
        Integer gold = (Integer) currentEnemy.get("enemy_gold_reward");
        message.append(" ゴールド").append(gold).append("を手に入れました！");

        // レベルアップメッセージの取得
        String levelUpMessage = (String) session.getAttribute("levelUpMessage");
        if (levelUpMessage != null) {
            message.append(" ").append(levelUpMessage);
        }
        //flagの値をfieldに変更
        session.setAttribute("flag", "field");
        return message.toString();
    }
    
        

       
    }

    