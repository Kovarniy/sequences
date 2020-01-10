package logic.Sequence.Expression;

import java.util.List;

public class BinaryOperator extends Operator {
    private Expression leftOperand;

    public BinaryOperator(OperatorType type, Expression leftOperand, Expression rightOperand) {
        if (type.isUnary()) throw new IllegalArgumentException("Type can't be unary");
        this.type = type;
        this.operand = rightOperand;
        this.leftOperand = leftOperand;
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    // то же, что и getOperand, просто так нагляднее пользователюЫ
    public Expression getRightOperand() {
        return operand;
    }

    @Override
    public boolean isLogical() {
        return (type == OperatorType.AND || type == OperatorType.OR || type == OperatorType.IMP)
                && leftOperand.isLogical() && operand.isLogical();
    }

    @Override
    public boolean isEnum() {
        return type == OperatorType.ENUM && (leftOperand.isEnum() || leftOperand.isLogical())
                                         && (operand.isEnum() || operand.isLogical());
    }

    @Override
    public boolean isSequence() {
        return type == OperatorType.SEQ && (leftOperand.isEnum() || leftOperand.isLogical()) && operand.isLogical();
    }


    @Override
    protected void toStringBuilder(StringBuilder sb) {
        char letter = type.getLetter();
        switch (type) {
            case ENUM:
                leftOperand.toStringBuilder(sb);
                sb.append(letter);
                operand.toStringBuilder(sb);
                break;
            case SEQ:
                // СЃР»РµРІР° РІСЃРµРіРґР° 1, СЃРїСЂР°РІР° РІСЃРµРіРґР° 0
                if (!(leftOperand instanceof ConstTrue))
                    leftOperand.toStringBuilder(sb);

                sb.append(letter);

                if (!(operand instanceof ConstFalse))
                    operand.toStringBuilder(sb);
                break;

            case OR:
            case AND:
            case IMP:
                if (leftOperand instanceof BinaryOperator) {
                    sb.append(HOP);
                    leftOperand.toStringBuilder(sb);
                    sb.append(HCL);
                } else
                    leftOperand.toStringBuilder(sb);
                sb.append(letter);

                if (operand instanceof BinaryOperator) {
                    sb.append(HOP);
                    operand.toStringBuilder(sb);
                    sb.append(HCL);
                } else
                    operand.toStringBuilder(sb);
        }
    }


    @Override
    protected void getVars(List<Variable> list) {
        leftOperand.getVars(list);
        super.getVars(list);
    }

    @Override
    protected boolean calculate(char[] vars, boolean[] values) {
        return type.calculate(leftOperand.calculate(vars, values), operand.calculate(vars, values));
    }

    @Override
    protected void enumToArray(List<Expression> list) {
        if (type == OperatorType.ENUM) {
            leftOperand.enumToArray(list);
            operand.enumToArray(list);
        } else
            list.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperator other = (BinaryOperator) o;

        return super.equals(o) && leftOperand.equals(other.leftOperand);
    }
}
