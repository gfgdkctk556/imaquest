package com.example.demo.controller;

import java.util.Random;
import java.util.Scanner;

public class BattleLoopController {

    public static void main(String[] args) {
        // プレイヤーと敵の初期化
        int playerHP = 100;
        int enemyHP = 80;

        // バトルループの開始
        while (playerHP > 0 && enemyHP > 0) {
            // プレイヤーの行動
            System.out.println("1. 攻撃 2. 防御");
            Scanner scanner = new Scanner(System.in);
            int playerAction = scanner.nextInt();

            // プレイヤーの行動の処理
            switch (playerAction) {
                case 1:
                    int playerDamage = calculateDamage(10); // 仮のダメージ計算
                    enemyHP -= playerDamage;
                    System.out.println("プレイヤーが" + playerDamage + "ダメージを与えた！");
                    break;
                case 2:
                    // 防御の処理
                    System.out.println("プレイヤーが防御した！");
                    break;
                default:
                    break;
            }

            // 敵の行動
            int enemyAction = new Random().nextInt(2) + 1; // ランダムな行動（仮）
            
            // 敵の行動の処理
            int enemyDamage = calculateDamage(8); // 仮のダメージ計算
            playerHP -= enemyDamage;
            System.out.println("敵が" + enemyDamage + "ダメージを与えた！");

            // バトルの結果の表示
            System.out.println("プレイヤーのHP: " + playerHP);
            System.out.println("敵のHP: " + enemyHP);
        }

        // 終了条件の確認
        if (playerHP <= 0) {
            System.out.println("プレイヤーが倒れました。");
        } else {
            System.out.println("敵を倒しました！");
        }
    }

    private static int calculateDamage(int baseDamage) {
        Random random = new Random();
        return baseDamage + random.nextInt(5);
    }
}
