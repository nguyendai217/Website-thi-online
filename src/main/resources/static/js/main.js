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
        $(this).toggleClass("show")

    })

    btnMenu.on('click', function (e) {
        menuMobile.toggleClass('show')
    })



});






