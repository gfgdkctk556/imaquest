function showItemDetails(clickedRow) {
    var itemName = clickedRow.cells[0].innerText;
    var itemQuantity = clickedRow.cells[2].innerText;
    var itemEffect = clickedRow.cells[4].innerText;
    var itemType = clickedRow.getAttribute("data-type");

    var itemDetailsContainer = document.getElementById("itemDetails");

    // アイテムの詳細情報を作成
    var itemDetailsHTML = "<h4>アイテム詳細</h4>";
    itemDetailsHTML += "<p>アイテム名: " + itemName + "</p>";
    itemDetailsHTML += "<p>所持数: " + itemQuantity + "</p>";
    itemDetailsHTML += "<p>効果: " + itemEffect + "</p>";

   

    itemDetailsHTML += "<button onclick=\"useItem('" + itemName + "',' " + itemType + "')\">アイテムを使用する</button>";

    // 詳細情報を表示
    itemDetailsContainer.innerHTML = itemDetailsHTML;
}


// アイテム使用関数
function useItem(itemName, itemType, itemEffect, itemQuantity) {
    // itemTypeに基づいてアイテムの効果を実行
   

    // Ajax通信
    $.ajax({
        type: "POST",
        url: "/useItem",
        data: {
			
            itemName: itemName,
            itemType: itemType,
            itemEffect: itemEffect,
            itemQuantity: itemQuantity,
           	itemType: itemType,
           	
        },
success: function(response) {
    // 成功時の処理 タイプのを判定
	if (itemType == 1 ) {
		
		alert("HPを回復したぞ！");
		// /menuを再読み込みする
		location.reload();
		
	} else if (itemType == 2 ) {
		
		alert("MPを回復したぞ！");
		// /menuを再読み込みする
		location.reload();
		if(a==1){
			alert("過剰だぞ！");
			location.reload();
		}
	}else if (itemType == 3 ) {
		alert("これは使いどきがあるぞ！");
		location.reload();
		
    }else if (itemType == 99) {

		 alert("ゆうたのなみだを使ったぞ");
		   // /menuを再読み込みする
		   location.reload();
	} 
	   
},
        error: function(error) {
                

            alert("満タンだぞ");
        }
    });
}

function showEquipmentDetails(clickedRow) {
    var equipmentName = clickedRow.cells[0].innerText;
    var equipmentQuantity = clickedRow.cells[2].innerText;
    var equipmentEffect = clickedRow.cells[4].innerText;
    var equipmentType = clickedRow.getAttribute("data-type");


// 装備の詳細情報を作成
   var equipmentDetailsHTML = "<h4>装備詳細</h4>";
    equipmentDetailsHTML += "<p>装備名: " + equipmentName + "</p>";
    equipmentDetailsHTML += "<p>所持数: " + equipmentQuantity + "</p>";
    equipmentDetailsHTML += "<p>効果: " + equipmentEffect + "</p>";
   
   
	equipmentDetailsHTML += "<button onclick=\"useEquipment('" + equipmentName + "',' " + equipmentType + "')\">装備する</button>";

	// 詳細情報を表示
	var equipmentDetailsContainer = document.getElementById("equipmentDetails");
	equipmentDetailsContainer.innerHTML = equipmentDetailsHTML;
       
}
// 装備使用関数

// 装備使用関数
function useEquipment(equipmentName, equipmentType, equipmentEffect, equipmentQuantity) {
    // Ajax通信
    $.ajax({
        type: "POST",
        url: "/equipItem",
        data: {
            equipmentName: equipmentName,
            equipmentType: equipmentType,
            equipmentEffect: equipmentEffect,
            equipmentQuantity: equipmentQuantity
            
        },
		success: function(response) {
			// 成功時の処理
			alert("装備しました。");
			// /menuを再読み込みする
			location.reload();
		},
		error: function(error) {
			// エラー時の処理
			console.error(error);
			alert("エラーが発生しました。");
			}
    });
}


// 装備を外すボタンの要素を取得
const removeEquipmentButton = document.getElementById('removeEquipmentButton');

// 装備を外すボタンにクリックイベントを追加
removeEquipmentButton.addEventListener('click', function() {
    // 装備を外す処理を実行
    removeEquipment();
});

// 装備を外す関数
function removeEquipment() {
    // 装備名を取得
    var equipmentName = "装備名"; // ここに装備名を取得するコードを追加

    // Ajaxリクエストを送信
    $.ajax({
        type: "POST",
        url: "/removeEquipmentBtn", // Spring Bootコントローラーのエンドポイント
        data: {
            equipmentName: equipmentName // 装備名を送信
        },
        success: function(response) {
            // 成功時の処理
            alert("装備を外しました");
            // menu画面を再読み込みするなどの処理を追加
            location.reload(); // 例: ページを再読み込み
        },
        error: function(error) {
            // エラー時の処理
            console.error(error);
            alert("エラーが発生しました。");
        }
    });
}

	


//フィールドに戻る処理
 function returnToLogin() {
				// フィールド画面に遷移する処理を追加
				window.location.href = "/field";
}