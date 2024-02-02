package com.example.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.AttackService;
import com.example.demo.service.MagicService;

@Controller
public class BattleController {
    private final BattleSQLController battleSQLController;
    private final AttackService attackService;
    private final MagicService magicService;

    @Autowired
    public BattleController(BattleSQLController battleSQLController, AttackService attackService, MagicService magicService) {
        this.battleSQLController = battleSQLController;
        this.attackService = attackService;
        this.magicService = magicService;
    }

    // 戦闘開始準備
    @GetMapping("/battleStart")
    public String battleStart(HttpSession session) {
        session.setAttribute("flag", "開始");
        System.out.println("通ったよ～ん");
        return "redirect:/battle";
    

    
    }
    // バトル中処理
    @GetMapping("/battle")
    public <magicService> String startBattle(Model model, HttpSession session,
                              @RequestParam(name = "playerAction", required = false) String playerAction,
                              @RequestParam(name = "selectedSpellName", required = false) String selectedSpellName) {

        // flagの値が開始の場合、モンスターを取得してバトル開始して、flagの値をバトル中に変更する
        // flagの値がバトル中の場合、バトル処理を行う。敵を倒した場合、flagの値を終了に変更する。
        // flagの値が終了の場合、バトル終了処理（経験値やゴールドの取得、合算を行いdbを更新）を行う。その後flagの値をfieldに変更する
        // flagの値がfieldの場合、フィールド画面に遷移する
        Integer playerId = (Integer) session.getAttribute("playerId");
        System.out.println("flag内" + session.getAttribute("flag"));
        if (playerId == null) {
            return "redirect:/login";
        }
        //もしflagの値がfieldの時、フィールド画面に遷移する
        if
        (session.getAttribute("flag").equals("field")) {
        	// セッションから敵の情報を削除
            session.removeAttribute("currentEnemy");
        	return "redirect:/field";
        	
        }
        //もししflagの値が終了の場合、バトル終了処理を行う
        if (session.getAttribute("flag").equals("終了")) {
        	Map<String, Integer> currentEnemy =  (Map<String, Integer>) session.getAttribute("currentEnemy");
        	//processEnemyDefeatメソッドを呼び出す引数はちゃんと合わせる
         battleSQLController.processEnemyDefeat(playerId,currentEnemy, session);
       
        	
            session.setAttribute("flag", "field");
         
        }

        // もしモンスターがまだセッションにない場合、新しいモンスターを取得してセッションに格納
        if (session.getAttribute("flag").equals("開始")) {
            battleSQLController.setCurrentEnemy(session);

        }
        
        Map<String, Integer> currentEnemy = (Map<String, Integer>) session.getAttribute("currentEnemy");

        // セッションからプレイヤー情報を取得
        String playerName = (String) session.getAttribute("character_Name");
        Integer playerLevel = (Integer) session.getAttribute("character_Level");
        Integer playerHP = (Integer) session.getAttribute("character_HP");
        Integer playerMP = (Integer) session.getAttribute("character_MP");

        // プレイヤー情報をテンプレートに追加
        model.addAttribute("playerName", playerName);
        model.addAttribute("playerLevel", playerLevel);
        model.addAttribute("playerHP", playerHP);
        model.addAttribute("playerMP", playerMP);
        model.addAttribute("enemies", session.getAttribute("monsterHP"));
        model.addAttribute("enemy", currentEnemy);

        if (playerAction != null) {
            switch (playerAction) {
                case "attack":
                    String attackServiceModel = attackService.performAttack(playerId, model, session);
                    model.addAttribute("attackServiceModel", attackServiceModel);
                    break;
                case "magic":
                	//魔法処理を呼び出す
                	//
                	magicService.performMagic(playerId, selectedSpellName, model, session);
                	//
               	    String magicServiceModel = magicService.performMagic(playerId, selectedSpellName, model, session);
               	    // モデルに魔法処理の結果を追加
               	    
                    model.addAttribute("magicServiceModel", magicServiceModel);
                    break;
                // 他のアクションに対する処理も追加
                    //逃げる処理；フィールド画面に遷移
                    case "escape":
                    	session.setAttribute("flag", "field");
                    	
                default:
                    break;
            }

        }

        // プレイヤーと敵のHPの変化を取得
        Integer playerHPChange = (Integer) session.getAttribute("playerHPChange");
        Integer enemyHPChange = (Integer) session.getAttribute("enemyHPChange");

        // テンプレートにHP変化を追加
        model.addAttribute("playerHPChange", playerHPChange != null ? playerHPChange : 0);
        model.addAttribute("enemyHPChange", enemyHPChange != null ? enemyHPChange : 0);

        // バトルメッセージを取得
        String battleResultMessage = (String) model.getAttribute("battleResultMessage");
        model.addAttribute("battleResultMessage", battleResultMessage);
        if (!(session.getAttribute("flag").equals("終了"))) {
            session.setAttribute("flag", "バトル中");
        }
        return "battle";
    
    }
}