//테이블 그리기
let list_table;
// let list_table = $(".modal_body")[0];
$(document).ready(function (){
	list_table = $(".modal_body")[0];
});
const member_table = (arr)=>{
	while(list_table.rows.length > 0){
		list_table.deleteRow(0);
	}
	for(let a of arr){
			const tr_row = list_table.insertRow();
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
			let data = `<p class="f600 list_name">${a.userid.usernm }</p>
						<p class="f600 list_id">${a.userid.id }</p>
							<div class="list_pos">
								<span class="list_dept">${a.deptid.deptnm }</span>
								<span class="list_lv">${a.joblvid.joblvnm}</span>
							</div>`
			cell.innerHTML = data;
			cell.classList.add("form_td");
	        cell++
	        
	        cell = tr_row.insertCell(2);
			cell.classList.add("form_td");
			cell.textContent = a.email;
	}
}
const list_search = ()=>{
    let list_input = $(".list_input").val();
    let type = $(".select_box").val();

	console.log("input: "+list_input+ " /type: "+type)
    
    $.ajax({
		url:"/member/getdeptby",  //서버주소
		type:"get",   				//전송방식
		dataType:'json',			//응답데이터 형태
		data:{val:list_input,type:type},
		success:function(res){		//응답 정상일때
            console.log(res)
			member_table(res.mlist);
		},
		error:function(){			//응답 에러일때
			console.log('error');
		}
	});
}