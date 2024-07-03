window.onload = function() {
	let checkId = document.querySelectorAll(".login_checkbox")[0];
    let loginId = document.getElementById("id");

    console.log(loginId)

    // 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 쿠키값 없으면 공백.
    var userLoginId = getCookie("userLoginId");
    loginId.value = userLoginId;
	
    // ID가 있는경우 아이디 저장 체크박스 체크
    if(loginId.value != ""){
    	checkId.checked = true;
    }
	
    checkId.onchange = function (event) {
        if(checkId.checked){ //checked true
            var userLoginId = loginId.value;
            setCookie("userLoginId", userLoginId, 30); // 30일 동안 쿠키 보관
        }else{ //checked false
        	deleteCookie("userLoginId");
        }
    };
    
    // // 아이디 저장하기가  눌린상태에서, ID를 입력한 경우
 
    loginId.addEventListener("keyup", function(e) {
        console.log("test")
    	if(checkId.checked){ //checked true
        	var userLoginId = loginId.value;
            setCookie("userLoginId", userLoginId, 30); // 30일 동안 쿠키 보관
        }
     })
}


function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}
 
function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}
 
function getCookie(cookieName) {
    cookieName = cookieName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cookieName);
    var cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}