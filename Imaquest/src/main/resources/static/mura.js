// village.js

function purchaseEquipment(equipment, cost) {
    // 装備の購入処理（デモなので直書き）
    document.getElementById('selected-purchase-status').innerHTML = `<h3>${equipment}を購入しました</h3><p>コスト: ${cost}ゴールド</p>`;
}

function sellEquipment(equipment, price) {
    // 装備の売却処理（デモなので直書き）
    document.getElementById('selected-sell-status').innerHTML = `<h3>${equipment}を売却しました</h3><p>価格: ${price}ゴールド</p>`;
}

function rest() {
    // 休む処理（デモなので直書き）
    document.getElementById('rest-status').innerHTML = `<h3>休憩しました</h3><p>HPとMPが回復しました</p>`;
}
