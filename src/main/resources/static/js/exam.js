// Khởi tạo dữ liệu
var id_test = test_id;
var currentPage = 1;
var limitPage = 5;
var checkUpdateApi = true;
var finishTest = false;

// json câu hỏi
const json = _dataJson;
var listQuestion =_dataJson;
console.log("listQuestion: ",listQuestion)
var listAnser = localStorage.getItem("test-" + id_test);
listAnser = listAnser == null ? { ...listQuestion } : JSON.parse(listAnser);

res = Object.keys(listAnser).map((k) => listAnser[k]);
listAnser = res;

/* Khởi tạo Thời gian */
var limitTime = timeOut; // Thời gian làm bài ( phút )
var startTime = localStorage.getItem("test-" + id_test + "-time"); // Thời gian bắt đầu làm bài
if (startTime === null) { // Nếu localstorage không có thì sẽ khởi tạo
    startTime = new Date();
    localStorage.setItem(("test-" + id_test + "-time"), startTime.getTime())
} else {
    startTime = new Date(parseInt(startTime));
}

var endTime = new Date(startTime.getTime());
endTime = endTime.setMinutes(endTime.getMinutes() + limitTime);
// endTime = endTime.setSeconds(endTime.getSeconds() + 35);
if (checkTestFinished(endTime) === false) {
    finishTest = true;
    renderListQuestion(listAnser);
} else {
    renderListQuestion(listAnser);
    var _countDown = setInterval(() => {
        let funcCountDown = countDownTime(endTime);
        if (funcCountDown === false) {
            clearInterval(_countDown);
            return;
        }
    }, 1000);

    // trả lời câu hỏi
    $(".list-question").on("change", ".radio-check", function(e) {
		e.preventDefault();
        checkUpdateApi = true;
        let _this = $(this);
        let _quest = _this.parents('.item-question');
        let keyQuest = _quest.attr('data-key');
        listAnser[keyQuest].yourAns = _this.val();
        localStorage.setItem("test-" + id_test, JSON.stringify(listAnser));
    });

    // Pre câu hỏi
    $('.btn-pre').click(function (e) {
		e.preventDefault();
        $("html, body").animate({ scrollTop: 180 }, 600);
        if (currentPage <= 1) return;
        currentPage--;
        renderListQuestion(listAnser);
        checkUpdateApi = false;
    });

    // Next câu hỏi
    $('.btn-next').click(function (e) {
		e.preventDefault();
        $("html, body").animate({ scrollTop: 180 }, 600);
        if (currentPage >= Math.ceil(json.length / limitPage)) return;
        currentPage++;
        renderListQuestion(listAnser);
        checkUpdateApi = false;
    });

    $(document).ready(function (){
    $('.btn-finish').click(function (e) {
		e.preventDefault();
        let arrRes = [];
        
        for (let i = 0; i < json.length; i++) {
            let data = $(`input[name="test[${i}]"]:checked`).val();
            arrRes.push((typeof data === "undefined" ? null : data));
        }

        let confirmFinish = confirm('Bạn chắc chắn muốn kết thúc bài thi?');
        if (confirmFinish === false) return;

        var token = $('#csrfToken').val();
		var header = $('#csrfHeader').val();

		console.log("json: ", json);
        $.ajax({
            type: "POST",
			headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
            url: "./kiemtra-process-result",
            data: {
                dataJson: JSON.stringify(arrRes),
                examId: exam_id,
                testId: id_test,
            },
            dataType: "json",
            success: function (response) {
                console.log("response: ",response)
                window.location.href = './history?testId=' + id_test;
            },
        });
        clearInterval(_countDown);
        $(this).hide();
        $("#hours").html("00");
        $("#minutes").html("00");
        $("#seconds").html("00");
        localStorage.removeItem("test-" + id_test + "-time")
        localStorage.removeItem("test-" + id_test);
    });

    });
}

function renderListQuestion(listAnser) {
    let stringListQuest = "";
    console.log(finishTest);
    let disabledAnswer = finishTest == true ? 'disabled' : '';
    listAnser.forEach((v, k) => {
        let showQuestion = 'hide';
        if (((currentPage - 1) * limitPage) <= k && ((currentPage) * limitPage) > k ) {
            showQuestion = '';
        }

        stringListQuest += `<div class='item-question ${showQuestion}' data-key="${k}"><div class='title'><b><span class='question-no'>${k + 1}</span>${v.content}</b></div><div class='content'>`;
        stringListQuest += `<div class='content-ans' style='display:none'><input ${v.yourAns !== null ? 'checked' : ''}  type='radio' class='radio-check' exam-id='${id_test}' name='test[${k}]' id='' value='0' title=''></div>`;
        v.listAns.forEach((va, ka) => {
            let _checked = (v.yourAns == (ka + 1)) ? 'checked' : '';
            stringListQuest += `<div class='content-ans'><input ${_checked} ${disabledAnswer} type='radio' class='radio-check' exam-id='${id_test}' name='test[${k}]' id='' value='${ka + 1}' title=''><span>${va}</span></div>`;
        });
        stringListQuest += '</div></div>';
    });

    $('.list-question').html(stringListQuest)
}

function checkTestFinished(endTime) {
    let currentTime = new Date();
    endTime = new Date(endTime);
    if ((endTime.getTime() - currentTime.getTime()) > 0) {
        return true;
    } else {
        return false;
    }
}

function countDownTime(endTime) {
    endTime = new Date(endTime);
    endTime = (Date.parse(endTime) / 1000);

    var now = new Date();
    now = (Date.parse(now) / 1000);

    var timeLeft = endTime - now;
    if (timeLeft < 0) {
        $("#hours").html("00");
        $("#minutes").html("00");
        $("#seconds").html("00");
        return false;
    }

    var days = Math.floor(timeLeft / 86400);
    var hours = Math.floor((timeLeft - (days * 86400)) / 3600);
    var minutes = Math.floor((timeLeft - (days * 86400) - (hours * 3600 )) / 60);
    var seconds = Math.floor((timeLeft - (days * 86400) - (hours * 3600) - (minutes * 60)));

    if (hours < "10") { hours = "0" + hours; }
    if (minutes < "10") { minutes = "0" + minutes; }
    if (seconds < "10") { seconds = "0" + seconds; }

    $("#hours").html(hours);
    $("#minutes").html(minutes);
    $("#seconds").html(seconds);
}