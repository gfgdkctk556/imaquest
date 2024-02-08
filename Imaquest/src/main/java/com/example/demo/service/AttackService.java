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
public class AttackService {

    private static final String SELECT_PLAYER_INFO_QUERY = "SELECT * FROM player_characters WHERE player_id = ?";
    private static final String SELECT_ENEMIES_QUERY = "SELECT * FROM enemy_info";

    private final JdbcTemplate jdbcTemplate;
    private final BattleSQLController battleSQLController;

    @Autowired
    public AttackService(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
        this.jdbcTemplate = jdbcTemplate;
        this.battleSQLController = battleSQLController;
    }
//攻撃処理
    // プレイヤーが敵に攻撃する処理
    public String performAttack(int playerId, Model model, HttpSession session) {
    	// プレイヤーの情報を取得
        Map<String, Object> playerInfo = getPlayerInfo(playerId);
        
        // 敵の情報を取得
        List<Map<String, Object>> enemies = getEnemies();
        // セッションから敵の情報を取得
        Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
        //プレイヤーの攻撃力と敵の防御力を取得
        int playerAttack = (int) playerInfo.get("character_Attack");
        int enemyDefense = (int) enemies.get(0).get("enemy_defense");
        // プレイヤーの攻撃を計算
        int playerDamage = calculateDamage(playerAttack, enemyDefense);
        // バトル結果メッセージ
        // バトル結果メッセージを格納する変数を宣言
        //初期値はnull、バトル結果メッセージが設定されなかった場合はnullのまま
        String battleResultMessage = null;

        // セッションからプレイヤーおよびモンスターのHPを取得
        Integer playerHP = (Integer) session.getAttribute("character_HP");
        if (playerHP == null) {
            // "playerHP"がnullの場合の処理
            // エラーメッセージを設定したり、デフォルトの値を使ったりする
            model.addAttribute("errorMessage", "プレイヤーのHP情報が取得できませんでした。");
            return "error"; // エラーページにリダイレクトなど
            // 以降の処理を続ける
        }

        // 新しい変数にモンスターのHPを格納
        int monsterHP = (int) currentEnemy.get("enemy_hp");

        // モンスターのHPを減算
        monsterHP -= playerDamage;
//モンスターのHPが0以下の場合の処理

        if (monsterHP <= 0) {
        	            // モンスターのHPが0以下の場合の処理
        	
        	// 新しい変数にモンスターのHPを格納
            currentEnemy.put("enemy_hp", 0);
        	
        	//敵が倒れたことを表示する
        	battleResultMessage = "プレイヤーが"+playerDamage+"を与えて倒しました！";
        	//バトルリザルトメッセージをBattleControllerに送る
        	model.addAttribute("battleResultMessage", battleResultMessage);
        	//flagを勝ちに変更
        	session.setAttribute("flag","勝ち");
        return "win";
        	            
            // モンスターのHPが0以下でない場合の処理
        	} else {
        	            
        		String monsterName = (String) currentEnemy.get("enemy_name");
        		
        		battleResultMessage = "プレイヤーのターン：プレイヤーが" + monsterName + "に" + playerDamage + "ダメージを与えました！";
        		//BattleControllerに送るバトルリザルトメッセージ
        		
        		// バトル結果メッセージをモデルに追加
        		model.addAttribute("battleResultMessage", battleResultMessage);
        		
        		System.out.println("monsterHP: " + monsterHP);
        		
        		
        		
        // 新しい変数にモンスターのHPを格納
        currentEnemy.put("enemy_hp", monsterHP);
        
        //セッションにバトル中を入れる
        session.setAttribute("flag", "バトル中");
     
        	}
        return "battle";
    }

    // 敵の攻撃処理
    public void performEnemyAttack(int playerId, Model model, HttpSession session) {
        // プレイヤーの情報を取得
        Map<String, Object> playerInfo = getPlayerInfo(playerId);

        // 敵の情報を取得
        List<Map<String, Object>> enemies = getEnemies();
        // セッションから敵の情報を取得
        Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");

        // プレイヤーの防御力と敵の攻撃力を取得
        int playerDefense = (int) playerInfo.get("character_Defense");
        int enemyAttack = (int) enemies.get(0).get("enemy_attack");

        // ダメージを計算
        int enemyDamage = calculateDamage(enemyAttack, playerDefense);

        // セッションからプレイヤーのHPを取得
        Integer playerHP = (Integer) session.getAttribute("character_HP");
        if (playerHP == null) {
            // エラーメッセージを設定したり、デフォルトの値を使ったりする
            model.addAttribute("errorMessage", "プレイヤーのHP情報が取得できませんでした。");
        }

        // プレイヤーのHPを減算
        playerHP -= enemyDamage;

        if (playerHP <= 0) {
            // プレイヤーが負けた場合の処理を実行
            loseMethod(session);
        }

        // プレイヤーのHPが0以下でない場合の処理
        String playerName = (String) playerInfo.get("character_Name");
        String enemyAttackMessage = "敵のターン：敵が" + playerName + "に" + enemyDamage + "ダメージを与えました！";
        //敵の攻撃メッセージをBattleControllerに送る、変数名はenemyAttackMessage
        model.addAttribute("enemyAttackMessage", enemyAttackMessage);
       

        // プレイヤーのHPをセッションに保存
        session.setAttribute("character_HP", playerHP);
    }
    private Map<String, Object> getPlayerInfo(int playerId) {
        return jdbcTemplate.queryForMap(SELECT_PLAYER_INFO_QUERY, playerId);
    }

    private List<Map<String, Object>> getEnemies() {
        return jdbcTemplate.queryForList(SELECT_ENEMIES_QUERY);
    }

    private int calculateDamage(int playerAttack, int enemyDefense) {
        Random random = new Random();
        int baseDamage = playerAttack - enemyDefense + random.nextInt(5);
        return Math.max(baseDamage, 1);
    }
 // バトルコントローラー内に負けのメソッドを追加
    public void loseMethod(HttpSession session) {
    	
    	//敵のセッションを削除
    	session.removeAttribute("currentEnemy");
        // プレイヤーの位置を初期位置にリセット（0, 0）
        session.setAttribute("resetPositionFlag", true);

        // プレイヤーのHPを全回復
        Integer maxHP = (Integer) session.getAttribute("maxHP");
        session.setAttribute("character_HP", maxHP);

        // バトルフラグを終了に変更
        session.setAttribute("flag", "負け");
    }


}
