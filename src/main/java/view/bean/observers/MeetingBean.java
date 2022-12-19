package view.bean.observers;

import model.UserFields;
import model.UserInfo;
import model.subjects.Meeting;
import view.boundary.UserManageCommunityBoundary;

import java.util.List;
import java.util.Map;

// Serializable needed to make a deep copy of the class
public class MeetingBean implements Observer {

    //////////////////////////////////////
    private String id;
    private String name;
    private String rating;
    private String date;
    private String time;
    private String image;
    private List<String> photos;
    private UserBean scheduler;
    private Map<String, UserBean> joiners;
    //////////////////////////////////////

    ////////////////////////
    private Meeting meeting;  // due to Pattern Observer
    ////////////////////////


    /*---------- GETTER ----------*/
    //////////////////////////////////////////
    public String getId() {
        return this.id;
    }
    //////////////////////////////////////////

    /////////////////////////////////////////////
    public String getName() {
        return this.name;
    }
    /////////////////////////////////////////////

    //////////////////////////////////////////////////
    public String getRating() {
        return this.rating;
    }
    //////////////////////////////////////////////////

    /////////////////////////////////////////////
    public String getDate() {
        return this.date;
    }
    /////////////////////////////////////////////

    /////////////////////////////////////////////
    public String getTime(){
        return this.time;
    }
    /////////////////////////////////////////////

    ///////////////////////////////////////////////
    public String getImage() {
        return this.image;
    }
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    public List<String> getPhotos() {
        return this.photos;
    }
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    public UserBean getScheduler() {
        return this.scheduler;
    }
    ///////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    public Map<String, UserBean> getJoiners() {
        return this.joiners;
    }
    //////////////////////////////////////////////////////////////////


    /*-------------------- SETTER --------------------*/
    ///////////////////////////////////////////////
    public void setId(String id) {
        this.id = id;
    }
    ///////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public void setName(String name) {
        this.name = name;
    }
    //////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public void setRating(String rating) {
        this.rating = rating;
    }
    ///////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public void setDate(String date) {
        this.date = date;
    }
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public void setTime(String time){
        this.time = time;
    }
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setImage(String image) {
        this.image = image;
    }
    //////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    ////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    public void setScheduler(UserBean scheduler) {
        this.scheduler = scheduler;
    }
    ////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    public void setJoiners(Map<String, UserBean> joiners) {
        this.joiners = joiners;
    }
    /////////////////////////////////////////////////////////////////////////////////


    /* Following methods for Observer Pattern */

    ///////////////////////////////////////////////////////////////////
    public void setSubject(Meeting meeting){
        this.meeting = meeting;
    }
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void update(Map<String, V> map) {
        String userNick = (String) map.get(UserFields.NICKNAME);
        String groupNick = (String) map.get(UserInfo.GROUP_NICK);
        String choice = (String) map.get(UserInfo.CHOICE);

        boolean joined = Boolean.parseBoolean(choice);

        if(joined) {
            UserBean userBean = (UserBean) this.meeting.joiners().get(userNick).getObserver();
            this.joiners.put(userBean.getNickname(), userBean);
        }else
            this.joiners.remove(userNick);

        this.notifyMeetingJoinerToView(groupNick, this.id, userNick, joined);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void notifyMeetingJoinerToView(String groupNick, String meetingID, String newJoinerNick, boolean joined){
        UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
        userManageCommunityBoundary.notifyMeetingJoiner(groupNick, meetingID, newJoinerNick, joined);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}