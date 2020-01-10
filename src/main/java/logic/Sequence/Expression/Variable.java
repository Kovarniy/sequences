package logic.Sequence.Expression;

import java.util.Arrays;
import java.util.List;

public class Variable extends Expression {

    protected char name;

    //TODO ЗАЧЕМ НУЖЕН ПУСТОЙ КОНСТРКУТОР
    protected Variable() {};

    public Variable(char name) {
        if (!canBeVar(name))
            throw new IllegalArgumentException("Illegal variable name");
        this.name = name;
    }

    public static boolean canBeVar(char letter) {
        return letter != 'v' && (letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z');
    }

    @Override
    public boolean isLogical() { return true; }

    @Override
    public boolean isEnum() { return false; }

    @Override
    public boolean isSequence() { return false; }

    @Override
    protected void toStringBuilder(StringBuilder sb) {
        sb.append(name);
    }

    @Override
    protected void getVars(List<Variable> list) {
        if (list.contains(this)) return;
        list.add(this);
    }

    @Override
    protected boolean calculate(char[] vars, boolean[] values) {
        //TODO Откуда берется Arrays
        int index = Arrays.binarySearch(vars, name);
        return values[index];
    }

    @Override
    protected void enumToArray(List<Expression> list) {
        list.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable other = (Variable) o;

        return name == other.name;
    }
}
