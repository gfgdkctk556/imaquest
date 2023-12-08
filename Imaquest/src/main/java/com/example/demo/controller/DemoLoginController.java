//package com.example.demo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class DemoLoginController {
//
//    // ログイン画面の表示
//    @GetMapping("/demologin")
//    public String showLoginForm() {
//        return "demologin";
//    }
//
//    // ログイン処理（デモプレイ用：簡略化）
//    @PostMapping("/demologin")
//    public String login(@RequestParam String username, Model model) {
//        // TODO: デモプレイ用のログイン処理（ここに実際の処理を追加）
//
//        if ("ng".equalsIgnoreCase(username)) {
//            return "demong"; // ログインNGの場合の画面遷移
//        }
//
//        model.addAttribute("error", true);
//        return "demofield"; // ログイン出来た時field画面に遷移
//    }
//    
// // ログイン画面に戻る
//    @GetMapping("/returntologin")
//    public String returnToLogin() {
//        return "demologin";
//    }
//
//    // 登録画面の表示
//    @GetMapping("/demoregister")
//    public String showRegisterForm(Model model) {
//        // メッセージを空でセットしておく
//        model.addAttribute("message", "");
//        return "demoregister";
//    }
//
//    // 登録処理（デモプレイ用：簡略化）
//    @PostMapping("/demoregister")
//    public String register(@RequestParam String playerId, @RequestParam String password, Model model) {
//        // TODO: デモプレイ用の登録処理（ここに実際の処理を追加）
//
//        model.addAttribute("message", "アカウント作成が完了しました！");
//        return "demologin"; // 登録完了後の画面遷移
//    }
//
//    // メニュー画面の表示
//    @GetMapping("/menu")
//    public String showMenu() {
//        return "menu";
//    }
//
// // フィールド画面に戻る
//    @GetMapping("/demofield")
//    public String returnToField() {
//        return "demofield";
//    }
// // バトル画面に遷移（デモプレイ用：エンカウント時）
//    @GetMapping("/battle")
//    public String showBattle() {
//        return "battle";
//    }
// // 村画面の表示
//    @GetMapping("/mura")
//    public String showVillage() {
//        return "mura";
//    }
//
//    // 装備の購入処理
//    @PostMapping("/purchaseEquipment")
//    public String purchaseEquipment(@RequestParam String equipment, @RequestParam int cost, Model model) {
//        // TODO: 実際の装備の購入処理を追加
//
//        // 購入した装備の情報を表示
//        model.addAttribute("selectedPurchaseStatus", equipment + "を購入しました。 コスト: " + cost);
//        return "mura"; // 購入後の画面遷移
//    }
//
//    // 装備の売却処理
//    @PostMapping("/sellEquipment")
//    public String sellEquipment(@RequestParam String equipment, @RequestParam int price, Model model) {
//        // TODO: 実際の装備の売却処理を追加
//
//        // 売却した装備の情報を表示
//        model.addAttribute("selectedSellStatus", equipment + "を売却しました。 価格: " + price);
//        return "mura"; // 売却後の画面遷移
//    }
//
//    // 休憩処理
//    @PostMapping("/rest")
//    public String rest(Model model) {
//        // TODO: 実際の休憩処理を追加
//
//        // 休憩後の情報を表示
//        model.addAttribute("restStatus", "休憩しました。 HPとMPが回復しました。");
//        return "mura"; // 休憩後の画面遷移
//    }
//}
//
