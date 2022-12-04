package view.controllerui.first;

import control.controlexceptions.InternalException;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.bean.BeanError;
import view.bean.UserSignInBean;
import view.bean.observers.GroupBean;
import view.bean.observers.UserBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage;
import view.graphicalui.first.SigninPage;

import java.util.HashMap;
import java.util.Map;

import static view.controllerui.first.Dialog.*;
import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.constcontainer.Protocol.FILE;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.PROFILE_BOX;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.profileBoxItems.IMAGE_USER;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.MainBarItems.*;


public class SigninPageEventHandler <T extends MouseEvent> implements EventHandler<T> {

    ////////////////////////////////////////////////////////////////
    private UserManageCommunityBoundary userManageCommunityBoundary;
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setUserManageCommunityBoundary(UserManageCommunityBoundary userManageCommunityBoundary) {
        this.userManageCommunityBoundary = userManageCommunityBoundary;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {

        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

            HBox mainBar = (HBox) SigninPage.getSigninPageInstance(null).getToolBar().getItems().get(MAIN_BAR.getIndex());

            if (event.getSource().equals(mainBar.getChildren().get(HOME_BUTTON.getIndex())))
                FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

            else if (event.getSource().equals(SigninPage.getSigninPageInstance(null).getBtnSignIn())) {
                SigninPage signinPage = SigninPage.getSigninPageInstance(null);
                UserSignInBean userSignInBean = new UserSignInBean();

                String stackError = this.checkUserCredentials(userSignInBean, signinPage.getNickname(), signinPage.getPassword());
                if (stackError != null)
                    errorDialog(stackError);
                else
                    this.accessUser(userSignInBean);
            }
            else
                Dialog.errorDialog("Something went wrong, please try later.");

            this.clearSigninCredentials(SigninPage.getSigninPageInstance(null));

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private String checkUserCredentials(UserSignInBean userSignInBean, String nickname, String password){

        String stackError = null;

        try{
            userSignInBean.setNickname(nickname);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignInBean.setPassword(password);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        return stackError;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void accessUser(UserSignInBean userSignInBean){

        // needed if client run the application and has already done sign-up phase in previous sessions
        if(this.userManageCommunityBoundary == null)
            this.userManageCommunityBoundary = new UserManageCommunityBoundary();

        try{

            Map<String, Object> mapObjects = this.userManageCommunityBoundary.logIntoSystem(userSignInBean);

            Map<String, GroupBean> mapGroupBean = this.castToGroupBean(mapObjects);
            UserBean userBean = this.castToUserBean(mapObjects);

            /* This transfer is needed to allow Home Page to continue with same Controller instance
             * referenced by this boundary */
            HomePage.getHandler().setUserManageCommunityBoundary(this.userManageCommunityBoundary);
            this.userManageCommunityBoundary = null; // reset reference

            HomePage.getHandler().setCurrUserBean(userBean);
            HomePage.getHandler().setMapGroupBean(mapGroupBean);

            FirstMain.getCurrScene().setRoot(Container.getRoot(HOME));
            openWaitDialog(); // to inform user to wait till Home is ready
            this.initHomePage(userBean, mapGroupBean);
            closeWaitDialog();

        }catch(InternalException internalException){
            errorDialog(internalException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    private Map<String, GroupBean> castToGroupBean(Map<String, Object> mapObjects){
        Map<String, GroupBean> mapGroupBean = new HashMap<>();
        for(Map.Entry<String, Object> entry : mapObjects.entrySet()) {
            if (entry.getValue() instanceof GroupBean) // discard UserBean instance
                mapGroupBean.put(entry.getKey(), (GroupBean) entry.getValue());
        }
        return mapGroupBean;
    }
    ///////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    private UserBean castToUserBean(Map<String, Object> mapObjects){
        UserBean userBean = null;
        for(Map.Entry<String, Object> entry : mapObjects.entrySet()){
            if(entry.getValue() instanceof UserBean) { // discard GroupBean instances
                userBean = (UserBean) entry.getValue();
                break;
            }
        }
        return userBean;
    }
    /////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    private void clearSigninCredentials(SigninPage signinPage){
        signinPage.getTextFieldNickname().clear();
        signinPage.getTextFieldPassword().clear();
    }
    ///////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    private void initHomePage(UserBean userBean, Map<String, GroupBean> mapGroupBean){
        HomePage homePage = HomePage.getHomePageInstance(null);

        this.initUserProfile(userBean);

        // Clear previous listview - in case of a SignOut followed by a SignIn
        homePage.getObservableListGroups().clear();
        for(Map.Entry<String, GroupBean> entry : mapGroupBean.entrySet()){
            homePage.initGroupView(userBean, entry.getValue());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initUserProfile(UserBean userBean){

        VBox homePageProfileBox = (VBox) HomePage.getHomePageInstance(null)
                .getToolBar().getItems().get(PROFILE_BOX.getIndex());
        Circle circleHomePageImageUser = (Circle) homePageProfileBox.getChildren().get(IMAGE_USER.getIndex());

        HomePage.UserProfileDialog.getUserProfileDialogInstance()
                .getLabelFullName().setText(userBean.getName() + " " + userBean.getSurname());
        HomePage.UserProfileDialog.getUserProfileDialogInstance()
                .getLabelNickname().setText("@"+userBean.getNickname());

        if(userBean.getImageProfile() != null) {
            HomePage.UserProfileDialog.getUserProfileDialogInstance()
                    .getCircleImgProfile().setFill(new ImagePattern(new Image(FILE + userBean.getImageProfile())));

            circleHomePageImageUser.setFill(new ImagePattern(new Image(FILE + userBean.getImageProfile())));
        }

        HomePage.UserProfileDialog.UsernameDialog.getUsernameDialogInstance()
                .getLabelCurrNickname().setText(userBean.getNickname());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
