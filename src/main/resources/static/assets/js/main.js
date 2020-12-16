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

// validate password profile
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

// validate form add subject
$('#btnAddSubject').click(function (){
    var nameSubject= $('#nameSubject').val();
    var codeSubject= $('#code-Subject').val();

    if(codeSubject== null || codeSubject == ""){
        $('#vld-code-Subject').removeClass('hide');
        return false;
    }else {
        $('#vld-code-Subject').addClass('hide');
    }

    if(nameSubject== null || nameSubject == ""){
        $('#vld-nameSubject').removeClass('hide');
        return false;
    }else {
        $('#vld-nameSubject').addClass('hide');
    }
    return true;
});
// add class
$('#btnAddClass').click(function (){
    var classCode= $('#classCode').val();
    var className= $('#className').val();
    var schoolId= $('#schoolId').val();

    if(classCode == null || classCode == "" || classCode == undefined){
        $('#vld-classCode').removeClass("hide");
        $('#classCode').focus();
        return false;
    }else {
        $('#vld-classCode').addClass("hide");
    }
    if(className == null || className == ""){
        $('#vld-className').removeClass("hide");
        $('#className').focus();
        return false
    }else {
        $('#vld-className').addClass("hide");
    }
    if(schoolId=="" || schoolId== null){
        $('#vld-schoolId').removeClass("hide");
        $('#schoolId').focus();
        return false;
    }else {
        $('#vld-schoolId').addClass("hide");
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
        $('#titleExam').focus();
        return false
    }else {
        $('#vld-titleExam').addClass('hide');
    }
    if(subjectId==""){
        $('#vld-subjectId').removeClass('hide');
        $('#subjectId').focus();
        return false
    }else {
        $('#vld-subjectId').addClass('hide');
    }
    if(classId==""){
        $('#vld-classId').removeClass('hide');
        $('#classId').focus();
        return false
    }else {
        $('#vld-classId').addClass('hide');
    }
    if(totalQuestion=='' || totalQuestion== null){
        $('#vld-totalQuestionExam').removeClass('hide');
        $('#totalQuestionExam').focus();
        return false
    }else {
        $('#vld-totalQuestionExam').addClass('hide');
    }
    if(timeoutExam =='' || timeoutExam == null){
        $('#vld-timeoutExam').removeClass('hide');
        $('#timeoutExam').focus();
        return false
    }else {
        $('#vld-timeoutExam').addClass('hide');
    }
    if(contentExam== '' || contentExam== null){
        $('#vld-contentExam').removeClass('hide');
        $('#contentExam').focus();
        return false
    }else {
        $('#vld-contentExam').addClass('hide');
    }
    return true;
});

// validate add news
$('#btnAddNews').click(function (){
    var title= $('#titleNews').val();
    var des= $('#descriptionNews').val();
    var category= $('#categoryId').val();
    if(title== "" || title== null){
        $("#vld-titleNews").removeClass('hide');
        $('#titleNews').focus();
        return false;
    }else {
        $("#vld-titleNews").addClass('hide');
    }

    if(des== "" || des== null){
        $("#vld-descriptionNews").removeClass('hide');
        $('#descriptionNews').focus();
        return false;
    }else {
        $("#vld-descriptionNews").addClass('hide');
    }

    if(category== "" || category== null){
        $("#vld-categoryId").removeClass('hide');
        $('#categoryId').focus();
        return false;
    }else {
        $("#vld-categoryId").addClass('hide');
    }
    return true;
});

$('#btnAddUser').click(function (){
    var email= $('#emailUser').val();
    var username= $('#usernameUser').val();
    var pass= $('#passwordUser').val();
    var role= $('#roleUser').val();
    var phoneUser= $('#phoneUser').val();
    function isPhoneNumber(phone) {
        var regex= /^0[0-9]{8}$/;
        if(regex.test(phone)){
            return true;
        }else {
            return false;
        }
    }
    if(username==""){
        $('#vld-username').removeClass('hide');
        $('#usernameUser').focus();
        return false;
    }else {
        $('#vld-username').addClass('hide');
    }

    var regEmail=/\S+@\S+\.\S+/;
    if(email=='' || regEmail.test(email)== false){
        $('#vld-email').removeClass('hide');
        $('#emailUser').focus();
        return false;
    }else {
        $('#vld-email').addClass('hide');
    }

    if(phoneUser != ""){
        var rgx= /((09|03|07|08|05)+([0-9]{8})\b)/g;
         if(phoneUser.length != 10 || rgx.test(phoneUser)== false){
             $('#vld-phone').removeClass('hide');
             $('#phoneUser').focus();
             return false;
         }else{
             $('#vld-phone').addClass('hide');
         }
    }else {
        $('#vld-phone').addClass('hide');
    }


    if(pass==""){
        $('#vld-pass').removeClass('hide');
        $('#passwordUser').focus();
        return false;
    }else {
        $('#vld-pass').addClass('hide');
    }

    if(role==""){
        $('#vld-roleUser').removeClass('hide');
        $('#roleUser').focus();
        return false;
    }else {
        $('#vld-roleUser').addClass('hide');
    }
})