// field.js

document.addEventListener("DOMContentLoaded", function () {
    const board = document.getElementById("board");
    let previousSquare = null;
    let villageCount = 0; // Counter for villages

    // プレイヤーの初期位置
    const player = document.createElement("div");
    player.classList.add("square", "player");
    player.setAttribute("data-row", "0");
    player.setAttribute("data-col", "0");
    player.textContent = "👤"; // 初期の絵文字を設定

    // クリック時の処理
    board.addEventListener("click", function (event) {
        const square = event.target;

        const playerRow = parseInt(player.getAttribute("data-row"), 10);
        const playerCol = parseInt(player.getAttribute("data-col"), 10);
        const clickedRow = parseInt(square.getAttribute("data-row"), 10);
        const clickedCol = parseInt(square.getAttribute("data-col"), 10);

        const isAdjacent = (Math.abs(clickedRow - playerRow) === 1 && clickedCol === playerCol) ||
            (Math.abs(clickedCol - playerCol) === 1 && clickedRow === playerRow);

        if (isAdjacent && isValidMove(clickedRow, clickedCol)) {
            // プレイヤーを移動
            player.classList.remove("player");
            square.classList.add("player");

            // プレイヤーの位置情報を更新
            player.setAttribute("data-row", clickedRow);
            player.setAttribute("data-col", clickedCol);

            // 絵文字をプレイヤーの位置に表示
            square.textContent = "👤";

            // 前回クリックされたマスの色を元に戻す
            if (previousSquare && previousSquare !== square && !previousSquare.classList.contains("village") && !previousSquare.classList.contains("town")) {
                previousSquare.classList.remove("player");
                previousSquare.textContent = ""; // 前の升目の絵文字をクリア
            }

            // 今回クリックされたマスを保存
            previousSquare = square;

            // エンカウントの判定
            if (Math.random() < encounterProbability) {
                // エンカウントが発生したら自動的にバトル画面に遷移
                startBattle();
            }

            // 村画面に移動する条件
            if (square.classList.contains("village")) {
                square.textContent = "村";
                window.location.href = "/mura";
            }

            // 町画面に移動する条件
            if (square.classList.contains("town")) {
                square.textContent = "町";
                window.location.href = "/machi";
            }
        }
    });

    // メニューボタンがクリックされたときの処理
    function openMenu() {
        // メニュー画面に遷移する処理を追加
        window.location.href = "/menu";
    }

    // ログイン画面に戻るボタンがクリックされたときの処理
    function returnToLogin() {
        // ログイン画面に遷移する処理を追加
        window.location.href = "/login";
    }

    // 戦闘開始処理
    function startBattle() {
        // バトル画面に遷移
        window.location.href = "/battleStart";
    }

    // ボードにプレイヤーを追加
    board.appendChild(player);

    // ボードに升目を生成
    for (let row = 0; row < 20; row++) {
        for (let col = 0; col < 20; col++) {
            const square = document.createElement("div");
            square.classList.add("square");

            // 2x2の村を配置（画面内右上、右下、左下）
            const isVillage = (
                (row === 2 && col === 15) ||
                (row === 2 && col === 16) ||
                (row === 16 && col === 15) ||
                (row === 16 && col === 16) ||
                (row === 2 && col === 2) ||
                (row === 2 && col === 3) ||
                (row === 3 && col === 2) ||
                (row === 3 && col === 3)
            );

            // 3x3の町を配置（画面内中央）
            const isTown = (
                (row >= 8 && row <= 10) &&
                (col >= 8 && col <= 10)
            );

            if (isVillage) {
                square.classList.add("village");
                square.textContent = "村";
                villageCount++;
            } else if (isTown) {
                square.classList.add("town");
                square.textContent = "町";
            }

            square.setAttribute("data-row", row.toString());
            square.setAttribute("data-col", col.toString());
            board.appendChild(square);
        }
    }

    // 移動が有効かどうかを確認する関数
    function isValidMove(row, col) {
        return row >= 0 && row < 20 && col >= 0 && col < 20;
    }

    // エンカウントの確率（仮に30%とします）
    const encounterProbability = 0.3;
});
