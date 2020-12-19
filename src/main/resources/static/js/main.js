// $(document).ready(function (){
//
//     $("#btn_reset").click(function (){
//         var email= $("#resetEmail").val();
//         $.ajax({
//             url: "./reset_password",
//             type: 'POST',
//             dataType: "text",
//             email: email,
//             success: function(response) {
//                 if(response == "OK") {
//                     $("#success_reset").show();
//                 } else {
//                     $("#error_reset").show();
//                 }
//             },
//             error: function(XMLHttpRequest, textStatus, errorThrown) {
//                 alert("Vui lòng nhập email để reset password !");
//             }
//         });
//     })
// })

var acc =$('#dropdownMenuButton').val();
if(acc != null || acc != undefined){
    $('#div-account').removeClass('d-none');
    $('#link-login').addClass('d-none');
}else {
    $('#div-account').addClass('d-none');
    $('#link-login').removeClass('d-none');
}

$(document).ready(function () {
    // var logout = $('#msg-logout').val();
    // if (logout != null) {
    //     alert("Đăng xuất thành công.");
    // }

    $('#btnSendContact').click(function (){
        var name = $('#form-name').val();
        var phone= $('#form-phone').val();
        var email= $('#form-email').val();
        var mess= $('#form-message').val();

        if(name==""){
            $('#err-form-name').removeClass("hide");
            $('#form-name').focus();
            return false;
        }else {
            $('#err-form-name').addClass("hide");
        }

        if(email==""){
            $('#err-form-email').removeClass("hide");
            $('#form-email').focus();
            return false;
        }else {
            $('#err-form-email').addClass("hide");
        }

        if(phone==""){
            $('#err-form-phone').removeClass("hide");
            $('#form-phone').focus();
            return false;
        }else {
            $('#err-form-phone').addClass("hide");
        }

        if(mess==""){
            $('#err-form-message').removeClass("hide");
            $('#form-message').focus();
            return false;
        }else {
            $('#err-form-message').addClass("hide");
        }
        return true;
    });

// update image profile user
    $("#btnUpdateImage").click(function (){
        var image= $('#fileImage').val();
        if(image=="" || image == null){
            $('#exampleModal').modal('show');
            return false;
        }else {
            return true;
        }
    });

//validate form change pass
    $("#btnSubmit").click(function (){
        var pass =$('#pass').val()
        var pass1= $("#pass1").val();
        var pass2= $("#pass2").val();
        if(pass== null || pass == ''){
            $('#err-pass').removeClass("hide");
            return false;
        }else {
            $('#err-pass').addClass("hide");
        }
        if(pass1== null|| pass1==""){
            $('#err-pass-new').removeClass("hide");
            return false;
        }else {
            $('#err-pass-new').addClass("hide");
        }
        if(pass1 != pass2){
            $('#err').removeClass("hide");
            return false;
        }
        return true;
    })
})

$(document).ready(function () {
    const btnMenu = $('.btn-showMenu');
    const menuMobile = $('.lg-menu');
    const subMenu = $('.lg-menu ul li ul');
    const menuItem = $('.lg-menu>ul>li ');

    $('a[data-toggle="pill"]').on('shown.bs.tab', function (e) {
        $('.study-slider').slick('setPosition');
    })

    $('.banner-slider').slick({
        arrows: true,
        dots: true,
        autoplay: true,
        autoplaySpeed: 5000,
        prevArrow: `<button type="button" class="prev-arrow arrow--big"><i class="fa fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow arrow--big"><i class="fa fa-chevron-right"></i></button>`,

    });

    $('.study-slider').slick({
        arrows: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        prevArrow: `<button type="button" class="prev-arrow "><i class="fa fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fa fa-chevron-right"></i></button>`,
    });


    $('.test-slider').slick({
        arrows: true,
        slidesToShow: 5,
        slidesToScroll: 5,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fa fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fa fa-chevron-right"></i></button>`,
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3,
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3
                },
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 2
                }
            }
        ]
    });

    $('.feel-slider').slick({
        infinite: true,
        speed: 500,
        dots: true,
        autoplay: true,
        autoplaySpeed: 5000,
        cssEase: 'linear',
        prevArrow: `<button type="button" class="prev-arrow arrow--big"><i class="fa fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow arrow--big"><i class="fa fa-chevron-right"></i></button>`,
    });

    menuItem.attr("hasSubMenu", "true").click(function (e) {
        $(this).toggleClass("show",500)

    })

    btnMenu.on('click', function (e) {
        menuMobile.toggleClass('show',500)
    })

    $('#fileImage').change(function() {
        showPreview(this);
    });

    $('#success').delay(5000).fadeOut(1000);
    $('#error').delay(5000).fadeOut(1000);
});


function showPreview(fileImage){
    file = fileImage.files[0];
    render = new FileReader();

    render.onload = function(e) {
        $('#imgPreview').attr('src',e.target.result);
    }
    render.readAsDataURL(file);
}





// validate form contact //










