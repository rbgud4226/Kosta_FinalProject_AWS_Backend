let list_table = $(".modal_body");

const member_table = (arr, num) => {
	while (list_table[num].rows.length > 0) {
		list_table[num].deleteRow(0);
	}
	for (let a of arr) {
		const tr_row = list_table[num].insertRow();
		tr_row.classList.add("list_line")

		cell = tr_row.insertCell(0);
		var input = document.createElement("input");
		input.type = "checkbox";
		input.name = "userid";
		input.value = a.userid.id;
		input.classList.add("list_ch")
		cell.appendChild(input);
		cell++
		cell = tr_row.insertCell(1);
		cell.class = "form_td";
		let data = `<p class="f600 list_name">${a.userid.usernm}</p>
							<div class="list_pos">
								<span class="list_dept">${a.deptid.deptid}</span>
								<span class="list_lv">${a.joblvid.joblvid}</span>
							</div>`
		cell.innerHTML = data;
		cell.classList.add("form_td");
		cell++

		cell = tr_row.insertCell(2);
		cell.classList.add("form_td");
		cell.textContent = a.email;
	}
}
const list_search_chat = (num, roomId) => {
	let list_input;
	let type;

	if (num === '0') {
		list_input = $('#exampleModal .list_input').val();
		type = $('#exampleModal .select_box').val();
	} else if (num === '1') {
		list_input = $('#exampleModal2 .list_input').val();
		type = $('#exampleModal2 .select_box').val();
	}

	console.log("input: " + list_input + " /type: " + type)
	console.log("input: " + list_input + " /type: " + type + " /roomId: " + roomId);
	$.ajax({
		url: "/member/getdeptby",  //서버주소
		type: "get",   				//전송방식
		dataType: 'json',			//응답데이터 형태
		data: { val: list_input, type: type, chatroomid: roomId },
		success: function(res) {		//응답 정상일때
			console.log(res)
			member_table(res.mlist, num);
		},
		error: function() {			//응답 에러일때
			console.log('error');
		}
	});
}
