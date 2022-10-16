package view.graphicalui.second;

public enum SignupInfo {
    OWN_NAME,
    OWN_SURNAME,
    ADDRESS,
    MAIL,
    CELL,
    ACCOUNT;

    public Integer getIndex(){
        return this.ordinal();
    }
}
