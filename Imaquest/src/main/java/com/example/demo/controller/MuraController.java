package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MuraController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/mura")
    public String showMura(Model model, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        Integer playerId = (Integer) session.getAttribute("playerId");

        // ユーザの情報を取得（所持ゴールドなど）
        List<Map<String, Object>> userDataList = jdbcTemplate.queryForList("SELECT * FROM player_characters WHERE player_id = ?", playerId);

        if (!userDataList.isEmpty()) {
            Map<String, Object> userData = userDataList.get(0);
            model.addAttribute("goldAmount", userData.get("character_gold") != null ? userData.get("character_gold") : 0);

        } else {
            // ユーザーデータが見つからない場合の処理（オプション）
            model.addAttribute("goldAmount", 0);
        }

        // アイテム一覧を取得
        List<String> itemList = jdbcTemplate.queryForList("SELECT item_name FROM items", String.class);
        model.addAttribute("itemList", itemList);
        
        
        
        // 装備リストを取得
        List<String> equipmentList = jdbcTemplate.queryForList("SELECT equipment_name FROM equipment", String.class);
        model.addAttribute("equipmentList", equipmentList);

        return "mura";
    }

    @PostMapping("/purchaseItem")
    @ResponseBody
    public String purchaseItem(
            @RequestParam String itemName,
            @RequestParam int quantity,
            HttpSession session
    ) {
        // ログイン中のユーザIDをセッションから取得
        Integer playerId = (Integer) session.getAttribute("playerId");

        // アイテムの価格とIDを取得
        Map<String, Object> itemDetails = jdbcTemplate.queryForMap("SELECT item_id, purchase_price FROM items WHERE item_name = ?", itemName);
        int itemPrice = (int) itemDetails.get("purchase_price");
        int itemId = (int) itemDetails.get("item_id");

        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);

        // 購入可能か確認
        if (playerGold >= itemPrice * quantity) {
            // アイテムを購入し、所持ゴールドを減らす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - itemPrice * quantity, playerId);

            // アイテムがすでに所持されているか確認
            Integer existingItemCount = jdbcTemplate.queryForObject("SELECT item_quantity FROM player_items WHERE player_id = ? AND item_id = ?", Integer.class, playerId, itemId);

            if (existingItemCount != null) {
                // すでに所持している場合は数量を増やす
                jdbcTemplate.update("UPDATE player_items SET item_quantity = item_quantity + ? WHERE player_id = ? AND item_id = ?", quantity, playerId, itemId);
            } else {
                // 所持していない場合は新しく追加
                jdbcTemplate.update("INSERT INTO player_items (player_id, item_id, item_quantity) VALUES (?, ?, ?)", playerId, itemId, quantity);
            }

            return "購入成功";
        } else {
            return "お金が足りません";
        }
    }

    @PostMapping("/purchaseEquipment")
    @ResponseBody
    public String purchaseEquipment(
            @RequestParam String equipmentName,
            @RequestParam int quantity,
            HttpSession session
    ) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        int equipmentPrice = jdbcTemplate.queryForObject("SELECT purchase_price FROM equipment WHERE equipment_name = ?", Integer.class, equipmentName);
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);

        if (playerGold >= equipmentPrice * quantity) {
            // 購入可能な場合
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - equipmentPrice * quantity, playerId);

            // プレイヤーがすでに装備を持っているか確認
            Integer equipmentId = jdbcTemplate.queryForObject("SELECT equipment_id FROM equipment WHERE equipment_name = ?", Integer.class, equipmentName);
            Integer existingQuantity = jdbcTemplate.queryForObject("SELECT equipment_quantity FROM player_equipment WHERE player_id = ? AND equipment_id = ?", Integer.class, playerId, equipmentId);

            if (existingQuantity != null) {
                // 既に持っている場合は数量を加算
                jdbcTemplate.update("UPDATE player_equipment SET equipment_quantity = ? WHERE player_id = ? AND equipment_id = ?", existingQuantity + quantity, playerId, equipmentId);
            } else {
                // 持っていない場合は新しく追加
                jdbcTemplate.update("INSERT INTO player_equipment (player_id, equipment_id, equipment_quantity) VALUES (?, ?, ?)", playerId, equipmentId, quantity);
            }

            return "購入成功";
        }

        return "お金が足りません";
    }

    
    @GetMapping("/itemDetails")
    @ResponseBody
    public Map<String, Object> getItemDetails(@RequestParam String itemName) {
        // アイテムの詳細情報を取得
        Map<String, Object> itemDetails = jdbcTemplate.queryForMap("SELECT item_effect, purchase_price FROM items WHERE item_name = ?", itemName);
        return itemDetails;
    }
    
 // 装備詳細情報取得エンドポイント
    @GetMapping("/equipmentDetails")
    @ResponseBody
    public Map<String, Object> getEquipmentDetails(@RequestParam String equipmentName) {
        Map<String, Object> equipmentDetails = jdbcTemplate.queryForMap("SELECT effect, sell_price AS purchase_price FROM equipment WHERE equipment_name = ?", equipmentName);
        // ここで必要なデータの加工や変換があれば行う
        return equipmentDetails;
    }
   
    // ...（他のメソッド）...

}
