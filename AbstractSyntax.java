import java.util.*;

class Program{
    /**
       The integral SimpleText code : declarations are beginning of file and the body (corps) at suit
    */  
    private ValueEnvironment declarations;
    private Corps corps;
    public Program(ValueEnvironment d, Corps c){
	declarations = d;
	corps = c;
    }
    /**
       Traduct the simpleText code(program) to html code
       @return the html code on string form
    */
    public String toHTML() throws Exception {
	//biginning of html codes
	String html = "<!DOCTYPE html>\n<html>\n\t<head>\n\t\t<title>Rendu de votre code</title>\n\t</head>\n\t<body>\n\t\t";
	html += corps.toHTML(declarations, 2);
	html +="\n\t</body>\n</html>"; //end of html code
	return html;
    }
}

class Corps extends ArrayList<Element> {
    /**
       "Corps" is the body of the SimpleText code.
       It is a "elements" list
    */
    /**
       "toHTML" of "Corps" gives the reprensantation of the elements SimpleText code on html
       @param list of declarations
       @param an integer for indentation, for a good writing of the html code
    */
    public String toHTML(ValueEnvironment dec, int indent) throws Exception {
	String res = "";
	for(Element elt : this)
	    res += elt.toHTML(dec, indent);
	return res;
    }
}

abstract class Element {
    abstract String toHTML(ValueEnvironment dec, int indent)
	throws Exception;
    /**
       Element is an abstract class. He cans be a world, italic, boldface...
       His method "toHTML" gives the reprensantation of the elements SimpleText code on html
       @param list of declarations
       @param an integer for indentation, for a good writing of the html code
    */
}

class Mot extends Element { // for the simple word
    private String value; // value of world
    public Mot(String s) {
	value = s;
    }
    
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	return value;
    } 
}

class Abbrev extends Element { // for the abbreviation on the body of the code
    private String valabb; // the abbreviation
    public Abbrev(String v) {
	valabb = v;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	if(dec.contains(valabb)){
	    return dec.getAbbValue(valabb).toHTML(dec, indent);
	}
	throw new Exception("Abbreviation '"+valabb+"' is not declared");
    }
}


class Linebreak extends Element {
    public Linebreak() {
    }
    
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	String res = "\n";
	for(int i = 0; i<indent; i++)
	    res += "\t";
	return res+"<br>";
    }
}

class BoldFace extends Element {
    private Corps corps; // text to put on boldface
    public BoldFace(Corps c) {
	corps = c;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	return "<b>"+corps.toHTML(dec, indent+1)+"</b>";
    }
}

class Italic extends Element {
    private Corps corps; // text to put in italic
    public Italic(Corps c) {
	corps = c;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	return "<i>"+corps.toHTML(dec, indent+1)+"</i>";
    }
}

class toColor extends Element { // to put the specify text part on color
    private String id; // color identifient
    private Corps corps; // text to color
    public toColor(String i, Corps c) {
	id = i;
	corps = c;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	if(dec.contains(id)){
	    return "<Font Color = \"" + dec.getColorValue(id) + "\">" + corps.toHTML(dec, indent) + "</Font>";
	}
	else if(id.length() == 6 && id.charAt(0) == id.toUpperCase().charAt(0)) // identifiant de 6 caractères qui commence par une majuscule ou un chiffre, à l'inverse d'une variable de declaration...
	    return "<Font Color = \"" + id + "\">" + corps.toHTML(dec, indent) + "</Font>";
	throw new Exception("color ident '"+id+"' is not recognized");
    }
}

class For extends Element {
    /**
       This class is a bonus.
       The boucle For permit to repeat a part of the text ...
       @param int varboucle : the boucle variable. We reapet v times the text part
       @param Corps corps : The part of the text to repeat
    */
    private int varboucle;
    private Corps corps;
    public For(int v, Corps c){
	varboucle = v;
	corps = c;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	String res = "";
	for(int i = 0; i < varboucle; i++)
	    res += corps.toHTML(dec, indent);
	return res;
    }
}

class Enumeration extends Element { // list of items
    private ArrayList<Item> items;
    public Enumeration(ArrayList<Item> i) {
	items = i;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	String res = "\n";
	for(int i = 0; i<indent; i++)
	    res += "\t";
	res += "<ol>\n";
	for(int i = 0; i < items.size(); i++){
	    for(int j = 0; j<indent+1; j++)
		res += "\t";
	    res += "<li>" +items.get(i).toHTML(dec, indent+1)+ "</li>\n";
	}
	for(int i = 0; i<indent; i++)
	    res += "\t";
	res += "</ol>\n";
	return res;
    }
}

class Item {
    private Corps corps;
    public Item(Corps c){
	corps = c;
    }
    public String toHTML(ValueEnvironment dec, int indent) 
	throws Exception {
	return corps.toHTML(dec, indent+1);
    }
}
