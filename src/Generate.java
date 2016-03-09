/**
 *
 * @author Ben Freke
 * (C) Ben Freke 2016
 * www.benfreke.com
 *
 */
public class Generate extends AbstractGenerate {

    /**
     * Receives Token token and String explanatoryMessage to generate an error and output this to the user.
     * It begins by spliting the explanatoryMessage to establish the filename.
     * Then, this establishes what error it is actually dealing with. For example, if the SyntaxAnalyser reads
     * in a blank token, when it is expecting an if or while, it may indicate that the programmer has not closed
     * an if statement (e.g. while \<statement\> end).
     * @param token
     * @param explanatoryMessage
     * @throws CompilationException
     */

    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        String exception = "";
        String[] parts = explanatoryMessage.split(",");
        explanatoryMessage = parts[0];
        String filename = parts[1];
        if (explanatoryMessage.equals(""))
        {
            switch (token.symbol) {
                case Token.elseSymbol:
                    exception = (   "\r\n------- Error ------- \r\n " +
                                    "File: " + filename + "\r\n " +
                                    "Line: " + token.lineNumber + "\r\n " +
                                    "Token: "+ token.text +" \r\n " +
                                    "Message: Received: \"else\" without corresponding \"if\"\r\n");
                    break;
                case Token.thenSymbol:
                    exception = (   "\r\n------- Error ------- \r\n " +
                            "File: " + filename + "\r\n " +
                            "Line: " + token.lineNumber + "\r\n " +
                            "Token: "+ token.text +" \r\n " +
                            "Message: Received: \"then\" without corresponding \"if\"\r\n");
                    break;
                case Token.loopSymbol:
                    exception = (   "\r\n------- Error ------- \r\n " +
                            "File: " + filename + "\r\n " +
                            "Line: " + token.lineNumber + "\r\n " +
                            "Token: "+ token.text +" \r\n " +
                            "Message: Received: \"loop\" without corresponding \"while\"\r\n");
                    break;
                case Token.untilSymbol:
                    exception = (   "\r\n------- Error ------- \r\n " +
                            "File: " + filename + "\r\n " +
                            "Line: " + token.lineNumber + "\r\n " +
                            "Token: "+ token.text +" \r\n " +
                            "Message: Received: \"until\" without corresponding \"do\"\r\n");
                    break;
            }
        }
        else if (explanatoryMessage.equals("EOF"))
            exception =
                        (
                                "\r\n------- Error ------- \r\n " +
                                "File: " + filename + "\r\n " +
                                "Line: " + token.lineNumber + "\r\n " +
                                "Token: " + token.text +"\r\n " +
                                "Expecting: " + explanatoryMessage + " " +
                                "\r\n Message: Received \"" + token.text + "\" after calling \"end\"\r\n"
                        );

        else if (token.symbol == Token.endSymbol)
            exception =
                (
                        "\r\n------- Error ------- \r\n " +
                                "File: " + filename + "\r\n " +
                                "Line: " + token.lineNumber + "\r\n " +
                                "Token: " + token.text +"\r\n " +
                                "Expecting: " + explanatoryMessage + " " +
                                "\r\n Message: Received \"" + token.text + "\" after calling \";\"\r\n"
                );

        else if (token.text != "")

            exception =
                    (
                            "\r\n------- Error ------- \r\n " +
                                    "File: " + filename + "\r\n " +
                                    "Line: " + token.lineNumber + "\r\n " +
                                    "Token: " + token.text +"\r\n " +
                                    "Expecting: " + explanatoryMessage + " " +
                                    "\r\n Message: Received \"" + token.text + "\"\r\n"
                    );

        else

            exception =
                    (
                            "\r\n------- Error ------- \r\n " +
                                    "File: " + filename + "\r\n " +
                                    "Line: " + token.lineNumber + "\r\n " +
                                    "Token: " + token.text +"\r\n " +
                                    "Expecting: " + explanatoryMessage + " " +
                                    "\r\n Message: Did not receive closing statement for " + explanatoryMessage + "\r\n"
                    );

        System.out.println(exception);
        throw new CompilationException(exception);

    }

}
