package model;

/* some extra info because of necessity for "updateUserNick(...)" [Class::Loader] and map's keys for Observer Pattern */
public final class UserInfo {

    //////////////////////////////////////////////////
    public static final String CURR_USER = "currUser";
    //////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    public static final String GROUP_OWNER = "groupOwner";
    public static final String GROUP_MEMBER = "groupMember";
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public static final String GROUP_NICK = "groupNick";
    public static final String CHOICE = "choice";
    ////////////////////////////////////////////////////


    ////////////////////
    private UserInfo(){}
    ////////////////////
}
