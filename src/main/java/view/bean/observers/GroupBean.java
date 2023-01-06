package view.bean.observers;

import control.controlutilities.Copier;
import control.controlutilities.CopyException;
import model.LinkRequestFields;
import model.MeetingFields;
import model.UserFields;
import model.subjects.Group;
import model.subjects.State;
import view.boundary.UserManageCommunityBoundary;

import java.util.HashMap;
import java.util.Map;

import static model.subjects.State.*;


public class GroupBean implements Observer{

    /////////////////////////////////////////////////////
    private String nickname;
    private String name;
    private String image;
    private UserBean owner;
    private Map<String, UserBean> mapMembers;
    private Map<String, MeetingBean> mapMeetings;
    private Map<String, LinkRequestBean> mapLinkRequests;
    /////////////////////////////////////////////////////

    ////////////////////
    private Group group;  // due to Pattern Observer
    ////////////////////


    /*---------- GETTER ----------*/
    //////////////////////////////////////////////////////
    public String getNickname() {
        return this.nickname;
    }
    //////////////////////////////////////////////////////

    /////////////////////////////////////////////
    public String getName() {
        return this.name;
    }
    /////////////////////////////////////////////

    ///////////////////////////////////////////////
    public String getImage() {
        return this.image;
    }
    ///////////////////////////////////////////////

    /////////////////////////////////////////////////
    public UserBean getOwner() {
        return this.owner;
    }
    /////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public Map<String, UserBean> getMapMembers() {
        return this.mapMembers;
    }
    ////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public Map<String, MeetingBean> getMapMeetings() {
        return this.mapMeetings;
    }
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, LinkRequestBean> getMapLinkRequests() {
        return this.mapLinkRequests;
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    /*-------------------- SETTER --------------------*/
    //////////////////////////////////////////////////////////////////////
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public void setName(String name) {
        this.name = name;
    }
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setImage(String image) {
        this.image = image;
    }
    //////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    public void setOwner(UserBean owner) {
        this.owner = owner;
    }
    ////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////
    public void setMapMembers(Map<String, UserBean> mapMembers) {
        this.mapMembers = mapMembers;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // because of composition between GroupBean and MeetingBean
    // Throws a MeetingBeanException instead of CopyException
    public void setMapMeetings(Map<String, MeetingBean> mapMeetings) throws CopyException {
        Map<String, MeetingBean> mapMeetingBean = new HashMap<>();
        for(Map.Entry<String, MeetingBean> entry : mapMeetings.entrySet())
            mapMeetingBean.put( entry.getKey(), (MeetingBean) Copier.deepCopy(entry.getValue()) );
        this.mapMeetings = mapMeetingBean;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // because of composition between GroupBean and LinkRequestBean
    // Throws a MeetingBeanException instead of CopyException
    public void setMapLinkRequests(Map<String, LinkRequestBean> mapLinkRequests) throws CopyException{
        Map<String, LinkRequestBean> mapLinkRequestsBean = new HashMap<>();
        for(Map.Entry<String, LinkRequestBean> entry : mapLinkRequests.entrySet())
            mapLinkRequestsBean.put(entry.getKey(), (LinkRequestBean) Copier.deepCopy(entry.getValue()));
        this.mapLinkRequests = mapLinkRequestsBean;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////



    /* Following methods for Observer Pattern */

    ///////////////////////////////////////////////////////////
    public void setSubject(Group group){
        this.group = group;
    }
    ///////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void update(Map<String, V> map) {
        State change = this.group.getChange();

        if(change.equals(GROUP_MEMBERS)) {

            String userNick = (String) map.get(UserFields.NICKNAME);

            UserBean userBean = (UserBean) this.group.members().get(userNick).getObserver();
            this.mapMembers.put(userBean.getNickname(), userBean);
            this.mapLinkRequests.remove(userNick);

            UserManageCommunityBoundary.notifyNewGroupMember(this.nickname, userBean.getNickname());
        }

        else if(change.equals(GROUP_MEETINGS)){

            String meetingID = (String) map.get(MeetingFields.ID);

            MeetingBean meetingBean = (MeetingBean) this.group.plannedMeeting().get(meetingID).getObserver();
            this.mapMeetings.put(meetingBean.getId(), meetingBean);

            UserManageCommunityBoundary.notifyNewMeeting(this.nickname, meetingBean.getId());
        }

        else if (change.equals(GROUP_REQUESTS)){
            String userRequestID = (String) map.get(LinkRequestFields.USERS_NICKNAME);

            LinkRequestBean linkRequestBean = (LinkRequestBean) this.group.linkRequests().get(userRequestID).getObserver();
            this.mapLinkRequests.put(linkRequestBean.getUserNick(), linkRequestBean);

            UserManageCommunityBoundary.notifyNewLinkRequest(linkRequestBean.getGroupNick(), linkRequestBean.getUserNick());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
