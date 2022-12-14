package control.tasks;

import control.controlutilities.SecureObjectInputStream;
import control.tasks.tasksexceptions.LoaderException;
import javafx.application.Platform;
import model.*;
import model.subjects.Group;
import model.subjects.Meeting;
import model.subjects.User;
import view.AlertTask;
import view.NotificationTask;
import view.bean.observers.GroupBean;

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


    ////////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        try{
            this.handleMessage();
        } catch (LoaderException loaderException) {
            // to change if GUI's framework change
            Platform.runLater(new AlertTask(loaderException.getMessage()));
        }

    }
    //////////////////////////////////////////////////////////////////////////////////

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

            case GROUP_JOIN:
                this.updateGroupMembers(secureOis);
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

            List<String> listGroupsNickOwned = this.collectGroups(secureOis);
            List<String> listGroupsNickJoined = this.collectGroups(secureOis);

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
    private void updateGroupMembers(SecureObjectInputStream secureOis) throws LoaderException {
        try{
            // it must have a reference to UserBean instance
            User newMember = (User) secureOis.readObject();
            String groupNick = (String) secureOis.readObject();

            // Observer Pattern triggered
            this.mapGroups.get(groupNick).updateMembers(newMember);

            /* for the user who doesn't have the group because he/she is just added into it,
            * he will be sent "GROUP_CREATION" enum so that it will be called "updateGroups" method */

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by readObject() method is-a-kind-of desired Class */
    private void updateGroups(SecureObjectInputStream secureOis, ObjectOutputStream os) throws LoaderException {

        // shared resource
        LocalBridge localBridge = new LocalBridge();

        // start Task
        new Thread(new NotificationTask(localBridge, Filter.GROUP_CREATION)).start();

        try(Socket newSocket = new Socket(localBridge.readIp(), localBridge.readPort());
            ObjectOutputStream newOs = new ObjectOutputStream(newSocket.getOutputStream())){
            // it must have a reference to GroupBean instance and its Serializable objects too

            os.writeObject("next"); // to make known client that it has received message

            Group newGroup = (Group) secureOis.readObject();
            this.mapGroups.put(newGroup.nickname(), newGroup);

            GroupBean groupBean = (GroupBean) newGroup.getObserver();

            newOs.writeObject(groupBean); // let Task object update GUI made with JavaFX

            localBridge.closeBridge();

        }catch (IOException | ClassNotFoundException exception){
            throw new LoaderException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

}
