package logic.Sequence.Expression;

import java.util.List;

// если справа от секвенции ничего нет, то туда надо записать ConstFalse
public class ConstFalse extends Variable {
    public ConstFalse() { name = '0'; }

    @Override
    protected void getVars(List<Variable> list) { } // нельзя изменит => не добавляет себя в список

    @Override
    protected boolean calculate(char[] vars, boolean[] values) {
        return false;
    }
}
