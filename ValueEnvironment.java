import java.util.*;

/**
   The class of the declarations;
   The list of color and abbreviations declarations
*/
class ValueEnvironment extends HashMap{

    /**
       Put the color constant on the list.
       @param the name of variable who represent the color identifient
       @param the identifient of this color
    */
    public void putColorCons(String name, String id) 
	throws Exception {
	this.put(name, id);
    }
    /**
       @return true if this string key is on list, false else
    */
    public boolean contains(String name){
	return this.containsKey(name);
    }
    /**
       return the value of the color string key
       @param the string key color
       @return the value of this key
    */
    public String getColorValue(String name) 
	throws Exception {
	return this.get(name).toString();
    }

    /**
       Put the abbreviation constant on the list.
       @param the abbreviation who will replace the expression
       @param word to abbreviate
    */
    public void putAbbCons(String abb, Corps corps) 
	throws Exception {
	this.put(abb, corps);
    }
    /**
       to get the integral expression value of the abbreviation
       @param the string key abbreviation
       @return the value of this key
    */
    public Corps getAbbValue(String abb) 
	throws Exception {
	return (Corps)(this.get(abb));
    }
}
