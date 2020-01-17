package logic.Sequence;
import logic.Sequence.Expression.*;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

class MyInteger{
    private int i;
    public MyInteger(int value) {
        i = value;
    }

    public void decrement() {
        i--;
    }
    public int getValue() {
        return i;
    }
}

public class Sequence {

    private BinaryOperator currentSeq;
    private Sequence[] bindSeq;
    private int ruleIndex;

    public Sequence(BinaryOperator currentSeq){
        if  (currentSeq instanceof BinaryOperator) {
            this.currentSeq = currentSeq;
            this.bindSeq = new Sequence[3];
            ruleIndex = -1;
        } else
            System.out.println("log: не удалось создать секвенцию!");

    }

    public void deleteBrunches() {
        for (int i = 0; i < 3; i++)
            bindSeq[i] = null;
        ruleIndex = -1;
    }

    public Expression getExpression(){
            return (Expression) currentSeq;
    }

    public String toString(){
        return currentSeq.toString();
    }

    public boolean canUseRule(int index) {
        Expression right = currentSeq.getRightOperand();
        Expression left = currentSeq.getLeftOperand();

        switch (index) {
            case 0:
                if (right instanceof Operator){
                    Operator rop = (Operator) right;
                    //TODO заменить == на equals
                    if ( rop.getType() == OperatorType.AND) return true;
                }
                return false;
            case 1:
            case 2:
                if ( !(right instanceof ConstFalse) )
                    return true;
                return false;
            case 3:
            case 4:
                if (right instanceof Operator){
                    Operator rop = (Operator) right;
                    if ( rop.getType() == OperatorType.OR)
                        return true;
                }
                return false;
            case 5:
                //Верно ли?
                if ( !(left instanceof ConstTrue) && !(right instanceof ConstFalse) )
                    return true;
                return false;
            case 6:
                if (right instanceof Operator){
                    Operator rop = (Operator) right;
                    if ( rop.getType() == OperatorType.IMP)
                        return true;
                }
                return false;
            case 7:
            case 8:
                // проверка на то, что справа есть переменная
                if (!(right instanceof ConstFalse))
                    return true;
                return false;
            case 9:
                if (right instanceof ConstFalse)
                    return true;
                return false;
            case 10:
            case 11:
                if(!(left instanceof ConstTrue))
                    return true;
                return false;
        }
        return false;
    }

