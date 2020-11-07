// $(document).ready(function (){
//
//     // $("#btn_reset").click(function (){
//     //     var email= $("#resetEmail").val();
//     //     $.ajax({
//     //         url: "./reset_password",
//     //         type: 'POST',
//     //         dataType: "text",
//     //         email: email,
//     //         success: function(response) {
//     //             if(response == "OK") {
//     //                 $("#success_reset").show();
//     //             } else {
//     //                 $("#error_reset").show();
//     //             }
//     //         },
//     //         error: function(XMLHttpRequest, textStatus, errorThrown) {
//     //             alert("Vui lòng nhập email để reset password !");
//     //         }
//     //     });
//     // })
//
//
//
// })
$(document).ready(function () {
    $('.banner-slider').slick({
        arrows: true,
        dots: true,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fas fa-chevron-right"></i></button>`,
    });

    $('.study-slider').slick({
        arrows: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fas fa-chevron-right"></i></button>`,
    });

    $('.study-slider--2').slick({
        arrows: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fas fa-chevron-right"></i></button>`,
    });

    $('.study-slider--3').slick({
        arrows: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fas fa-chevron-right"></i></button>`,
    });

    $('.test-slider').slick({
        arrows: true,
        slidesToShow: 5,
        slidesToScroll: 5,
        prevArrow: `<button type="button" class="prev-arrow"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow"><i class="fas fa-chevron-right"></i></button>`,
    });


    $('.feel-slider').slick({
        infinite: true,
        speed: 500,
        fade: true,
        cssEase: 'linear',
        prevArrow: `<button type="button" class="prev-arrow arrow--big"><i class="fas fa-chevron-left"></i></button>`,
        nextArrow: `<button type="button" class="next-arrow arrow--big"><i class="fas fa-chevron-right"></i></button>`,
    });
});





