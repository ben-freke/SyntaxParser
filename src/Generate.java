/**
 *
 * @author Ben Freke
 * (C) Ben Freke 2016
 * www.benfreke.com
 *
 */
public class Generate extends AbstractGenerate {
    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        String exception = "";
        if (explanatoryMessage == null)
        {
            switch (token.symbol) {
                case Token.elseSymbol:
                    exception = ("------- Error ------- \n Line: " + token.lineNumber + "\n Token: "+ token.text +" \n Message: Received: \"else\" without corresponding \"if\"");
                    break;
                case Token.thenSymbol:
                    exception = ("Error. Received \"then\" without corresponding \"if\"");
                    break;
                case Token.loopSymbol:
                    exception = ("Error. Received \"loop\" without corresponding \"while\"");
                    break;
                case Token.untilSymbol:
                    exception = ("Error. Received \"until\" without corresponding \"do\"");
                    break;
            }
        }
        else if (explanatoryMessage.equals("EOF"))exception = ("------- Error ------- \n Line: " + token.lineNumber + "\n Token: "+ token.text +"\n Expecting: " + explanatoryMessage + " \n Message: Received: \"" + token.text + "\" after calling \"end\"\n");
        else if (token.symbol == Token.endSymbol) exception = ("Error. Received: \"" + token.text + "\" on Line " + token.lineNumber + " after receiving \";\".");
        else if (token.text != "") exception = ("Error. Received: \"" + token.text + "\" on Line " + token.lineNumber + ". Expecting: \"" + explanatoryMessage + "\"");
        else exception = ("Error. Did not receive closing statement for \"" + explanatoryMessage + "\" on Line " + token.lineNumber);
        System.out.println(exception);
        throw new CompilationException(exception);

    }


}
