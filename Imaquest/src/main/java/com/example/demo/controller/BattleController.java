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
        return "redirect:/battle";
 
    }
    
    @GetMapping("/handleLoseFlag")
    public String handleLoseFlag(HttpSession session) {
        // flagに負けが入っている場合の処理
        if ("負け".equals(session.getAttribute("flag"))) {
            // フラグが負けの場合の処理を実行
            // JavaScriptの関数を呼び出してプレイヤーの位置を初期化
            return "<script>resetPlayerPosition();</script>";
            
        } else if ("勝ち".equals(session.getAttribute("flag"))) {
            Integer playerId = (Integer) session.getAttribute("playerId");
            Map<String, Integer> currentEnemy = (Map<String, Integer>) session.getAttribute("currentEnemy");
            // バトル終了処理を行う
            battleSQLController.processEnemyDefeat(playerId, currentEnemy, session);
            // 敵のsessionを削除
            session.removeAttribute("currentEnemy");
            System.out.println("敵のセッションを削除しました！");
            return "redirect:/field";
        }
        return "";
    }

    @GetMapping("/battle")
    public String startBattle(Model model, HttpSession session,
       @RequestParam(name = "playerAction", required = false) String playerAction,
       @RequestParam(name = "selectedSpellName", required = false) String selectedSpellName) {

        Integer playerId = (Integer) session.getAttribute("playerId");
        if (playerId == null) {
            return "redirect:/login";
        }

        // もしモンスターがまだセッションにない場合、新しいモンスターを取得してセッションに格納
        if (session.getAttribute("flag").equals("開始")) {
            battleSQLController.setCurrentEnemy(session);
        }
        
		if (session.getAttribute("flag").equals("勝ち")||session.getAttribute("flag").equals("負け")) {
			return "redirect:/handleLoseFlag";
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

        // もし敵がまだ生きている場合、バトル中フラグをセットしてバトルを続行
        if (!currentEnemy.isEmpty() && currentEnemy.get("enemy_hp") > 0) {
            session.setAttribute("flag", "バトル中");
        }

        // プレイヤーアクションがある場合、バトル処理を行う
        if (playerAction != null) {
            switch (playerAction) {
                case "attack":
                    String attackServiceModel = attackService.performAttack(playerId, model, session);
                    model.addAttribute("attackServiceModel", attackServiceModel);
					if (!(attackServiceModel.equals("win"))) {
	                    // 敵の攻撃処理を呼び出す
	                    attackService.performEnemyAttack(playerId, model, session);
					}
                    break;
                case "magic":
                    String magicServiceModel = magicService.performMagic(playerId, selectedSpellName, model, session);
                    model.addAttribute("magicServiceModel", magicServiceModel);
                    break;
                case "escape":
                    session.removeAttribute("currentEnemy");
                    return "redirect:/field";
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

        return "battle";
    }

}