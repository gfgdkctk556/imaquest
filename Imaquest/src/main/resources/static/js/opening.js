var clickCount = 0;

function startAdventure() {
    // ユーザー名を取得
    var username = document.getElementById('username').value;

    // ログインフォームを非表示にし、冒険のメッセージを表示する
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('adventure').style.display = 'block';

    // ユーザー名を表示
    document.getElementById('username-placeholder').innerText = username;

    // ログイン後にページをスクロール
    window.scrollTo({
        top: document.body.scrollHeight,
        behavior: 'smooth'
    });

    // クリック回数をカウント
    clickCount++;

    // 2回目のクリックでフィールド画面に移動
    if (clickCount === 2) {
        document.getElementById('adventure').style.display = 'none';
        document.getElementById('field').style.display = 'block';
    }
}
