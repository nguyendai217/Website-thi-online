$(document).ready(function () {
    // modal confirm
    $('.table #confirmDelete').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#removeModalCenter #delRef').attr('href', href);
        $('#removeModalCenter').modal();
    });

    $('#success').delay(5000).fadeOut(1000);
    $('#error').delay(5000).fadeOut(1000);
});

$(document).ready(function () {
    // modal confirm active
    $('.table #confirmActive').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#removeModalCenterActive #delRef').attr('href', href);
        $('#removeModalCenterActive').modal();
    });

    $('#success').delay(5000).fadeOut(1000);
    $('#error').delay(5000).fadeOut(1000);
});

$(document).ready(function () {
    // modal confirm active
    $('.table #confirmDisable').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#removeModalCenterDisable #delRef').attr('href', href);
        $('#removeModalCenterDisable').modal();
    });

    $('#success').delay(5000).fadeOut(1000);
    $('#error').delay(5000).fadeOut(1000);
});

$(document).ready(function () {
    $('#fileImage').change(function() {
        showPreview(this);
    });
});
function showPreview(fileImage){
    file = fileImage.files[0];
    render = new FileReader();

    render.onload = function(e) {
        $('#imgPreview').attr('src',e.target.result);
    }
    render.readAsDataURL(file);
}

// validate form

// add class
$('#btnAddClass').click(function (){
    var classCode= $('#classCode').val();
    var className= $('#className').val();
    if(classCode == null || classCode == "" || classCode == undefined){
        $('#vld-classCode').removeClass("hide");
        return false;
    }else {
        $('#vld-classCode').addClass("hide");
    }

    if(className == null || className == ""){
        $('#vld-className').removeClass("hide");
        return false
    }else {
        $('#vld-className').addClass("hide");
    }
    return true;
})

// validate add exam
$('#btnAddExam').click(function (){
    var title= $('#titleExam').val();
    var totalQuestion= $('#totalQuestionExam').val();
    var timeoutExam= $('#timeoutExam').val();
    var contentExam= $('#contentExam').val();
    var subjectId= $('#subjectId').val();
    var classId= $('#classId').val();
    if(title==''){
        $('#vld-titleExam').removeClass('hide');
        return false
    }else {
        $('#vld-titleExam').addClass('hide');
    }
    if(subjectId==""){
        $('#vld-subjectId').removeClass('hide');
        return false
    }else {
        $('#vld-subjectId').addClass('hide');
    }
    if(classId==""){
        $('#vld-classId').removeClass('hide');
        return false
    }else {
        $('#vld-classId').addClass('hide');
    }
    if(totalQuestion=='' || totalQuestion== null){
        $('#vld-totalQuestionExam').removeClass('hide');
        return false
    }else {
        $('#vld-totalQuestionExam').addClass('hide');
    }
    if(timeoutExam =='' || timeoutExam == null){
        $('#vld-timeoutExam').removeClass('hide');
        return false
    }else {
        $('#vld-timeoutExam').addClass('hide');
    }
    if(contentExam== '' || contentExam== null){
        $('#vld-contentExam').removeClass('hide');
        return false
    }else {
        $('#vld-contentExam').addClass('hide');
    }
    return true;
});