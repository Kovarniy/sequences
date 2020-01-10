package logic.Sequence.Expression;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Expression {
    public static final char HOP = '(';
    public static final char HCL = ')';

    // приватная, используется в публичной перегрузке функции
    private static Expression stringToExpression(String str, int begin, int end) {
        // избавляемся от нулей в начале
        for (; begin < end; begin++)
            if (str.charAt(begin) != ' ') break;
        // избавляемся от нулей в конце
        for (; begin < end; end--)
            if (str.charAt(end - 1) != ' ') break;

        if (begin == end) return null;

        int minPriority = Integer.MAX_VALUE;
        int minPriorityI = -1;
        int hooks = 0;

        // находим оператор с минимальным приоритетом
        for (int i = begin; i < end; i++) {
            char ch = str.charAt(i);

            if (ch == HOP) {
                hooks += 10;
                continue;
            } else if (ch == HCL) {
                hooks -= 10;
                continue;
            }

            if (OperatorType.hasChar(ch)) { // существует ли такой оператор
                int priority = OperatorType.fromChar(ch).getPriority();
                if (priority + hooks <= minPriority) {
                    minPriority = priority + hooks;
                    minPriorityI = i;
                }
            }
        }

        //если нет операторов, значит это должна быть переменная
        if (minPriorityI == -1) {
            // проходим все символы, которые не могут быть переменными
            for (int i = begin; i < end; i++) {
                if (Variable.canBeVar(str.charAt(i))) return new Variable(str.charAt(i));
            }
            return null;
        }

        // получаем оператор с самым низким приоритетом
        OperatorType type = OperatorType.fromChar(str.charAt(minPriorityI));

        // получаем правый операнд(он есть и у унарного и у бинарного операторов)
        Expression rightOperand = stringToExpression(str, minPriorityI + 1, end);

        // если оператор унарный
        if (type.isUnary()) {
            if (rightOperand == null) return null;
            return new Operator(type, rightOperand);
        }

        // если оператор бинарный
        Expression leftOperand = stringToExpression(str, begin, minPriorityI);

        // если это штопор, то если слева пусто - 1, если справа пусто - 0
        if (type == OperatorType.SEQ) {
            if (leftOperand == null) leftOperand = new ConstTrue();
            if (rightOperand == null) rightOperand = new ConstFalse();
            return new BinaryOperator(OperatorType.SEQ, leftOperand, rightOperand);
        }

        //НЕПОНЯТНО
        if (leftOperand == null) return rightOperand;
        else if (rightOperand == null) return leftOperand;

        return new BinaryOperator(type, leftOperand, rightOperand);
    }

    public static Expression stringToExpression(String str) {
        return stringToExpression(str, 0, str.length());
    }

    public abstract boolean isLogical(); // выражение, состоящее из NOT, OR, AND, IMP
    public abstract boolean isEnum(); // оператор ',' у которого операнды - логические выражения или ','
    public abstract boolean isSequence(); // оператор штопор, у которого левый операнд - Enum или Logical, а правый - логическое выражение

    protected abstract void getVars(List<Variable> list);

    // возвращает список всех переменных в выражении(константы не входят)
    public List<Variable> getVars() {
        List<Variable> vars = new LinkedList<>();
        getVars(vars);
        return vars;
    }

    // вычислить значение выражения при определенных значениях переменных
    protected abstract boolean calculate(char[] vars, boolean[] values);

    // при любых значениях переменных - истина(тождественно-истинное выражение), для секвенций говорит - доказуема или нет
    public boolean isAlwaysTrue() {
        List<Variable> varsList = getVars();
        int size = varsList.size();

        char [] vars = new char[size];
        for (int i = 0; i < size; i++)
            vars[i] = varsList.get(i).name;

        boolean [] values = new boolean[size];
        Arrays.sort(vars); // сортируем потому что
                           // в классе Variable используется binarySearch, чтобы переменная могла быстро найти себя в списке

        if (calculate(vars, values) == false) return false; // если ложь при всех нулях, то сразу возвращаем ложь

        // перебираем все возможные значения переменных. Пример: 000 001 010 011 ... 111
        while (true) {
            int i = size - 1;
            while (i >= 0 && values[i] == true) i--;
            if (i < 0) return true;         // если дошли до 11...1, то все варианты перебраны, можно вернуть истину

            values[i] = true;
            for (int j = i + 1; j < size; j++)
                values[j] = false;

            if(calculate(vars, values) == false) return false; // если ложь, то сразу возвращаем ложь
        }
    }


    protected abstract void enumToArray(List<Expression> list);

    public List<Expression> enumToArray() {
        List<Expression> list = new LinkedList<>();
        enumToArray(list);
        return list;
    }

    // используется для toString() чтобы не создавать каждый раз новую строку, а добавлять к StringBuilder'у
    protected abstract void toStringBuilder(StringBuilder sb);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb);
        return sb.toString();
    }
}
