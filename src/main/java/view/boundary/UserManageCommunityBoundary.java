package view.boundary;

import control.ManageCommunityController;
import control.UserLoginController;
import control.controlexceptions.InternalException;
import view.bean.GroupCreationBean;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;
import view.bean.observers.GroupBean;
import view.controllerui.first.Dialog;
import view.controllerui.second.Message;
import view.controllerui.second.handlerstates.*;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage;
import view.graphicalui.second.Home;
import view.graphicalui.second.SecondMain;
import view.graphicalui.second.Shell;

import java.util.Map;


// LIKE A FACADE PATTERN - SET OF API
public class UserManageCommunityBoundary {

    ////////////////////////////////////////////////////////////
    private ManageCommunityController manageCommunityController;
    ////////////////////////////////////////////////////////////

    ///////////////////////////////////
    private boolean signInDone = false;
    ///////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public UserSignInBean registerIntoSystem(UserSignUpBean userInfo) throws InternalException {
        if(manageCommunityController == null)
            manageCommunityController = new ManageCommunityController();
        return manageCommunityController.setUpSignUpPhase(new UserLoginController(), userInfo);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Object> logIntoSystem(UserSignInBean userCredentials) throws InternalException{
        if(manageCommunityController == null)
            manageCommunityController = new ManageCommunityController();
        this.signInDone = true;
        return manageCommunityController.setUpSignInPhase(new UserLoginController(), userCredentials);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    public void createGroup(GroupCreationBean groupCreationBean) throws InternalException{
        if(manageCommunityController == null)
            manageCommunityController = new ManageCommunityController();
        this.manageCommunityController.setUpGroup(groupCreationBean);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public void notifyNewCurrUserNick(){
        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateUserProfileNick();

        else if(SecondMain.getCurrScene() != null)
            Shell.getShellHandler().updateUserNick(Home.getHomeInstance());
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public void notifyNewGroupOwnerNick(String groupNick, String newUserNick){
        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateGroupOwnerNick(groupNick, newUserNick);

        else if(SecondMain.getCurrScene() != null)
            this.notifyNewNick(groupNick);
    }
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void notifyNewNick(String groupNick){
        if (Shell.getShellHandler().getStateMachine().getState() instanceof StateMembersView
                && Home.getHomeInstance().getGroupNickname().equals(groupNick)) {

            Shell.getShellHandler().getStateMachine().setState(StateGroupOptions.getStateGroupOptionsInstance());
            Shell.getShellHandler().updateGroupMemberNick();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void notifyNewGroupMemberNick(String groupNick, String oldUserNick, String newUserNick){
        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateGroupMemberNick(groupNick, oldUserNick, newUserNick);

        else if(SecondMain.getCurrScene() != null)
            this.notifyNewNick(groupNick);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    public void notifyNewGroup(GroupBean groupBean){

        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateGroupsList(groupBean);

        else if(SecondMain.getCurrScene() != null)
            Shell.getShellHandler().updateGroupsList(groupBean);
    }
    ////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    public void notifyNewGroupMember(String groupNick, String userNick){

        if(FirstMain.getCurrScene() != null){
            HomePage.getHandler().updateGroupMembersList(groupNick, userNick);
        }
        else if(SecondMain.getCurrScene() != null){
            this.notifyNewNick(groupNick);
        }
    }
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void notifyNewMeeting(String groupNick, String newMeetingID){
        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateMeetingsList(groupNick, newMeetingID);

        else if(SecondMain.getCurrScene() != null
                && Shell.getShellHandler().getStateMachine().getState() instanceof StateMeetingsView
                && Home.getHomeInstance().getGroupNickname().equals(groupNick)){

            Shell.getShellHandler().getStateMachine().setState(StateGroupOptions.getStateGroupOptionsInstance());
            Shell.getShellHandler().updateMeetingsList();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void notifyMeetingJoiner(String groupNick, String meetingID, String newUserNick, boolean joined){
        if(FirstMain.getCurrScene() != null)
            HomePage.getHandler().updateMeetingJoinersList(groupNick, meetingID, newUserNick, joined);

        else if(SecondMain.getCurrScene() != null) {
            Home home = Home.getHomeInstance();
            if(Shell.getShellHandler().getStateMachine().getState() instanceof StateMeetingJoinersView
                    && home.getMeetingId().equals(meetingID) && home.getGroupNickname().equals(groupNick)){
                Shell.getShellHandler().getStateMachine().setState(StateMeetingDetails.getStateMeetingDetailsInstance());
                Shell.getShellHandler().updateMeetingParticipants();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    // to make known user that something went wrong with tasks
    public static void alertUser(String message){

        // User is using First GUI
        if(FirstMain.getCurrScene() != null)
            Dialog.errorDialog(message);

        // User is using Second GUI
        else if(SecondMain.getCurrScene() != null)
            Message.infoErrorMsg(message);
    }
    //////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////
    public boolean hasStartedSignIn(){
        return this.signInDone;
    }
    /////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    public void freeResources() throws InternalException {
        this.manageCommunityController.stopListener();
    }
    ///////////////////////////////////////////////////////

}
