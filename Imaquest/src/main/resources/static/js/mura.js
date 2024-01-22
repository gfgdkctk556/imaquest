document.addEventListener("DOMContentLoaded", function () {
    // アイテム一覧の要素を取得
    var itemListElement = document.getElementById("itemList");

    // アイテム一覧をクリックしたときの処理
    itemListElement.addEventListener("click", function (event) {
        // クリックされたアイテム名を取得して詳細表示関数を呼び出す
        var clickedItemName = event.target.innerText;
        showItemDetails(clickedItemName);
        showPurchaseButton(); // アイテムが選択されたら購入ボタンを表示
    });

     // 装備一覧の要素を取得
    var equipmentListElement = document.getElementById("equipmentList");

    // 装備一覧をクリックしたときの処理
    equipmentListElement.addEventListener("click", function (event) {
        // クリックされた装備名を取得して詳細表示関数を呼び出す
        var clickedEquipmentName = event.target.innerText;
        showEquipmentDetails(clickedEquipmentName);
        showPurchaseButton(); // 装備が選択されたら購入ボタンを表示
    });
// 購入ボタンの表示を切り替える関数
    function showPurchaseButton() {
        var purchaseButtonElement = document.getElementById("purchaseButton");
        purchaseButtonElement.style.display = "block";
    }
  });   

// アイテム詳細表示関数
function showItemDetails(itemName) {
    // 装備詳細を非表示にする
    hideEquipmentDetails();

    // Ajaxリクエストを使用してアイテム詳細を取得
    fetch("/itemDetails?itemName=" + itemName)
        .then(response => response.json())
        .then(data => {
            // 取得した詳細情報を表示する処理
            var itemDetailsElement = document.getElementById("itemDetailsContainer");
            itemDetailsElement.style.display = "block";
            document.getElementById("equipmentDetailsContainer").style.display = "none";  // 装備の詳細を非表示にする
            document.getElementById("itemName").innerText = "アイテム名: " + itemName;
            document.getElementById("itemEffect").innerText = "効果: " + data.item_effect;
            document.getElementById("purchasePrice").innerText = "購入価格: " + data.purchase_price;
        })
        .catch(error => console.error("アイテム詳細の取得エラー:", error));
}





// 装備詳細表示関数
function showEquipmentDetails(equipmentName) {
    // アイテム詳細を非表示にする
    hideItemDetails();

    // Ajaxリクエストを使用して装備詳細を取得
    fetch("/equipmentDetails?equipmentName=" + equipmentName)
        .then(response => response.json())
        .then(data => {
            var equipmentDetailsElement = document.getElementById("equipmentDetailsContainer");
            equipmentDetailsElement.style.display = "block";
            document.getElementById("itemDetailsContainer").style.display = "none";  // アイテムの詳細を非表示にする
            document.getElementById("equipmentName").innerText = "装備名: " + equipmentName;
            document.getElementById("equipmentEffect").innerText = "効果: " + data.effect;
            document.getElementById("purchasePriceEquipment").innerText = "価格: " + data.purchase_price;
        
        })
        .catch(error => console.error("装備詳細の取得エラー:", error));
}






// アイテム詳細非表示関数
function hideItemDetails() {
    var itemDetailsElement = document.getElementById("itemDetailsContainer");
    itemDetailsElement.style.display = "none";
}
// 購入ボタンの非表示関数
function hidePurchaseButton() {
    var purchaseButtonElement = document.getElementById("purchaseButton");
    purchaseButtonElement.style.display = "none";
}
// 装備詳細非表示関数
function hideEquipmentDetails() {
    var equipmentDetailsElement = document.getElementById("equipmentDetailsContainer");
    equipmentDetailsElement.style.display = "none";
}



// アイテム購入関数
function purchaseItem() {
    var itemName = document.getElementById("itemName").innerText.split(": ")[1];
    var quantity = document.getElementById("purchaseQuantity").value;

    // Ajaxリクエストを使用してアイテム購入処理を実行
    fetch("/purchaseItem", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "itemName=" + itemName + "&quantity=" + quantity,
    })
    .then(response => response.text())
    .then(data => {
        if (data === "購入成功") {
            alert("購入成功");  // 購入成功メッセージをアラートで表示
            // 必要であれば画面を更新する処理をここに追加
        } else {
            alert("お金が足りません");  // 購入不可メッセージをアラートで表示
        }
    })
    .catch(error => console.error("アイテム購入エラー:", error));
}

// 装備購入関数
function purchaseEquipment() {
    var equipmentName = document.getElementById("equipmentName").innerText.split(": ")[1];
    var quantity = document.getElementById("purchaseQuantityEquipment").value;

    // Ajaxリクエストを使用して装備購入処理を実行
    fetch("/purchaseEquipment", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "equipmentName=" + equipmentName + "&quantity=" + quantity,
    })
    .then(response => response.text())
    .then(data => {
        alert(data);  // 購入成功メッセージをアラートで表示
        // 必要であれば画面を更新する処理をここに追加
    })
    .catch(error => console.error("装備購入エラー:", error));
}
