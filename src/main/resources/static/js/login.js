document.addEventListener("DOMContentLoaded", function() {
    function toggleResetPswd(e){
        e.preventDefault();
        $('#logreg-forms .form-signin').toggle() // display:block or none
        $('#logreg-forms .form-reset').toggle() // display:block or none
    }

    function toggleSignUp(e){
        e.preventDefault();
        $('#logreg-forms .form-signin').toggle(); // display:block or none
        $('#logreg-forms .form-signup').toggle(); // display:block or none
    }

    $(()=>{
        // Login Register Form
        $('#logreg-forms #forgot_pswd').click(toggleResetPswd);
        $('#logreg-forms #cancel_reset').click(toggleResetPswd);
        $('#logreg-forms #btn-signup').click(toggleSignUp);
        $('#logreg-forms #cancel_signup').click(toggleSignUp);
    })

    $('#btnSubmitRegister').click(function (){
        var pass= $('#user-pass').val();
        var phone= $('#phoneRegister').val();
        if(pass.length < 5){
            $('#vld-passRegister').removeClass('hide');
            return false;
        }else {
            $('#vld-passRegister').addClass('hide');
        }
        var rgx= /((09|03|07|08|05)+([0-9]{8})\b)/g;
        if(phone.length != 10 || rgx.test(phone)== false){
            $('#vld-phoneRegister').removeClass('hide');
            return false;
        }else {
            $('#vld-phoneRegister').addClass('hide');
        }
        return true;
    })

});