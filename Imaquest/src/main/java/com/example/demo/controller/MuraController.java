package com.example.demo.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        if (playerId == null) {
            return "redirect:/login";
        }
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
     // 所持アイテム一覧を取得 
        List<String> playerItemList = jdbcTemplate.queryForList(
                "SELECT items.item_name FROM items " +
                "LEFT JOIN player_items ON items.item_id = player_items.item_id AND player_items.player_id = ? " +
                "WHERE player_items.item_id IS NOT NULL AND (player_items.item_quantity > 0 OR player_items.item_quantity IS NULL)", 
                String.class, playerId);
        // ログ出力
        System.out.println("Player Item List: " + playerItemList);
        model.addAttribute("playerItemList", playerItemList);
        // 所持装備一覧を取得
        List<String> playerEquipmentList = jdbcTemplate.queryForList(
                "SELECT equipment.equipment_name FROM equipment " +
                "LEFT JOIN player_equipment ON equipment.equipment_id = player_equipment.equipment_id AND player_equipment.player_id = ? " +
                "WHERE player_equipment.equipment_quantity > 0", 
                String.class, playerId);
        model.addAttribute("playerEquipmentList", playerEquipmentList);
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);
        model.addAttribute("playerGold", playerGold);
//
//        // 所持アイテム詳細を取得
//        List<Map<String, Object>> playerItemsDetails = getPlayerItemsDetails(session);
//        model.addAttribute("playerItemsDetails", playerItemsDetails);
//
        // 所持装備詳細を取得
        List<Map<String, Object>> playerEquipmentDetails = getPlayerEquipmentDetails(session);
        model.addAttribute("playerEquipmentDetails", playerEquipmentDetails);
        return "mura";
    }
//アイテム購入処理
    @PostMapping("/purchaseItem")
    @ResponseBody
    public String purchaseItem(
            @RequestParam String itemName,
            @RequestParam int quantity,
            HttpSession session
    ) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        // クリックされたアイテムの価格を取得
        int itemPrice = jdbcTemplate.queryForObject("SELECT purchase_price FROM items WHERE item_name = ?", Integer.class, itemName);
        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);
        // 購入可能か確認
        if (playerGold >= itemPrice * quantity) {
            // アイテムを購入し、所持ゴールドを減らす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - itemPrice * quantity, playerId);
            Integer existingQuantity = null;
            
               // アイテムを所持しているか確認
               try {
                    existingQuantity = jdbcTemplate.queryForObject(
                            "SELECT item_quantity FROM player_items WHERE player_id = ? AND item_id = (SELECT item_id FROM items WHERE item_name = ?)",
                            Integer.class, playerId, itemName);
                } catch (EmptyResultDataAccessException e) {
                    existingQuantity = null;
                       jdbcTemplate.update("INSERT INTO player_items (player_id, item_id, item_quantity) VALUES (?, (SELECT item_id FROM items WHERE item_name = ?), ?)", playerId, itemName, quantity);
               }
            
            if (existingQuantity != null) {
                // すでにアイテムを所持している場合、数量を加算
               
                jdbcTemplate.update("UPDATE player_items SET item_quantity = item_quantity + ? WHERE player_id = ? AND item_id = (SELECT item_id FROM items WHERE item_name = ?)", quantity, playerId, itemName
                                        );
            } 
            return "購入成功";
        } else {
            return "お金が足りません";
        }
    }
