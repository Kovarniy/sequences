package logic.Sequence.Expression;

public enum OperatorType {
    NOT ((char)0x00AC, false, 6) {
        @Override
        public boolean calculate(boolean left, boolean right) { return !right; } // левый операнд не используется
    },
    AND ('&', true, 5) {
        @Override
        public boolean calculate(boolean left, boolean right) { return left && right; }
    },
    OR  ((char)0x2228, true, 4) {
        @Override
        public boolean calculate(boolean left, boolean right) { return left || right; }
    },
    IMP ((char)8594, true, 3) {
        @Override
        public boolean calculate(boolean left, boolean right) { return !left || right; }
    },
    ENUM(',', true, 2) {
        @Override
        public boolean calculate(boolean left, boolean right) { return left && right; }
    },
    SEQ ((char)0x22A2, true, 1) {
        @Override
        public boolean calculate(boolean left, boolean right) { return !left || right; }
    };

    private char letter;
    private boolean isBinary;
    private int priority;
    //конструктор
    OperatorType(char letter, boolean isBinary, int priority) {
        this.letter = letter;
        this.isBinary = isBinary;
        this.priority = priority;
    }
    // получить тип из символа
    public static OperatorType fromChar(char letter) {
        for (OperatorType type : OperatorType.values()) {
            if (letter == type.letter) return type;
        }
        throw new IllegalArgumentException("the letter is not an Operator");
    }

    public static boolean hasChar(char letter) {
        for (OperatorType type : OperatorType.values()) {
            if (letter == type.letter) return true;
        }
        return false;
    }
    //TODO посчитать значение при определенными левым и правым операндами
    public abstract boolean calculate(boolean left, boolean right);

    public char getLetter() {
        return letter;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isBinary() { return isBinary; }

    public boolean isUnary() { return !isBinary; }

    @Override
    public String toString() {
        return "" + letter;
    }
}
