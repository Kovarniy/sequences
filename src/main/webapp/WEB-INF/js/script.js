// вызов функции по завершению загрузки страницы
$(document).ready(function() {
    $(document).ready(function(){
        PopUpHide();
    });
    /*TODO Разобраться с тем, что такое e*/
    $("#popup1").submit(function(e) {  //указать вашу форму
        e.preventDefault(); // отменит перезагрузку
        inputExpression();
    });
    $("#inputForm").submit(function(e) {
        e.preventDefault();
        newSequnces();
    });
});

var sequntion;
var addExpr;
var glRuleId;
// Голбальные x,y - содержат индекс ipnuta, который находится в фокусе
var glX;
var glY;

/* QUERIES */
function newSequnces() {
    $.ajax({
        url : 'inputSequences',     // URL - сервлет
        data : {                 // передаваемые сервлету данные
            inputSeq : $('#inputSeq').val(),
            queryType : "newSequnces"
        },
        success : function(response) {  //response - строка
            sequntion = JSON.parse(response);   //парсим строку в json "объект"
            var error = sequntion.errorCode;
            var answer = sequntion.answer;

            if (error == 1){
                $('#ajaxUserServletResponse').text(answer);
            } else{
                clearSeqField();
                addInputField(answer, 0, 0);
            }
        },
        error: function(){
            console.log('error!');
        }
    });
}

function canUseRule(content) {
    glX  = content.getAttribute('x');
    console.log(glX);
    glY = content.getAttribute('y');
    console.log(glY);

    $.ajax({
        url : 'inputSequences',
        data : {
            inputSeq : content.value,
            queryType : "testRules"
        },
        success : function(response) {
            var res = JSON.parse(response);
            var rules = res.rule;
            //console.log(rules);
            $('.rulesBtn').each(function(index,elem) {
                //console.log(rules);
                if (rules[index] == false) {
                    $(elem).prop("disabled", true);
                    $(elem).addClass("cantUseRule");
                    $(elem).removeClass("canUseRule");
                }
                else {
                    $(elem).prop("disabled", false);
                    $(elem).addClass("canUseRule");
                    $(elem).removeClass("cantUseRule");
                }
            });
        },
        error: function(){
            console.log('error!');
        }
    });
}

function useRule(ruleId) {

    glRuleId = ruleId;
    var type = typeof glRuleId;
    if ( type != "number" ){
        console.log("Incorrect rule type or rule number");
        return;
    }

    switch (glRuleId) {
        case 1:
        case 2:
        case 5:
        case 7:
        case 9:
        case 10:
            console.log("Need to add expressions");
            $("#popup1").show();
            break;
        default:
            ajaxUseRule(glRuleId,null);
    }
}

/**/

/* useRule uses these functions */

function inputExpression() {
    console.log("to Send expressions");
    addExpr = $('#addExpression').val();
    ajaxUseRule(glRuleId, addExpr);
}

function ajaxUseRule(_ruleId,_addExpr) {
    console.log("We uses ruleId = " + _ruleId);
    $.ajax({
        url: 'inputSequences',
        data : {
            ruleId: _ruleId,
            queryType : "useRule",
            /*Текущие x,y и addExpr являются заглушками */
            addExpr : _addExpr,
            x : glX,
            y : glY
        },
        success : function(response) {
            /*Тут будет обработка ошибочных и верных запросов*/
            //sequntion = JSON.parse(response);
            console.log(sequntion);
            var error = sequntion.errorCode;
            var answer = sequntion.answer;
            if (error == 1){
                $('#addExprResponse').text(answer);
            } else{
                PopUpHide();
                clearSeqField();
                addInputField(answer, 0,0);
            }
        },
        error: function(){
            console.log('error!');
        }
    });
}

/**/

/* Functions for display sequentions tree */

function clearSeqField() {
    var container = document.getElementById('seqForm');

    while (container.firstChild) {
        container.removeChild(container.firstChild);
    }
}

function addInputField(response, _x, _y) {
    console.log("response   =   ");
    console.log(response);
    var answer = response;
    var expr = answer.expr;
    console.log('expr = ');
    console.log(expr);
    var y = _y;
    var x = _x;
    var seqStatus = answer.status;
    console.log('status = ');
    console.log(seqStatus);

    $('#seqForm').append('<input type="text" value="' + expr + '"  onfocus="canUseRule(this); return false;" ' +
        'x="' + _x + '" y="' + y + '" class="currentSeq ' + seqStatus + ' " readonly>');
    y++;

        /* Определям в какоую ветку мы идем */
    if(answer.bind0 == "null"){
        console.log("its null");
        return;
    } else {
        console.log("b0");
        addInputField(answer.bind0, x * 3 + 0, y);
    }
    if (answer.bind1 != "null"){
        console.log("b1");
        addInputField(answer.bind1, x * 3 + 1, y);
    }
    if (answer.bind2 != "null"){
        console.log("b2");
        addInputField(answer.bind2, x * 3 + 2, y);
    }

}


/**/

function PopUpHide(){
    $("#popup1").hide();
}