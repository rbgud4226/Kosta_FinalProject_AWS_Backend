let cnt = 0;
//시계 변경
let time = new Date(); 
// 날짜
let year = time.getFullYear();
let month = ('0' + (time.getMonth() + 1)).slice(-2);
let day = ('0' + time.getDate()).slice(-2);
let dateString = year + '.' + month  + '.' + day + "일 ";
// 시간
var hours = ('0' + time.getHours()).slice(-2); 
var minutes = ('0' + time.getMinutes()).slice(-2);

var timeString = hours + ':' + minutes ;
$("#record_time").html(dateString+timeString);
$(".month").html(year+"."+(month-1));

let table = $(".record_list")[0];


//테이블 그리기
const table_draw = (arr)=>{	
	while(table.rows.length > 0){
		table.deleteRow(0);
	}
	for(let a of arr){
			const tr_row = table.insertRow();

	        cell = tr_row.insertCell(0);
	        cell.textContent = a.day
	        cell++

	        cell = tr_row.insertCell(1);
	        cell.textContent = a.dayOfWeek;
	        cell++
	        
	        cell = tr_row.insertCell(2);
	        cell.textContent = a.workHours;
	        cell++
	        
	        cell = tr_row.insertCell(3);
	        cell.textContent = a.state;
	}
}

//달력 이동
const arrow_btn = (num)=>{
	cnt = cnt + num;
   let cmonth = month;
   if(-3<cnt && cnt<0){
	   $("#record_right").addClass("cursor").removeClass("arrow_off");
	   $("#record_left").addClass("cursor").removeClass("arrow_off");
   }
   else if(cnt<=-3){
	   cnt = -3;
	   $("#record_left").addClass("arrow_off").removeClass("cursor")
   }else if (cnt>=0){
	   cnt = 0;
	   $("#record_right").addClass("arrow_off").removeClass("cursor")
   }

   cmonth = month - 1 + cnt;
   if(cmonth<1){
	   cmonth = 12;
	   year--;
   }else if(cmonth>12){
	   cmonth = 1;
	   year++;
   }

   $(".month").html(year+"."+cmonth);
}