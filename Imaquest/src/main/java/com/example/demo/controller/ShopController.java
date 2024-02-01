//package com.example.demo.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//// BattleSQLControllerから色々なメソッドを呼び出す
//// バトル画面のコントローラー
////表示用htmlはbattle.html
////画面の動きはbattle.js
////dbの処理はBattleSQLController.javaが主に行う
//
//
//@Controller
//public class BpattleController {
//	//	ランダムな敵を選択するメソッド
//	private Map<String, Object> getRandomEnemy() {
//	    List<Map<String, Object>> enemies = getEnemies();
//	    Random random = new Random();
//	    return enemies.get(random.nextInt(enemies.size()));
//	}
//	private void setCurrentEnemy(HttpSession session) {
//	    List<Map<String, Object>> enemies = getEnemies();
//	    Random random = new Random();
//	    Map<String, Object> currentEnemy = enemies.get(random.nextInt(enemies.size()));
//	    session.setAttribute("currentEnemy", currentEnemy);
//	}
//
//
//
//    private static final String SELECT_PLAYER_INFO_QUERY = "SELECT * FROM player_characters WHERE player_id = ?";
//    private static final String SELECT_ENEMIES_QUERY = "SELECT * FROM enemy_info";
//    private static final String SELECT_PLAYER_SPELLS_QUERY = "SELECT s.* FROM player_skill ps JOIN magic_skills s ON ps.magic_skill_id = s.magic_skill_id WHERE ps.player_id = ?";
//    private static final String SELECT_SPELL_DETAILS_QUERY = "SELECT * FROM magic_skills WHERE magic_skill_name = ?";
//
//    private final JdbcTemplate jdbcTemplate;
//    private final BattleSQLController battleSQLController;
//
//    public BattleController(JdbcTemplate jdbcTemplate, BattleSQLController battleSQLController) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.battleSQLController = battleSQLController;
//    }
////Battleの画面に出てくるメッセージが表示される内容
//    //battle.htmlのbattleMessageに表示される
//    //ダメージなどの結果をバトルコメントに入れてhtmlに表示する
//    //htmlで各ボタンが押されたときにbattleMessageを更新する
//    
//    
//    @GetMapping("/battle-comment")
//    public ResponseEntity<Map<String, Object>> getBattleComment(HttpSession session, Model model) {
//    	
//        // プレイヤー情報保存
//    	Integer playerId = (Integer) session.getAttribute("playerId");
//
//        if (playerId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Map<String, Object> playerInfo = getPlayerInfo(playerId);
//        
//        // セッションからモンスター情報を取得
//        Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
//        if (currentEnemy == null) {
//            // モンスター情報がセッションに存在しない場合は新しく取得して保存
//            setCurrentEnemy(session);
//            currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
//        }
//
//        // モンスター名を取得時にhtmlに表示、今後バトルコメントに処理が追加されたらここに追加する
//        String monsterName = (String) currentEnemy.get("enemy_name");
//
//        String battleMessage = "バトル開始！" + monsterName + "が現れた！";
//        model.addAttribute("battleMessage", battleMessage);
//        model.addAttribute("monsterName", monsterName);
//
//        Map<String, Object> battleData = new HashMap<>();
//        battleData.put("comment", battleMessage);
//
//        return ResponseEntity.ok(battleData);
//    }
//
//
//    @GetMapping("/battle")
//    public String startBattle(Model model, HttpServletRequest request, HttpSession session,
//            @RequestParam(name = "startBattle", required = false) String startBattle,
//            @RequestParam(name = "playerAction", required = false) String playerAction,
//            @RequestParam(name = "selectedSpellName", required = false) String selectedSpellName) {
//        Integer playerId = (Integer) session.getAttribute("playerId");
//
//        if (playerId == null) {
//            return "redirect:/login";
//        }
//        
//        Map<String, Object> playerInfo = getPlayerInfo(playerId);
//        List<Map<String, Object>> enemies = getEnemies();
//        List<Map<String, Object>> spells = getPlayerSpells(playerId);
//
//        // セッションからモンスター情報を取得
//        Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
//        if (currentEnemy == null) {
//            // モンスター情報がセッションに存在しない場合は新しく取得して保存
//            setCurrentEnemy(session);
//            currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");
//        }
//
//        model.addAttribute("playerInfo", playerInfo);
//        model.addAttribute("enemies", enemies);
//        model.addAttribute("spells", spells);
//        model.addAttribute("enemy", currentEnemy); // セッションから取得した敵情報を追加
//
//        if (startBattle != null) {
//            String monsterName = (String) currentEnemy.get("enemy_name");
//            model.addAttribute("battleMessage", monsterName + "が現れた！");
//            model.addAttribute("battleStarted", true);
//        }
////プレイヤーの行動を選択する
//        //選択し終えたら処理をする
//        
//        if (playerAction != null) {
//            switch (playerAction) {
//            //攻撃だった場合の処理、処理をした後にbattleSQLControllerから敵の体力を減らす処理を呼び出す
//            //
//                case "attack":
//                    performPlayerAttack(playerId, model);
//                    
//                    
//                    break;
//                    //魔法だった場合
//                case "magic":
//                    performPlayerMagic(playerId, selectedSpellName, model);
//                    break;
//                    	
//                case "item":
//                	//アイテムだった場合battleSQLControllerからアイテムを取得するコードを書く
//                	//battleSQLController.processItemUse(playerId, itemId);
//                	//model.addAttribute("battleResultMessage", "アイテムを使用しました！");
//                	
//                	
//                	
//                	
//                    break;
//                    
//                default:
//                	
//                    break;
//                    
//            }
//        }
//
//        battleSQLController.processBattleRewards(playerId, 1, 1, 1);
//
//        return "battle";
//    }
//    
//
//    private Map<String, Object> getPlayerInfo(int playerId) {
//        return jdbcTemplate.queryForMap(SELECT_PLAYER_INFO_QUERY, playerId);
//    }
//
//    private List<Map<String, Object>> getEnemies() {
//        return jdbcTemplate.queryForList(SELECT_ENEMIES_QUERY);
//    }
//
//    private void performPlayerAttack(int playerId, Model model) {
//        Map<String, Object> playerInfo = getPlayerInfo(playerId);
//        List<Map<String, Object>> enemies = getEnemies();
//
//        int playerAttack = (int) playerInfo.get("character_Attack");
//        int enemyDefense = (int) enemies.get(0).get("enemy_defense");
//        int playerDamage = calculateDamage(playerAttack, enemyDefense);
//
//        Map<String, Object> enemy = enemies.get(0);
//        int enemyHP = (int) enemy.get("enemy_hp");
//        enemyHP -= playerDamage;
//
//        if (enemyHP <= 0) {
//            battleSQLController.processEnemyDefeat(playerId, enemy);
//            // 敵を倒した場合のメッセージ
//            String monsterName = (String) enemy.get("enemy_name");
//            model.addAttribute("battleResultMessage", "モンスター名が現れた！プレイヤーが" + monsterName + "に" + playerDamage + "ダメージを与えて、モンスターを倒しました！");
//        } else {
//            // 敵がまだ生存している場合のメッセージバトルコメントに表示
//        	            String monsterName = (String) enemy.get("enemy_name");
//        	            model.addAttribute("battleResultMessage", "プレイヤーが" + monsterName + "に" + playerDamage + "ダメージを与えました！");
//            
//        }
//    }
//    
//    
//
//    
// // ランダムな敵を選択するメソッド
//    private Map<String, Object> getRandomEnemy(List<Map<String, Object>> enemies) {
//        Random random = new Random();
//        int randomIndex = random.nextInt(enemies.size());
//        return enemies.get(randomIndex);
//    }
//
//// ダメージを計算するメソッド
//    private int calculateDamage(int playerAttack, int enemyDefense) {
//        Random random = new Random();
//        int baseDamage = playerAttack - enemyDefense + random.nextInt(5);
//        return Math.max(baseDamage, 1);
//    }
////プレイヤーの魔法を選択する
//    private List<Map<String, Object>> getPlayerSpells(int playerId) {
//        return jdbcTemplate.queryForList(SELECT_PLAYER_SPELLS_QUERY, playerId);
//    }
////	魔法の詳細を取得する
//    private Map<String, Object> getSpellDetails(String spellName) {
//        return jdbcTemplate.queryForMap(SELECT_SPELL_DETAILS_QUERY, spellName);
//    }
////	魔法を使う処理
//    @GetMapping("/battle-data")
//    public ResponseEntity<Map<String, Object>> getBattleData(HttpSession session) {
//        Integer playerId = (Integer) session.getAttribute("playerId");
////	プレイヤーがログインしていない場合はエラーレスポンスを返す
//        if (playerId == null) {
//            // プレイヤーがログインしていない場合はエラーレスポンスを返す
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        //	プレイヤーの情報を取得
//        Map<String, Object> playerInfo = getPlayerInfo(playerId);
//        List<Map<String, Object>> enemies = getEnemies();
//        Map<String, Object> randomEnemy = getRandomEnemy(enemies);
//
//        Map<String, Object> battleData = new HashMap<>();
//        battleData.put("playerInfo", playerInfo);
//        battleData.put("enemies", enemies);
//        battleData.put("spells", getPlayerSpells(playerId));
//        battleData.put("enemy", randomEnemy);
//        //    プレイヤーの情報をレスポンスとして返す
//        return ResponseEntity.ok(battleData);
//    }
//    
//    // モンスター情報を取得するメソッド
//    @GetMapping("/enemy")
//    public ResponseEntity<Map<String, Object>> getEnemyInfo(HttpSession session) {
//        // セッションに保存されたモンスター情報がない場合は新しく取得して保存
//        if (session.getAttribute("currentEnemy") == null) {
//            setCurrentEnemy(session);
//        }
//
//        // セッションからモンスター情報を取得
//        Map<String, Object> enemyInfo = (Map<String, Object>) session.getAttribute("currentEnemy");
//
//        // 取得した敵情報をレスポンスとして返す
//        return ResponseEntity.ok(enemyInfo);
//    }
//
//
//
////魔法を使う処理
//    private void performPlayerMagic(int playerId, String spellName, Model model) {
//        // プレイヤー情報とモンスター情報を取得
//        Map<String, Object> playerInfo = getPlayerInfo(playerId);
//        List<Map<String, Object>> enemies = getEnemies();
//        Map<String, Object> enemy = enemies.get(0);
//
//        // 選択された魔法の詳細情報を取得
//        Map<String, Object> spellDetails = getSpellDetails(spellName);
//
//        // 魔法攻撃力を取得
//        int magicAttackPower = (int) spellDetails.get("magic_attack_power");
//
//        // モンスターの体力を取得
//        int enemyHP = (int) enemy.get("enemy_hp");
//
//        // プレイヤーがモンスターに与えるダメージを計算
//        int playerDamage = calculateMagicDamage(magicAttackPower);
//
//        // モンスターの体力を減少させる
//        enemyHP -= playerDamage;
//
//        if (enemyHP <= 0) {
//            // モンスターが倒れた場合の処理
//            battleSQLController.processEnemyDefeat(playerId, enemy);
//        }
//
//        // バトル結果メッセージを表示
//        model.addAttribute("battleResultMessage", "プレイヤーが" + spellName + "を使用し、"+enemy+"に" + playerDamage + "ダメージを与えて倒しました！");
//    }
//
//    private int calculateMagicDamage(int magicAttackPower) {
//        // 魔法ダメージの計算
//        Random random = new Random();
//        int baseDamage = magicAttackPower + random.nextInt(5);
//        return Math.max(baseDamage, 1); // 最低ダメージを1に設定
//    }
//
//}
