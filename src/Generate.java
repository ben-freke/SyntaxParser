/**
 * Created by benfreke on 07/03/2016.
 */
public class Generate extends AbstractGenerate {
    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        if (token.text != "") System.out.println("Woah. Received: \"" + token.text + "\" on Line " + token.lineNumber + ". Expecting: \"" + explanatoryMessage + "\"");
        else System.out.println ("Woah. Did not receive closing statement \"" + explanatoryMessage + "\" on Line " + token.lineNumber);
    }


}
