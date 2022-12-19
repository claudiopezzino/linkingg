package view;

/* This Class is needed to adapt different a String value written in different manners from different Views
 * into a unique String value that can be used by Controller without any dependency from the specific View */
public final class ConstAdapter {

    ////////////////////////////////////////////////////////////////
    public static final String ADAPTED_FILTER_NAME = "name";
    public static final String ADAPTED_FILTER_NICKNAME = "nickname";
    public static final String ADAPTED_FILTER_PROVINCE = "province";
    ////////////////////////////////////////////////////////////////


    ////////////////////////
    private ConstAdapter(){}
    ////////////////////////
}
