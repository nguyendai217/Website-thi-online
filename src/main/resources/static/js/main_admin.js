$(document).ready(function () {
    var treeviewMenu = $('.app-menu');
// Toggle Sidebar
    $('[data-toggle="sidebar"]').click(function (event) {
        event.preventDefault();
        $('.app').toggleClass('sidenav-toggled');
    });

// Activate sidebar treeview toggle
    $("[data-toggle='treeview']").click(function (event) {
        event.preventDefault();
        if (!$(this).parent().hasClass('is-expanded')) {
            treeviewMenu.find("[data-toggle='treeview']").parent().removeClass('is-expanded');
        }
        $(this).parent().toggleClass('is-expanded');
    });

// Set initial active toggle
    $("[data-toggle='treeview.'].is-expanded").parent().toggle('is-expanded');

//Activate bootstrip tooltips
//    $("[data-toggle='tooltip']").tooltip();


    $('#success').delay(5000).fadeOut(1000);
    $('#error').delay(5000).fadeOut(1000);

    $('#datepicker').datepicker({
        iconsLibrary: 'fontawesome',
        uiLibrary: 'bootstrap4',
        format: 'dd-mm-yyyy'
    });
});