//출근기록 조회
const deptrecord = (num)=>{
	arrow_btn(num)
	$.ajax({
		url:"/auth/record/list",  //서버주소
		type:"get",   				//전송방식
		dataType:'json',			//응답데이터 형태
		data:{dept:dept,cnt:cnt},
		success:function(res){		//응답 정상일때
			deptTable_draw(res)
		},
		error:function(){			//응답 에러일때
			console.log('error');
		}
	});
}


let dept_table = $(".dept_record_list")[0];

const deptTable_draw = (arr)=>{
	console.log("dept_table: "+dept_table)
	while(dept_table.rows.length > 0){
		dept_table.deleteRow(0);
	}
  
	for(let a of arr){
			const tr_row = dept_table.insertRow();

	        cell = tr_row.insertCell(0);
	        cell.textContent = a.name
	        cell++

	        cell = tr_row.insertCell(1);
	        cell.textContent = a.deptNum;
	        cell++
	        
	        cell = tr_row.insertCell(2);
	        cell.textContent = a.joblv;
	        cell++
	        
	        cell = tr_row.insertCell(3);
	        cell.textContent = a.totalRecords;
          cell++

          cell = tr_row.insertCell(4);
	        cell.textContent = a.lateCount;
	        cell++

          cell = tr_row.insertCell(5);
	        cell.textContent = a.workTime;
          cell++

          cell = tr_row.insertCell(6);
	        cell.textContent = a.overWork;
	}
}