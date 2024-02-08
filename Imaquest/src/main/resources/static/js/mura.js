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
			// 取得した詳細情報を表示する処理
			var equipmentDetailsElement = document.getElementById("equipmentDetailsContainer");
			equipmentDetailsElement.style.display = "block";
			document.getElementById("itemDetailsContainer").style.display = "none";  // アイテムの詳細を非表示にする
			document.getElementById("equipmentName").innerText = "装備名: " + equipmentName;
			document.getElementById("equipmentEffect").innerText = "効果: " + data.equipment_effect;
			document.getElementById("purchasePriceEquipment").innerText = "購入価格: " + data.purchase_price;
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
// 所持アイテム詳細非表示関数
function hidePlayerItemDetails() {
	var playerItemDetailsElement = document.getElementById("playeritemDetailsContainer");
	playerItemDetailsElement.style.display = "none";
}
// アイテム売却ボタンの非表示関数
function hideSellButtonitem() {
	var sellButtonElement = document.getElementById("sellButton");
	sellButtonElement.style.display = "none";
}
// 所持装備詳細非表示関数
function hidePlayerEquipmentDetails() {
	var playerEquipmentDetailsElement = document.getElementById("playerequipmentDetailsContainer");
	playerEquipmentDetailsElement.style.display = "none";
}
// 装備売却ボタンの非表示関数
function hideSellButtonEquipment() {
	var sellButtonEquipmentElement = document.getElementById("sellButtonEquipment");
	sellButtonEquipmentElement.style.display = "none";
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
        alert(data); // サーバーからのメッセージをアラートで表示
        // 必要であれば画面を更新する処理をここに追加
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
        alert(data); // サーバーからのメッセージをアラートで表示
        // 必要であれば画面を更新する処理をここに追加
    })
    .catch(error => console.error("装備購入エラー:", error));
}
// 所持アイテム一覧表示関数
function showPlayerItems() {
    // Ajaxリクエストを使用して所持アイテム一覧を取得
    fetch("/playerItemsDetails")
        .then(response => response.json())
        .then(data => {
			
            var playerItemListElement = document.getElementById("playerItemList");

        
            for (var i = 0; i < data.length; i++) {
                var playerItemElement = document.createElement("li");
                playerItemElement.innerText = "アイテム名: " + data[i].item_name + ", 売却価格: " + data[i].sell_price;

                // クリック時に詳細を表示するためのイベントリスナーを追加
                playerItemElement.addEventListener("click", function (event) {
                    var clickedItemName = event.target.innerText.split(",")[0].split(": ")[1];
                    showPlayerItemDetails(clickedItemName);
                });

                playerItemListElement.appendChild(playerItemElement);
            }
        })
        .catch(error => console.error("所持アイテム一覧の取得エラー:", error));
}


// 所持装備一覧表示関数

function showPlayerEquipment() {
	// Ajaxリクエストを使用して所持装備一覧を取得
	fetch("/playerEquipmentDetails")
		.then(response => response.json())
		.then(data => {
			var playerEquipmentListElement = document.getElementById("playerEquipmentList");

			playerEquipmentListElement.innerHTML = ""; // 一覧をクリア
			for (var i = 0; i < data.length; i++) {
				var playerEquipmentElement = document.createElement("li");
				playerEquipmentElement.innerText = "装備名: " + data[i].equipment_name + ", 売却価格: " + data[i].sell_price;

				// クリック時に詳細を表示するためのイベントリスナーを追加
				playerEquipmentElement.addEventListener("click", function(event) {
					var clickedEquipmentName = event.target.innerText.split(",")[0].split(": ")[1];
					showPlayerEquipmentDetails(clickedEquipmentName);
				});

				playerEquipmentListElement.appendChild(playerEquipmentElement);
			}
		})
		.catch(error => console.error("所持装備一覧の取得エラー:", error));
}

