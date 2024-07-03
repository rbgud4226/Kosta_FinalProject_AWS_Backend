// 테이블의 체크박스를 통해 간트차트 표현 T/F
$(document).ready(function() {
    $('.chart_edit_btn').on('click', function () {
        var thisRow = $(this).closest('tr').find('td:eq(0)').text().trim();
        var checked = $(this).closest('tr').find('td:eq(6)').find('input[type="checkbox"]').prop('checked');
        console.log(thisRow);
        console.log(checked);

        $.ajax({
            url:"/auth/chart/checkbox",
            dataType:'json',
            method:'POST',
            data:{
                taskid : thisRow,
                charstatus : checked ? 'yes' : 'no'
            }
        });
    });

    $('.taskidset').on('click', function (){
        var thisRow = $(this).closest('tr').find('td:eq(0)').text().trim();
        console.log(thisRow);

        const taskid = document.getElementById("modal_taskid");
        const title = document.getElementById("modal_title");
        const resource = document.getElementById("modal_resource");
        const st = document.getElementById("modal_st");
        const ed = document.getElementById("modal_ed");
        const percent = document.getElementById("modal_percent");

        taskid.value = $(this).closest('tr').find('td:eq(0)').text().trim();
        title.value = $(this).closest('tr').find('td:eq(2)').text().trim();
        resource.value = $(this).closest('tr').find('td:eq(1)').text().trim();
        st.value = $(this).closest('tr').find('td:eq(3)').text().trim();
        ed.value = $(this).closest('tr').find('td:eq(4)').text().trim();
        percent.value = $(this).closest('tr').find('td:eq(5)').text().trim();

    });

    $('.taskidshare').on('click', function (){
        const taskid = document.getElementById("share_taskid");
        taskid.value = $(this).closest('tr').find('td:eq(0)').text().trim();
    });
});

const taskidinit=()=>{
    const taskid = document.getElementById("modal_taskid");
    const title = document.getElementById("modal_title");
    const resource = document.getElementById("modal_resource");
    const st = document.getElementById("modal_st");
    const ed = document.getElementById("modal_ed");
    const percent = document.getElementById("modal_percent");

    taskid.value=0;
    title.value=null;
    resource.value=null;
    st.value=null;
    ed.value=null;
    percent.value=null;
}