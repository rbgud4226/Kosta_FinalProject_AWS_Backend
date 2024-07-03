document.addEventListener('DOMContentLoaded', function() {
    $.ajax({
        url : "/auth/chart/cdata",
        dataType : "json",
        method : "get",
        success:function (list){
            console.log(list);
            var calendarEl = document.getElementById('calendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                headerToolbar:{
                    left: 'prev',
                    center: 'title',
                    right: 'next'
                },
                selectable : true,
                selectMirror : true,
                navLinks : true,
                titleFormat: function (date) {
                    year = date.date.year;
                    month = date.date.month + 1;
                    return year + "년 " + month + "월";
                },
                dayMaxEvents: true, // allow "more" link when too many events
                // 이벤트 객체 필드 document : https://fullcalendar.io/docs/event-object
                events: list
            });

            calendar.render();
        }
    });
});