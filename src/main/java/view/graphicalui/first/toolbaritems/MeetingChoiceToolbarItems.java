package view.graphicalui.first.toolbaritems;


public enum MeetingChoiceToolbarItems {
    LOGO,
    SEPARATOR_1,
    SHARING_BUTTON,
    SEPARATOR_2,
    TITLE,
    SEPARATOR_3,
    MAIN_BAR,
    SEPARATOR_4,
    PROFILE_BOX;

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


    /////////////////////////////
    public enum profileBoxItems{
        IMAGE_USER,
        LINK_SIGNOUT;

        /////////////////////////////////////////////////////
        public Integer getIndex(){
            return this.ordinal();
        }
        /////////////////////////////////////////////////////
    }
    //////////////////////////////////////////////////////////////

    /*-----------------------------------------------------------*/

    /////////////////////////////////////////////////////
    public Integer getIndex(){
        return this.ordinal();
    }
    /////////////////////////////////////////////////////
}
