package view.graphicalui.first;

public enum ListViewGroupItems {
    GROUP_IMAGE,
    GROUP_DETAILS;

    /*--------------------- inner-enum ---------------------*/
    public enum groupDetails{
        GROUP_NAME,
        GROUP_NICKNAME;

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    public Integer getIndex(){
        return this.ordinal();
    }
}
