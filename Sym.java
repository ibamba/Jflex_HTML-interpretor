public enum Sym {
    BEGINDOC,
    ENDDOC,
    MOT,
    LINEBREAK,
    BF, // BoldFace
    IT, // Italic
    LACC, // Light accolade
    RACC, // Right accolade
    BEGINENUM,
    ENDENUM,
    ITEM,
    SETTER,
    COLOR,
    ID, // color indentifiant
    ABB, // abbreviation token
    VALABB, // for abbreviation declaration
    FOR, // bonus : boucle for, for repitition
    EOF;  // token representinting the end of file
}
