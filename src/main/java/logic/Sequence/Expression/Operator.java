package logic.Sequence.Expression;

import java.util.List;

public class Operator extends Expression {
    protected Expression operand; // правый операнд

    protected OperatorType type;

    protected Operator() {}

    public Operator(OperatorType type, Expression operand) {
        if (type.isBinary()) throw new IllegalArgumentException("Type can't be binary");
        this.type = type;
        this.operand = operand;
    }

    public Expression getOperand() {
        return operand;
    }

    public OperatorType getType() { return type; }

    //TODO непонятно, почему так работает isLogical
    @Override
    public boolean isLogical() { return operand.isLogical(); }

    @Override
    public boolean isEnum() { return false; }

    @Override
    public boolean isSequence() { return false; }

    @Override
    protected void toStringBuilder(StringBuilder sb) {
        sb.append(type.getLetter());
        if (operand instanceof BinaryOperator) {
            sb.append(HOP);
            operand.toStringBuilder(sb);
            sb.append(HCL);
        } else
            operand.toStringBuilder(sb);
    }

    @Override
    protected void getVars(List<Variable> list) {
        operand.getVars(list);
    }

    @Override
    protected boolean calculate(char[] vars, boolean[] values) {
        return type.calculate(false, operand.calculate(vars, values));
    }

    @Override
    protected void enumToArray(List<Expression> list) {
        list.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator other = (Operator) o;

        return operand.equals(other.operand) && type == other.type;
    }
}
