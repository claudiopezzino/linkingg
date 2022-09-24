package view.controllerui.first;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.UsernameDialog;
import view.graphicalui.first.SignupPage;
import view.graphicalui.first.SignupPage.CredentialDialog;

import java.util.Map;
import java.util.Optional;

import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.MainBarItems.*;


public class SignupPageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {

        HBox mainBar = (HBox) SignupPage.getSignupPageInstance(null).getToolBar().getItems().get(MAIN_BAR.getIndex());

        if(event.getSource().equals(mainBar.getChildren().get(HOME_BUTTON.getIndex())))
            FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

        else if(event.getSource().equals(SignupPage.getSignupPageInstance(null).getBtnSignUp())) {

            CredentialDialog credentialDialog = CredentialDialog.getCredentialDialogInstance();
            SignupPage signupPage = SignupPage.getSignupPageInstance(null);

            UserProfileDialog userProfileDialog = UserProfileDialog.getUserProfileDialogInstance();

            this.makeUserCredentials(credentialDialog, signupPage);

            Optional<Map<String, String>> result = credentialDialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()) {
                this.assignFullNameToUserProfile(userProfileDialog, signupPage);
                this.assignUsernameToUsernameDialog(result.get());
                this.assignUsernameToUserProfile(result.get());
                FirstMain.getCurrScene().setRoot(Container.getRoot(SIGN_IN_PAGE));
            }
        }

        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Work in progress...");
            alert.showAndWait();
        }

        this.clearSignupTextFields(SignupPage.getSignupPageInstance(null));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    private void makeUserCredentials(CredentialDialog credentialDialog, SignupPage signupPage){

        String name = signupPage.getTextFieldName().getText();
        String surname = signupPage.getTextFieldSurname().getText();

        String username = name.toLowerCase() + surname.toLowerCase();
        String password = "password";

        credentialDialog.getLabelNickname().setText(username);
        credentialDialog.getLabelPassword().setText(password);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////

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
        signupPage.getTextFieldPhone().clear();
    }
    //////////////////////////////////////////////////////////

}
