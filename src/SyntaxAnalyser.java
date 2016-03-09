import com.sun.xml.internal.ws.api.pipe.NextAction;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author: Ben Freke
 */
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
        acceptTerminal(2);
        _statementList_();
    }

    void _statementList_() throws IOException, CompilationException {
        while (nextToken.symbol != Token.endSymbol)
        {
            _statement_();
            if (nextToken.symbol != Token.endSymbol) acceptTerminal(Token.semicolonSymbol);
            else {
                acceptTerminal(Token.endSymbol);
                break;
            }
        }
    }

    void _statement_() throws IOException, CompilationException {
        switch (nextToken.symbol)
        {
            case 3: /**Procedure**/
                acceptTerminal(3);
                _procedureStatement_();
                break;
            case 7: /**Do**/
                acceptTerminal(7);
                _untilStatement_();
                break;
            case 16: /**Identifier**/
                acceptTerminal(16);
                _assignmentStatement_();
                break;
            case 17: /**If**/
                acceptTerminal(17);
                _ifStatement_();
                break;
            case 36: /**While**/
                acceptTerminal(36);
                _condition_();
                acceptTerminal(23);
                _statementList_();
                acceptTerminal(23);
                break;

        }
    }

    void _assignmentStatement_() throws IOException, CompilationException {
        acceptTerminal(1);
        switch (nextToken.symbol)
        {
            case 31:
                acceptTerminal(31);
                break;
            default:
                _expression_();
        }

    }

    void _procedureStatement_() throws IOException, CompilationException {
        acceptTerminal(16);
        acceptTerminal(20);
        _argumentList_();
        acceptTerminal(29);
    }

    void _argumentList_() throws IOException, CompilationException {
        switch (nextToken.symbol)
        {
            case 16:
                acceptTerminal(16);
                break;
            default:
                _argumentList_();
                acceptTerminal(5);
                acceptTerminal(16);
        }
    }

    void _untilStatement_() throws IOException, CompilationException {

        _statementList_();
        acceptTerminal(35);
        _condition_();

    }

    void _whileStatement_() throws IOException, CompilationException {}

    void _ifStatement_() throws IOException, CompilationException {
        _condition_();
        acceptTerminal(34);
        _statementList_();
        switch (nextToken.symbol)
        {
            case 8:
                acceptTerminal(8);
                acceptTerminal(17);
                break;
            case 9:
                acceptTerminal(9);
                _statementList_();
                acceptTerminal(8);
                acceptTerminal(17);
                break;
            default:
                acceptTerminal(Token.ifSymbol);
                break;
        }

    }

    void _expression_() throws IOException, CompilationException {

        switch (nextToken.symbol)
        {
            case 27:
                acceptTerminal(27);
                _term_();
                break;
            case 24:
                acceptTerminal(24);
                _term_();
                break;
            default:
                _term_();
                if (nextToken.symbol == 27 || nextToken.symbol == 24) _expression_();
                break;
        }

    }

    void _factor_() throws IOException, CompilationException {

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

    }

    void _term_() throws IOException, CompilationException {
        switch (nextToken.symbol)
        {
            case 6:
                acceptTerminal(6);
                _factor_();
                break;
            case 33:
                acceptTerminal(33);
                _factor_();
                break;
            default:
               _factor_();
                if (nextToken.symbol == 6 || nextToken.symbol == 33) _term_();
                break;
        }
    }

    void _condition_() throws IOException, CompilationException {
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
    }

    public void _conditionalOperator_()  throws IOException, CompilationException {
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
    }

    public boolean identifierToken() throws IOException, CompilationException
    {
        Token tok = lex.getNextToken();
        if (tok.symbol == 1)
        {
            tok = lex.getNextToken();
            if (tok.symbol == 26 || tok.symbol == 31 )
            {
                return true;
            }
            myGenerate.reportError(tok, "Assignment without valid identifier.");
            return false;
        }
        myGenerate.reportError(tok, "Identifier without equals sign.");
        return false;
    }

    @Override
    void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol != symbol) {
            myGenerate.reportError(nextToken, Token.getName(symbol));
            throw new CompilationException();
        } else {
            myGenerate.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        }
    }
}
