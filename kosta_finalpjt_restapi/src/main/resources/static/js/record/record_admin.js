let less30 = 0;
let less1 = 0;
let less2 = 0;
let over2 =0

$(document).ready(function(){
  $.ajax({
      url: "/auth/record/over",
      type:"get",   
		  dataType:'json',
      success: function(res){
         less30 = res.overAvgTime[0].less30;
         less1 = res.overAvgTime[0].less1hour;
         less2 = res.overAvgTime[0].less2hours;
         over2 = res.overAvgTime[0].over2hours;
      },
      error:function(){			//응답 에러일때
        console.log('error');
      }
  });
  $.ajax({
    url: "/auth/record/deptlist",
    type:"get",   
    dataType:'json',
    success: function(res){
      for(let dept of res.deptlist){
         var option = $(`<option value="${dept.deptid}">${dept.deptnm}</option>`);
        $('#dept_list').append(option);  
      }
    },
    error:function(){			//응답 에러일때
      console.log('error');
    }
});
});

// 사내 전체 추가 근무 통계
function drawOverChart() {
    // Create the data table for Anthony's pizza.
    var data = new google.visualization.DataTable();
    data.addColumn('string', '횟수');
    data.addColumn('number', '번');
    data.addRows([
      ['30분 미만', less30],
      ['30분 이상 1시간 미만', less1],
      ['1시간 이상 2시간 미만', less2],
      ['2시간 초과', over2]
    ]);

    // Set options for Anthony's pie chart.
    var options = { width:700,
                   height:400};

    // Instantiate and draw the chart for Anthony's pizza.
    var chart = new google.visualization.BarChart(document.getElementById('over_chart_div'));
    chart.draw(data, options);
  }


//부서별 근무 시간 월별 비교 통계
function drawChart() {
    var chartDiv = document.getElementById('chart_div');
    var data = new google.visualization.DataTable();
    var months = []; 

    data.addColumn('string', 'Month');
    for(let dept in chartObj){
      if(months.length<chartObj[dept].length){
        for(let a = 0 ; a<chartObj[dept].length;a++){
          months.push(chartObj[dept][a].month)
        }

      }
      data.addColumn('number', dept);
    }

  months.forEach(function(month) {
    let rowData = [month];
      // 각 부서에 대한 데이터 추가
      for (let dept in chartObj) {
          var workhours = 0; 
          // 해당 월의 데이터가 있으면 값 추가
          var deptData = chartObj[dept];
          for (var i = 0; i < deptData.length; i++) {
              if (deptData[i].month === month) {
                  workhours = deptData[i].workhours;
                  break;
              }
          }
          rowData.push(workhours); // 해당 부서의 월별 근무 시간을 배열에 추가
      }
          data.addRow(rowData);
      });

    var materialOptions = {
      width: 700,
      height: 400,
      // colors: ['#a4d4ff', '#dc3912', '#ff9900'],
    };

    function drawMaterialChart() {
      var materialChart = new google.charts.Line(chartDiv);
      materialChart.draw(data, materialOptions);
    }
    drawMaterialChart();

  }


  
//부서 근태 조회(관리자용)
function dept_admin(num){
	let dept = $("select[name=dept] option:selected").val();
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

