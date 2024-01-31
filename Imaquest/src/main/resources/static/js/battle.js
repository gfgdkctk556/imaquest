//// コメント表示用の変数
//var commentIndex = 0;
//var comments = [];
//
//// プレイヤーのステータス
//var players = [
//    {
//        id: 1,
//        name: "プレイヤーキャラ",
//        level: 1,
//        experience: 0,
//        hp: 100,
//        mp: 50,
//        attack: 20,
//        defense: 10,
//        image: "player_image_url" // プレイヤーの画像URLを設定
//    },
//    {
//        id: 2,
//        name: "魔法使い",
//        level: 1,
//        experience: 0,
//        hp: 120,
//        mp: 30,
//        attack: 15,
//        defense: 8,
//        image: "mage_image_url" // 魔法使いの画像URLを設定
//    }
//];
//
//// 敵のステータス
//var enemy = {
//    id: 1,
//    name: "スライム",
//    level: 1,
//    hp: 50,
//    attack: 10,
//    defense: 5,
//    goldReward: 10,
//    itemRewardId: 1,
//    image: "slime_image_url" // スライムの画像URLを設定
//};
//
//// 戦闘の進行を管理する変数
//var isPlayerTurn = true; // true: プレイヤーのターン, false: 敵のターン
//
//// 道具の処理
//function useItem() {
//    // 仮のアイテムデータ（実際のゲームではデータベースから取得など）
//    var items = [
//        { name: "回復薬", quantity: 3 },
//        { name: "魔法の羽", quantity: 5 },
//        { name: "毒消し草", quantity: 2 }
//    ];
//
//    // 道具表示領域の要素を取得
//    var itemListContainer = document.getElementById("item-list");
//
//    // アイテム情報を表示するためのコンテナを取得
//    var itemContainer = document.getElementById("item-container");
//
//    // アイテム表示領域をクリア
//    itemContainer.innerHTML = "";
//
//    // アイテム情報を順に表示
//    items.forEach(function (item) {
//        var itemElement = document.createElement("p");
//        itemElement.textContent = item.name + ": " + item.quantity + "個";
//        itemContainer.appendChild(itemElement);
//    });
//
//    // 道具表示領域を表示
//    itemListContainer.style.display = "block";
//}
//
//// 戦闘アクションの選択肢を表示
//function showBattleOptions() {
//    hideAllSubMenus();
//    document.getElementById("battle-options").style.display = "block";
//}
//
//// 攻撃の処理
//function attack() {
//    if (isPlayerTurn) {
//        // プレイヤーの攻撃
//        var damage = calculateDamage(players[0].attack, enemy.defense);
//        enemy.hp -= damage;
//
//        showBattleComment(players[0].name + "が敵に" + damage + "のダメージを与えた!");
//    } else {
//        // 敵の攻撃
//        var damage = calculateDamage(enemy.attack, players[0].defense);
//        players[0].hp -= damage;
//
//        showBattleComment("敵が" + players[0].name + "に" + damage + "のダメージを与えた!");
//    }
//
//    // ターンを切り替える
//    isPlayerTurn = !isPlayerTurn;
//
//    // ステータス更新
//    updateStatus();
//
//    // 戦闘結果を判定
//    checkBattleResult();
//}
//
//// 魔法の選択肢を表示
//function selectMagic() {
//    hideAllSubMenus();
//    document.getElementById("magic-options").style.display = "block";
//}
//
//// 防御の処理
//function defend() {
//    // 防御処理（実際のゲームではダメージ軽減や特殊効果の発動など）
//    showBattleComment(players[0].name + "が防御してダメージを軽減した!");
//
//    // ターンを切り替える
//    isPlayerTurn = !isPlayerTurn;
//
//    // ステータス更新
//    updateStatus();
//
//    // 戦闘結果を判定
//    checkBattleResult();
//}
//
//// 固有スキルの処理
//function useUniqueSkill() {
//    // 固有スキルの処理（実際のゲームでは様々な効果があります）
//    showBattleComment(players[0].name + "が固有スキルを使用して敵に効果を与えた!");
//
//    // ターンを切り替える
//    isPlayerTurn = !isPlayerTurn;
//
//    // ステータス更新
//    updateStatus();
//
//    // 戦闘結果を判定
//    checkBattleResult();
//}
//
//// 魔法の処理
//function castSpell(spellName) {
//    if (isPlayerTurn) {
//        // プレイヤーの魔法
//        var damage = calculateDamage(30, enemy.defense); // 仮のダメージ計算
//        enemy.hp -= damage;
//
//        showBattleComment(players[0].name + "が" + spellName + "で敵に" + damage + "のダメージを与えた!");
//    } else {
//        // 敵の魔法
//        var damage = calculateDamage(20, players[0].defense); // 仮のダメージ計算
//        players[0].hp -= damage;
//
//        showBattleComment("敵が" + players[0].name + "に" + spellName + "で" + damage + "のダメージを与えた!");
//    }
//
//    // ターンを切り替える
//    isPlayerTurn = !isPlayerTurn;
//
//    // ステータス更新
//    updateStatus();
//
//    // 戦闘結果を判定
//    checkBattleResult();
//}
//
//// 勝ちの処理
//function battleWin() {
//    comments = [
//        "スライムを倒した！",
//        "経験値を５手に入れた！",
//        "勇者のレベルが2に上がった！",
//        "それぞれ能力値が上昇します！",
//        "攻撃力が２上がった。防御力が４上がった。HPが４上がった！"
//    ];
//    showNextComment();
//
//    // ターン制復活
//    isPlayerTurn = true;
//
//    // ステータス更新
//    updateStatus();
//}
//
//// 次のコメントを表示
//function showNextComment() {
//    if (commentIndex < comments.length) {
//        showBattleComment(comments[commentIndex]);
//        commentIndex++;
//    } else {
//        // All comments are displayed, reset the index for the next battle
//        commentIndex = 0;
//    }
//}
//
//// 負けの処理
//function battleLose() {
//    comments = ["全滅した！"];
//    showNextComment();
//}
//
//// 戦闘結果を判定
//function checkBattleResult() {
//    if (enemy.hp <= 0) {
//        // 敵を倒した場合<script src="js/battle.js"></script>
//        battleWin();
//    } else if (players[0].hp <= 0) {
//        // プレイヤーが全滅した場合
//        battleLose();
//    }
//}
//
//// ダメージ計算
//function calculateDamage(attack, defense) {
//    // 仮のダメージ計算（実際のゲームでは敵のステータスに基づいて計算）
//    var damage = Math.max(attack - defense, 0);
//    return damage;
//}
//
//// ステータス更新
//function updateStatus() {
//    // プレイヤーのステータスを表示
//    document.getElementById("player-status").innerHTML =
//        "<h3>" + players[0].name + "</h3>" +
//        "HP: " + players[0].hp + "<br>" +
//        "MP: " + players[0].mp;
//
//    // 敵のステータスを表示
//    document.getElementById("enemy-status").innerHTML =
//        "<h3>" + enemy.name + "</h3>" +
//        "HP: " + enemy.hp;
//}
//
//// コメント表示
//function showBattleComment(comment) {
//    var commentContainer = document.getElementById("battle-comment");
//    commentContainer.innerHTML = comment;
//    commentContainer.style.display = "block";
//
//    // Add click event listener to show the next comment
//    commentContainer.onclick = function() {
//        showNextComment();
//    };
//}
//
//// 次のコメントを表示
//function showNextComment() {
//    if (commentIndex < comments.length) {
//        showBattleComment(comments[commentIndex]);
//        commentIndex++;
//
//        // If all comments are displayed, reset the index for the next battle
//        if (commentIndex === comments.length) {
//            commentIndex = 0;
//        }
//    }
//}
//
//// すべてのサブメニューを非表示にする処理
//function hideAllSubMenus() {
//    var subMenus = document.querySelectorAll(".sub-action-menu");
//    subMenus.forEach(function (menu) {
//        menu.style.display = "none";
//    });
//}
