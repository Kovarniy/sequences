package logic.Sequence.Expression;

import java.util.List;

// если слева от секвенции ничего нет, то туда надо записать ConstTrue
public class ConstTrue extends Variable {
    public ConstTrue() { name = '1'; }

    @Override
    protected void getVars(List<Variable> list) { } // нельзя изменит => не добавляет себя в список

    @Override
    protected boolean calculate(char[] vars, boolean[] values) {
        return true;
    }
}