//装備購入処理
    @PostMapping("/purchaseEquipment")
    @ResponseBody
    public String purchaseEquipment(
            @RequestParam String equipmentName,
            @RequestParam int quantity,
            HttpSession session
    ) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        // 装備の価格とIDを取得
        Map<String, Object> equipmentInfo = jdbcTemplate.queryForMap("SELECT equipment_id, purchase_price FROM equipment WHERE equipment_name = ?", equipmentName);
        int equipmentId = (int) equipmentInfo.get("equipment_id");
        int equipmentPrice = (int) equipmentInfo.get("purchase_price");
        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);
        // 購入可能か確認
        if (playerGold >= equipmentPrice * quantity) {
            // 装備を購入し、所持ゴールドを減らす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - equipmentPrice * quantity, playerId);
            // 装備を所持しているか確認
            Integer existingQuantity = null;
            try {
                existingQuantity = jdbcTemplate.queryForObject(
                        "SELECT equipment_quantity FROM player_equipment WHERE player_id = ? AND equipment_id = ?",
                        Integer.class, playerId, equipmentId);
            } catch (EmptyResultDataAccessException e) {
                existingQuantity = null;
                jdbcTemplate.update("INSERT INTO player_equipment (player_id, equipment_id, equipment_quantity) VALUES (?, ?, ?)", playerId, equipmentId, quantity);
            }
            if (existingQuantity != null) {
                // すでに装備を所持している場合、数量を加算
                jdbcTemplate.update("UPDATE player_equipment SET equipment_quantity = equipment_quantity + ? WHERE player_id = ? AND equipment_id = ?", quantity, playerId, equipmentId);
            }
            return "購入成功";
        } else {
            return "お金が足りません";
        }
    }
    
    @GetMapping("/itemDetails")
    @ResponseBody
    public Map<String, Object> getItemDetails(@RequestParam String itemName ) {      
        // アイテムの詳細情報を取得
        Map<String, Object> itemDetails = jdbcTemplate.queryForMap("SELECT item_effect, purchase_price FROM items WHERE item_name = ?", itemName);
        return itemDetails;
    }
    
 // 装備詳細情報取得エンドポイント
    @GetMapping("/equipmentDetails")
    @ResponseBody
    public Map<String, Object> getEquipmentDetails(@RequestParam String equipmentName) {
        //所持装備の名前と売却額を取得
                Map<String, Object> equipmentDetails = jdbcTemplate.queryForMap("SELECT equipment_effect, purchase_price FROM equipment WHERE equipment_name = ?", equipmentName);
        // ここで必要なデータの加工や変換があれば行う
        return equipmentDetails;
    }
   
 // 所持アイテム詳細情報取得エンドポイント
    @GetMapping("/playerItemsDetails")
    @ResponseBody
    public List<Map<String, Object>> getPlayerItemsDetails(HttpSession session) {
        Integer playerId = (Integer) session.getAttribute("playerId");
        
     // プレイヤーが所持しているアイテムの詳細情報を取得
        List<Map<String, Object>> playerItemsDetails = jdbcTemplate.queryForList(
                "SELECT items.item_name, player_items.item_quantity, items.sell_price FROM items " +
                "LEFT JOIN player_items ON items.item_id = player_items.item_id AND player_items.player_id = ?",
                playerId
        );
        //player_items.item_quantityがnullの場合、0を代入する
                for (Map<String, Object> playerItemDetail : playerItemsDetails) {
                    if (playerItemDetail.get("item_quantity") == null) {
                        playerItemDetail.put("item_quantity", 0);
                    }
                }
        return playerItemsDetails;
    }
    // 所持装備詳細情報取得エンドポイント
    @GetMapping("/playerEquipmentDetails")
    @ResponseBody
    public List<Map<String, Object>> getPlayerEquipmentDetails(HttpSession session) {
                Integer playerId = (Integer) session.getAttribute("playerId");
                // プレイヤーが所持している装備の詳細情報を取得
                List<Map<String, Object>> playerEquipmentDetails = jdbcTemplate.queryForList(
                 "SELECT equipment.equipment_name, player_equipment.equipment_quantity, equipment.sell_price FROM equipment " +
                 "LEFT JOIN player_equipment ON equipment.equipment_id = player_equipment.equipment_id AND player_equipment.player_id = ?",
                 playerId
                );
             
                
                //player_equipment.equipment_quantityがnullの場合、0を代入する
                for (Map<String, Object> playerEquipmentDetail : playerEquipmentDetails) {
                    if (playerEquipmentDetail.get("equipment_quantity") == null) {
                        playerEquipmentDetail.put("equipment_quantity", 0);
                    }
                    
                }
        return playerEquipmentDetails;
      
    }
    
    // アイテム売却処理
    @PostMapping("/sellItem")
    @ResponseBody
    public String sellItem(
            @RequestParam String itemName,
            @RequestParam String quantity,
            HttpSession session
    ) {
        System.out.println(quantity + "\t" + itemName);
        int i_quantity = Integer.parseInt(quantity);
        //quantityの値がnullになっていなければ、0に変換する
        
        
   
        
        
        Integer playerId = (Integer) session.getAttribute("playerId");
        // 所持アイテムの売却価格とアイテムIDを取得
        Map<String, Object> itemInfo = jdbcTemplate.queryForMap("SELECT item_id, sell_price FROM items WHERE item_name = ?", itemName);
        int itemId = (int) itemInfo.get("item_id");
        int itemPrice = (int) itemInfo.get("sell_price");
        System.out.println(itemId+"\t"+itemPrice+"\t"+playerId+"\t"+itemName+"\t"+i_quantity);
        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);
        // 売却可能か確認
        Integer existingQuantity = jdbcTemplate.queryForObject(
                "SELECT item_quantity FROM player_items WHERE player_id = ? AND item_id = ?",
                Integer.class, playerId, itemId);
        if (existingQuantity != null && existingQuantity >= i_quantity) {
            int newGold = playerGold + itemPrice * i_quantity;
            // アイテムを売却し、所持ゴールドを増やす
            int i = jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?",newGold, playerId);
              
            // アイテムの所持数を減らす
            jdbcTemplate.update("UPDATE player_items SET item_quantity = item_quantity - ? WHERE player_id = ? AND item_id = ?", quantity, playerId, itemId);
            return "売却成功";
        } else {
                    return "所持数が足りません";
        }
    }
    
 //装備売却処理
    @PostMapping("/sellEquipment")
    @ResponseBody
    public String sellequipment(
            @RequestParam String equipmentName,
            @RequestParam String quantity,
            HttpSession session
    ) {
        System.out.println(quantity + "\t" + equipmentName);
        int e_quantity = Integer.parseInt(quantity);
        Integer playerId = (Integer) session.getAttribute("playerId");
        
        // 所持アイテムの売却価格とアイテムIDを取得
        Map<String, Object> itemInfo = jdbcTemplate.queryForMap("SELECT equipment_id, sell_price FROM equipment WHERE equipment_name = ?", equipmentName);
        int equipmentId = (int) itemInfo.get("equipment_id");
        int equipmentPrice = (int) itemInfo.get("sell_price");
        System.out.println(equipmentId+"\t"+equipmentPrice+"\t"+playerId+"\t"+equipmentName+"\t"+e_quantity);
        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);
        // 売却可能か確認
        Integer existingQuantity = jdbcTemplate.queryForObject(
                "SELECT equipment_quantity FROM player_equipment WHERE player_id = ? AND equipment_id = ?",
                Integer.class, playerId, equipmentId);
        if (existingQuantity != null && existingQuantity >= e_quantity) {
            int newGold = playerGold + equipmentPrice * e_quantity;
            // アイテムを売却し、所持ゴールドを増やす
            int i = jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?",newGold, playerId);
              
            // アイテムの所持数を減らす
            jdbcTemplate.update("UPDATE player_equipment SET equipment_quantity = equipment_quantity - ? WHERE player_id = ? AND equipment_id = ?", quantity, playerId, equipmentId);
            return "売却成功";
        } else {
                    return "所持数が足りません";
        }
    }
}