package view.graphicalui.second;

public enum SigninInfo {
    OWN_NICKNAME,
    OWN_PASSWORD;

    public Integer getIndex(){
        return this.ordinal();
    }
}
