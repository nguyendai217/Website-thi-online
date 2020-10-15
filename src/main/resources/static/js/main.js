$(document).ready(function (){

    $("#btn_reset").click(function (){
        var email= $("#resetEmail").val();
        $.ajax({
            url: "./reset_password",
            type: 'POST',
            dataType: "text",
            email: email,
            success: function(response) {
                if(response == "OK") {
                    $("#success_reset").show();
                } else {
                    $("#error_reset").show();
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("Vui lòng nhập email để reset password !");
            }
        });
    })
})

