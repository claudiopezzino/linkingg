package view.boundary;

import control.ManageCommunityController;
import control.UserLoginController;
import control.controlexceptions.InternalException;
import control.notifications.ConcreteNotification;
import control.notifications.NoNotificationException;
import control.notifications.Notification;
import control.notifications.notificationdecorations.LinkRequestDecorator;
import javafx.application.Platform;
import view.bean.*;
import view.bean.observers.GroupBean;
import view.graphicalui.first.Dialog;
import view.controllerui.second.Message;
import view.controllerui.second.handlerstates.*;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage;
import view.graphicalui.second.Home;
import view.graphicalui.second.SecondMain;
import view.graphicalui.second.Shell;

import java.util.List;
import java.util.Map;


// LIKE A FACADE PATTERN - SET OF API
public class UserManageCommunityBoundary {

    ////////////////////////////////////////////////////////////
    private ManageCommunityController manageCommunityController;
    ////////////////////////////////////////////////////////////

    ///////////////////////////////////
    private boolean signInDone = false;
    ///////////////////////////////////


    /*------------------------------------------ FROM ACTOR TO SYSTEM ------------------------------------------*/

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<GroupFilteredBean> searchGroupsByFilter(SearchFilterBean searchFilterBean) throws InternalException{
        if(manageCommunityController == null)
            manageCommunityController = new ManageCommunityController();
        return manageCommunityController.findGroupsByFilter(searchFilterBean);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // always after searchGroupsByFilter, so there is no need to check if controller is null
    public void sendLinkRequestToGroup(LinkRequestCreationBean linkRequestCreationBean) throws InternalException{
        this.manageCommunityController.makeAndDeliverLinkRequest(linkRequestCreationBean);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void acceptLinkRequest(NewGroupMemberBean newGroupMemberBean) throws InternalException{
        if(manageCommunityController == null)
            manageCommunityController = new ManageCommunityController();
        this.manageCommunityController.addUserIntoGroup(newGroupMemberBean);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////


    /*------------------------------------------ FROM SYSTEM TO ACTOR ------------------------------------------*/

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewCurrUserNick(){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateUserProfileNick());

        else if(SecondMain.getCurrScene() != null)
            Platform.runLater(() -> Shell.getShellHandler().updateUserNick(Home.getHomeInstance()));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewGroupOwnerNick(String groupNick, String newUserNick){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateGroupOwnerNick(groupNick, newUserNick));

        else if(SecondMain.getCurrScene() != null)
            notifyNewNick(groupNick);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void notifyNewNick(String groupNick){
        if (Shell.getShellHandler().getStateMachine().getState() instanceof StateMembersView
                && Home.getHomeInstance().getGroupNickname().equals(groupNick)) {

            Shell.getShellHandler().getStateMachine().setState(StateGroupOptions.getStateGroupOptionsInstance());

            Platform.runLater(() -> Shell.getShellHandler().updateGroupMemberNick());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewGroupMemberNick(String groupNick, String oldUserNick, String newUserNick){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateGroupMemberNick(groupNick, oldUserNick, newUserNick));

        else if(SecondMain.getCurrScene() != null)
            notifyNewNick(groupNick);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewGroup(GroupBean groupBean){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateGroupsList(groupBean));

        else if(SecondMain.getCurrScene() != null) {
            Shell.getShellHandler().getStateMachine().setState(StateMain.getStateMainInstance());
            Platform.runLater(() -> Shell.getShellHandler().updateGroupsList(groupBean));
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewGroupMember(String groupNick, String userNick){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateGroupMembersList(groupNick, userNick));

        else if(SecondMain.getCurrScene() != null)
            notifyNewNick(groupNick);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewMeeting(String groupNick, String newMeetingID){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateMeetingsList(groupNick, newMeetingID));

        else if(SecondMain.getCurrScene() != null
                && Shell.getShellHandler().getStateMachine().getState() instanceof StateMeetingsView
                && Home.getHomeInstance().getGroupNickname().equals(groupNick)){

            Shell.getShellHandler().getStateMachine().setState(StateGroupOptions.getStateGroupOptionsInstance());
            Platform.runLater(() -> Shell.getShellHandler().updateMeetingsList());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyMeetingJoiner(String groupNick, String meetingID, String newUserNick, boolean joined){
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> HomePage.getHandler().updateMeetingJoinersList(groupNick, meetingID, newUserNick, joined));

        else if(SecondMain.getCurrScene() != null) {
            Home home = Home.getHomeInstance();
            if(Shell.getShellHandler().getStateMachine().getState() instanceof StateMeetingJoinersView
                    && home.getMeetingId().equals(meetingID) && home.getGroupNickname().equals(groupNick)){

                Shell.getShellHandler().getStateMachine().setState(StateMeetingDetails.getStateMeetingDetailsInstance());
                Platform.runLater(() -> Shell.getShellHandler().updateMeetingParticipants());
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void notifyNewLinkRequest(String groupNick, String userNick){
        if(FirstMain.getCurrScene() != null){
            Platform.runLater(() -> {
                Notification newNotification;
                try{
                    Notification lastNotification = Dialog.getLastNotification();
                    newNotification = new LinkRequestDecorator(lastNotification, userNick, groupNick);
                }catch (NoNotificationException noNotificationException) {
                    newNotification = new LinkRequestDecorator(new ConcreteNotification(), userNick, groupNick);
                }
                Dialog.showNotificationDialog(newNotification);
            });
        }else if(SecondMain.getCurrScene() != null)
            Platform.runLater(() -> {
                Notification newNotification =
                        new LinkRequestDecorator(new ConcreteNotification(), userNick, groupNick);
                Message.linkRequestMsg(newNotification);
            });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    // to make known user that something went wrong with tasks
    public static void alertUser(String message){
        // User is using First GUI
        if(FirstMain.getCurrScene() != null)
            Platform.runLater(() -> Dialog.errorDialog(message)); // to change if GUI framework change

        // User is using Second GUI
        else if(SecondMain.getCurrScene() != null)
            Platform.runLater(() -> Message.infoErrorMsg(message)); // to change if GUI framework change
    }
    ///////////////////////////////////////////////////////////////



    /*--- USED WHEN ACTOR CLOSE THE APPLICATION OR DO SIGN-OUT ---*/
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
    /*------------------------------------------------------------*/

}
