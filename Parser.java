import java.util.*;

class Parser {
    
    /*
                     GRAMMAIRE
     *
     * DOCUMENT -> DECLARATIONS CORPS EOF
     * DECLARATIONS -> \set{ ID } { CONSTANTE_COULEUR } DECLARATIONS 
                       | \abb{VALABB}{SUITE_ELEMENTS} DECLARATIONS
		       | E
     * CORPS -> \begindoc SUITE_ELEMENTS \enddoc
     * SUITE_ELEMENTS -> ELEMENT SUITE_ELEMENTS | E
     * ELEMENT -> MOT
                  | VALABB
                  | \linebreak
		  | \bf{ SUITE_ELEMENTS }
		  | \it{ SUITE_ELEMENTS }
		  | \couleur{ VAL_COL } { SUITE_ELEMENTS }
		  | \for{ INT } { SUITE_ELEMENTS }
		  | ENUMERATION
     * VAL_COL -> CONSTANTE_COULEUR | ID
     * ENUMERATION -> \beginenum SUITE_ITEMS \endenum
     * SUITE_ITEMS -> ITEMS SUITE_ITEMS | E
     * ITEM -> \item SUITE_ELEMENTS
     */
    
    protected LookAhead1 reader;
    
    public Parser(LookAhead1 r) {
	reader=r;
    }

    public Program nontermDoc() throws Exception {
	ValueEnvironment dec = nontermDeclarations();
	Corps corps = nontermCorps();
	reader.eat(Sym.EOF);
	return new Program(dec, corps);
    }

    public ValueEnvironment nontermDeclarations() throws Exception {
	ValueEnvironment res = new ValueEnvironment();
	if(reader.check(Sym.SETTER)){ // declaration is color declaration
	    reader.eat(Sym.SETTER);
	    reader.eat(Sym.LACC);
	    String id = reader.getStringValue(); // color identifiant is a String
	    reader.eat(Sym.ID);
	    reader.eat(Sym.RACC);
	    reader.eat(Sym.LACC);
	    String name = reader.getStringValue(); // the variable name of the identifiant
	    reader.eat(Sym.MOT);
	    reader.eat(Sym.RACC);
	    res.putColorCons(name, id);
	    ValueEnvironment rec = nontermDeclarations(); // recursion, the suit of rule, for other declarations
	    res.putAll(rec); // We add news elements on res
	    return res;
	}
	if(reader.check(Sym.ABB)){ // declaration is for abbreviation
	    reader.eat(Sym.ABB);
	    reader.eat(Sym.LACC);
	    String abb = termValAbb();
	    reader.eat(Sym.RACC);
	    reader.eat(Sym.LACC);
	    Corps corps = nontermSuiteElements();
	    reader.eat(Sym.RACC);
	    res.putAbbCons(abb, corps);
	    ValueEnvironment rec = nontermDeclarations(); // recursion, the suit of rule, for other declarations
	    res.putAll(rec);
	}
	return res;
    }

    /**
       method to verify the token VALABB
       @return the value of the token
    */
    public String termValAbb() throws Exception {
	if(reader.check(Sym.VALABB)){
	    String res = reader.getStringValue();
	    reader.eat(Sym.VALABB);
	    return res;
	}
	throw new ParserException(reader.getString(), Sym.VALABB.toString(), reader.getLine(), reader.getColumn());
    }
    
    public Corps nontermCorps() throws Exception {
	reader.eat(Sym.BEGINDOC);
	Corps res = nontermSuiteElements();
	reader.eat(Sym.ENDDOC);
	return res;
    }
    
    public Corps nontermSuiteElements() throws Exception {
	Corps res = new Corps();
	if(!reader.check(Sym.ENDDOC) && !reader.check(Sym.RACC) && !reader.check(Sym.ITEM) && !reader.check(Sym.ENDENUM)){
	    // the rule is not upsilon (empty)
	    res.add(nontermElement());
	    Corps resRec = nontermSuiteElements(); // recursion, the suit of rule, for others elements list
	    for(int i = 0; i < resRec.size(); i++)
		res.add(resRec.get(i));
	}
	return res;
    }

    public Element nontermElement() throws Exception {
	if (reader.check(Sym.MOT)) {
	    Element res = new Mot(reader.getStringValue());
	    reader.eat(Sym.MOT);
	    return res;
	}
	if(reader.check(Sym.VALABB)){
	    return new Abbrev(termValAbb());
	}
	if (reader.check(Sym.LINEBREAK)) {
	    Element res = new Linebreak();
	    reader.eat(Sym.LINEBREAK);
	    return res;
	}
	if (reader.check(Sym.BF)) {
	    reader.eat(Sym.BF);
	    reader.eat(Sym.LACC);
	    Corps c = nontermSuiteElements();
	    reader.eat(Sym.RACC);
	    return new BoldFace(c);
	}
	if (reader.check(Sym.IT)) {
	    reader.eat(Sym.IT);
	    reader.eat(Sym.LACC);
	    Corps c = nontermSuiteElements();
	    reader.eat(Sym.RACC);
	    return new Italic(c);
	}
	if(reader.check(Sym.COLOR)){
	    reader.eat(Sym.COLOR);
	    reader.eat(Sym.LACC);
	    String col = termVal_Col();
	    reader.eat(Sym.RACC);
	    reader.eat(Sym.LACC);
	    Corps c = nontermSuiteElements();
	    reader.eat(Sym.RACC);
	    return new toColor(col, c);
	}
	// Bonus : boucle for
	if(reader.check(Sym.FOR)){
	    reader.eat(Sym.FOR);
	    reader.eat(Sym.LACC);
	    int varboucle = 0;
	    try{
		varboucle = Integer.parseInt(reader.getStringValue());
	    }
	    catch(Exception e){
		throw new ParserException(reader.getString(), "INT", reader.getLine(), reader.getColumn());
	    }
	    reader.eat(Sym.MOT);
	    reader.eat(Sym.RACC);
	    reader.eat(Sym.LACC);
	    Corps c = nontermSuiteElements();
	    reader.eat(Sym.RACC);
	    return new For(varboucle, c);	    
	}
	else{
	    return nontermEnumeration();
	}   	
    }

    
    /**
       method to get the value of color
       @return the value of the token
    */
    public String termVal_Col() throws Exception {
	if(reader.check(Sym.MOT)){
	    String res = reader.getStringValue();
	    reader.eat(Sym.MOT);
	    return res;
	}
	else{
	    String res = reader.getStringValue();
	    reader.eat(Sym.ID);
	    return res;
	}
    }
    
    public Element nontermEnumeration() throws Exception {
	if(reader.check(Sym.BEGINENUM)){
	    reader.eat(Sym.BEGINENUM);
	    ArrayList<Item> items = nontermSuiteItems();
	    reader.eat(Sym.ENDENUM);
	    return new Enumeration(items);
	}
	throw new ParserException(reader.getString(), "ELEMENT", reader.getLine(), reader.getColumn());
    }

    public ArrayList<Item> nontermSuiteItems() throws Exception {
	ArrayList<Item> res = new ArrayList<Item>();
	if(!reader.check(Sym.ENDENUM)){
	    res.add(nontermItem());
	    ArrayList<Item> resRec = nontermSuiteItems();
	    for(int i = 0; i < resRec.size(); i++)
		res.add(resRec.get(i));
	}
	//The upsilon rule case
	return res;
    }
    
    public Item nontermItem() throws Exception {
	reader.eat(Sym.ITEM);
	return new Item(nontermSuiteElements());
    }
    
}
