package view.graphicalui.first.listviewitems;

public enum ListViewLinkRequestItems {
    CIRCLE_USER_IMAGE,
    VBOX_USER_DETAILS;

    /*--------------------- inner-enum ---------------------*/
    public enum UserDetails{
        LABEL_USER_NAME,
        LABEL_USER_NICKNAME;

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    public Integer getIndex(){
        return this.ordinal();
    }
}
