//テーブル名itemsこのテーブルがアイテムの情報をすべて持っています
//Columns:
//item_id int AI PK アイテムのIDですこれで所持アイテムテーブルと参照して所持しているか判断しています。
//item_name varchar(255) アイテムの名前が格納されてます
//item_effect varchar(255) アイテムの説明分が入ってます。
//purchase_price int アイテムの購入時に参照する価格です。
//sell_price int アイテム売却時に参照する価格です。
//item_HP_heal int アイテム使用時にHPを回復するときの値が格納されてます
//item_MP_heal int アイテム使用時にMPを回復するときの値が格納されてます
//item_damage int アイテム使用時に敵に与えるダメージの値が格納されてます
//item_status_heal int 状態異常を回復するときに使うものです
//item_type int アイテムのタイプが格納されてます1がHP回復2がMP回復3が敵にダメージを与える99がHPとMPを回復するの4種類のあります。
//item_attack intアイテム使用時に攻撃力を上げる値が格納されてます。
//所持アイテムはこのテーブルで管理していますTable: player_items
//Columns:
//player_id int　player_charactersテーブルのplayer_id と同じ値でログインしているユーザーを判断しています。
//item_id int  itemsテーブルにあるplayer_id と同じ値が格納されます
//item_quantity int各アイテムの所持数を判断しています
//Table: equipment装備の情報をすべて持っています
//Columns:
//equipment_id int AI PK 装備のIDですこれで所持装備テーブルと参照して所持しているか判断しています。
//equipment_name varchar(255) 装備の名前が格納されてます
//equipment_effect varchar(255)　装備の説明分がが入ってます。
//attack_power int 装備の攻撃力が格納されてます
//defense_power int 装備の防御力が格納されてます
//magic_power int 魔法攻撃力
//purchase_price int 装備の購入時に参照する価格です。
//sell_price int 装備の売却時に参照する価格です。
//magicpoint int セッションのMP値に加算する値が格納されてます
//equipment_type int　装備のタイプが格納されてます1が攻撃数値UP2が防御力値がUP3魔法の攻撃力を上げる11が攻撃数値とMPを上げる12が防御力とMPを上げる13が魔法の攻撃力とMPを上げるの6種類のあります。
//所持装備はこのテーブルで管理していますTable: player_equipment
//Columns:
//player_id int　player_charactersテーブルのplayer_id と同じ値でログインしているユーザーを判断しています。
//equipment_id int  equipmentテーブルにあるplayer_id と同じ値が格納されます
package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/menu")
	public String showMenu(Model model, HttpSession session) {
		// ログイン中のユーザIDをセッションから取得
		Integer playerId = (Integer) session.getAttribute("playerId");
		if (playerId == null) {
			return "redirect:/login";
		}
		// プレイヤーの全情報をデータベースから取得
		
		Map<String, Object> playerInfo = jdbcTemplate.queryForMap(
				"SELECT character_Name, character_Level, character_gold, character_HP, character_Attack, character_Defense, character_MP, character_MagicAttack FROM player_characters WHERE player_id = ?",
				playerId);
		
		int maxHP = (int) playerInfo.get("character_HP");
		// セッションからユーザー情報を取得

		String characterName = (String) session.getAttribute("character_Name");
		int characterLevel = (int) session.getAttribute("character_Level");
		int characterGold = (int) session.getAttribute("character_gold");
		int characterHP = (int) session.getAttribute("character_HP");
		int characterAttack = (int) session.getAttribute("character_Attack");
		int characterDefense = (int) session.getAttribute("character_Defense");
		int characterMP = (int) session.getAttribute("character_MP");
		int characterMagicAttack = (int) session.getAttribute("character_MagicAttack");
		int maxMP = (int) session.getAttribute("maxMP");
		// モデルにユーザー情報を渡してビューで表示
		model.addAttribute("characterName", characterName);
		model.addAttribute("characterLevel", characterLevel);
		model.addAttribute("characterGold", characterGold);
		model.addAttribute("characterHP", characterHP);
		model.addAttribute("maxHP", maxHP);
		model.addAttribute("characterAttack", characterAttack);
		model.addAttribute("characterDefense", characterDefense);
		model.addAttribute("characterMP", characterMP);
		model.addAttribute("maxMP", maxMP);
		model.addAttribute("characterMagicAttack", characterMagicAttack);
		
	 // 所持アイテムIDと所持アイテム一覧と個数とタイプを取得
		List<Map<String, Object>> playerItemListWithQuantity = jdbcTemplate.queryForList(
		        "SELECT items.item_id, items.item_name, items.item_effect, items.item_type," +
		                "COALESCE(player_items.item_quantity, 0) as item_quantity " +
		                "FROM items " +
		                "LEFT JOIN player_items ON items.item_id = player_items.item_id " +
		                "AND player_items.player_id = ? " +
		                "WHERE player_items.item_id IS NOT NULL " +
		                "AND (player_items.item_quantity > 0 OR player_items.item_quantity IS NULL) " +
		                "ORDER BY items.item_id",
		        playerId);


		Map<String, Map<String, Object>> playerItemMap = new HashMap<>();
		for (Map<String, Object> item : playerItemListWithQuantity) {
			String itemName = (String) item.get("item_name");
			int itemQuantity = ((Number) item.get("item_quantity")).intValue();
			String itemEffect = (String) item.get("item_effect");
			int itemtype = ((Number) item.get("item_type")).intValue();

			Map<String, Object> itemDetails = new HashMap<>();
			itemDetails.put("itemQuantity", itemQuantity);
			itemDetails.put("itemEffect", itemEffect);
			itemDetails.put("itemType", itemtype);
			
			playerItemMap.put(itemName, itemDetails);
		}

		model.addAttribute("playerItemMap", playerItemMap);

		//所持装備一覧と個数と所持装備名と装備タイプを取得
		
		List<Map<String, Object>> playerEquipmentListWithQuantity = jdbcTemplate.queryForList(
				"SELECT equipment.equipment_id, equipment.equipment_name, equipment.equipment_effect, equipment.equipment_type,"
						+
						"COALESCE(player_equipment.equipment_quantity, 0) as equipment_quantity " +
						"FROM equipment " +
						"LEFT JOIN player_equipment ON equipment.equipment_id = player_equipment.equipment_id " +
						"AND player_equipment.player_id = ? " +
						"WHERE player_equipment.equipment_id IS NOT NULL " +
						"AND (player_equipment.equipment_quantity > 0 OR player_equipment.equipment_quantity IS NULL) "
						+
						"ORDER BY equipment.equipment_id",
				playerId);
		
	    Map<String, Map<String, Object>> playerEquipmentMap = new HashMap<>();
		for (Map<String, Object> equipment : playerEquipmentListWithQuantity) {
			String equipmentName = (String) equipment.get("equipment_name");
			int equipmentQuantity = ((Number) equipment.get("equipment_quantity")).intValue();
			String equipmentEffect = (String) equipment.get("equipment_effect");
			int equipmentType = ((Number) equipment.get("equipment_type")).intValue();

			Map<String, Object> equipmentDetails = new HashMap<>();
			equipmentDetails.put("equipmentQuantity", equipmentQuantity);
			equipmentDetails.put("equipmentEffect", equipmentEffect);
			equipmentDetails.put("equipmentType", equipmentType);

			playerEquipmentMap.put(equipmentName, equipmentDetails);
		}
		model.addAttribute("playerEquipmentMap", playerEquipmentMap);
		return "menu";
	}
	// アイテムを使用する処理
	
		@PostMapping("/useItem")
	 @RequestMapping(value = "/useItem", method = RequestMethod.POST)
	    public Object useItem(@RequestParam String itemName, Model model, HttpSession session) {
	        try {
	            // ログイン中のユーザIDをセッションから取得
	            Integer playerId = (Integer) session.getAttribute("playerId");

	            if (playerId == null) {
	                return "redirect:/login";
	            }

	            // プレイヤーのHPとMPデータベースから取得
	            Map<String, Object> playerDetails = jdbcTemplate.queryForMap(
	                    "SELECT character_HP, character_MP FROM player_characters WHERE player_id = ?",
	                    playerId
	            );

	            //データベースのHPとMPを取得
	            int maxHP = ((Number) playerDetails.get("character_HP")).intValue();
	          //sessionのmaxMPを取得
	            
	            int maxmp = (int) session.getAttribute("maxMP");
	            
	            // アイテムの効果とタイプを取得
	            Map<String, Object> itemDetails = jdbcTemplate.queryForMap(
	                    "SELECT item_id, item_type, item_HP_heal, item_MP_heal FROM items WHERE item_name = ?",
	                    itemName
	            );

	           
	            int itemType = ((Number) itemDetails.get("item_type")).intValue();
	            int itemHPHeal = ((Number) itemDetails.get("item_HP_heal")).intValue();
	            int itemMPHeal = ((Number) itemDetails.get("item_MP_heal")).intValue();
	            int characterHP = (int) session.getAttribute("character_HP");
	            int characterMP = (int) session.getAttribute("character_MP");
	            
	            // アイテムの効果を適用
	            if (itemType == 1) {
	                // タイプが1の場合、セッションのプレイヤーのHPにitem_HP_heal分加算またはHP_MAXより多い場合はHP_MAXで上書き
	
	                if (characterHP + itemHPHeal < maxHP) {
						characterHP += itemHPHeal;
						session.setAttribute("character_HP", characterHP);
						
	                } else if (characterHP + itemHPHeal == maxHP) {
						session.setAttribute("character_HP", maxHP);
					
					}else if (characterHP == maxHP) {
						System.out.println("HPが満タンです");
						return "menu"+1;
					}
	                
	             // アイテムの所持数を減らす
		            jdbcTemplate.update(
		                    "UPDATE player_items SET item_quantity = item_quantity - 1 WHERE player_id = ? AND item_id = ?",
		                    playerId, itemDetails.get("item_id"));
	            } else if (itemType == 2) {
	                // タイプが2の場合、プレイヤーのMPにitem_MP_heal分加算またはMP_MAXより多い場合はMP_MAXで上書き
	                if (characterMP + itemMPHeal < maxmp) {
	                	characterMP += itemMPHeal;
	                	session.setAttribute("character_MP", characterMP);
					} else if (characterMP + itemMPHeal == maxmp ) {
						session.setAttribute("character_MP", maxmp);
					} else if (characterMP + itemMPHeal == maxmp + itemMPHeal){
						System.out.println("MPが満タンです");
						
						return "menu"+1;
					}
	                System.out.println(characterMP);
	                
	             // アイテムの所持数を減らす
		            jdbcTemplate.update(
		                    "UPDATE player_items SET item_quantity = item_quantity - 1 WHERE player_id = ? AND item_id = ?",
		                    playerId, itemDetails.get("item_id"));
	            } else if (itemType == 99) {
	                // タイプが99の場合、HPとMPを全回復
	            	 if (characterMP + itemMPHeal <= maxmp) {
	            		 session.setAttribute("character_HP", maxHP);
	 	                session.setAttribute("character_MP", maxmp);
	 	               
	 	            
	            	 }else   if (characterHP + itemHPHeal <= maxHP) {
	            		 session.setAttribute("character_HP", maxHP);
	 	                session.setAttribute("character_MP", maxmp);
	 	               
		 	         }else if (characterHP == maxHP && characterMP == maxmp) {
		 	               return "menu"+1;
		 	         
	               
		 	         }
	             // アイテムの所持数を減らす
		            jdbcTemplate.update(
		                    "UPDATE player_items SET item_quantity = item_quantity - 1 WHERE player_id = ? AND item_id = ?",
		                    playerId, itemDetails.get("item_id"));
 
	            }else if (itemType == 3) {
		 System.out.println("ダメージ");
		 	    }
	            
	            // 成功メッセージをHTMLに渡す
	          
	            return "menu";
	        } catch (Exception e) {
	            // エラーが発生した場合のログ出力
	            e.printStackTrace();
	            // エラーメッセージをセット
	            
	            return "menu"; // エラー画面にリダイレクト
	        }
	    }
	 // 装備をする処理
	 	@PostMapping("/equipItem")
		@RequestMapping(value = "/equipItem", method = RequestMethod.POST)
        public String equipItem(@RequestParam String equipmentName, Model model, HttpSession session) {
            try {
                // ログイン中のユーザIDをセッションから取得
                Integer playerId = (Integer) session.getAttribute("playerId");

                if (playerId == null) {
                    return "redirect:/login";
                }

                // 装備の効果とタイプを取得
                Map<String, Object> equipmentDetails = jdbcTemplate.queryForMap(
                        "SELECT equipment_id, equipment_type, attack_power, defense_power, magic_power, magicpoint FROM equipment WHERE equipment_name = ?",
                        equipmentName
                );
                
                
                int equipmentType = ((Number) equipmentDetails.get("equipment_type")).intValue();
                int attackPower = ((Number) equipmentDetails.get("attack_power")).intValue();
                int defensePower = ((Number) equipmentDetails.get("defense_power")).intValue();
                int magicPower = ((Number) equipmentDetails.get("magic_power")).intValue();
                int magicpoint = ((Number) equipmentDetails.get("magicpoint")).intValue();

              //現在ログインしているプレイヤーの攻撃力と防御力と魔法攻撃力を取得
				Map<String, Object> playerDetails = jdbcTemplate.queryForMap(
						"SELECT character_Attack, character_Defense, character_MagicAttack,character_MP FROM player_characters WHERE player_id = ?",
						playerId);
             //プレイヤーの最大MPを取得
                int characterMaxMP = (int) session.getAttribute("maxMP");

               //データべースの攻撃力、防御力、魔法攻撃力入れる
                   int characterAttack = ((Number) playerDetails.get("character_Attack")).intValue();
                   int characterDefense = ((Number) playerDetails.get("character_Defense")).intValue();
                   int characterMagicAttack = ((Number) playerDetails.get("character_MagicAttack")).intValue();
                   int characterMP = ((Number) playerDetails.get("character_MP")).intValue();
                // 装備の効果を適用
           
                if (equipmentType == 1) {
                    // タイプが1の場合、プレイヤーの攻撃力にattack_powerを加算
                    
                    characterAttack += attackPower;
                    characterDefense = characterDefense;
                    characterMagicAttack = characterMagicAttack;
                    session.setAttribute("character_Attack", characterAttack);
                    session.setAttribute("character_Defense", characterDefense);
                    session.setAttribute("character_MagicAttack", characterMagicAttack);
                } else if (equipmentType == 2) {
                    // タイプが2の場合、プレイヤーの防御力にdefense_powerを加算
                	characterAttack = characterAttack;
                    characterDefense += defensePower;
                    characterMagicAttack = characterMagicAttack;
                    session.setAttribute("character_Attack", characterAttack);
                    session.setAttribute("character_Defense", characterDefense);
                    session.setAttribute("character_MagicAttack", characterMagicAttack);
                    
                } else if (equipmentType == 3) {
                    // タイプが3の場合、プレイヤーの魔法攻撃力にmagic_powerを加算
                    
                     characterAttack = characterAttack;
                     characterDefense = characterDefense;
                	characterMagicAttack += magicPower;
                	
					session.setAttribute("character_Attack", characterAttack);
					session.setAttribute("character_Defense", characterDefense);
					session.setAttribute("character_MagicAttack", characterMagicAttack);
               
                } else if (equipmentType == 11) {
                	// タイプが11の場合、プレイヤーの攻撃力にattack_powerを加算、プレイヤーのMPにmagicpointを加算
                	
                	System.out.println("magicpoint");
                    characterAttack += attackPower;
                    characterDefense = characterDefense;
                    characterMagicAttack = characterMagicAttack;
                    characterMaxMP = characterMP;
                    characterMaxMP += magicpoint;
                    session.setAttribute("character_Attack", characterAttack);
                    session.setAttribute("character_Defense", characterDefense);
                    session.setAttribute("character_MagicAttack", characterMagicAttack); 
                    session.setAttribute("maxMP", characterMaxMP);
                    
                    
                } else if (equipmentType == 12) {
                	// タイプが12の場合、プレイヤーの防御力にdefense_powerを加算、プレイヤーのMPにmagicpointを加算
          System.out.println("magicpoint");
                	characterAttack = characterAttack;
                    characterDefense += defensePower;
                    characterMagicAttack = characterMagicAttack;
                    characterMaxMP = characterMP;
                    characterMaxMP += magicpoint;
                    
                    session.setAttribute("character_Attack", characterAttack);
                    session.setAttribute("character_Defense", characterDefense);   
                    session.setAttribute("character_MagicAttack", characterMagicAttack);
                    session.setAttribute("maxMP", characterMaxMP);
                } else if (equipmentType == 13) {
                	// タイプが13の場合、プレイヤーの魔法攻撃力にmagic_powerを加算、プレイヤーのMPにmagicpointを加算
                	System.out.println("magicpoint");
                	characterAttack = characterAttack;
                	characterDefense = characterDefense;
                	characterMagicAttack += magicPower;
                	characterMaxMP = characterMP;
                	characterMaxMP += magicpoint;

                    session.setAttribute("character_Attack", characterAttack);
                    session.setAttribute("character_Defense", characterDefense);
                    session.setAttribute("character_MagicAttack", characterMagicAttack);
                    session.setAttribute("maxMP", characterMaxMP);
                  
             
                    
                }
                //装備した装備IDをセッションに保存
                session.setAttribute("equipmentId", equipmentDetails.get("equipment_id"));
               System.out.println(equipmentDetails.get("magicpoint"));
             // 成功メッセージをHTMLに渡す
	     
                return "menu";
                } catch (Exception e) {
                	// エラーが発生した場合のログ出力
					e.printStackTrace();
					// エラーメッセージをセット
				
					return "menu"; // エラー画面にリダイレクト
					
            }	 
		
		}	
	// 装備を外す処理
	@PostMapping("/removeEquipmentBtn")
	@RequestMapping(value = "/removeEquipmentBtn", method = RequestMethod.POST)
    public String removeEquipmentBtn(@RequestParam String equipmentName, Model model, HttpSession session) {
        try {
            // ログイン中のユーザIDをセッションから取得
            Integer playerId = (Integer) session.getAttribute("playerId");

            if (playerId == null) {
                return "redirect:/login";
            }
            //現在ログインしているプレイヤーの攻撃力と防御力と魔法攻撃力を取得
            Map<String, Object> playerDetails = jdbcTemplate.queryForMap(
					"SELECT character_Attack, character_Defense, character_MagicAttack,character_MP FROM player_characters WHERE player_id = ?",
					playerId);
                       
                        		 //プレイヤーの最大MPを取得
                                int characterMaxMP = (int) session.getAttribute("maxMP");
                                //現状MP
                                int characterNOWMP = (int) session.getAttribute("character_MP");
                               //データべースの攻撃力、防御力、魔法攻撃力入れる
                                   int characterAttack = ((Number) playerDetails.get("character_Attack")).intValue();
                                   int characterDefense = ((Number) playerDetails.get("character_Defense")).intValue();
                                   int characterMagicAttack = ((Number) playerDetails.get("character_MagicAttack")).intValue();
                                   int characterMP = ((Number) playerDetails.get("character_MP")).intValue();
                                   //データベースにあるプレイヤーの攻撃力、防御力、魔法攻撃力をセッションの攻撃力、防御力、魔法攻撃力に上書きする。
                  

                        session.setAttribute("character_Attack", characterAttack);
                        session.setAttribute("character_Defense", characterDefense);
                        session.setAttribute("character_MagicAttack", characterMagicAttack);
                        session.setAttribute("maxMP", characterMP);
                        
                        if( characterMP < characterNOWMP) {
                        	 session.setAttribute("character_MP", characterMP);
                        }
                        
        }catch (Exception e) {
        	// エラーが発生した場合のログ出力
        	System.out.println("エラーが発生しました。");
        }
		return "menu";
        }
}