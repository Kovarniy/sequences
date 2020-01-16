package logic;

import java.util.Scanner;
import logic.Sequence.Expression.*;
import logic.Sequence.*;

public class Tester {
    // заменяет пользовательские символы операторов на символы, понятные программе
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

    // 1|-0

    public static void main(String[] args) {
        /*Sequence mainSeq = new Sequence((BinaryOperator)Expression.stringToExpression(translateChars("-x&x=")));
        mainSeq.useRule9(Expression.stringToExpression(translateChars("x")));

        System.out.println("bind 0 " + mainSeq.getBind(0));
        System.out.println("get 0 " + mainSeq.get(0,0));

        Sequence br1 = mainSeq.getBind(0);
        Sequence br2 = mainSeq.getBind(1);
        br1.useRule2(Expression.stringToExpression(translateChars("-x")));
        br2.useRule1(Expression.stringToExpression(translateChars("x")));
        System.out.println("hui" + mainSeq.get(0, 2));
        System.out.println("hui" + mainSeq.get(1, 2));
        System.out.println("hui" + mainSeq.get(1, 1));
        System.out.println(mainSeq.get(2, 2));
        System.out.println(mainSeq.get(0, 3));
        */

        Sequence mainSeq = new Sequence((BinaryOperator)Expression.stringToExpression(translateChars("y,y>z=x>z")));
        boolean canUseRul5_10 = mainSeq.useRule5(Expression.stringToExpression(translateChars("yvz")));
        System.out.println("canUseRul5_10 = " + canUseRul5_10);
        Sequence br1 = mainSeq.get(0,1);
        System.out.println("br1 = " + br1);

        System.out.println(mainSeq.toJson().toString());

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("Введите выражение: ");
            String str = in.nextLine();
            Expression expr = Expression.stringToExpression(translateChars(str));
            Expression addexpr = Expression.stringToExpression(translateChars("a"));

            Sequence s1 = new Sequence((BinaryOperator)expr);
            System.out.println(s1);

            System.out.println("Write rule number: ");
            int num = in.nextInt();
            str = in.nextLine();

            if (num == 1 || num == 2 || num == 5 || num == 7 || num == 9 || num == 10){
                System.out.println("Add expressions: ");
                str = in.nextLine();
                addexpr = Expression.stringToExpression(translateChars(str));
            }

            switch (num){
                case 0:
                    s1.useRule0();
                    break;
                case 1:
                    s1.useRule1(addexpr);
                    break;
                case 2:
                    s1.useRule2(addexpr);
                    break;
                case 3:
                    s1.useRule3();
                    break;
                case 4:
                    s1.useRule4();
                    break;
                case 5:
                    s1.useRule5(addexpr);
                    break;
                case 6:
                    s1.useRule6();
                    break;
                case 7:
                    s1.useRule7(addexpr);
                    break;
                case 8:
                    s1.useRule8();
                    break;
                case 9:
                    s1.useRule9(addexpr);
                    break;
                case 10:
                    s1.useRule10(addexpr);
                    break;
                case 11:
                    s1.useRule11();
                    break;
            }

            System.out.println(expr);
            System.out.println("is logical: " + expr.isLogical());
            System.out.println("is enum: " + expr.isEnum());
            System.out.println("is sequence: " + expr.isSequence());
            System.out.println("vars: " + expr.getVars());
            System.out.println("is always true: " + expr.isAlwaysTrue());
            System.out.println("enum: " + expr.enumToArray());

            if (expr instanceof BinaryOperator) {
                BinaryOperator s = (BinaryOperator) expr;
                if (s.isSequence() && s.getLeftOperand().equals(s.getRightOperand()))
                    System.out.println("АКСИОМА!!!");
            }



        }
    }
}
