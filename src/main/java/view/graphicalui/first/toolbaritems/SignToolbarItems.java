package view.graphicalui.first.toolbaritems;


public enum SignToolbarItems {
    LOGO,
    SEPARATOR_1,
    SOCIAL_BAR,
    SEPARATOR_2,
    TITLE,
    SEPARATOR_3,
    MAIN_BAR,
    SEPARATOR_4,
    UNDO_REDO_BAR;

    /*---------------------- Inner-Enum ------------------------*/

    /////////////////////////////
    public enum MainBarItems{
        HOME_BUTTON,
        SEPARATOR,
        REFRESH_BUTTON;

        /////////////////////////////////////////////////////
        public Integer getIndex(){
            return this.ordinal();
        }
        /////////////////////////////////////////////////////
    }
    //////////////////////////////////////////////////////////

    /*-----------------------------------------------------------*/

    /* If necessary, set-up other inner-enum(s) to symbolize elements into SignToolbarItems' enum(s) */

    /////////////////////////////////////////////////////
    public Integer getIndex(){
        return this.ordinal();
    }
    /////////////////////////////////////////////////////
}
