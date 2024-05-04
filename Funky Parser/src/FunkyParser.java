import java.util.List;

public class FunkyParser {
    private final FunkyScanner scanner;  // scanner is created at instantiation

    /* Constructor: Create parser to use the specified scanner
     * Params: scan : Scanner to be used
     */
    public FunkyParser(FunkyScanner scan) {
        scanner = scan;
    }

    /* Invoke parser
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    public TreeNode parse() {
        return parseFunky();
    }

    /* Parse from start variable (Funky)
     * Params: none
     * Returns: parse tree (TreeNode)
     */


    private TreeNode parseFunky() {
        FunkyToken token = scanner.lookahead();

        if (token.getType() == FunkyToken.END) {
            return new TreeNode("Funky", (List<TreeNode>) new TreeNode("end", parseFunction(), parseFunky()));

        } else if (token.getType() == FunkyToken.DEF) {
            return new TreeNode("Funky", parseFunction(), parseFunky());
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }



    /* Parse from start variable (Function)
     * Params: none
     * Returns: parse tree (TreeNode)
     */

    private TreeNode parseFunction() {
        FunkyToken defToken = scanner.lookahead();
        TreeNode def = new TreeNode(defToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        FunkyToken idToken = scanner.lookahead();
        TreeNode id = new TreeNode(idToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        FunkyToken leftparenToken = scanner.lookahead();
        TreeNode leftparen = new TreeNode(leftparenToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        TreeNode params = parseParams();

        FunkyToken rightparenToken = scanner.lookahead();
        TreeNode rightparen = new TreeNode(rightparenToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        FunkyToken leftbraceToken = scanner.lookahead();
        TreeNode leftbrace = new TreeNode(leftbraceToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        TreeNode statements = parseStatements();

        FunkyToken rightbraceToken = scanner.lookahead();
        TreeNode rightbrace = new TreeNode(rightbraceToken.toString(), parseFunction(), parseFunky());
        scanner.nextLookahead();

        return new TreeNode("Function", List.of(new TreeNode[]{def, id, leftparen, params, rightparen, leftbrace, statements, rightbrace}));
    }


    /* Parse Params
     * Params: none
     * Returns: parse tree (TreeNode)
     */

    private TreeNode parseParams() {
        // Assume predictor set is
        // )                  : Params -> epsilon
        // id                 : Params -> id Params
        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RIGHT_PAREN:
                return new TreeNode("Params", (List<TreeNode>) new TreeNode(" ", parseFunction(), parseFunky()));
            case FunkyToken.ID:
                scanner.nextLookahead();
                return new TreeNode("Params", new TreeNode(" " + token, parseFunction(), parseFunky()), parseParams());
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }



    /* Parse Statements
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseStatements() {
        // Assume predictor set is
        // }                  : Statements -> epsilon
        // if return          : Statements -> Statement Statements
        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RIGHT_BRACE:
                return new TreeNode("Statements", (List<TreeNode>) new TreeNode(" ", parseFunction(), parseFunky()));
            case FunkyToken.IF:
            case FunkyToken.RETURN:
                return new TreeNode("Statements",parseStatement(),parseStatements());
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }


    /* Parse Statement
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseStatement() {
        // Assume predictor set is
        // return             : Statement -> return Expression
        // if                 : Statement -> if ( Expression ) { Statements } Else
        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RETURN:
                scanner.nextLookahead();
                return new TreeNode("Statement", new TreeNode("return", parseFunction(), parseFunky()), parseExpression());
            case FunkyToken.IF:
                TreeNode iF = new TreeNode(token.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                FunkyToken leftParenToken = scanner.lookahead();
                TreeNode leftparen = new TreeNode(leftParenToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                TreeNode expression = parseExpression();

                FunkyToken rightParenToken = scanner.lookahead();
                TreeNode rightparen = new TreeNode(rightParenToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                FunkyToken leftBraceToken = scanner.lookahead();
                TreeNode leftbrace = new TreeNode(leftBraceToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                TreeNode statements = parseStatements();

                FunkyToken rightBraceToken = scanner.lookahead();
                TreeNode rightbrace = new TreeNode(rightBraceToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                TreeNode parseElse = parseElse();

                return new TreeNode("Statement", List.of(new TreeNode[]{iF, leftparen, expression, rightparen, leftbrace, statements, rightbrace, parseElse}));
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }

    /* Parse Else
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseElse() {
        // Assume predictor set is
        // } if return        : Else -> epsilon
        // else               : Else -> else { Statements }

        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RIGHT_BRACE:
            case FunkyToken.IF:
            case FunkyToken.RETURN:
                return new TreeNode("Else", (List<TreeNode>) new TreeNode("", parseFunction(), parseFunky()));
            case FunkyToken.ELSE:
                TreeNode Else = new TreeNode(token.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                FunkyToken leftBraceToken = scanner.lookahead();
                TreeNode leftbrace = new TreeNode(leftBraceToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                TreeNode statements = parseStatements();

                FunkyToken rightBraceToken = scanner.lookahead();
                TreeNode rightbrace = new TreeNode(rightBraceToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                return new TreeNode("Else", List.of(new TreeNode[]{Else, leftbrace, statements, rightbrace}));
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }

    /* Parse Expression
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseExpression() {
        // Assume predictor set is
        // NUMBER             : Expression -> NUMBER
        // STRING             : Expression -> STRING
        // id                 : Expression -> id Args
        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.NUMBER:
            case FunkyToken.STRING:
                scanner.nextLookahead();
                return new TreeNode("Expression", (List<TreeNode>) new TreeNode(token.toString(), parseFunction(), parseFunky()));
            case FunkyToken.ID:
                scanner.nextLookahead();
                return new TreeNode("Expression", new TreeNode(" " + token, parseFunction(), parseFunky()), parseArgs());
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }

    /* Parse Args
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseArgs() {
        // Assume predictor set is
        // } ) STRING NUMBER id : Args -> epsilon
        // (                    : Args -> ( Arguments )

        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RIGHT_BRACE:
            case FunkyToken.RIGHT_PAREN:
            case FunkyToken.STRING:
            case FunkyToken.NUMBER:
            case FunkyToken.ID:
                return new TreeNode("Args", (List<TreeNode>) new TreeNode(" ", parseFunction(), parseFunky()));
            case FunkyToken.LEFT_PAREN:
                TreeNode left = new TreeNode(token.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                TreeNode arguments = parseArguments();
                FunkyToken rightToken = scanner.lookahead();

                TreeNode right = new TreeNode(rightToken.toString(), parseFunction(), parseFunky());
                scanner.nextLookahead();

                return new TreeNode("Args", left, arguments);
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }

    /* Parse Arguments
     * Params: none
     * Returns: parse tree (TreeNode)
     */
    private TreeNode parseArguments() {
        // Assume predictor set is
        // )                  : Arguments -> epsilon
        // NUMBER STRING id   : Arguments -> Expression Arguments
        FunkyToken token = scanner.lookahead();
        switch (token.getType()) {
            case FunkyToken.RIGHT_PAREN:
                return new TreeNode("Arguments", (List<TreeNode>) new TreeNode(" ", parseFunction(), parseFunky()));
            case FunkyToken.NUMBER:
            case FunkyToken.STRING:
            case FunkyToken.ID:
                return new TreeNode("Arguments", parseExpression(), parseArguments());
        }
        return new TreeNode("ERROR: " + token, parseFunction(), parseFunky());
    }
}

