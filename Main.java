import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
	if (args.length < 1) {
	    System.out.println("<filename> expected : java Main <filename>");
	    System.exit(1);
	}
	
	File input = new File(args[0]);
	Reader reader = new FileReader(input);
	Lexer lexer = new Lexer(reader);
	LookAhead1 look = new LookAhead1(lexer);
	
	Parser parser = new Parser(look);
	Program prog = null; 
	try {
	    prog = parser.nontermDoc();
	    System.out.println("The code is correct.\n");
	}
	catch (Exception e){
	    System.out.println("The code is not correct.");
	    System.out.println(e);
	    System.exit(1);
	}
	try {
	    String html = prog.toHTML();
	    LectureEcriture file = new LectureEcriture();
	    String nom = args[0]+".html";
	    if(file.ouvrir(nom, "W")){
		file.ecrire(html);
		file.fermer();
		System.out.println("Program successfully compiled to HTML\nThrow the file "+nom);
	    }
	    else
		System.out.println("Open file error");
	}
	catch (Exception e){
	    System.out.println("Compilation error");
	    System.out.println(e);
	}
    }
}
