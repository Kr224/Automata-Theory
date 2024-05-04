public class FunkyScanner {
    private String input;
    private int position;

    public FunkyScanner(String input) {
        this.input = input;
        this.position = 0;
    }

    public FunkyToken lookahead() {
        if (position >= input.length()) {
            return new FunkyToken(FunkyToken.END);
        }
        char nextChar = input.charAt(position);
        switch (nextChar) {
            case '(':
                return new FunkyToken(FunkyToken.LEFT_PAREN); // corrected to LEFT_PAREN
            case ')':
                return new FunkyToken(FunkyToken.RIGHT_PAREN);
            case '{':
                return new FunkyToken(FunkyToken.LEFT_BRACE); // corrected to LEFT_BRACE
            case '}':
                return new FunkyToken(FunkyToken.RIGHT_BRACE);
            case 'd': // Assuming 'def' keyword
                return new FunkyToken(FunkyToken.DEF);
            case 'i':
                return new FunkyToken(FunkyToken.IF);
            case 'r':
                return new FunkyToken(FunkyToken.RETURN);
            case 'e':
                return new FunkyToken(FunkyToken.ELSE);
            default:
                if (Character.isLetter(nextChar)) {
                    return new FunkyToken(FunkyToken.ID);
                }
        }
        // If none of the above cases match, return an unknown token
        return new FunkyToken(-1);
    }

    public void nextLookahead() {
        position++;
    }
}
