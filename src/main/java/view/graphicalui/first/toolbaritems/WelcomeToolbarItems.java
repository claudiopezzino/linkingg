package view.graphicalui.first.toolbaritems;

public enum WelcomeToolbarItems {
    LOGO,
    SEPARATOR_1,
    SOCIAL_BAR,
    SEPARATOR_2,
    INFO_BAR,
    SEPARATOR_3,
    SIGN_BAR;

    /*---------------------- Inner-Enum ------------------------*/

    ///////////////////////////
    public enum SignBarItems{
        SIGN_UP_BUTTON,
        SEPARATOR,
        SIGN_IN_BUTTON;


        /////////////////////////////////////////////////////
        public Integer getIndex(){
            return this.ordinal();
        }
        /////////////////////////////////////////////////////

    }
    /////////////////////////////////////////////////////////////

    /*-----------------------------------------------------------*/

    /* If necessary, set-up other inner-enum(s) to symbolize elements into WelcomeToolbarItems' enum(s) */

    /////////////////////////////////////////////////////
    public Integer getIndex(){
        return this.ordinal();
    }
    /////////////////////////////////////////////////////

}
