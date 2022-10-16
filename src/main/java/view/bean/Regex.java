package view.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Regex {

    //////////////////////////////////////////////////////////////////////
    public static final String ADDRESS = "[^0-9]+,[0-9]+,[^0-9]+,[^0-9]+";
    public static final String MAIL = "[a-z][\\w-_.]*@[a-z]+\\.[a-z]+";
    public static final String CELL = "3[0-9]{2}[0-9]{7}";
    public static final String ACCOUNT = "Standard";
    public static final String GENERIC_STRING  = "[\\w-_.]*";
    public static final String ALPHA_STRING = "[^0-9]+";
    //////////////////////////////////////////////////////////////////////

    /////////////////
    private Regex(){}
    /////////////////

    //////////////////////////////////////////////////////////////
    public static boolean isMatching(String value, String match){
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    //////////////////////////////////////////////////////////////

}
