package view.controllerui.second;

import control.controlexceptions.InternalException;
import control.notifications.ConcreteNotification;
import control.notifications.Notification;
import control.notifications.notificationdecorations.LinkRequestDecorator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.bean.BeanError;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;
import view.bean.observers.GroupBean;
import view.bean.observers.LinkRequestBean;
import view.bean.observers.UserBean;
import view.boundary.UserManageCommunityBoundary;
import view.controllerui.second.handlerstates.*;
import view.graphicalui.second.*;
import view.graphicalui.second.signinquestionstates.QuestionEnd;

import java.util.HashMap;
import java.util.Map;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;
import static view.graphicalui.second.SigninInfo.*;
import static view.graphicalui.second.SignupInfo.*;


public class KeyEventHandler<T extends Event> implements EventHandler<T> {

    ////////////////////////////////////////////////////////////////
    private UserManageCommunityBoundary userManageCommunityBoundary;
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////
    private Map<String, GroupBean> mapGroupBean;
    ////////////////////////////////////////////

    //////////////////////////////
    private UserBean currUserBean;
    //////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private final HandlerStateMachine stateMachine = new HandlerStateMachine();
    ///////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    public HandlerStateMachine getStateMachine() {
        return this.stateMachine;
    }
    //////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public Map<String, GroupBean> getMapGroupBean() {
        return this.mapGroupBean;
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public UserBean getCurrUserBean() {
        return this.currUserBean;
    }
    ///////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserManageCommunityBoundary getUserManageCommunityBoundary(){
        return this.userManageCommunityBoundary;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setUserManageCommunityBoundary(UserManageCommunityBoundary userManageCommunityBoundary) {
        this.userManageCommunityBoundary = userManageCommunityBoundary;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {

        KeyEvent keyEvent = (KeyEvent) event;
        if( keyEvent.getCode().equals(KeyCode.ENTER) ){

            if( event.getSource().equals(Welcome.getWelcomeInstance().getPrompt()) )
                this.welcomeHandle(Welcome.getWelcomeInstance());

            else if( event.getSource().equals(Signup.getSignupInstance().getPrompt()) )
                this.signupHandle(Signup.getSignupInstance());

            else if( event.getSource().equals(Signin.getSigninInstance().getPrompt()) )
                this.signinHandle(Signin.getSigninInstance());

            else if( event.getSource().equals(Home.getHomeInstance().getPrompt()) ) {
                this.homeHandle(Home.getHomeInstance());
            }

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    public void updateUserNick(Home home){
        String newCurrUserNick = this.getCurrUserBean().getNickname();
        home.setCurrUserNickname(newCurrUserNick);
    }
    //////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public void updateGroupMemberNick(){
        if(this.getStateMachine().getState() instanceof StateGroupOptions) {
            Home home = Home.getHomeInstance();
            home.getPrompt().setText(MEMBERS);
            this.stateMachine.checkCmd(home);
        }
    }
    /////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    public void updateGroupsList(GroupBean groupBean){
        this.mapGroupBean.put(groupBean.getNickname(), groupBean);
        if(this.stateMachine.getState() instanceof StateMain) {
            Home home = Home.getHomeInstance();
            home.getPrompt().setText(VIEW_GROUPS);
            this.stateMachine.checkCmd(home);
        }
    }
    //////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public void updateMeetingsList(){
        if(this.getStateMachine().getState() instanceof StateGroupOptions){
            Home home = Home.getHomeInstance();
            home.getPrompt().setText(MEETINGS);
            this.stateMachine.checkCmd(home);
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public void updateMeetingParticipants(){
        if(this.getStateMachine().getState() instanceof StateMeetingDetails){
            Home home = Home.getHomeInstance();
            home.getPrompt().setText(PARTICIPANTS);
            this.stateMachine.checkCmd(home);
        }
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    private void welcomeHandle(Welcome welcome){
        if( welcome.getPrompt().getText().equals(SIGNUP) ) {
            Signup.getSignupInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signup.getSignupInstance());
        }

        else if( welcome.getPrompt().getText().equals(SIGNIN) ) {
            Signin.getSigninInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signin.getSigninInstance());
        }

        else if( welcome.getPrompt().getText().equals(HELP)
                || welcome.getPrompt().getText().equals(SOCIAL)
                || welcome.getPrompt().getText().equals(ABOUT)
                || welcome.getPrompt().getText().equals(OVERVIEW) )

            infoMsg();

        else
            errorMsg();

        welcome.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void signupHandle(Signup signup){
        if( signup.getPrompt().getText().equals(EXIT) || signup.getPrompt().getText().equals(BACK) ) {

            signup.setSignMode(true);
            signup.getPrompt().clear();
            signup.restoreScreen();
            SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
        }

        else if(signup.getPrompt().getText().equals(PREV) && !signup.getUserInfo().isEmpty())
            signup.prevScreen();

        else if(signup.getPrompt().getText().equals(CONFIRM) && signup.getStateMachine().getQuestion() == null)
            this.initSignUpPhase(signup);

        else if(signup.getSignMode().equals(true) && signup.getPrompt().getText().equals(MANUAL)){
            signup.setSignMode(false);
            signup.restoreScreen();
            signup.displayQuestion();
            signup.getPrompt().clear();
        }

        else if( signup.getPrompt().getText().equals(BUSINESS_OWNER) || signup.getPrompt().getText().equals(PREMIUM)
                || signup.getPrompt().getText().equals(GOOGLE) || signup.getPrompt().getText().equals(FACEBOOK))
            infoMsg();

        else if(!signup.getPrompt().getText().isEmpty() && !signup.getPrompt().getText().equals(PREV)
                &&  signup.getStateMachine().getQuestion() != null && signup.getSignMode().equals(false)){

            signup.fillUserInfo(signup.getPrompt().getText());
            signup.getScreen().appendText(signup.getPrompt().getText());
            signup.displayQuestion();
            signup.getPrompt().clear();
        }

        else errorMsg();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    private void initSignUpPhase(Signup signup){
        UserSignUpBean userSignUpBean = new UserSignUpBean();
        String stackError = this.verifyUserInfo(signup, userSignUpBean);
        if(stackError != null)
            infoErrorMsg(stackError);
        else {
            this.registerUser(userSignUpBean);
            signup.setSignMode(true);
            Signin.getSigninInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signin.getSigninInstance());
        }
        signup.getPrompt().clear();
        signup.restoreScreen();
    }
    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private String verifyUserInfo(Signup signup, UserSignUpBean userSignUpBean){
        String stackError = null;

        try{
            userSignUpBean.setName(signup.getUserInfo().get(OWN_NAME.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setSurname(signup.getUserInfo().get(OWN_SURNAME.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setAddress(signup.getUserInfo().get(ADDRESS.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setEmail(signup.getUserInfo().get(MAIL.getIndex()));
        }catch (BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setCell(signup.getUserInfo().get(CELL.getIndex()));
        }catch (BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setAccount(signup.getUserInfo().get(ACCOUNT.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        return stackError;
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registerUser(UserSignUpBean userSignUpBean){
        this.userManageCommunityBoundary = new UserManageCommunityBoundary();
        try {
            UserSignInBean userSignInBean = this.userManageCommunityBoundary.registerIntoSystem(userSignUpBean);
            credentialMsg(userSignInBean.getNickname(), userSignInBean.getPassword());
        }catch(InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void signinHandle(Signin signin){

        if( signin.getPrompt().getText().equals(EXIT) || signin.getPrompt().getText().equals(BACK) ) {

            signin.setSignMode(true);
            signin.getPrompt().clear();
            signin.restoreScreen();
            SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
        }

        else if(signin.getPrompt().getText().equals(PREV) && !signin.getUserInfo().isEmpty())
            signin.prevScreen();

        else if(signin.getPrompt().getText().equals(CONFIRM) && signin.getStateMachine().getQuestion() == null) {
            this.initSigninPhase(signin);
            this.showNotifications();
        }

        else if(signin.getSignMode().equals(true) && signin.getPrompt().getText().equals(MANUAL)){
            signin.setSignMode(false);
            signin.restoreScreen();
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else if(signin.getPrompt().getText().equals(GOOGLE)
                || signin.getPrompt().getText().equals(FACEBOOK))
            infoMsg();

        else if( !signin.getPrompt().getText().isEmpty() && !signin.getPrompt().getText().equals(PREV)
                && signin.getStateMachine().getQuestion() != null && signin.getSignMode().equals(false)
                && !signin.getStateMachine().getQuestion().equals(QuestionEnd.getQuestionEndInstance())){

            signin.fillUserInfo(signin.getPrompt().getText());
            signin.getScreen().appendText(signin.getPrompt().getText());
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else if(!signin.getPrompt().getText().isEmpty() && !signin.getPrompt().getText().equals(PREV)
                && signin.getStateMachine().getQuestion() != null && signin.getSignMode().equals(false)
                && signin.getStateMachine().getQuestion().equals(QuestionEnd.getQuestionEndInstance())){

            signin.fillUserInfo(signin.getPrompt().getText());
            signin.getScreen().appendText("*".repeat(signin.getPrompt().getText().length()));
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else errorMsg();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private void initSigninPhase(Signin signin){
        UserSignInBean userSignInBean = new UserSignInBean();
        String stackError = this.verifyUserCredentials(signin, userSignInBean);
        if(stackError != null)
            infoErrorMsg(stackError);
        else {
            this.accessUser(userSignInBean);
            signin.setSignMode(true);
        }
        signin.getPrompt().clear();
        Signin.getSigninInstance().displaySignOptions();
    }
    ///////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    private String verifyUserCredentials(Signin signin, UserSignInBean userSignInBean){
        String stackError = null;

        try{
            userSignInBean.setNickname(signin.getUserInfo().get(OWN_NICKNAME.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try {
            userSignInBean.setPassword(signin.getUserInfo().get(OWN_PASSWORD.getIndex()));
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        return stackError;
    }
    /////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void accessUser(UserSignInBean userSignInBean){

        // needed if client run the application and has already done sign-up phase in previous sessions
        if(this.userManageCommunityBoundary == null)
            this.userManageCommunityBoundary = new UserManageCommunityBoundary();

        try{
            Map<String, Object> mapObjects = this.userManageCommunityBoundary.logIntoSystem(userSignInBean);

            this.assignCurrUserBean(mapObjects);
            this.assignMapGroupsBean(mapObjects);

            this.assignUserCredentials(this.currUserBean.getName()+" "+this.currUserBean.getSurname(),
                    this.currUserBean.getNickname(), userSignInBean.getPassword());

            Home.getHomeInstance().initWelcomeMsg();
            Home.getHomeInstance().restoreScreen();
            SecondMain.getCurrScene().setRoot(Home.getHomeInstance());

        }catch(InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    private void assignCurrUserBean(Map<String, Object> mapObjects){
        UserBean userBean = null;
        for(Map.Entry<String, Object> entry : mapObjects.entrySet()){
            if(entry.getValue() instanceof UserBean) { // discard GroupBean instances
                userBean = (UserBean) entry.getValue();
                break;
            }
        }
        this.currUserBean = userBean;
    }
    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    private void assignMapGroupsBean(Map<String, Object> mapObjects){
        Map<String, GroupBean> mapGroupsBean = new HashMap<>();
        for(Map.Entry<String, Object> entry : mapObjects.entrySet()) {
            if (entry.getValue() instanceof GroupBean) // discard UserBean instance
                mapGroupsBean.put(entry.getKey(), (GroupBean) entry.getValue());
        }
        this.mapGroupBean = mapGroupsBean;
    }
    ////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private void assignUserCredentials(String fullName, String nickname, String password){

        Home.setCurrUserFullName(fullName);

        Home.getHomeInstance().setCurrUserNickname(nickname);
        Home.getHomeInstance().setCurrUserPassword(password);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showNotifications(){
        Notification notification = null;
        for (Map.Entry<String, GroupBean> groupEntry : mapGroupBean.entrySet()) {
            for (Map.Entry<String, LinkRequestBean> linkRequestEntry : groupEntry.getValue().getMapLinkRequests().entrySet()) {
                if (notification == null)
                    notification = new ConcreteNotification();
                String userNick = linkRequestEntry.getValue().getUserNick();
                String groupNick = linkRequestEntry.getValue().getGroupNick();
                notification = new LinkRequestDecorator(notification, userNick, groupNick);
            }
        }
        // show if some notification is present
        if (notification != null)
            Message.linkRequestMsg(notification);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    private void homeHandle(Home home){

        /*------------------------------------------*/
        if(home.getPrompt().getText().equals(EXIT))
            this.moveBackToWelcomePage(home);
        /*------------------------------------------*/

        /*--------------------------------------------------*/
        else if(home.getPrompt().getText().equals(HOME_PAGE))
            this.moveBackToHomePage(home);
        /*--------------------------------------------------*/

        /*----------------------------------*/
        else
            this.stateMachine.checkCmd(home);
        /*----------------------------------*/

    }
    ///////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void moveBackToHomePage(Home home){
        if(this.userManageCommunityBoundary != null && this.userManageCommunityBoundary.hasStartedSignIn()){
            try{
                this.userManageCommunityBoundary.freeResources();
            }catch (InternalException internalException){
                infoErrorMsg(internalException.getMessage());
            }
        }
        // Otherwise, call freeResources of second boundary
        home.restoreScreen();
        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    private void moveBackToWelcomePage(Home home){
        if(this.userManageCommunityBoundary.hasStartedSignIn()){
            try{
                this.userManageCommunityBoundary.freeResources();
            }catch (InternalException internalException){
                infoErrorMsg(internalException.getMessage());
            }
        }
        // Otherwise, call freeResources of second boundary
        home.restoreScreen();
        home.getPrompt().clear();
        SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
    }
    ////////////////////////////////////////////////////////////////////

}
