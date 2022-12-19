package view.bean;

public class SearchFilterBean {

    ////////////////////////////
    private String filter;
    private String filterName;
    private String currUserNick; // to fetch from db groups that not contain whose user is owner and member
    ////////////////////////////


    //////////////////////////////////////////////////
    public String getFilter() {
        return this.filter;
    }
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public String getFilterName() {
        return this.filterName;
    }
    //////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////
    public String getCurrUserNick() {
        return this.currUserNick;
    }
    /////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    public void setFilter(String filter) {
        this.filter = filter;
    }
    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    public void setCurrUserNick(String currUserNick) {
        this.currUserNick = currUserNick;
    }
    //////////////////////////////////////////////////////////////////////////////////////
}