package logic.queries;

import logic.Sequence.Expression.BinaryOperator;
import logic.Sequence.Expression.Expression;
import logic.Sequence.Expression.OperatorType;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import logic.Sequence.Sequence;
import logic.Sequence.Expression.*;
import org.json.JSONObject;

public class GetQuery {


    public static String translateChars(String str) {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            switch (ch) {
                case '-': sb.append(OperatorType.NOT); break; // OperatorType приводится к строке(переопределен метод toString)
                case '&': sb.append(OperatorType.AND); break;
                case 'v': sb.append(OperatorType.OR); break;
                case '>': sb.append(OperatorType.IMP); break;
                case ',': sb.append(OperatorType.ENUM); break;
                case '=': sb.append(OperatorType.SEQ); break;
                default: sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static boolean checkExpressions(Expression expr) {
        if(expr == null)
            return false;
        if (!(expr instanceof BinaryOperator))
            return false;
        return true;
    }

    public static Sequence newSeqQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String inputSeq = req.getParameter("inputSeq").trim();
        resp.setContentType("text/plain");
        //resp.setContentType("application/json");
        System.out.println("inputSeq= " + inputSeq);
        Expression expr = Expression.stringToExpression(translateChars(inputSeq));
        System.out.println("expr= " + expr.toString());
        JSONObject jsonAnswer = new JSONObject();

        Sequence seq = null;
        if (!expr.isSequence()){
            System.out.println("Это не секвенция!");
            jsonAnswer.put("answer", "Это не секвенция!");
            jsonAnswer.put("errorCode", 1);
        }
        else if (expr instanceof BinaryOperator) {
            BinaryOperator s = (BinaryOperator) expr;
            if (s.isSequence() && s.getLeftOperand().equals(s.getRightOperand())){
                jsonAnswer.put("answer", "Это аксиома!");
                jsonAnswer.put("errorCode", 1);
            }
            else if (!expr.isAlwaysTrue()) {
                jsonAnswer.put("answer", "Эта секвенция не доказуема!");
                jsonAnswer.put("errorCode", 1);
            }
            else {
                seq = new Sequence((BinaryOperator) expr);
                jsonAnswer.put("answer", seq.toJson());
                jsonAnswer.put("errorCode", 0);
               // System.out.println(jsonAnswer.toString());
            }
        }

        OutputStream outStream = resp.getOutputStream();
        outStream.write(jsonAnswer.toString().getBytes("UTF-8"));

        outStream.flush();
        outStream.close();

        if (seq != null) return seq;
        else
            return null;
    }

    public static void testRuleQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean[] rulesArray = new boolean[11];
        String seq = req.getParameter("inputSeq").trim();

        Expression expr = Expression.stringToExpression(translateChars(seq));
        JSONObject jsonAnswer = new JSONObject();

        if (!checkExpressions(expr)){
            jsonAnswer.put("answer", "Ошибка преобразования выражения");
            jsonAnswer.put("errorCode", 1);
            return;
        }
        Sequence seq1 = new Sequence((BinaryOperator) expr);
        for (int i = 0; i < 11 ;i++){
    //        System.out.println(seq1.canUseRule(i));
            rulesArray[i] = seq1.canUseRule(i);
        }

        jsonAnswer.put("rule", rulesArray);

        OutputStream outStream = resp.getOutputStream();
        outStream.write(jsonAnswer.toString().getBytes("UTF-8"));

        outStream.flush();
        outStream.close();
    }

    public static void useRuleQuery(HttpServletRequest req, HttpServletResponse resp, Sequence seq) throws IOException {
        //TODO проверка на null
        //Получаем нужную секвенцию
        String x = req.getParameter("x").trim();
        String y = req.getParameter("y").trim();
        Sequence branch = seq.get(0, 0);
        System.out.println("seq get = " + branch);

        String ruleId = req.getParameter("ruleId").trim();
        System.out.println("id = " + ruleId);

        String addExprStr = req.getParameter("addExpr").trim();
        Expression addExpr;

        /*
        * Проверка добавленна на всякий случай. Поскольку теоретически на сервер
        * не может придти пустое выражение, т.к. на уровне html стоит запер на пустые
        * выражения
        * */

        if (addExprStr != "null")
            addExpr = Expression.stringToExpression(translateChars(addExprStr));
        else
            addExpr = null;

        JSONObject jsonAnswer = new JSONObject();
        if (!checkExpressions(addExpr)){
            System.out.println("ошибка");
            jsonAnswer.put("answer", "Ошибка преобразования выражения");
            jsonAnswer.put("errorCode", 1);
        }

        switch (Integer.parseInt(ruleId)){
            case 0:
                branch.useRule0();
                break;
            case 1:
                branch.useRule1(addExpr);
                break;
            case 2:
                branch.useRule2(addExpr);
                break;
            case 3:
                branch.useRule3();
                break;
            case 4:
                branch.useRule4();
                break;
            case 5:
                branch.useRule5(addExpr);
                break;
            case 6:
                branch.useRule6();
                break;
            case 7:
                branch.useRule7(addExpr);
                break;
            case 8:
                branch.useRule8();
                break;
            case 9:
                branch.useRule9(addExpr);
                break;
            case 10:
                branch.useRule10(addExpr);
                break;
            case 11:
                branch.useRule11();
                break;
        }

        jsonAnswer.put("answer", seq.toJson());
       // System.out.println("json  = " + jsonAnswer.toString());

        OutputStream outStream = resp.getOutputStream();
        outStream.write(jsonAnswer.toString().getBytes("UTF-8"));

        outStream.flush();
        outStream.close();
    }


}
