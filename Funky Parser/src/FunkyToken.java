public class FunkyToken {
    public static final int END = 0;
    public static final int DEF = 1;
    public static final int RIGHT_PAREN = 2;
    public static final int LEFT_PAREN = 8;
    public static final int LEFT_BRACE = 9;
    public static final int ID = 3;
    public static final int RIGHT_BRACE = 4;
    public static final int IF = 5;
    public static final int RETURN = 6;
    public static final int ELSE = 7;
    public static final int NUMBER = 10;
    public static final int STRING = 11;

    private int type;

    public FunkyToken(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case END:
                return "END";
            case DEF:
                return "DEF";
            case RIGHT_PAREN:
                return "RIGHT_PAREN";
            case ID:
                return "ID";
            case RIGHT_BRACE:
                return "RIGHT_BRACE";
            case IF:
                return "IF";
            case RETURN:
                return "RETURN";
            case ELSE:
                return "ELSE";
            case LEFT_PAREN:
                return "LEFT_PAREN";
            case LEFT_BRACE:
                return "LEFT_BRACE";
            default:
                return "UNKNOWN";
        }
    }
}
