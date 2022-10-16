package view.controllerui.first;

import control.controlexceptions.InternalException;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.bean.BeanError;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.UsernameDialog;
import view.graphicalui.first.SignupPage;
import view.graphicalui.first.SignupPage.CredentialDialog;
import view.graphicalui.first.SignupPage.InfoErrorDialog;

import java.util.Map;
import java.util.Optional;

import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.MainBarItems.*;


public class SignupPageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {

        HBox mainBar = (HBox) SignupPage.getSignupPageInstance(null).getToolBar().getItems().get(MAIN_BAR.getIndex());

        if(event.getSource().equals(mainBar.getChildren().get(HOME_BUTTON.getIndex())))
            FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

        else if(event.getSource().equals(SignupPage.getSignupPageInstance(null).getBtnSignUp())) {
            SignupPage signupPage = SignupPage.getSignupPageInstance(null);
            UserSignUpBean userSignUpBean = new UserSignUpBean();

            String stackError = this.checkUserInfo(userSignUpBean, signupPage.getName(), signupPage.getSurname(), signupPage.getAddress(), signupPage.getMail(), signupPage.getCell(), signupPage.getAccount());
            if(stackError != null)
                this.showErrorDialog(stackError);
            else {
                this.registerUser(signupPage, userSignUpBean);
                FirstMain.getCurrScene().setRoot(Container.getRoot(SIGN_IN_PAGE));
            }
        }

        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Work in progress...");
            alert.showAndWait();
        }

        this.clearSignupTextFields(SignupPage.getSignupPageInstance(null));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    private void showErrorDialog(String errors){
        InfoErrorDialog infoErrorDialog = InfoErrorDialog.getInfoErrorDialogInstance();
        infoErrorDialog.getLabelErrors().setText(errors);
        infoErrorDialog.showAndWait();
    }
    ////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registerUser(SignupPage signupPage, UserSignUpBean userSignUpBean){
        UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
        try{
            UserSignInBean userSignInBean = userManageCommunityBoundary.registerIntoSystem(userSignUpBean);

            CredentialDialog credentialDialog = CredentialDialog.getCredentialDialogInstance();
            this.revealUserCredentials(signupPage, credentialDialog, userSignInBean);
        }
        catch(InternalException internalException){
            this.showErrorDialog(internalException.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String checkUserInfo(UserSignUpBean userSignUpBean, String name, String surname, String address, String email, String cell, String account){

        String stackError = null;

        try{
            userSignUpBean.setName(name);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setSurname(surname);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setAddress(address);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setEmail(email);
        }catch (BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setCell(cell);
        }catch (BeanError beanError){
            stackError = beanError.displayErrors();
        }

        try{
            userSignUpBean.setAccount(account);
        }catch(BeanError beanError){
            stackError = beanError.displayErrors();
        }

        return stackError;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void revealUserCredentials(SignupPage signupPage, CredentialDialog credentialDialog, UserSignInBean userSignInBean){
        credentialDialog.getLabelNickname().setText(userSignInBean.getNickname());
        credentialDialog.getLabelPassword().setText(userSignInBean.getPassword());
        Optional<Map<String, String>> result = credentialDialog.showAndWait();
        UserProfileDialog userProfileDialog = UserProfileDialog.getUserProfileDialogInstance();
        this.assignFullNameToUserProfile(userProfileDialog, signupPage);
        if (result.isPresent() && !result.get().isEmpty()) {
            this.assignUsernameToUsernameDialog(result.get());
            this.assignUsernameToUserProfile(result.get());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    private void assignUsernameToUsernameDialog(Map<String, String> result){
        UsernameDialog.getUsernameDialogInstance()
                .getLabelCurrNickname().setText(result.get("nickname"));
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private void assignUsernameToUserProfile(Map<String, String> result){
        UserProfileDialog.getUserProfileDialogInstance()
                .getLabelNickname().setText("(@" + result.get("nickname") + ")");
    }
    ///////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void assignFullNameToUserProfile(UserProfileDialog userProfileDialog, SignupPage signupPage){
        userProfileDialog.getLabelFullName()
                .setText(signupPage.getTextFieldName().getText()+" "+signupPage.getTextFieldSurname().getText());
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    private void clearSignupTextFields(SignupPage signupPage){
        signupPage.getTextFieldName().clear();
        signupPage.getTextFieldSurname().clear();
        signupPage.getTextFieldAddress().clear();
        signupPage.getTextFieldMail().clear();
        signupPage.getTextFieldCell().clear();
    }
    //////////////////////////////////////////////////////////

}
