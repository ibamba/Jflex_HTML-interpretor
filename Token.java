class Token {

    protected Sym symbol;
    
    public Token(Sym s) {
    	symbol=s;
    }
    
    public Sym symbol() {
    	return symbol;
    }
    
    public String toString(){
    	return "Symbol : "+this.symbol;
    }
}   

class StringToken extends Token {

    private String value;
    
    public StringToken(Sym s,String v) {
        super(s);
        value=v;
    }
    
    public String getValue(){
        return value;
    }
    
    public String toString(){
    	return "Symbol : "+symbol+" | Value : "+value;
    }
}

class AbbrevToken extends StringToken {
    private String value;    
    public AbbrevToken(Sym s,String v) {
        super(s, "\\"+v);
    }
}
