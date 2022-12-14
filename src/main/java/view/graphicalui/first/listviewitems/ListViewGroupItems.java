package view.graphicalui.first.listviewitems;

public enum ListViewGroupItems {
    OWNER_INFO,
    GROUP_INFO,
    MEMBERS_INFO;

    /*--------------------- inner-enum ---------------------*/
    public enum GroupInfo{
        GROUP_IMAGE,
        GROUP_DETAILS;

        /*------------------ inner-inner-enum ------------------*/
        public enum GroupDetails{
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
    /*------------------------------------------------------*/

    /*--------------------- inner-enum ---------------------*/
    public enum OwnerInfo{
        OWNER_LABEL,
        OWNER_NICK;

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    /*--------------------- inner-enum ---------------------*/
    public enum MembersInfo{
        MEMBERS_LABEL,
        MEMBERS_FLOW_PANE;

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    public Integer getIndex(){
        return this.ordinal();
    }
}