    /*TODO Однако перед записью новых веток, необхадиво занулить все ветки, ну мало ли
    *  вдруг там что-то было записано. Например мы хотим испольховать правило, которое создает одну ветку.
    *  Но у текущей секвенции уже есть две ветки. Поэтому надо избавиться от них, а потом уже записывать */
    public boolean useRule0(){
        if (!canUseRule(0)) return false;
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        //Приходится использовать приведение типов т.к. getXOperand() возвращает Expressions
        Expression right0 = ((BinaryOperator) right).getLeftOperand();
        Expression right1 = ((BinaryOperator) right).getRightOperand();

        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, right0));
        bindSeq[1] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, right1));

        System.out.println("Rule 0.1 " + bindSeq[0]);
        System.out.println("Rule 0.2 " + bindSeq[1]);

        ruleIndex = 0;
        return true;
    }

    public boolean useRule1(Expression expr) {
        if (!canUseRule(1)) return false;

        //Что если хранить left и right в объекте?
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        BinaryOperator temp = new BinaryOperator(OperatorType.AND, right, expr);
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, temp));
        System.out.println("Rule 1 " + bindSeq[0]);
        ruleIndex = 1;
        return true;
    }
    public boolean useRule2(Expression expr){
        if (!canUseRule(2)) return false;

        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        BinaryOperator temp = new BinaryOperator(OperatorType.AND, expr, right);
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, temp));
        System.out.println(bindSeq[0]);
        System.out.println("Rule 2 " + bindSeq[0]);
        ruleIndex = 2;
        return true;
    }

    public boolean useRule3(){
        if (!canUseRule(3)) return false;

        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        Expression right0 = ((BinaryOperator) right).getLeftOperand();
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, right0));

        System.out.println("Rule 3 " + bindSeq[0]);
        ruleIndex = 3;
        return true;
    }
    public boolean useRule4(){
        if (!canUseRule(4)) return false;

        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        Expression right0 = ((BinaryOperator) right).getRightOperand();
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, right0));

        System.out.println("Rule 4 " + bindSeq[0]);
        ruleIndex = 4;
        return true;
    }
    public boolean useRule5(Expression expr){
        if (!canUseRule(5)) return false;

        //Проверка на то, чтобы добавляемое выражение являлось диъюнкцией.
        if ( !(expr instanceof Operator) )
            return false;
        Operator op = (Operator) expr;
        if ( !op.getType().equals(OperatorType.OR) )
            return false;

        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();
        Expression iputLeft = ((BinaryOperator) expr).getLeftOperand();
        Expression iputRight = ((BinaryOperator)  expr).getRightOperand();

        BinaryOperator temp = new BinaryOperator(OperatorType.ENUM, left, iputLeft);
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, temp, right));
        System.out.println("Rule 5.1 " + bindSeq[0]);

        temp = new BinaryOperator(OperatorType.ENUM, left, iputRight);
        bindSeq[1] = new Sequence(new BinaryOperator(OperatorType.SEQ, temp, right));
        System.out.println("Rule 5.2 " + bindSeq[1]);
        temp = new BinaryOperator(OperatorType.OR, iputLeft, iputRight);
        bindSeq[2] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, temp));
        System.out.println("Rule 5.3 " + bindSeq[2]);
        ruleIndex = 5;
        return true;
    }
    public boolean useRule6(){
        if (!canUseRule(6)) return false;
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        Expression right0 = ((BinaryOperator) right).getLeftOperand();
        Expression right1 = ((BinaryOperator) right).getRightOperand();

        BinaryOperator temp = new BinaryOperator(OperatorType.ENUM, left, right0);
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, temp, right1));
        System.out.println("Rule 6 " + bindSeq[0]);
        ruleIndex = 6;

        return true;
    }
    public boolean useRule7(Expression expr){
        if (!canUseRule(7)) return false;
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();

        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, expr));
        System.out.println("Rule 7.1 " + bindSeq[0]);
        BinaryOperator temp = new BinaryOperator(OperatorType.IMP, expr, right);
        bindSeq[1] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, temp));
        System.out.println("Rule 7.2 " + bindSeq[1]);
        ruleIndex = 7;

        return true;
    }
    public boolean useRule8(){
        if (!canUseRule(8)) return false;
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();
        Operator temp = new Operator(OperatorType.NOT, right);
        BinaryOperator temp2 = new BinaryOperator(OperatorType.ENUM, left, temp);
        ConstFalse cF = new ConstFalse();
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, temp2, cF));
        System.out.println("Rule 8 " + bindSeq[0]);
        ruleIndex = 8;
        return true;
    }
    public boolean useRule9(Expression expr){
        if (!canUseRule(9)) return false;
        Expression left = currentSeq.getLeftOperand();
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, expr));
        System.out.println("Rule 9.1 " + bindSeq[0]);
        Operator temp = new Operator(OperatorType.NOT, expr);
        bindSeq[1] = new Sequence(new BinaryOperator(OperatorType.SEQ, left, temp));
        System.out.println("Rule 9.2 " + bindSeq[1]);
        ruleIndex = 9;
        return true;
    }
    public boolean useRule10(Expression expr){
        if (!canUseRule(10)) return false;
        List<Expression> inputOper = expr.enumToArray();
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();
        List<Expression> outputOper = left.enumToArray();
        //Проверка на то, что пользователь ввел допустимые выражения в антициденте
        //тоесть там не появились новые значения
        for (Expression io: inputOper)
            if (!outputOper.contains(io))
                return false;

        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, expr, right));
        System.out.println("Rule 10 " + bindSeq[0]);
        ruleIndex = 10;
        return true;
    }
    public boolean useRule11(){
        if (!canUseRule(11)) return false;
        Expression left = currentSeq.getLeftOperand();
        Expression right = currentSeq.getRightOperand();
        List<Expression> leftList = left.enumToArray();

        int len = leftList.size();
        leftList.remove(len-1);
        Expression temp = leftList.get(0);
        leftList.remove(0);

        for (Expression t: leftList){
            temp = new BinaryOperator(OperatorType.ENUM, temp, t);
        }
        bindSeq[0] = new Sequence(new BinaryOperator(OperatorType.SEQ, temp, right));
        System.out.println("Rule 11 " + bindSeq[0]);
        ruleIndex = 11;
        return true;
    }

    public boolean isAxiom() {
        return currentSeq.getLeftOperand().equals(currentSeq.getRightOperand());
    }

    protected void toJson(JSONObject childData) {

        for (int i = 0; i < 3; i++) {
            if (bindSeq[i] == null) {
                childData.put("bind"+i, "null");
                if (i == 2) return;
            }
            else{

                JSONObject childData2 = bindSeq[i].toJson();
                childData.put("bind"+i, childData2);

            }
            //System.out.println(childData.toString());
        }
    }

    public JSONObject toJson() {
        JSONObject childData = new JSONObject();
        childData.put("expr", currentSeq);
        //y = x = 0;
        //System.out.println(childData.toString());
        toJson(childData);
        return childData;
    }

    public boolean isProven() {
        if (bindSeq[0] == null) // все ветви null
            if (this.isAxiom())
                return true;
            else
                return false;

        for (Sequence seq : bindSeq)
            if (seq != null && !seq.isProven())
                return false;
        return true;
    }

    private Sequence get(MyInteger x, int y) {
        if (y == 1) {
            for (int i = 0; i < 3; i++) {
                if (bindSeq[i] != null) {
                    if (x.getValue() == 0)
                        return bindSeq[i];
                    x.decrement();
                } else
                x.decrement();  //Для полнго расчета индексов
            }
        }
        else
            for (int i = 0; i < 3; i++) {
                Sequence seq = bindSeq[i] != null ? bindSeq[i].get(x, y - 1) : null;
                if (seq != null) return seq;
            }
        return null;
    }

    public Sequence get(int x, int y) {
        if (x == 0 && y == 0) return this;
        return get(new MyInteger(x), y);
    }

    public Sequence getBind(int index) {
        if (bindSeq[0] == null &&  bindSeq[1] == null && bindSeq[2] == null)
            return this;
        return bindSeq[index];
    }


}
