// вызов функции по завершению загрузки страницы
$(document).ready(function() {
    $(document).ready(function(){
        PopUpHide();
    });
});

var sequntion;

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
            console.log("error = " + error);

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
            console.log(rules);

            $('.rulesBtn').each(function(index,elem) {
                console.log(rules);
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
    var type = typeof ruleId;
    if ( type != "number" ){
        console.log("Incorrect rule type or rule number");
        return;
    }

    /*  TODO Сделать проверку на корректность добавочного выражения  */
    /*PopUp окно*/
    switch (ruleId) {
        case 1:
        case 2:
        case 5:
        case 7:
        case 9:
        case 10:
            $("#popup1").show();
            break;
    }


    $.ajax({
        url: 'inputSequences',
        data : {
            inputSeq: sequntion,
            ruleId: ruleId,
            queryType : "useRule",
            /*Текущие x,y и addExpr являются заглушками */
            addExpr : null,
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

