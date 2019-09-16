
%%
%public
%class Lexer
%unicode
%type Token
%line
%column

%{
  //Pour gérer les exceptions
  public int getColumn(){
  	 return this.yycolumn;
  }

  public int getLine(){
  	  return this.yyline;
  }

%}

%yylexthrow Exception
%state SPECIAL
%state ID

blank = [\n\r\t] 
mot = [^\n\r\t\\{}#]+
id = [0-9A-F]{6}

%%
<YYINITIAL> {
 "\\"			{yybegin(SPECIAL);}
 "#"			{yybegin(ID);}
 "{"			{return new Token(Sym.LACC);}
 "}"			{return new Token(Sym.RACC);}
 {mot}			{return new StringToken(Sym.MOT,yytext());}
 {blank}		{}
  <<EOF>>    		{return new Token(Sym.EOF);}
}
<SPECIAL> {
 "begindoc"		{yybegin(YYINITIAL);return new Token(Sym.BEGINDOC);}
 "enddoc"	 	{yybegin(YYINITIAL);return new Token(Sym.ENDDOC);}
 "linebreak"		{yybegin(YYINITIAL);return new Token(Sym.LINEBREAK);}
 "bf"			{yybegin(YYINITIAL);return new Token(Sym.BF);}
 "it"			{yybegin(YYINITIAL);return new Token(Sym.IT);}
 "beginenum"		{yybegin(YYINITIAL);return new Token(Sym.BEGINENUM);}
 "endenum"		{yybegin(YYINITIAL);return new Token(Sym.ENDENUM);}
 "item"			{yybegin(YYINITIAL);return new Token(Sym.ITEM);}
 "set"			{yybegin(YYINITIAL);return new Token(Sym.SETTER);}
 "couleur"		{yybegin(YYINITIAL);return new Token(Sym.COLOR);}
 "abb"			{yybegin(YYINITIAL);return new Token(Sym.ABB);}
 "for"			{yybegin(YYINITIAL);return new Token(Sym.FOR);}
 [^\n\r\t\\{}# ]+	{yybegin(YYINITIAL);return new AbbrevToken(Sym.VALABB, yytext());}
}
<ID> {
 {id}			{yybegin(YYINITIAL);return new StringToken(Sym.ID, yytext());}
 [^]			{throw new Exception("Lexer exception at line "+ (yyline+1) + " column " + (yycolumn+1));}
}