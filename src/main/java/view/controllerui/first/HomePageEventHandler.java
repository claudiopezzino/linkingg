package view.controllerui.first;

import control.controlexceptions.InternalException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import view.bean.GroupCreationBean;
import view.bean.observers.GroupBean;
import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage;
import view.graphicalui.first.HomePage.MeetingGalleryDialog;
import view.graphicalui.first.HomePage.GroupMemberProfileDialog;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.GroupCreationDialog;
import view.graphicalui.first.HomePage.LinkRequestsDialog;
import view.graphicalui.first.HomePage.SearchGroupsDialog;
import view.graphicalui.first.HomePage.SearchUsersDialog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static view.controllerui.first.Dialog.errorDialog;
import static view.graphicalui.first.constcontainer.HomePageFields.*;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.*;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GroupInfo.GROUP_DETAILS;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GroupInfo.GROUP_IMAGE;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.GroupInfo.GroupDetails.*;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.MembersInfo.MEMBERS_FLOW_PANE;
import static view.graphicalui.first.listviewitems.ListViewGroupItems.OwnerInfo.OWNER_NICK;
import static view.graphicalui.first.listviewitems.ListViewMeetingItems.*;
import static view.graphicalui.first.listviewitems.ListViewMeetingItems.JoinersInfo.FLOW_PANE_JOINERS;
import static view.graphicalui.first.listviewitems.ListViewMeetingItems.MeetingInfo.LABEL_MEETING_ID;
import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.constcontainer.Image.*;
import static view.graphicalui.first.constcontainer.Protocol.FILE;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.*;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.profileBoxItems.*;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.RADIO_BUTTONS_CONTAINER;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.SEARCH_FIELDS;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.radioButtonsContainerItems.RADIO_BUTTON_GROUPS;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.searchFieldsItems.SEARCH_BUTTON;


