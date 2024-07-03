$(document).ready(function(){
    $.ajax({
        url: "/auth/docx/mainList",
        type:"get",   
        dataType:'json',
        success: function(res){
            console.log(res)
            table_draw(res.list)
        },
        error:function(){			//응답 에러일때
          console.log('error');
        }
    });

    let table = $(".main_docx_list")[0];
    //테이블 그리기
    const table_draw = (arr)=>{	
        while(table.rows.length > 0){
            table.deleteRow(0);
        }
        for(let a of arr){
                const tr_row = table.insertRow();
                tr_row.style=`
                        line-height: 3rem;
                        border-bottom: 2px solid #E2E4EC;
                `
                cell = tr_row.insertCell(0);
                cell.textContent = a.formnum
                cell++
    
                cell = tr_row.insertCell(1);
                cell.innerHTML = `<a href='/auth/docx/getdocx?formnum=${a.formnum}&docxkey=${a.docxkey}&formtype=${a.formtype}'}>${a.title}</a>`
 
                cell++
                
                cell = tr_row.insertCell(2);
                cell.textContent = a.writer.usernm;
                cell
        }
    }
    

})



