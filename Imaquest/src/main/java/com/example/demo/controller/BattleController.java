package com.example.demo.controller;

import java.util.HashMap;
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

	//戦闘開始準備
	@GetMapping("/battleStart")
	public String battleStart(HttpSession session){
		session.setAttribute("flag", "開始");
		System.out.println("通ったよ～ん");
		return "redirect:/battle";
	}

	//バトル中処理
	@GetMapping("/battle")
	public String startBattle(Model model, HttpSession session,
			@RequestParam(name = "playerAction", required = false) String playerAction,
			@RequestParam(name = "selectedSpellName", required = false) String selectedSpellName) {

		Integer playerId = (Integer) session.getAttribute("playerId");
		System.out.println("flag内" + session.getAttribute("flag"));
		if (playerId == null) {
			return "redirect:/login";
		}
		if (session.getAttribute("flag").equals("終了")) {
			model.addAttribute("battleResultMessage", "死体打ちすな");
			Map<String, Object> currentEnemy = new HashMap<>();
			currentEnemy.put("enemy_name", "");
			currentEnemy.put("enemy_level", "");
			currentEnemy.put("enemy_hp", "");
            model.addAttribute("enemy", currentEnemy);
            //バトル終了後の処理記述
			if (session.getAttribute("flag").equals("field")) {
				return "field";
			}
				processBattleEnd(playerId, model);
                return "battle";			
		}

			// もしモンスターがまだセッションにない場合、新しいモンスターを取得してセッションに格納
		if(session.getAttribute("flag").equals("開始")) {
			battleSQLController.setCurrentEnemy(session);
			
		}
			Map<String, Object> currentEnemy = (Map<String, Object>) session.getAttribute("currentEnemy");

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
				String magicServiceModel = magicService.performMagic(playerId, selectedSpellName, model, session);
				model.addAttribute("magicServiceModel", magicServiceModel);
				break;
				// 他のアクションに対する処理も追加
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
		if(!(session.getAttribute("flag").equals("終了"))) {
		session.setAttribute("flag", "バトル中");}
		return "battle";
	}

	private void processBattleEnd(Integer playerId, Model model) {
		// バトル終了後の処理をここに追加
		// 例: 勝利画面や敗北画面への遷移
	}
}
