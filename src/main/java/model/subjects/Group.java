package model.subjects;

import control.controlutilities.Copier;
import control.controlutilities.CopyException;
import model.GroupFields;
import model.MeetingFields;
import model.UserFields;

import java.util.HashMap;
import java.util.Map;


public class Group extends Subject {

    ///////////////////////////////////////////////
    private final String nickname;
    private final String name;
    private final String image;
    private final User owner;
    private final Map<String, User> mapMembers;
    private final Map<String, Meeting> mapMeetings;
    ///////////////////////////////////////////////

    /////////////////////
    private State change; // needed for Observer Pattern
    /////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Group (Map<String, String> groupDetails, User owner, Map<String, User> mapMembers, Map<String, Meeting> mapMeetings) throws CopyException {
        this.nickname = groupDetails.get(GroupFields.NICKNAME);
        this.name = groupDetails.get(GroupFields.NAME);
        this.image = groupDetails.get(GroupFields.IMAGE);
        this.owner = owner;
        this.mapMembers = mapMembers;

        this.mapMeetings = new HashMap<>();
        for(Map.Entry<String, Meeting> entry : mapMeetings.entrySet())
            this.mapMeetings.put(entry.getKey(), (Meeting) Copier.deepCopy(entry.getValue()));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    public Map<String, Meeting> plannedMeeting(){
        return this.mapMeetings;
    }
    /////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////
    public User owner(){
        return this.owner;
    }
    //////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    public Map<String, User> members(){
        return this.mapMembers;
    }
    //////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String imageProfile(){
        return this.image;
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////
    public String name(){
        return this.name;
    }
    //////////////////////////////////////////

    //////////////////////////////////////////////////
    public String nickname(){
        return this.nickname;
    }
    //////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public void updateMembers(User newMember){
        this.change = State.GROUP_MEMBERS;

        this.mapMembers.put(newMember.credentials().getKey(), newMember);

        Map<String, String> map = new HashMap<>();
        map.put(UserFields.NICKNAME, newMember.credentials().getKey());

        this.notifyObserver(map);
    }
    /////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public void updateMeetings(Meeting newMeeting){
        this.change = State.GROUP_MEETINGS;

        this.mapMeetings.put(newMeeting.location().getKey(), newMeeting);

        Map<String, String> map = new HashMap<>();
        map.put(MeetingFields.ID, newMeeting.location().getKey());

        this.notifyObserver(map);
    }
    /////////////////////////////////////////////////////////////////////

    /*

       * If needed for other Subject instances, insert the following method into Subject Class with State attribute,
       * this State attribute is needed to make known Observer what type of change occurred, so that it can
       * know what type of content expect from Map parameter inside update method and what type of getState do
                                                                                                                   */

    ////////////////////////////////////////////////
    public State getChange() {
        return this.change;
    }  // method for Observer Pattern
    ////////////////////////////////////////////////

}
