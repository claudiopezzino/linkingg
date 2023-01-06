package view.controllerui.first;

import control.controlexceptions.InternalException;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import view.bean.LinkRequestCreationBean;
import view.bean.NewGroupMemberBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.first.HomePage;
import view.graphicalui.first.HomePage.SearchGroupsDialog;
import view.graphicalui.first.HomePage.SearchGroupsDialog.ConfirmationDialog;
import view.graphicalui.first.HomePage.GroupCreationDialog;
import view.graphicalui.first.HomePage.LinkRequestsDialog;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.UsernameDialog;
import view.graphicalui.first.HomePage.UserProfileDialog.PasswordDialog;
import view.graphicalui.first.PageDialog;

import java.io.File;
import java.util.Map;
import java.util.Optional;


import static view.graphicalui.first.Dialog.errorDialog;
import static view.graphicalui.first.constcontainer.HomePageFields.NICKNAME;
import static view.graphicalui.first.constcontainer.Protocol.FILE;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GROUP_INFO;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GroupInfo.GROUP_DETAILS;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GroupInfo.GroupDetails.GROUP_NICKNAME;
import static view.graphicalui.first.listviewitems.ListViewSearchGroupItems.*;
import static view.graphicalui.first.listviewitems.ListViewSearchGroupItems.GroupDetails.*;
import static view.graphicalui.first.listviewitems.ListViewLinkRequestItems.*;
import static view.graphicalui.first.listviewitems.ListViewLinkRequestItems.UserDetails.*;


