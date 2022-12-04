package model.subjects;

import javafx.util.Pair;
import model.MeetingFields;
import model.UserFields;
import model.UserInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Serializable needed to make a deep copy of the class
public class Meeting extends Subject implements Serializable {

    ///////////////////////////////////////////
    private final String id;
    private final String name;
    private final String rating;
    private final String date;
    private final String time;
    private final String image;
    private final List<String> photos;
    private final User scheduler;
    private final Map<String, User> mapJoiners;
    ///////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Meeting(Map<String, String> meetingDetails, List<String> photos, User scheduler, Map<String, User> mapJoiners){
        this.id = meetingDetails.get(MeetingFields.ID);
        this.name = meetingDetails.get(MeetingFields.NAME);
        this.rating = meetingDetails.get(MeetingFields.RATING);
        this.date = meetingDetails.get(MeetingFields.DATE);
        this.time = meetingDetails.get(MeetingFields.TIME);
        this.image = meetingDetails.get(MeetingFields.IMAGE);

        this.photos = photos;
        this.scheduler = scheduler;
        this.mapJoiners = mapJoiners;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    public Map<String, User> joiners(){
        return this.mapJoiners;
    }
    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////
    public User scheduler(){
        return this.scheduler;
    }
    //////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public List<String> gallery(){
        return this.photos;
    }
    /////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    public Pair<String, String> timeTable(){
        return new Pair<>(this.date, this.time);
    }
    ////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String assessment(){
        return this.rating;
    }
    ///////////////////////////////////////////////////

    //////////////////////////////////////////////////
    public String mainPicture(){
        return this.image;
    }
    //////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    public Pair<String, String> location(){
        return new Pair<>(this.id, this.name);
    }
    /////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public void updateJoiners(String groupNick, User user, Boolean choice){

        boolean joined = choice;

        if(joined)
            this.mapJoiners.put(user.credentials().getKey(), user);
        else
            this.mapJoiners.remove(user.credentials().getKey());

        Map<String, String> map = new HashMap<>();
        map.put(UserFields.NICKNAME, user.credentials().getKey());
        map.put(UserInfo.GROUP_NICK, groupNick);
        map.put(UserInfo.CHOICE, String.valueOf(joined));

        this.notifyObserver(map);
    }
    ////////////////////////////////////////////////////////////////////////

}
