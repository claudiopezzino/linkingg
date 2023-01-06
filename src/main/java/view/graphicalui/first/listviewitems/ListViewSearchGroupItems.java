package view.graphicalui.first.listviewitems;

public enum ListViewSearchGroupItems {
    CIRCLE_GROUP_IMAGE,
    VBOX_GROUP_DETAILS;

    /*--------------------- inner-enum ---------------------*/
    public enum GroupDetails{
        LABEL_GROUP_NAME,
        LABEL_GROUP_NICKNAME;

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    public Integer getIndex(){
        return this.ordinal();
    }
}
