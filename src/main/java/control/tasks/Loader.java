package control.tasks;

import control.controlutilities.SecureObjectInputStream;
import control.tasks.tasksexceptions.LoaderException;
import model.*;
import model.subjects.Group;
import model.subjects.LinkRequest;
import model.subjects.Meeting;
import model.subjects.User;
import view.bean.observers.GroupBean;
import view.boundary.UserManageCommunityBoundary;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;


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


    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        try{
            this.handleMessage();
        } catch (LoaderException loaderException) {
            UserManageCommunityBoundary.alertUser(loaderException.getMessage());
        }

    }
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    // SecureObjectInputStream to avoid injections (Sonar warning)
    private void handleMessage() throws LoaderException {
        try(ObjectOutputStream os = new ObjectOutputStream(this.socket.getOutputStream());
            SecureObjectInputStream secureOis = new SecureObjectInputStream(this.socket.getInputStream())) {

            /* even though the developer knows the logic,
             * it would be better to insert an "instanceof" check
             * to verify that Object instance returned by readObject() method is-a-kind-of "Filter"
             * */
            Filter filter = (Filter) secureOis.readObject();
            this.checkFilter(filter, secureOis, os);

            this.socket.close();

        } catch (IOException | ClassNotFoundException exception) {
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void checkFilter(Filter filter, SecureObjectInputStream secureOis, ObjectOutputStream os) throws LoaderException {
        switch (filter){
            case USER_NICK_CHANGE:
                this.updateUserNick(secureOis);
                break;

            case MEETING_JOIN:
                this.updateMeetingJoiners(secureOis);
                break;

            case LINK_REQUEST:
                this.updateLinkRequests(secureOis ,os);
                break;

            case GROUP_JOIN:
                this.updateGroupMembers(secureOis, os);
                break;

            case MEETING_CREATION:
                this.updateMeetings(secureOis);
                break;

            case GROUP_CREATION:
                this.updateGroups(secureOis, os);
                break;

            default:
                break;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private List<String> collectGroups(SecureObjectInputStream secureOis) throws IOException, ClassNotFoundException {
        List<String> listGroups = new ArrayList<>();

        String groupNick;
        do {
            groupNick = (String) secureOis.readObject();
            if(groupNick.equals("end"))
                listGroups.add(groupNick);
        }while (!groupNick.equals("end"));

        return listGroups;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateUserNick(SecureObjectInputStream secureOis) throws LoaderException {
        try{
            String oldUserNick = (String) secureOis.readObject();
            String newUserNick = (String) secureOis.readObject();

            // if user nick does not appear into group they own, this list is no more necessary
            List<String> listGroupsNickOwned = this.collectGroups(secureOis);

            List<String> listGroupsNickJoined = this.collectGroups(secureOis);
            // to add also Link Requests sent to the groups so that they can be modified ad hoc

            /* meetingsID are NOT necessary because they are refreshed after every click event with updated contents
            * into the First-GUI while regarding the Second-GUI they are refreshed every new page load */

            int num = 0;
            Map<String, String> mapGroupsNick = new HashMap<>();
            for (String ownGroupNick : listGroupsNickOwned) {
                mapGroupsNick.put(UserInfo.GROUP_OWNER + num, ownGroupNick);
                num++;
            }

            num=0;
            for (String otherGroupNick : listGroupsNickJoined) {
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

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class
     * */
    private void updateMeetingJoiners(SecureObjectInputStream secureOis) throws LoaderException {
        try {
            String userNick = (String) secureOis.readObject();
            String groupNick = (String) secureOis.readObject();
            String meetingID = (String) secureOis.readObject();
            Boolean userChoice = (Boolean) secureOis.readObject();

            // it must have a reference to its UserBean instance
            User joiner = this.mapGroups.get(groupNick).members().get(userNick);

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).plannedMeeting().get(meetingID).updateJoiners(groupNick, joiner, userChoice);

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateGroupMembers(SecureObjectInputStream secureOis, ObjectOutputStream os) throws LoaderException {
        try{
            // to make known client that it has received message
            os.writeObject("next");
            // it must have a reference to UserBean instance
            User newMember = (User) secureOis.readObject();

            // to make known client that it has received message
            os.writeObject("next");
            String groupNick = (String) secureOis.readObject();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateMembers(newMember);

            /* for the user who doesn't have the group because he/she is just added into it,
            * he/she will be sent "GROUP_CREATION" enum so that it will be called "updateGroups" method */

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateMeetings(SecureObjectInputStream secureOis) throws LoaderException {
        try{
            // it must have a reference to MeetingBean instance and its Serializable objects too
            Meeting newMeeting = (Meeting) secureOis.readObject();
            String groupNick = (String) secureOis.readObject();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateMeetings(newMeeting);

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateLinkRequests(SecureObjectInputStream secureOis, ObjectOutputStream os) throws LoaderException{
        try{
            os.writeObject("next"); // to make known client that it has received message

            LinkRequest newLinkRequest = (LinkRequest) secureOis.readObject();
            String groupNick = newLinkRequest.destination();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateLinkRequests(newLinkRequest);

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateGroups(SecureObjectInputStream secureOis, ObjectOutputStream os) throws LoaderException {
        try{
            os.writeObject("next"); // to make known client that it has received message

            // it must have a reference to GroupBean instance and its Serializable objects too
            Group newGroup = (Group) secureOis.readObject();
            this.mapGroups.put(newGroup.nickname(), newGroup);

            GroupBean groupBean = (GroupBean) newGroup.getObserver();
            UserManageCommunityBoundary.notifyNewGroup(groupBean);

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
