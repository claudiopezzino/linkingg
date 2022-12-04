package view.bean.observers;

import model.UserInfo;
import model.subjects.User;
import view.boundary.UserManageCommunityBoundary;

import java.io.Serializable;
import java.util.Map;


public class UserBean implements Serializable, Observer{

    ////////////////////////////
    private String name;
    private String surname;
    private String nickname;
    private String imageProfile;
    ////////////////////////////

    //////////////////
    private User user;  // due to Pattern Observer
    //////////////////


    /*---------- GETTER ----------*/
    /////////////////////////////////////////////
    public String getName() {
        return this.name;
    }
    /////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String getSurname() {
        return this.surname;
    }
    ///////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public String getNickname() {
        return this.nickname;
    }
    /////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////
    public String getImageProfile() {
        return this.imageProfile;
    }
    /////////////////////////////////////////////////////////////


    /*-------------------- SETTER --------------------*/
    //////////////////////////////////////////////////////
    public void setName(String name) {
        this.name = name;
    }
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    public void setSurname(String surname) {
        this.surname = surname;
    }
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
    //////////////////////////////////////////////////////////////////////////////////////


    /* Following methods for Observer Pattern */

    ////////////////////////////////////////////////////////
    public void setSubject(User user){
        this.user = user;
    }
    ////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void update(Map<String, V> map) {

        String oldNick = this.nickname;
        String newNick = this.user.credentials().getKey();

        this.nickname = newNick;

        boolean isCurrUser = Boolean.parseBoolean((String) map.get(UserInfo.CURR_USER));
        if (isCurrUser)
            this.notifyNewCurrUserNickToView();

        for (Map.Entry<String, V> entry : map.entrySet()){
            if(entry.getKey().contains(UserInfo.GROUP_OWNER))
                this.notifyNewGroupOwnerNickToView((String) entry.getValue(), newNick);
            else if(entry.getKey().contains(UserInfo.GROUP_MEMBER))
                this.notifyNewGroupMemberNickToView((String) entry.getValue(), oldNick, newNick);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void notifyNewCurrUserNickToView(){
        UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
        userManageCommunityBoundary.notifyNewCurrUserNick();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void notifyNewGroupOwnerNickToView(String groupNick, String newNick){
        UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
        userManageCommunityBoundary.notifyNewGroupOwnerNick(groupNick, newNick);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void notifyNewGroupMemberNickToView(String groupNick, String oldNick, String newNick){
        UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
        userManageCommunityBoundary.notifyNewGroupMemberNick(groupNick, oldNick, newNick);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

}
