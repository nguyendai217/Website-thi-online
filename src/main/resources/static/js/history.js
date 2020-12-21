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

listAnser = { ...listQuestion };

res = Object.keys(listAnser).map((k) => listAnser[k]);
listAnser = res;

renderListQuestion(listAnser);
// Pre câu hỏi
$('.btn-pre').click(function (e) {
	e.preventDefault();
    $("html, body").animate({ scrollTop: 400 }, 600);
    if (currentPage <= 1) return;
    currentPage--;
    renderListQuestion(listAnser);
    checkUpdateApi = false;
});

// Next câu hỏi
$('.btn-next').click(function (e) {
	e.preventDefault();
    $("html, body").animate({ scrollTop: 400 }, 600);
    if (currentPage >= Math.ceil(json.length / limitPage)) return;
    currentPage++;
    renderListQuestion(listAnser);
    checkUpdateApi = false;
});



function renderListQuestion(listAnser) {
    console.log(result_detail);
    let stringListQuest = "";
    let countAnsCorrect = 0;
    let disabledAnswer = finishTest == true ? 'disabled' : '';
    listAnser.forEach((v, k) => { // danh sách câu hỏi
        let showQuestion = 'hide';
        if (((currentPage - 1) * limitPage) <= k && ((currentPage) * limitPage) > k ) {
            showQuestion = '';
        }

        stringListQuest += `<div class='item-question ${showQuestion}' data-key="${k}"><div class='title'><b><span class='question-no'>${k + 1}</span>${v.content}</b></div><div class='content'>`;
        v.listAns.forEach((va, ka) => { // danh sách các câu trả lời
            let _classAns = '';
            if (result_detail[k] == (ka + 1)) { // nếu trả lời sai
                _classAns = "test-ans-err";
            }

            if (v.ansCorrect == (ka + 1)) { // lấy ra giá trị đúng của câu hỏi
                _classAns = "test-ans-success";
                if (v.ansCorrect == result_detail[k]) { // Nếu trả lời đúng
                    _classAns = "test-ans-success";
                    countAnsCorrect++;
                }
            }

            let _checked = (v.yourAns == (ka + 1)) ? 'checked' : '';
            stringListQuest += `<div class='content-ans ${_classAns}  '><input ${_checked} ${disabledAnswer} type='radio' class='radio-check' disabled exam-id='${id_test}' name='test[${k}]' id='' value='${ka + 1}' title=''><span>${va}</span></div>`;
        });
        stringListQuest += '</div></div>';
    });

    $('.result-test-data').html(countAnsCorrect + " câu / trên tổng số " + listAnser.length + " câu")
    $('.list-question').html(stringListQuest)
}