// Khởi tạo dữ liệu
var id_test = 1; // cái này là id bài thi
var currentPage = 1; // page hiện tại bộ đề chia theo các câu ví dụ (5 câu 1)
var limitPage = 5; // page hiện tại bộ đề chia theo các câu ví dụ (5 câu 1)
var checkUpdateApi = true; // cái này để check khi next nhiều trang mà không thay đổi câu hỏi thì k có hiện tượng call Api update nhiều lần
var finishTest = false; // kết thúc bài thi

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
    // Kiểm tra bài thi đã hoàn thành
    // Nếu hoàn thành thì move về trang lịch sử
    // window.local = .....
} else {
    /* khởi tạo bài thi */
    // render câu hỏi
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
        checkUpdateApi = true;
        let _this = $(this);
        let _quest = _this.parents('.item-question');
        let keyQuest = _quest.attr('data-key');
        listAnser[keyQuest].yourAns = _this.val();
        localStorage.setItem("test-" + id_test, JSON.stringify(listAnser));
        // Call api cập nhập ở đây
    });

    // Pre câu hỏi
    $('.btn-pre').click(function () {
        if (currentPage <= 1) return;
        currentPage--;
        renderListQuestion(listAnser);
        checkUpdateApi = false;
    });

    // Next câu hỏi
    $('.btn-next').click(function () {
        if (currentPage >= Math.ceil(json.length / limitPage)) return;
        currentPage++;
        renderListQuestion(listAnser);
        checkUpdateApi = false;
    });

    $('.btn-finish').click(function () {
        let confirmFinish = confirm('Bạn chắc chắn muốn kết thúc bài thi?');
        if (confirmFinish === false) return;

        clearInterval(_countDown);
        $(this).hide();
        $("#hours").html("00");
        $("#minutes").html("00");
        $("#seconds").html("00");
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
        v.listAns.forEach((va, ka) => {
            let _checked = (v.yourAns == (ka + 1)) ? 'checked' : '';
            stringListQuest += `<div class='content-ans'><input ${_checked} ${disabledAnswer} type='radio' class='radio-check' name='test-${id_test}-quest-${k}' id='' value='${ka + 1}' title=''><span>${va}</span></div>`;
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