public class DialogEventHandler <T extends MouseEvent> implements EventHandler<T> {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {
        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            if(event.getSource().equals(GroupCreationDialog.getGroupCreationDialogInstance().getCircleGroupImg()))
                this.manageGroupImage();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getCircleImgProfile()))
                this.manageUserImage();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getBtnNickname()))
                this.manageUserNickname();

            else if(event.getSource().equals(UserProfileDialog.getUserProfileDialogInstance().getBtnPassword()))
                this.manageUserPassword();

            else
                errorDialog("Something went wrong, please try later.");
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*---------------------------------------- INNER-CLASS ----------------------------------------*/
    public static class LinkRequestSendingHandler<T extends MouseEvent> implements EventHandler<T>{

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void handle(T event) {
            /* onClick show a dialog asking user if they are sure to send link request
            * to group clicked, if yes start sending link request logic, if no do nothing */
            if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
                    && SearchGroupsDialog.getSingletonInstance().getListViewGroups().getSelectionModel().getSelectedItem() != null){

                ListView<VBox> listViewGroups = SearchGroupsDialog.getSingletonInstance().getListViewGroups();
                VBox vBoxGroupSelected = listViewGroups.getSelectionModel().getSelectedItem();
                VBox vBoxGroupDetails = (VBox) vBoxGroupSelected.getChildren().get(VBOX_GROUP_DETAILS.getIndex());

                Circle circleGroupImage = (Circle) vBoxGroupSelected.getChildren().get(CIRCLE_GROUP_IMAGE.getIndex());
                Label labelGroupName = (Label) vBoxGroupDetails.getChildren().get(LABEL_GROUP_NAME.getIndex());
                Label labelGroupNick = (Label) vBoxGroupDetails.getChildren().get(LABEL_GROUP_NICKNAME.getIndex());

                Circle circleImage = new Circle(40, circleGroupImage.getFill());
                Label labelName = new Label(labelGroupName.getText());
                Label labelNick = new Label(labelGroupNick.getText());

                ConfirmationDialog confirmationDialog =
                        new ConfirmationDialog("Send link request", circleImage, labelName, labelNick);
                Optional<Map<String, String>> result = confirmationDialog.showAndWait();

                if (result.isPresent() && !result.get().isEmpty()){

                    String[] groupNickElems = result.get().get(NICKNAME).split(" {4}");
                    String groupNick = groupNickElems[1];
                    String currUserNick = HomePage.getHandler().getCurrUserBean().getNickname();
                    LinkRequestCreationBean linkRequestCreationBean = makeLinkRequestCreationBean(groupNick, currUserNick);

                    UserManageCommunityBoundary userManageCommunityBoundary = HomePage.getHandler().getUserManageCommunityBoundary();
                    try{
                        userManageCommunityBoundary.sendLinkRequestToGroup(linkRequestCreationBean);
                        listViewGroups.getItems().remove(vBoxGroupSelected);
                        // show a dialog that inform user the request has just been sent
                    }catch (InternalException internalException){
                        errorDialog(internalException.getMessage());
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////////////////
        private LinkRequestCreationBean makeLinkRequestCreationBean(String groupNick, String userNick){
            LinkRequestCreationBean linkRequestCreationBean = new LinkRequestCreationBean();

            linkRequestCreationBean.setGroupNick(groupNick);
            linkRequestCreationBean.setUserNick(userNick);

            return linkRequestCreationBean;
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
    }
    /*---------------------------------------------------------------------------------------------*/

    /*---------------------------------------- INNER-CLASS ----------------------------------------*/
    public static class LinkRequestAcceptingHandler<T extends MouseEvent> implements EventHandler<T>{

        @Override
        public void handle(T event) {
            /* onClick show a dialog asking group owner if they are sure to accept user into
            * their group, if yes add member into group, if no do nothing */
            if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
                    LinkRequestsDialog.getLinkRequestsDialogInstance().getListViewUserRequests().getSelectionModel().getSelectedItem() != null){

                ListView<VBox> listViewUserRequests = LinkRequestsDialog.getLinkRequestsDialogInstance().getListViewUserRequests();
                VBox vBoxSelectedUser = listViewUserRequests.getSelectionModel().getSelectedItem();

                Circle circleUserImage = (Circle) vBoxSelectedUser.getChildren().get(CIRCLE_USER_IMAGE.getIndex());

                VBox vBoxUserDetails = (VBox) vBoxSelectedUser.getChildren().get(VBOX_USER_DETAILS.getIndex());

                Label labelUserNick = (Label) vBoxUserDetails.getChildren().get(LABEL_USER_NICKNAME.getIndex());
                Label labelUserName = (Label) vBoxUserDetails.getChildren().get(LABEL_USER_NAME.getIndex());

                Circle circleImage = new Circle(40, circleUserImage.getFill());
                Label labelName = new Label(labelUserName.getText());
                Label labelNick = new Label(labelUserNick.getText());

                ConfirmationDialog confirmationDialog =
                        new ConfirmationDialog("Accept link request", circleImage, labelName, labelNick);

                Optional<Map<String, String>> result = confirmationDialog.showAndWait();
                if (result.isPresent() && !result.get().isEmpty()) {

                    String[] userTokens = labelUserNick.getText().split(" +");
                    String userNick = userTokens[1];

                    VBox vBoxSelectedGroup = HomePage.getHomePageInstance(null).getListViewGroups().getSelectionModel().getSelectedItem();
                    HBox hBoxGroupInfo = (HBox) vBoxSelectedGroup.getChildren().get(GROUP_INFO.getIndex());
                    VBox vBoxGroupDetails = (VBox) hBoxGroupInfo.getChildren().get(GROUP_DETAILS.getIndex());
                    Label labelGroupNick = (Label) vBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());

                    String[] groupTokens = labelGroupNick.getText().split(" +");
                    String groupNick = groupTokens[1];

                    NewGroupMemberBean newGroupMemberBean = new NewGroupMemberBean();
                    newGroupMemberBean.setUserNick(userNick);
                    newGroupMemberBean.setGroupNick(groupNick);
                    newGroupMemberBean.setGroupOwnerNick(HomePage.getHandler().getCurrUserBean().getNickname());

                    UserManageCommunityBoundary userManageCommunityBoundary = HomePage.getHandler().getUserManageCommunityBoundary();
                    if (userManageCommunityBoundary == null) {
                        userManageCommunityBoundary = new UserManageCommunityBoundary();
                        HomePage.getHandler().setUserManageCommunityBoundary(userManageCommunityBoundary);
                    }

                    try{
                        userManageCommunityBoundary.acceptLinkRequest(newGroupMemberBean);
                        listViewUserRequests.getItems().remove(vBoxSelectedUser);
                    } catch (InternalException internalException) {
                        errorDialog(internalException.getMessage());
                    }
                }


            }
        }
    }
    /*---------------------------------------------------------------------------------------------*/

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

}
