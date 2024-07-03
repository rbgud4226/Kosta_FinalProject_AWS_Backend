$(document).ready(function (){

    $('.mail_btn').on('click', function (){
        var tit = document.getElementsByName("title");
        var con = document.getElementsByName("content");
        var rf = document.getElementsByName("ref");
        var rr = document.getElementsByName("receiver");

       $.ajax({
           url:"/mail/sendmail",
           dataType:'json',
           method:'post',
           data:{
               title : tit,
               content : con,
               ref : rf,
               receiver : rr
           },
           success: function (response) {
               // 성공 시 처리할 내용
               console.log("Mail sent successfully:", response);
           },
           error: function (error) {
               // 에러 시 처리할 내용
               console.error("Error sending mail:", error);
           }
       }) ;
    });
});