// 所持アイテム詳細表示関数
function showPlayerItemDetails(clickedItemName) {
	hidePlayerEquipmentDetails(); // 所持装備詳細を非表示にする
    // Ajaxリクエストを使用して所持アイテム詳細を取得
    fetch("/playerItemsDetails")
        .then(response => response.json())
        .then(data => {
            var playerItemDetailsElement = document.getElementById("playeritemDetailsContainer");
            playerItemDetailsElement.style.display = "block";

            // アイテムの詳細を検索
            const selectedItem = data.find(item => item.item_name === clickedItemName);

            if (selectedItem) {
                document.getElementById("playeritemName").innerText = "アイテム名: " + selectedItem.item_name;
                
                // 所持数を整数型に変換
                const itemQuantity = parseInt(selectedItem.item_quantity, 10);
                document.getElementById("playeritemQuantity").innerText = "所持数: " + itemQuantity;
                
                document.getElementById("sellPrice").innerText = "売却価格: " + selectedItem.sell_price;
            } else {
                console.error("該当するアイテムが見つかりませんでした");
            }
        })
        .catch(error => console.error("所持アイテム詳細の取得エラー:", error));
}


/// アイテム売却関数
function sellItem() {
    var itemName = document.getElementById("playeritemName").innerText.split(": ")[1];
    
    // クエリセレクタでquantity要素を取得
    var quantityElement = document.getElementById("playerItemQuantity").value;

    // Ajaxリクエストを使用してアイテム売却処理を実行
    fetch("/sellItem", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "itemName=" + itemName + "&quantity=" + quantityElement,
    })
    .then(response => response.text())
    .then(data => {
        alert(data); // サーバーからのメッセージをアラートで表示
        console.log(data);
        // 必要であれば画面を更新する処理をここに追加
    })
    .catch(error => console.error("アイテム売却エラー:", error));
}

// 所持装備詳細表示関数

function showPlayerEquipmentDetails(clickedEquipmentName) {
	hidePlayerItemDetails(); // 所持アイテム詳細を非表示にする
	// Ajaxリクエストを使用して所持装備詳細を取得
	fetch("/playerEquipmentDetails")
		.then(response => response.json())
		.then(data => {
			var playerEquipmentDetailsElement = document.getElementById("playerequipmentDetailsContainer");
			playerEquipmentDetailsElement.style.display = "block";

			// 装備の詳細を検索
			const selectedEquipment = data.find(equipment => equipment.equipment_name === clickedEquipmentName);

			if (selectedEquipment) {
				document.getElementById("playerequipmentName").innerText = "装備名: " + selectedEquipment.equipment_name;
				document.getElementById("playerequipmentQuantity").innerText = "所持数: " + selectedEquipment.equipment_quantity;
				document.getElementById("sellPriceEquipment").innerText = "売却価格: " + selectedEquipment.sell_price;
			} else {
				console.error("該当する装備が見つかりませんでした");
			}
		})
		.catch(error => console.error("所持装備詳細の取得エラー:", error));
}

// 装備売却関数

function sellEquipment() {
	var equipmentName = document.getElementById("playerequipmentName").innerText.split(": ")[1];

	// クエリセレクタでquantity要素を取得
	var quantityElement = document.getElementById("playerEquipmentQuantity").value;

	// Ajaxリクエストを使用して装備売却処理を実行
	fetch("/sellEquipment", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
		},
		body: "equipmentName=" + equipmentName + "&quantity=" + quantityElement,
	})
		.then(response => response.text())
		.then(data => {
			alert(data); // サーバーからのメッセージをアラートで表示
			console.log(data);
			// 必要であれば画面を更新する処理をここに追加
		})
		.catch(error => console.error("装備売却エラー:", error));
}
// フィールドに戻るボタンのクリックイベント
function goBack() {
    // フィールドに遷移する処理を追加
    // 例えば、フィールドのURLが "/field" だと仮定しています
    window.location.href = "/field";
}

