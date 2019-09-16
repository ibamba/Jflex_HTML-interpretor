all : Main.class

Sym.class : Sym.java
	javac Sym.java

Token.class : Token.java Sym.class
	javac Token.java

Lexer.java : text.flex
	jflex text.flex

Lexer.class: Lexer.java Token.class
	javac Lexer.java

LectureEcriture.class : LectureEcriture.java
	javac LectureEcriture.java

ValueEnvironment.class : ValueEnvironment.java AbstractSynatx.class
	javac ValueEnvironment.java

AbstractSyntax.class : AbstractSyntax.java
	javac AbstractSyntax.java

ParserException.class : ParserException.java
	javac ParserException.java

LookAhead1.class : LookAhead1.java ParserException.class Lexer.class
	javac LookAhead1.java

Parser.class : Parser.java AbstractSyntax.class LookAhead1.class
	javac Parser.java

TestLexer.class : TestLexer.java Lexer.class
	javac TestLexer

Main.class : Main.java Parser.class
	javac Main.java

clean :
	-rm *.class Lexer.java testGood/*.html

veryclean : clean
	-rm *~
