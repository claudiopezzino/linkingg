package view.graphicalui.first.toolbaritems;


public enum HomeToolbarItems {
    LOGO,
    SEPARATOR_1,
    NEW_GROUP_BUTTON,
    SEPARATOR_2,
    SEARCH_BAR,
    SEPARATOR_3,
    PROFILE_BOX;

    /*---------------------- Inner-Enums ------------------------*/

    ////////////////////////////
    public enum searchBarItems{
        RADIO_BUTTONS_CONTAINER,
        SEARCH_FIELDS;

        /*-------------------- Inner-Inner-Enums ---------------------*/
        public enum radioButtonsContainerItems{
            RADIO_BUTTON_GROUPS,
            RADIO_BUTTON_USERS,
            RADIO_BUTTON_PAGES,

            RADIO_BUTTON_PROVINCE,
            RADIO_BUTTON_NAME,
            RADIO_BUTTON_NICKNAME;

            ////////////////////////////////////////////////////
            public Integer getIndex(){
                return this.ordinal();
            }
            ////////////////////////////////////////////////////

        }
        /*------------------------------------------------------------*/

        /*-------------------- Inner-Inner-Enums ---------------------*/
        public enum searchFieldsItems{
            SEARCH_AREA,
            SEARCH_BUTTON;

            /////////////////////////////////////////////////////
            public Integer getIndex(){
                return this.ordinal();
            }
            /////////////////////////////////////////////////////
        }
        /*------------------------------------------------------------*/

        /////////////////////////////////////////////////////
        public Integer getIndex(){
            return this.ordinal();
        }
        /////////////////////////////////////////////////////
    }
    //////////////////////////////////////////////////////////////

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
