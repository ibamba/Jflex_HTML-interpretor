import java.io.*;
import java.util.*;

public class LectureEcriture{

    private PrintWriter fW; //Pour l'écriture
    private Scanner fR; //Pour la lecture
    private char mode; //Pour savoir si on ouvre le fichier en lecture ou en écriture

    /**
       Open the file
       @param the path of the file
       @param the mode to know if the file is opened to read or to write
       @return true if the file succesfuly opened, false else
    */
    public boolean ouvrir(String nomDuFichier, String s){
	
	try{
	    mode = (s.toUpperCase()).charAt(0);
	    if(mode == 'R')
		fR = new Scanner(new File(nomDuFichier));
	    else if(mode == 'W')
		fW = new PrintWriter(new BufferedWriter(new FileWriter(nomDuFichier)));
	    else{
		System.out.println("format error");
		return false;
	    }
	    
	    return true;
	}
	catch(IOException e){
	    return false;
	}
    }
    
    public void ecrire(String chaine){
	fW.println(chaine);	
    }

    /**
       read the file and return his string restrained
       @return the string restrained of file
    */
    public String lire() throws IOException{
	String res = "";
	while(fR.hasNextLine()){
	    res += fR.nextLine();
	}
	return res;
    }
    
    public void fermer() throws IOException{
	if(mode == 'R')
	    fR.close();
	else if(mode == 'W')
	    fW.close();
	else
	    System.out.println("Error : not file to close");
    }
}
