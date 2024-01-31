//package com.example.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.example.demo.service.AttackService;
//import com.example.demo.service.MagicService;
//
//@Controller
//public class ActionController {
//
//    private final AttackService attackService;
//    private final MagicService magicService;
//
//    @Autowired
//    public ActionController(AttackService attackService, MagicService magicService) {
//        this.attackService = attackService;
//        this.magicService = magicService;
//    }
//
//    @PostMapping("/action")
//    public String performAction(
//            @RequestParam(name = "playerId") Integer playerId,
//            @RequestParam(name = "playerAction") String playerAction,
//            @RequestParam(name = "selectedSpellName", required = false) String selectedSpellName,
//            Model model) {
//
//        switch (playerAction) {
//            case "attack":
//                attackService.performAttack(playerId, model);
//                break;
//            case "magic":
//                magicService.performMagic(playerId, selectedSpellName, model);
//                break;
//            // 他のアクションに対する処理も追加
//            default:
//                break;
//        }
//
//        // バトルが終了した場合の処理などを追加
//
//        // 遷移先を指定
//        return "battle";
//    }
//}
