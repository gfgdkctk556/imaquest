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

@Controller
public class MuraController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/mura")
    public String showMura(Model model, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // ユーザの情報を取得（所持ゴールドなど）
        List<Map<String, Object>> userDataList = jdbcTemplate.queryForList("SELECT * FROM player_characters WHERE player_id = ?", playerId);

        if (!userDataList.isEmpty()) {
            Map<String, Object> userData = userDataList.get(0);
            model.addAttribute("goldAmount", userData.get("character_gold") != null ? userData.get("character_gold") : 0);

        } else {
            // ユーザーデータが見つからない場合の処理（オプション）
            model.addAttribute("goldAmount", 0);
        }

        // 装備リストを取得
        List<String> equipmentList = jdbcTemplate.queryForList("SELECT equipment_name FROM equipment", String.class);
        model.addAttribute("equipmentList", equipmentList);

        return "mura";
    }


    @PostMapping("/purchaseItem")
    public String purchaseItem(@RequestParam String itemName, @RequestParam int quantity, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // アイテムの価格を取得
        int itemPrice = jdbcTemplate.queryForObject("SELECT item_value FROM items WHERE item_name = ?", Integer.class, itemName);

        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);

        // 購入可能か確認
        if (playerGold >= itemPrice * quantity) {
            // アイテムを購入し、所持ゴールドを減らす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - itemPrice * quantity, playerId);
            // TODO: アイテムの所持数を増やす処理を追加
        }

        return "redirect:/mura";
    }

    @PostMapping("/sellItem")
    public String sellItem(@RequestParam String itemName, @RequestParam int quantity, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // アイテムの価格を取得
        int itemPrice = jdbcTemplate.queryForObject("SELECT item_value FROM items WHERE item_name = ?", Integer.class, itemName);

        // アイテムの所持数を取得
        int playerItemQuantity = jdbcTemplate.queryForObject("SELECT item_quantity FROM player_items WHERE player_id = ? AND item_name = ?", Integer.class, playerId, itemName);

        // 売却可能か確認
        if (playerItemQuantity >= quantity) {
            // アイテムを売却し、所持ゴールドを増やす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", itemPrice * quantity, playerId);
            // TODO: アイテムの所持数を減らす処理を追加
        }

        return "redirect:/mura";
    }

    @PostMapping("/purchaseEquipment")
    public String purchaseEquipment(@RequestParam String equipmentName, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // 装備の価格を取得
        int equipmentPrice = jdbcTemplate.queryForObject("SELECT purchase_price FROM equipment WHERE equipment_name = ?", Integer.class, equipmentName);

        // ユーザの所持ゴールドを取得
        int playerGold = jdbcTemplate.queryForObject("SELECT character_gold FROM player_characters WHERE player_id = ?", Integer.class, playerId);

        // 購入可能か確認
        if (playerGold >= equipmentPrice) {
            // 装備を購入し、所持ゴールドを減らす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", playerGold - equipmentPrice, playerId);
            // TODO: 装備を所持品に追加する処理を追加
        }

        return "redirect:/mura";
    }

    @PostMapping("/sellEquipment")
    public String sellEquipment(@RequestParam String equipmentName, HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // 装備の価格を取得
        int equipmentPrice = jdbcTemplate.queryForObject("SELECT sell_price FROM equipment WHERE equipment_name = ?", Integer.class, equipmentName);

        // 装備の所持数を取得
        int playerEquipmentQuantity = jdbcTemplate.queryForObject("SELECT equipment_quantity FROM player_equipment WHERE player_id = ? AND equipment_name = ?", Integer.class, playerId, equipmentName);

        // 売却可能か確認
        if (playerEquipmentQuantity > 0) {
            // 装備を売却し、所持ゴールドを増やす
            jdbcTemplate.update("UPDATE player_characters SET character_gold = ? WHERE player_id = ?", equipmentPrice, playerId);
            // TODO: 装備を所持品から減らす処理を追加
        }

        return "redirect:/mura";
    }

    @PostMapping("/rest")
    public String rest(HttpSession session) {
        // ログイン中のユーザIDをセッションから取得
        String playerId = (String) session.getAttribute("userId");

        // TODO: 休む処理を追加

        return "redirect:/mura";
    }
}
