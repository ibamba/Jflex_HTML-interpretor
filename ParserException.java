public class ParserException extends Exception {
    public ParserException(String found, String expected, int line, int column) {
	super("Parser exception : '"+expected+"' expected but '"+found+"' found at line "+(line+1)+" and column "+(column+1));
    }
}
