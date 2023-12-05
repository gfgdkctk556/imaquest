// 選択したアイテムのステータスを保持する変数
let selectedEquipment = "";

function selectCharacter(character) {
    // 選択したキャラクターのステータス表示（デモなので直書き）
    document.getElementById('selected-character-status').innerHTML = `<h3>${character}のステータス</h3><p>HP: 100</p><p>MP: 50</p>`;
}

function selectEquipment(equipment) {
    // 選択した武具のステータス表示（デモなので直書き）
    document.getElementById('selected-equipment-status').innerHTML = `<h3>${equipment}のステータス</h3><p>攻撃力: +10</p><p>防御力: +5</p>`;
    
    // 選択したアイテムを変数に保存
    selectedEquipment = equipment;
}

function selectItem(item) {
    // 選択したアイテムの説明表示（デモなので直書き）
    document.getElementById('selected-item-description').innerHTML = `<h3>${item}</h3><p>回復アイテムです。</p>`;
    
    // 選択したアイテムを変数に保存
    selectedEquipment = item;
}

// 装備および外すボタンのクリックイベント処理
function equipItem() {
    if (selectedEquipment) {
        showMessage(`装備しました: ${selectedEquipment}`);
    } else {
        showMessage("アイテムが選択されていません");
    }
}

function unequipItem() {
    if (selectedEquipment) {
        showMessage(`外しました: ${selectedEquipment}`);
    } else {
        showMessage("アイテムが選択されていません");
    }
}

// メッセージ表示関数
function showMessage(message) {
    const messageArea = document.getElementById('message-area');
    messageArea.innerText = message;
}
