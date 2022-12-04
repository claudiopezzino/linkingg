package control.tasks;

import control.tasks.tasksexceptions.LoaderException;
import model.*;
import model.subjects.Group;
import model.subjects.Meeting;
import model.subjects.User;
import view.AlertTask;
import view.bean.observers.GroupBean;
import view.boundary.UserManageCommunityBoundary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Loader implements Runnable {

    ////////////////////////////
    private final Socket socket;
    ////////////////////////////

    ///////////////////////////////////////////
    private final Map<String, Group> mapGroups;
    ///////////////////////////////////////////

    ////////////////////////////
    // its instance will be the same for groups'/meetings' owner/member
    private final User currUser; // needed for action "nick_change"
    ////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    public Loader(Socket socket, Map<String, Group> mapGroups, User user) {
        this.socket = socket;
        this.mapGroups = mapGroups;
        this.currUser = user;
    }
    ////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {

        try{
            this.handleMessage();
        } catch (LoaderException loaderException) {
            new Thread(new AlertTask(loaderException.getMessage())); // to change if GUI's framework change
        }

    }
    //////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    private void handleMessage() throws LoaderException {
        try( ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream()) ) {
            /* even though the developer knows the logic,
             * it would be better to insert an "instanceof" check
             * to verify that Object instance returned by readObject() method is-a-kind-of "Filter"
             * */
            Filter filter = (Filter) ois.readObject();
            this.checkFilter(filter, ois);
        } catch (IOException | ClassNotFoundException exception) {
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void checkFilter(Filter filter, ObjectInputStream objectInputStream) throws LoaderException {
        switch (filter){
            case USER_NICK_CHANGE:
                this.updateUserNick(objectInputStream);
                break;

            case MEETING_JOIN:
                this.updateMeetingJoiners(objectInputStream);
                break;

            case GROUP_JOIN:
                this.updateGroupMembers(objectInputStream);
                break;

            case MEETING_CREATION:
                this.updateMeetings(objectInputStream);
                break;

            case GROUP_CREATION:
                this.updateGroups(objectInputStream);
                break;

            default:
                break;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateUserNick(ObjectInputStream objectInputStream) throws LoaderException {
        try{
            String oldUserNick = (String) objectInputStream.readObject();
            String newUserNick = (String) objectInputStream.readObject();

            String[] groupsNickOwned = (String[]) objectInputStream.readObject();
            String[] groupsNickJoined = (String[]) objectInputStream.readObject();

            /* meetingsID are NOT necessary because they are refreshed after every click event with updated contents
            * into the First-GUI while regarding the Second-GUI they are refreshed every new page load */

            int num = 0;
            Map<String, String> mapGroupsNick = new HashMap<>();
            for (String ownGroupNick : groupsNickOwned) {
                mapGroupsNick.put(UserInfo.GROUP_OWNER + num, ownGroupNick);
                num++;
            }

            num=0;
            for (String otherGroupNick : groupsNickJoined) {
                mapGroupsNick.put(UserInfo.GROUP_MEMBER + num, otherGroupNick);
                num++;
            }

            // the user who is running the application has changed their username
            if(this.currUser.credentials().getKey().equals(oldUserNick))
                mapGroupsNick.put(UserInfo.CURR_USER, Boolean.toString(true));

            // another user has received notification
            else
                mapGroupsNick.put(UserInfo.CURR_USER, Boolean.toString(false));

            // Observer Pattern triggered
            this.currUser.updateNickname(newUserNick, mapGroupsNick);

            this.socket.close();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class
     * */
    private void updateMeetingJoiners(ObjectInputStream objectInputStream) throws LoaderException {
        try {
            String userNick = (String) objectInputStream.readObject();
            String groupNick = (String) objectInputStream.readObject();
            String meetingID = (String) objectInputStream.readObject();
            Boolean userChoice = (Boolean) objectInputStream.readObject();

            // it must have a reference to its UserBean instance
            User joiner = this.mapGroups.get(groupNick).members().get(userNick);

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).plannedMeeting().get(meetingID).updateJoiners(groupNick, joiner, userChoice);

            this.socket.close();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateGroupMembers(ObjectInputStream objectInputStream) throws LoaderException {
        try{
            // it must have a reference to UserBean instance
            User newMember = (User) objectInputStream.readObject();
            String groupNick = (String) objectInputStream.readObject();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateMembers(newMember);

            /* for the user who doesn't have the group because he/she is just added into it,
            * he will be sent "GROUP_CREATION" enum so that it will be called "updateGroups" method */
            this.socket.close();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateMeetings(ObjectInputStream objectInputStream) throws LoaderException {
        try{
            // it must have a reference to MeetingBean instance and its Serializable objects too
            Meeting newMeeting = (Meeting) objectInputStream.readObject();
            String groupNick = (String) objectInputStream.readObject();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateMeetings(newMeeting);

            this.socket.close();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateGroups(ObjectInputStream objectInputStream) throws LoaderException {
        try{
            // it must have a reference to GroupBean instance and its Serializable objects too
            Group newGroup = (Group) objectInputStream.readObject();
            this.mapGroups.put(newGroup.nickname(), newGroup);

            GroupBean groupBean = (GroupBean) newGroup.getObserver();

            // it will NOT work because a Thread CANNOT invoke elements connected to the GUI,
            // that's why it needs to delegate the work to a Task object (JavaFX library)
            UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
            userManageCommunityBoundary.notifyNewGroup(groupBean);

            this.socket.close();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

}