public class HomePageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    ////////////////////////////////////////////
    private Map<String, GroupBean> mapGroupBean;
    ////////////////////////////////////////////

    //////////////////////////////
    private UserBean currUserBean;
    //////////////////////////////

    ////////////////////////////////////////////////////////////////
    private UserManageCommunityBoundary userManageCommunityBoundary;
    ////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setUserManageCommunityBoundary(UserManageCommunityBoundary userManageCommunityBoundary) {
        this.userManageCommunityBoundary = userManageCommunityBoundary;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserManageCommunityBoundary getUserManageCommunityBoundary() {
        return this.userManageCommunityBoundary;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setMapGroupBean(Map<String, GroupBean> mapGroupBean) {
        this.mapGroupBean = mapGroupBean;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    public void setCurrUserBean(UserBean currUserBean) {
        this.currUserBean = currUserBean;
    }
    ////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {

        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

            VBox profileBox = (VBox) HomePage.getHomePageInstance(null).getToolBar().getItems().get(PROFILE_BOX.getIndex());
            HBox searchBar = (HBox) HomePage.getHomePageInstance(null).getToolBar().getItems().get(SEARCH_BAR.getIndex());

            HBox searchFields = (HBox) searchBar.getChildren().get(SEARCH_FIELDS.getIndex());

            if(event.getSource().equals(profileBox.getChildren().get(LINK_SIGNOUT.getIndex())))
                this.manageSignOut();

            else if(event.getSource().equals(profileBox.getChildren().get(IMAGE_USER.getIndex())))
                this.manageUserProfile();

            else if(event.getSource().equals(searchFields.getChildren().get(SEARCH_BUTTON.getIndex())))
                this.manageSearch(searchBar);

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getToolBar().getItems().get(NEW_GROUP_BUTTON.getIndex())))
                this.manageGroupCreation();

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getBtnProposeMeeting()))
                FirstMain.getCurrScene().setRoot(Container.getRoot(MEETING_CHOICE_PAGE));

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getBtnLinkRequests())
                    && HomePage.getHomePageInstance(null).getListViewGroups().getFocusModel().getFocusedItem() != null){
                this.manageLinkRequests();
            }

            else {
                Dialog.errorDialog("Something went wrong, please try later.");
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    private void manageSignOut(){
        if(this.userManageCommunityBoundary != null
                && this.userManageCommunityBoundary.hasStartedSignIn()){
            try{
                this.userManageCommunityBoundary.freeResources();
            }catch (InternalException internalException){
                errorDialog(internalException.getMessage());
            }
        }
        // Otherwise, call freeResources of second boundary
        FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));
    }
    //////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    public void updateUserProfileNick(){
        HomePage.UserProfileDialog.getUserProfileDialogInstance()
                .getLabelNickname().setText("@" + this.currUserBean.getNickname());

        HomePage.UserProfileDialog.UsernameDialog.getUsernameDialogInstance()
                .getLabelCurrNickname().setText(this.currUserBean.getNickname());
    }
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateGroupOwnerNick(String groupNick, String newOwnerNick){
        String ownerNick = this.mapGroupBean.get(groupNick).getMapMembers().get(newOwnerNick).getNickname();
        String currUserNick = this.currUserBean.getNickname();

        // curr user does NOT own target group
        if(!ownerNick.equals(currUserNick)){
            ObservableList<VBox> observableListGroups = this.getListGroups();
            for (VBox vBoxGroup : observableListGroups){
                String currGroupNick = this.getCurrGroupNick(vBoxGroup);
                if(currGroupNick.equals(groupNick)){
                    HBox hBoxOwnerInfo = (HBox) vBoxGroup.getChildren().get(OWNER_INFO.getIndex());
                    Hyperlink hyperlinkOwner = (Hyperlink) hBoxOwnerInfo.getChildren().get(OWNER_NICK.getIndex());
                    hyperlinkOwner.setText("@"+newOwnerNick);
                    break;
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateGroupMemberNick(String groupNick, String oldUserNick, String newUserNick){
        ObservableList<VBox> observableListGroups = this.getListGroups();
        for (VBox vBoxGroup : observableListGroups){
            String currGroupNick = this.getCurrGroupNick(vBoxGroup);
            if(currGroupNick.equals(groupNick)) {
                VBox vBoxMembersInfo = (VBox) vBoxGroup.getChildren().get(MEMBERS_INFO.getIndex());
                FlowPane flowPaneMembers = (FlowPane) vBoxMembersInfo.getChildren().get(MEMBERS_FLOW_PANE.getIndex());
                for(Node member : flowPaneMembers.getChildren()){
                    Hyperlink hyperlinkMember = (Hyperlink) member;
                    String[] hyperLinkElems = hyperlinkMember.getText().split("@");
                    String memberNick = hyperLinkElems[1];
                    if(memberNick.equals(oldUserNick)) {
                        hyperlinkMember.setText("@" + newUserNick);
                        break;
                    }
                }
                break;
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    private ObservableList<VBox> getListGroups(){
        HomePage homePage = HomePage.getHomePageInstance(null);
        ListView<VBox> listViewGroups = homePage.getListViewGroups();
        return listViewGroups.getItems();
    }
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private String getCurrGroupNick(VBox vBoxGroup){
        HBox hBoxGroupInfo = (HBox) vBoxGroup.getChildren().get(GROUP_INFO.getIndex());
        VBox vBoxGroupDetails = (VBox) hBoxGroupInfo.getChildren().get(GROUP_DETAILS.getIndex());
        Label labelGroupNick = (Label) vBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());
        String[] groupNickElems = labelGroupNick.getText().split(" {4}");
        return groupNickElems[1];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    public void updateGroupsList(GroupBean groupBean){
        this.mapGroupBean.put(groupBean.getNickname(), groupBean);
        // apply change to home page view
        HomePage.getHomePageInstance(null)
                .initGroupView(this.currUserBean, groupBean);
    }
    //////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String fetchSelectedGroupNick(){

        String selectedGroupNick = null;

        if(HomePage.getHomePageInstance(null).getListViewGroups().getSelectionModel().getSelectedItem() != null){

            HomePage homePage = HomePage.getHomePageInstance(null);
            ListView<VBox> listViewGroups = homePage.getListViewGroups();
            VBox vBoxSelectedItem = listViewGroups.getSelectionModel().getSelectedItem();
            HBox hBoxGroupInfo = (HBox) vBoxSelectedItem.getChildren().get(GROUP_INFO.getIndex());
            VBox vBoxGroupDetails = (VBox) hBoxGroupInfo.getChildren().get(GROUP_DETAILS.getIndex());
            Label labelSelectedGroupNick = (Label) vBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());
            selectedGroupNick = labelSelectedGroupNick.getText();
        }
        return selectedGroupNick;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateMeetingsList(String groupNick, String newMeetingID){

        String selectedGroupNick = this.fetchSelectedGroupNick();

        if(selectedGroupNick != null && selectedGroupNick.equals(groupNick)) {

            GroupBean groupBean = this.mapGroupBean.get(groupNick);
            MeetingBean meetingBean = this.mapGroupBean.get(groupNick).getMapMeetings().get(newMeetingID);

            HomePage.getHomePageInstance(null).initMeetingView(groupBean, meetingBean);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateMeetingJoinersList(String groupNick, String meetingID, String joinerNick, boolean joined){
        String selectedGroupNick = this.fetchSelectedGroupNick();

        if(selectedGroupNick != null && selectedGroupNick.equals(groupNick)){
            HomePage homePage = HomePage.getHomePageInstance(null);
            ListView<HBox> listViewMeetings = homePage.getListViewMeetings();
            ObservableList<HBox> observableListMeetings = listViewMeetings.getItems();

            for(HBox meeting : observableListMeetings){

                VBox vBoxMeetingInfo = (VBox) meeting.getChildren().get(MEETING_INFO.getIndex());
                Label labelMeetingID = (Label) vBoxMeetingInfo.getChildren().get(LABEL_MEETING_ID.getIndex());
                String[] meetingIdComponents = labelMeetingID.getText().split("#");
                String currMeetingID = meetingIdComponents[1];

                if(currMeetingID.equals(meetingID)){
                    VBox vBoxJoinersInfo = (VBox) meeting.getChildren().get(JOINERS_INFO.getIndex());
                    FlowPane flowPaneJoiners = (FlowPane) vBoxJoinersInfo.getChildren().get(FLOW_PANE_JOINERS.getIndex());
                    this.makeDecision(joinerNick, joined, flowPaneJoiners);
                    break;
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private void makeDecision(String joinerNick, boolean joined, FlowPane flowPaneJoiners){
        if(joined)
            flowPaneJoiners.getChildren().add(new Label("@" + joinerNick));

        else{
            ObservableList<Node> observableListJoiners = flowPaneJoiners.getChildren();
            for(Node joiner : observableListJoiners) {
                Label labelJoiner = (Label) joiner;
                if(labelJoiner.getText().equals(joinerNick)) {
                    observableListJoiners.remove(joiner);
                    break;
                }
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateGroupMembersList(String groupNick, String newMemberNick){

        HomePage homePage = HomePage.getHomePageInstance(null);
        ListView<VBox> listViewGroups = homePage.getListViewGroups();
        ObservableList<VBox> observableListGroups = listViewGroups.getItems();

        Hyperlink hyperlinkNewMember = new Hyperlink(newMemberNick);
        hyperlinkNewMember.setOnMouseClicked(new ListViewHandler.HyperLinkGroupMemberHandler<>(groupNick, newMemberNick));

        for (VBox group : observableListGroups){
            HBox hBoxGroupDetails = (HBox) group.getChildren().get(GROUP_DETAILS.getIndex());
            Label labelCurrGroupNick = (Label) hBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());
            String currGroupNick = labelCurrGroupNick.getText();
            if(currGroupNick.equals(groupNick)){
                VBox vboxGroupMembers = (VBox) group.getChildren().get(MEMBERS_INFO.getIndex());
                FlowPane flowPaneGroupMembers = (FlowPane) vboxGroupMembers.getChildren().get(MEMBERS_FLOW_PANE.getIndex());
                flowPaneGroupMembers.getChildren().add(hyperlinkNewMember);
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*-------------------------------------- INNER-CLASS --------------------------------------*/
    public static class ListViewHandler<T extends MouseEvent> implements EventHandler<T>{

        ///////////////////////////////////////////////////
        private static ListViewHandler<MouseEvent> handler;
        ///////////////////////////////////////////////////

        ///////////////////////////
        private ListViewHandler(){}
        ///////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void handle(T event) {
            if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
                    && HomePage.getHomePageInstance(null).getListViewGroups().getSelectionModel().getSelectedItem() != null){

                HomePage homePage = HomePage.getHomePageInstance(null);
                ListView<VBox> listViewGroups = homePage.getListViewGroups();
                VBox vBoxSelectedItem = listViewGroups.getSelectionModel().getSelectedItem();
                HBox hBoxGroupInfo = (HBox) vBoxSelectedItem.getChildren().get(GROUP_INFO.getIndex());
                Circle circleGroupImageSelected = (Circle) hBoxGroupInfo.getChildren().get(GROUP_IMAGE.getIndex());
                Paint paintImageGroup = circleGroupImageSelected.getFill();
                homePage.getCircleGroupFocused().setFill(paintImageGroup);

                VBox vBoxGroupDetails = (VBox) hBoxGroupInfo.getChildren().get(GROUP_DETAILS.getIndex());
                Label labelGroupNick = (Label) vBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());
                String[] tokens = labelGroupNick.getText().split(" +");
                String groupNick = tokens[1];

                // Clear previous listview
                homePage.getObservableListMeetings().clear();

                GroupBean groupBean = HomePage.getHandler().mapGroupBean.get(groupNick);
                for (Map.Entry<String, MeetingBean> entry : groupBean.getMapMeetings().entrySet())
                    homePage.initMeetingView(groupBean, entry.getValue());

            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////
        public static ListViewHandler<MouseEvent> getListViewHandler(){
            if(handler == null)
                handler = new ListViewHandler<>();
            return handler;
        }
        ////////////////////////////////////////////////////////////////

        /*------------------------------------- INNER_INNER_CLASS -------------------------------------*/
        public static class HyperLinkGroupMemberHandler<T extends MouseEvent> implements EventHandler<T>{

            ////////////////////////////////
            private final String groupNick;
            private final String memberNick;
            ////////////////////////////////


            /////////////////////////////////////////////////////////////////////////
            public HyperLinkGroupMemberHandler(String groupNick, String memberNick){
                this.groupNick = groupNick;
                this.memberNick = memberNick;
            }
            /////////////////////////////////////////////////////////////////////////


            ////////////////////////////////////////////////////////////////////////
            @Override
            public void handle(T event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){

                    UserBean userBean = HomePage.getHandler()
                            .mapGroupBean.get(this.groupNick)
                            .getMapMembers().get(this.memberNick);

                    if(userBean == null)
                        userBean = HomePage.getHandler()
                                .mapGroupBean.get(this.groupNick)
                                .getOwner();

                    String fullName = userBean.getName()+" "+userBean.getSurname();
                    String nickname = userBean.getNickname();
                    String image = userBean.getImageProfile();

                    if(image == null)
                        image = ANONYMOUS_PROFILE;

                    GroupMemberProfileDialog groupMemberProfileDialog =
                            new GroupMemberProfileDialog(fullName, nickname, image);

                    groupMemberProfileDialog.showAndWait();
                }
            }
            ////////////////////////////////////////////////////////////////////////

        }
        /*---------------------------------------------------------------------------------------------*/

        /*------------------------------------------- INNER_INNER_CLASS -------------------------------------------*/
        public static class MeetingChoiceChangeListener implements ChangeListener<Boolean>{

            ///////////////////////////////
            private final String meetingID;
            ///////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////////
            public MeetingChoiceChangeListener(String meetingID){
                this.meetingID = meetingID;
            }
            ////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                // current user chooses to take part into meeting
                boolean isSelected = newValue;
                if(isSelected){

                }

                // current user chooses to not take part into meeting
                else{

                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        /*----------------------------------------------------------------------------------------------------------*/

        /*------------------------------------------- INNER_INNER_CLASS --------------------------------------------*/
        public static class MeetingGalleryEventHandler<T extends MouseEvent> implements EventHandler<T>{

            ///////////////////////////////
            private final String meetingID;
            private final String groupNick;
            ///////////////////////////////

            ///////////////////////////////////////////////////////////////////////
            public MeetingGalleryEventHandler(String meetingID, String groupNick){
                this.meetingID = meetingID;
                this.groupNick = groupNick;
            }
            ///////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////
            @Override
            public void handle(T event) {
                List<String> listPhoto =
                        HomePage.getHandler().mapGroupBean
                                .get(groupNick)
                                .getMapMeetings()
                                .get(meetingID)
                                .getPhotos();

                this.showGallery(listPhoto);
            }
            ////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////////
            private void showGallery(List<String> listPhoto){
                MeetingGalleryDialog meetingGalleryDialog = new MeetingGalleryDialog(listPhoto);
                meetingGalleryDialog.showAndWait();
            }
            ////////////////////////////////////////////////////////////////////////////////////
        }
        /*----------------------------------------------------------------------------------------------------------*/

    }

    /*----------------------------------------- INNER-CLASS -----------------------------------------*/
    public static class GoogleMapsChangeListener<T extends Worker.State> implements ChangeListener<T>{
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            if (newValue == Worker.State.SUCCEEDED)
                HomePage.getLabelGoogleMapState().setText("Google Maps");
            else
                HomePage.getLabelGoogleMapState().setText("Loading...");
        }
    }
    /*-----------------------------------------------------------------------------------------------*/

    ///////////////////////////////////////////////////////////////////////////////
    private void manageUserProfile(){
        UserProfileDialog dialog = UserProfileDialog.getUserProfileDialogInstance();
        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().isEmpty())
            this.changeUserImageProfile(result.get());
    }
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void changeUserImageProfile(Map<String, String> result){
        VBox homePageProfileBox = (VBox) HomePage.getHomePageInstance(null)
                .getToolBar().getItems().get(PROFILE_BOX.getIndex());
        Circle circleHomePageImageUser = (Circle) homePageProfileBox.getChildren().get(IMAGE_USER.getIndex());

        if(result.get(IMAGE) != null && !result.get(IMAGE).equals(UPLOAD_PHOTO))
            circleHomePageImageUser.setFill(new ImagePattern(new Image(FILE + result.get(IMAGE))));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private void manageGroupCreation(){
        GroupCreationDialog dialog = GroupCreationDialog.getGroupCreationDialogInstance();
        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().isEmpty())
            this.createGroup(result.get());
        dialog.getCircleGroupImg().setFill(new ImagePattern(new Image(UPLOAD_PHOTO)));
        dialog.getLabelGroupImgPath().setText("");
        dialog.getTextFieldGroupName().setText("");
        dialog.getTextFieldGroupNick().setText("");
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    private void createGroup(Map<String, String> result){

        String groupName = result.get(NAME);
        String groupNick = result.get(NICKNAME);
        String groupImage = result.get(IMAGE);

        if(groupName != null && groupNick != null && groupImage != null) {

            GroupCreationBean groupCreationBean = new GroupCreationBean();

            groupCreationBean.setName(groupName);
            groupCreationBean.setNickname(groupNick);
            groupCreationBean.setImage(groupImage);
            groupCreationBean.setOwner(this.currUserBean.getNickname());

            try{
                // because sign-up and sign-in may not be done with this boundary
                if(this.userManageCommunityBoundary == null)
                    this.userManageCommunityBoundary = new UserManageCommunityBoundary();
                this.userManageCommunityBoundary.createGroup(groupCreationBean);
            }catch(InternalException internalException){
                errorDialog(internalException.getMessage());
            }

        }

    }
    /////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private void manageLinkRequests(){
        LinkRequestsDialog dialog = LinkRequestsDialog.getLinkRequestsDialogInstance();
        dialog.showAndWait();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageSearch(HBox searchBar){
        GridPane radioButtonsContainer = (GridPane) searchBar.getChildren().get(RADIO_BUTTONS_CONTAINER.getIndex());
        RadioButton radioButton = (RadioButton) radioButtonsContainer.getChildren().get(RADIO_BUTTON_GROUPS.getIndex());

        RadioButton selectedToggle = (RadioButton) radioButton.getToggleGroup().getSelectedToggle();
        if(selectedToggle.getText().equals("Groups")){
            SearchGroupsDialog searchGroupsDialog = new SearchGroupsDialog("Groups result", "Groups");
            searchGroupsDialog.showAndWait();
        }
        else if(selectedToggle.getText().equals("Users")){
            SearchUsersDialog searchUsersDialog = new SearchUsersDialog();
            searchUsersDialog.showAndWait();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
