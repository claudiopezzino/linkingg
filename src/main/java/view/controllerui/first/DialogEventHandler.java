package view.controllerui.first;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import view.graphicalui.first.HomePage.SearchGroupsDialog;
import view.graphicalui.first.HomePage.GroupCreationDialog;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.UsernameDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.PasswordDialog;
import view.graphicalui.first.PageDialog;

import java.io.File;
import java.util.Map;
import java.util.Optional;


import static view.graphicalui.first.constcontainer.Protocol.FILE;


public class DialogEventHandler <T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {
        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            if(event.getSource().equals(GroupCreationDialog.getGroupCreationDialogInstance().getCircleGroupImg()) )
                this.manageGroupImage();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getCircleImgProfile()))
                this.manageUserImage();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getBtnNickname()))
                this.manageUserNickname();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getBtnPassword()))
                this.manageUserPassword();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getBtnLinkInvitations()))
                this.manageLinkInvitations();
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    private File showImageFileChooser(PageDialog dialog){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser
                .ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    private void changeUsername(Map<String, String> result){
        UsernameDialog.getUsernameDialogInstance()
                .getLabelCurrNickname().setText(result.get("newNickname"));

        UserProfileDialog.getUserProfileDialogInstance()
                .getLabelNickname().setText("(@" + result.get("newNickname") + ")");
    }
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageGroupImage(){
        GroupCreationDialog dialog = GroupCreationDialog.getGroupCreationDialogInstance();

        File selectedImageFile = this.showImageFileChooser(dialog);
        if( selectedImageFile != null ) {
            dialog.getCircleGroupImg().setFill(new ImagePattern(new Image(FILE+selectedImageFile.getAbsolutePath())));
            dialog.getLabelGroupImgPath().setText(selectedImageFile.getAbsolutePath());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageUserImage(){
        UserProfileDialog dialog = UserProfileDialog.getUserProfileDialogInstance();

        File selectedImageFile = this.showImageFileChooser(dialog);
        if(selectedImageFile != null) {
            dialog.getCircleImgProfile().setFill(new ImagePattern(new Image(FILE + selectedImageFile.getAbsolutePath())));
            dialog.setImgProfilePath(selectedImageFile.getAbsolutePath());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    private void manageUserNickname(){
        UsernameDialog dialog = UsernameDialog.getUsernameDialogInstance();

        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().isEmpty())
            this.changeUsername(result.get());
        dialog.getTextFieldNewNickname().setText("");
    }
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    private void manageUserPassword(){
        PasswordDialog dialog = PasswordDialog.getPasswordDialogInstance();
        dialog.showAndWait();
    }
    ////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageLinkInvitations(){
        SearchGroupsDialog searchGroupsDialog = new SearchGroupsDialog("Link invitations", "Invitations");
        searchGroupsDialog.showAndWait();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
