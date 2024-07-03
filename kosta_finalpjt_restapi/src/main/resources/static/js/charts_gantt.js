$(document).ready(function () {
    google.charts.load('current', {'packages': ['gantt']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var loginId = document.getElementById(usersid);

        $.ajax({
            url: "/auth/chart/data",
            dataType: 'json',
            method: 'GET',
            data: {
                loginId: loginId
            }
        }).done(function (list) {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Task ID');
            data.addColumn('string', 'Task Name');
            data.addColumn('string', 'resource');
            data.addColumn('date', 'Start Date');
            data.addColumn('date', 'End Date');
            data.addColumn('number', 'Duration');
            data.addColumn('number', 'Percent Complete');
            data.addColumn('string', 'Dependencies');

            list.forEach(function (row) {
                console.log(row);
                var startDate = new Date(row.st);
                var endDate = new Date(row.ed);
                var id = '"' + row.taskid + '"';
                data.addRow([
                    id,
                    row.title,
                    row.chartResource,
                    startDate,
                    endDate,
                    null,
                    row.percent,
                    null
                ]);

                var numberOfRows = data.getNumberOfRows(); // 행의 개수를 얻습니다.
                var chartHeight = numberOfRows * 30 + 150; // 각 행의 높이를 기준으로 차트의 높이 계산 (예: 각 행의 높이가 30)

                var options = {
                    height : chartHeight,
                    gantt: {
                        criticalPathEnabled: false,
                    },
                    tooltip: {
                        isHtml: true
                    },
                    palette:[
                        { color: 'black', dark: 'black', light: 'black' }, // Task ID 색상을 검은색으로 지정
                    ]
                };

                var chart = new google.visualization.Gantt(document.getElementById('chart_div'));

                chart.draw(data, options);
            });
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Error : " + textStatus, errorThrown);
        });
    }
});