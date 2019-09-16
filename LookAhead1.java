import java.io.*;

class LookAhead1  {
    
    private Token current;
    private Lexer lexer;
    
    public LookAhead1(Lexer l) 
	throws Exception {
	lexer=l;
	current=lexer.yylex();
    }
    
    public boolean check(Sym s)
	throws Exception {
	return (current.symbol() == s); 
    }
    
    public void eat(Sym s) throws Exception {
	/* consumes a token of type s from the stream,
	   exception when the contents does not start on s.   */
	if(current == null){
	    throw new ParserException("Null", s.toString(), lexer.getLine(), lexer.getColumn());
	}
	if (!check(s)) {
	    throw new ParserException(current.toString(), s.toString(), lexer.getLine(), lexer.getColumn());
	}
	
	System.out.println(current);
	
	current=lexer.yylex();
    }
     
    public String getStringValue() throws Exception {
	if (current instanceof StringToken) {
	    StringToken t = (StringToken) current;
	    return t.getValue();
	} 
	throw new Exception("LookAhead error: get value from a non-valued token at line " +lexer.getLine()+", column "+lexer.getColumn());   
    }

    public String getString() { // outputs a string form of the current Token
	return current.toString();
    }

    public int getLine(){ // outputs the line of current token
	return lexer.getLine();
    }

    public int getColumn(){ // outputs the column of current token
	return lexer.getColumn();
    }
    
}
