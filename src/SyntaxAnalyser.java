/**
 *
 * @author Ben Freke
 * (C) Ben Freke 2016
 * www.benfreke.com
 *
 */

import java.io.IOException;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
    private String file;

    /**
     * Super constructor which creates the Lexical Analyser from the filename.
     * @param filename
     */
    public SyntaxAnalyser(String filename){
        file = filename;
        try {
            lex = new LexicalAnalyser(filename);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Inherited from abstract class.
     * Begins the work on the top level token.
     * @throws IOException
     * @throws CompilationException
     */
    @Override
    public void _statementPart_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement part>");
        acceptTerminal(Token.beginSymbol);
        _statementList_();
        myGenerate.finishNonterminal("<statement part>");

    }

    /**
     * Loops through each statement encapsulated with a 'begin' and 'end'
     * @throws IOException
     * @throws CompilationException
     */
    private void _statementList_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement list>");
        while (nextToken.symbol != Token.endSymbol)
        {
            _statement_();
            if (nextToken.symbol != Token.endSymbol){
                acceptTerminal(Token.semicolonSymbol);
                if (nextToken.symbol == Token.endSymbol) acceptTerminal(0);
            }
            else {
                break;
            }
        }
        myGenerate.finishNonterminal("<statement list>");
        acceptTerminal(Token.endSymbol);
    }

    /**
     * Takes the first token of a statement and determines which type of statement it is, for example a procedure.
     * Once the type has been determined, the method runs the relevant method representing a non-terminal.
     * @throws IOException
     * @throws CompilationException
     */
    private void _statement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement>");
        switch (nextToken.symbol)
        {
            case Token.callSymbol: /**Procedure**/
                _procedureStatement_();
                break;
            case Token.untilSymbol: /**Until**/
                _untilStatement_();
                break;
            case Token.identifier: /**Identifier**/
                _assignmentStatement_();
                break;
            case Token.ifSymbol: /**If**/
                _ifStatement_();
                break;
            case Token.whileSymbol: /**While**/
                _whileStatement_();
                break;
        }
        myGenerate.finishNonterminal("<statement>");

    }

    /**
     * The method for the non-terminal assignment statement. Assignment statement must
     * be a identifier becoming an expression, or an identifier becoming a stringConstant
     * @throws IOException
     * @throws CompilationException
     */
    private void _assignmentStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<assignment statement>");
        acceptTerminal(Token.identifier);
        acceptTerminal(Token.becomesSymbol);
        switch (nextToken.symbol)
        {
            case Token.stringConstant:
                acceptTerminal(Token.stringConstant);
                break;
            default:
                _expression_();
        }
        myGenerate.finishNonterminal("<assignment statement>");
    }

    /**
     * The method for the non-terminal procedure statement. Procedure statement must have terminal 'call' followed
     * by an identifier and then the non-terminal argument list
     * @throws IOException
     * @throws CompilationException
     */
    private void _procedureStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<procedure statement>");
        acceptTerminal(Token.callSymbol);
        acceptTerminal(Token.identifier);
        acceptTerminal(Token.leftParenthesis);
        _argumentList_();
        acceptTerminal(Token.rightParenthesis);
        myGenerate.finishNonterminal("<procedure statement>");

    }

    /**
     * The method for the non-terminal argument list. This can either be an identifier, or an argument list followed
     * by an identifier.
     * @throws IOException
     * @throws CompilationException
     */
    private void _argumentList_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<argument list>");
        switch (nextToken.symbol)
        {
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            default:
                _argumentList_();
                acceptTerminal(Token.commaSymbol);
                acceptTerminal(Token.identifier);
        }
        myGenerate.finishNonterminal("<argument list>");

    }

    /**
     * The method for the non-terminal until statement. This must receive terminal do, followed by a statement list,
     * followed by terminal until, followed by a condition.
     * @throws IOException
     * @throws CompilationException
     */
    private void _untilStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<until statement>");
        acceptTerminal(Token.doSymbol);
        _statementList_();
        acceptTerminal(Token.untilSymbol);
        _condition_();
        myGenerate.finishNonterminal("<until statement>");

    }


    /**
     * The method for the non-terminal until statement. This statement must receive terminal while, followed by a
     * condition, followed by terminal loop, followed by a statement list, followed by terminals end and loop.
     * @throws IOException
     * @throws CompilationException
     */
    private void _whileStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<while statement>");
        acceptTerminal(Token.whileSymbol);
        _condition_();
        acceptTerminal(Token.loopSymbol);
        _statementList_();
        acceptTerminal(Token.loopSymbol);
        myGenerate.finishNonterminal("<while statement>");

    }

    /**
     * The method for the non-terminal if statement. This statement must receive terminal if, followed by non-terminal
     * condition, followed by terminal then, followed by non-terminal statement list and finally followed by terminals
     * end and if
     * @throws IOException
     * @throws CompilationException
     */
    private void _ifStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<if statement>");
        acceptTerminal(Token.ifSymbol);
        _condition_();
        acceptTerminal(Token.thenSymbol);
        _statementList_();
        switch (nextToken.symbol)
        {
            case Token.endSymbol:
                acceptTerminal(Token.endSymbol);
                acceptTerminal(Token.ifSymbol);
                break;
            case Token.elseSymbol:
                acceptTerminal(Token.elseSymbol);
                _statementList_();
                acceptTerminal(Token.endSymbol);
                acceptTerminal(Token.ifSymbol);
                break;
            default:
                acceptTerminal(Token.ifSymbol);
                break;
        }
        myGenerate.finishNonterminal("<if statement>");
    }

    /**
     * The method for the non-terminal expression. This can either be the non-terminal term, or terminal + followed by
     * non-terminal term, or terminal - followed by non-terminal term
     * @throws IOException
     * @throws CompilationException
     */
    private void _expression_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<expression>");
        _term_();
        while (nextToken.symbol == Token.plusSymbol || nextToken.symbol == Token.minusSymbol)
        {
            acceptTerminal(nextToken.symbol);
            _term_();
        }

        myGenerate.finishNonterminal("<expression>");
    }

    /**
     * The method for the non-terminal factor. This can either by an identifier, or a number constant, or an expression
     * @throws IOException
     * @throws CompilationException
     */
    private void _factor_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<factor>");
        switch (nextToken.symbol)
        {
            case 16:
                acceptTerminal(16);
                break;
            case 26:
                acceptTerminal(26);
                break;
            case 20:
                acceptTerminal(20);
                _expression_();
                acceptTerminal(29);
                break;
            default:
                break;
        }
        myGenerate.finishNonterminal("<factor>");

    }

    /**
     * The method for the non-terminal term. This can either be a factor, or a terminal * followed by a factor, or a
     * terminal / followed by a factor
     * @throws IOException
     * @throws CompilationException
     */
    private void _term_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<term>");
        _factor_();
        while (nextToken.symbol == Token.timesSymbol || nextToken.symbol == Token.divideSymbol)
        {
            acceptTerminal(nextToken.symbol);
            _factor_();
        }
        //if (nextToken.symbol == Token.divideSymbol || nextToken.symbol == Token.timesSymbol) _term_();
        myGenerate.finishNonterminal("<term>");
    }

    /**
     * The method for the non-terminal condition. This can either be the terminal identifier, followed by a
     * conditional operator, followed by another identifier, or be an identifier followed by a conditional operator
     * followed by a number constant, or the same but followed by a string constant
     * @throws IOException
     * @throws CompilationException
     */
    private void _condition_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<condition>");
        acceptTerminal(16);
        _conditionalOperator_();
        switch (nextToken.symbol){
            case Token.identifier:
                acceptTerminal(Token.identifier);
                break;
            case Token.numberConstant:
                acceptTerminal(Token.numberConstant);
                break;
            case Token.stringConstant:
                acceptTerminal(Token.stringConstant);
                break;
        }
        myGenerate.finishNonterminal("<condition>");

    }

    /**
     * The method for the non-terminal conditional operator, which ensures the token meets the possible conditional
     * operators available
     * @throws IOException
     * @throws CompilationException
     */
    private void _conditionalOperator_()  throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<conditional operator>");
        switch (nextToken.symbol){
            case Token.greaterEqualSymbol:
                acceptTerminal(Token.greaterEqualSymbol);
                break;
            case Token.greaterThanSymbol:
                acceptTerminal(Token.greaterThanSymbol);
                break;
            case Token.equalSymbol:
                acceptTerminal(Token.equalSymbol);
                break;
            case Token.notEqualSymbol:
                acceptTerminal(Token.notEqualSymbol);
                break;
            case Token.lessEqualSymbol:
                acceptTerminal(Token.lessEqualSymbol);
                break;
            case Token.lessThanSymbol:
                acceptTerminal(Token.lessThanSymbol);
                break;
        }
        myGenerate.finishNonterminal("<conditional operator>");

    }

    /**
     * Receives integer symbol (the integer representation of the received token). From this, checks whether the
     * symbol is expected. If it is, it runs myGenerate.insertTerminal(nextToken) and moves the lexical analyser
     * forwards, otherwise it generates an error, reporting this with the myGenerate class and throwing a
     * CompilationException.
     * @param symbol
     * @throws IOException
     * @throws CompilationException
     */
    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol != symbol) {
            if (symbol == Token.semicolonSymbol)
            {
                myGenerate.reportError(nextToken, "," + file);
            }
            myGenerate.reportError(nextToken, (symbol == 0 ? "Statement" : Token.getName(symbol)) + "," + file);
        } else {
            myGenerate.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        }
    }
}
