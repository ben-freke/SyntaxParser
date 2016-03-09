/**
 *
 * @author Ben Freke
 * (C) Ben Freke 2016
 * www.benfreke.com
 *
 */

import java.io.IOException;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
    public SyntaxAnalyser(String filename){
        try {
            lex = new LexicalAnalyser(filename);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    void _statementPart_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement part>");
        acceptTerminal(Token.beginSymbol);
        _statementList_();
        myGenerate.finishNonterminal("<statement part>");

    }

    void _statementList_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<statement list>");
        while (nextToken.symbol != Token.endSymbol)
        {
            _statement_();
            if (nextToken.symbol != Token.endSymbol) acceptTerminal(Token.semicolonSymbol);
            else {
                break;
            }
        }
        myGenerate.finishNonterminal("<statement list>");
        acceptTerminal(Token.endSymbol);
    }

    void _statement_() throws IOException, CompilationException {
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

    void _assignmentStatement_() throws IOException, CompilationException {
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

    void _procedureStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<procedure statement>");
        acceptTerminal(Token.callSymbol);
        acceptTerminal(Token.identifier);
        acceptTerminal(Token.leftParenthesis);
        _argumentList_();
        acceptTerminal(Token.rightParenthesis);
        myGenerate.finishNonterminal("<procedure statement>");

    }

    void _argumentList_() throws IOException, CompilationException {
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

    void _untilStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<until statement>");
        acceptTerminal(Token.doSymbol);
        _statementList_();
        acceptTerminal(Token.untilSymbol);
        _condition_();
        myGenerate.finishNonterminal("<until statement>");

    }

    void _whileStatement_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<while statement>");
        acceptTerminal(Token.whileSymbol);
        _condition_();
        acceptTerminal(Token.loopSymbol);
        _statementList_();
        acceptTerminal(Token.loopSymbol);
        myGenerate.finishNonterminal("<while statement>");

    }

    void _ifStatement_() throws IOException, CompilationException {
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

    void _expression_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<expression>");
        _term_();
        while (nextToken.symbol == Token.plusSymbol || nextToken.symbol == Token.minusSymbol)
        {
            acceptTerminal(nextToken.symbol);
            _term_();
        }

        myGenerate.finishNonterminal("<expression>");
    }

    void _factor_() throws IOException, CompilationException {
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

    void _term_() throws IOException, CompilationException {
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

    void _condition_() throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<condition>");
        acceptTerminal(16);
        _conditionalOperator_();
        switch (nextToken.symbol){
            case 16:
                acceptTerminal(16);
                break;
            case 26:
                acceptTerminal(26);
                break;
            case 31:
                acceptTerminal(31);
                break;
        }
        myGenerate.finishNonterminal("<condition>");

    }

    public void _conditionalOperator_()  throws IOException, CompilationException {
        myGenerate.commenceNonterminal("<conditional operator>");
        switch (nextToken.symbol){
            case 14:
                acceptTerminal(14);
                break;
            case 15:
                acceptTerminal(15);
                break;
            case 11:
                acceptTerminal(11);
                break;
            case 25:
                acceptTerminal(25);
                break;
            case 21:
                acceptTerminal(21);
                break;
            case 22:
                acceptTerminal(22);
                break;
        }
        myGenerate.finishNonterminal("<conditional operator>");

    }

    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol != symbol) {
            if (symbol == Token.semicolonSymbol)
            {
                myGenerate.reportError(nextToken, null);
            }
            myGenerate.reportError(nextToken, Token.getName(symbol));
            throw new CompilationException();
        } else {
            myGenerate.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        }
    }
}
