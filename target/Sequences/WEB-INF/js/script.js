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

/* QUERIES */
function newSequnces() {
    $.ajax({
        url : 'inputSequences',     // URL - сервлет
        data : {                 // передаваемые сервлету данные
            inputSeq : $('#inputSeq').val(),
            queryType : "newSequnces"
        },
        success : function(response) {
            sequntion = JSON.parse(response);
            console.log(sequntion);
            error = sequntion.errorCode;
          //  console.log("error = " + error);

            if (error == 1){
                answer = sequntion.answer;
                $('#ajaxUserServletResponse').text(answer);
            } else{
                expr = sequntion.answer.expr;
                addInputField(expr);
            }
        },
        error: function(){
            console.log('error!');
        }
    });
}

function canUseRule(content) {
    $.ajax({
        url : 'inputSequences',
        data : {
            inputSeq : content.value,
            queryType : "testRules"
        },
        success : function(response) {
            var res = JSON.parse(response);
            rules = res.rule;
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
    /*  TODO необходимо получать (x,y) активного input  */
    glRuleId = ruleId;
    var type = typeof glRuleId;
    if ( type != "number" ){
        console.log("Incorrect rule type or rule number");
        return;
    }

    /*  TODO Сделать проверку на корректность добавочного выражения  */
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
    PopUpHide();
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
            x : 0,
            y : 0
        },
        success : function(response) {
            sequntion = JSON.parse(response);
            console.log(sequntion);
        },
        error: function(){
            console.log('error!');
        }
    });
}

/**/

function addInputField(answer) {
    /*
    * TODO Необзодимо расчитать индексы (x,y) и положение для каждого
    *  нового inputa
    * */
    $('body').append('<input type="text" value="' + answer + '"  readonly onfocus="canUseRule(this); return false;" class="currentSeq"> ');
}

function PopUpHide(){
    $("#popup1").hide();
}