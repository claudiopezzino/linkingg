package view.graphicalui.first;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import view.bean.observers.GroupBean;
import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;
import view.controllerui.first.HomePageEventHandler;
import view.controllerui.first.HomePageEventHandler.GoogleMapsChangeListener;
import view.controllerui.first.HomePageEventHandler.ListViewHandler;
import view.controllerui.first.HomePageEventHandler.ListViewHandler.MeetingGalleryEventHandler;
import view.controllerui.first.HomePageEventHandler.ListViewHandler.HyperLinkGroupMemberHandler;
import view.controllerui.first.HomePageEventHandler.ListViewHandler.MeetingChoiceChangeListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.HomePageFields.*;
import static view.graphicalui.first.constcontainer.Image.*;
import static view.graphicalui.first.constcontainer.Protocol.FILE;


public class HomePage extends Parent {

    /////////////////////////////////////////
    private static HomePage homePageInstance;
    /////////////////////////////////////////

    ////////////////////////
    private ToolBar toolBar;
    ////////////////////////

    //////////////////////////////////////
    private static boolean toolBarPresent;
    //////////////////////////////////////

    /////////////////////////////////
    private Button btnProposeMeeting;
    /////////////////////////////////

    //////////////////////////////////
    private Circle circleGroupFocused;
    //////////////////////////////////

    ///////////////////////////////////////////////////////////
    private static final Label labelGoogleMapSate = new Label();
    ///////////////////////////////////////////////////////////

    //////////////////////////////////////////////////
    private ListView<VBox> listViewGroups;
    private ObservableList<VBox> observableListGroups;
    //////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    private ListView<HBox> listViewMeetings;
    private ObservableList<HBox> observableListMeetings;
    ////////////////////////////////////////////////////

    ///////////////////////////////
    private Button btnLinkRequests;
    ///////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////
    private static final HomePageEventHandler<MouseEvent> handler = new HomePageEventHandler<>();
    /////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////
    private HomePage(Toolbar toolbar){
        this.getChildren().add(this.setUpHomePage(toolbar));
    }
    //////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    private HBox setUpSearchBar(){
        HBox hBoxSearchBarContainer = new HBox();
        hBoxSearchBarContainer.getStyleClass().add(HBOX);

        TextField textFieldSearch = new TextField();
        textFieldSearch.setPromptText("User nickname");

        /*----------------------------------------------------*/
        Button btnSearch = new Button();
        btnSearch.setGraphic(new ImageView(LENS));
        btnSearch.getStyleClass().add(BUTTONS_PREVIEW);
        btnSearch.setOnMouseClicked(handler);
        /*-----------------------------------------------------*/

        hBoxSearchBarContainer.getChildren().addAll(textFieldSearch, btnSearch);

        return hBoxSearchBarContainer;
    }
    //////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////
    private Button setUpBtnDeleteMeeting(){
        Button btnDeleteMeeting = new Button("Delete Meeting", new ImageView(DELETE_MEETING));
        btnDeleteMeeting.getStyleClass().add(SIGN_BUTTONS);
        btnDeleteMeeting.setOnMouseClicked(handler);
        return btnDeleteMeeting;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private HBox setUpGroupBtnOpts(){
        Button btnDeleteGroup = new Button("Delete group", new ImageView(DELETE_GROUP));
        this.btnLinkRequests = new Button("View requests", new ImageView(LINK_REQUESTS));

        btnDeleteGroup.getStyleClass().addAll(SIGN_BUTTONS);
        this.btnLinkRequests.getStyleClass().addAll(SIGN_BUTTONS);

        btnDeleteGroup.setOnMouseClicked(handler);
        this.btnLinkRequests.setOnMouseClicked(handler);

        HBox hBoxBtnOpts = new HBox(btnDeleteGroup, btnLinkRequests);
        hBoxBtnOpts.getStyleClass().addAll(HBOX, VBOX);

        return hBoxBtnOpts;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    private VBox setUpGroupsArea(){

        VBox vBoxGroupArea = new VBox();
        vBoxGroupArea.getStyleClass().addAll(HBOX, VBOX);

        this.observableListGroups = FXCollections.observableArrayList();
        this.listViewGroups = new ListView<>();
        this.listViewGroups.setOnMouseClicked(ListViewHandler.getListViewHandler());

        VBox vBoxGroupsPreview = new VBox();
        ImageView imageViewGroupsPreview = new ImageView(GROUP);
        Label labelGroupsPreview = new Label("No group available");
        vBoxGroupsPreview.getChildren().addAll(imageViewGroupsPreview, labelGroupsPreview);
        vBoxGroupsPreview.getStyleClass().add(HBOX);

        this.listViewGroups.setPlaceholder(vBoxGroupsPreview);
        this.listViewGroups.setItems(this.observableListGroups);

        Label labelButtonScope = new Label("WARNING:   Following buttons work only for focused group/meeting!");

        vBoxGroupArea.getChildren().addAll(labelButtonScope, this.setUpGroupBtnOpts(), this.listViewGroups);

        return vBoxGroupArea;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    private VBox setUpMeetingsArea(){

        VBox vBoxMeetingArea = new VBox();
        vBoxMeetingArea.getStyleClass().addAll(HBOX, VBOX);

        this.listViewMeetings = new ListView<>();
        this.observableListMeetings = FXCollections.observableArrayList();

        VBox vBoxMeetingsPreview = new VBox();
        ImageView imageViewMeetings = new ImageView(MEETING);
        Label labelMeetings = new Label("No meeting available");
        vBoxMeetingsPreview.getChildren().addAll(imageViewMeetings, labelMeetings);
        vBoxMeetingsPreview.getStyleClass().add(HBOX);

        this.listViewMeetings.setPlaceholder(vBoxMeetingsPreview);
        this.listViewMeetings.setItems(this.observableListMeetings);

        vBoxMeetingArea.getChildren().addAll(this.setUpBtnDeleteMeeting(), this.listViewMeetings);

        return vBoxMeetingArea;
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    private BorderPane setUpGroupsAndMeetingsArea(){
        BorderPane borderPaneGroupsAndMeetingArea = new BorderPane();

        borderPaneGroupsAndMeetingArea.setTop(setUpGroupsArea());
        borderPaneGroupsAndMeetingArea.setBottom(setUpMeetingsArea());

        return borderPaneGroupsAndMeetingArea;
    }
    /////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private HBox setUpMapBar(){

        /*-----------------------------------------------------------------------------------------*/
        btnProposeMeeting = new Button("Propose meeting", new ImageView(LOCATION));
        btnProposeMeeting.getStyleClass().add(SIGN_BUTTONS);
        btnProposeMeeting.setOnMouseClicked(handler);
        /*-----------------------------------------------------------------------------------------*/

        /*-------------------------------------------------------------------------------*/
        circleGroupFocused = new Circle(30);
        circleGroupFocused.setFill(new ImagePattern(new Image(CREATE_GROUP)));
        /*--------------------------------------------------------------------------------*/

        HBox hBoxMapBar = new HBox();
        hBoxMapBar.getStyleClass().addAll(VBOX, HBOX);
        hBoxMapBar.getChildren().addAll(circleGroupFocused, new Separator(), setUpSearchBar(), new Separator(), btnProposeMeeting, new Separator(), labelGoogleMapSate);

        return hBoxMapBar;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private WebView setUpMapContainer(){
        // add WebEngine to load Google Maps
        WebView webViewMap = new WebView();
        WebEngine webEngine = webViewMap.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(new GoogleMapsChangeListener<>());
        webEngine.load("https://www.google.it");
        return webViewMap;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    private BorderPane setUpSearchArea(){
        BorderPane borderPaneSearchArea = new BorderPane();

        borderPaneSearchArea.setTop(setUpMapBar());
        borderPaneSearchArea.setCenter(setUpMapContainer());

        return borderPaneSearchArea;
    }
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    private VBox setUpHomePage(Toolbar toolbar){

        this.toolBar = toolbar.draw();

        VBox vBoxHomePageRoot = new VBox();
        vBoxHomePageRoot.getStyleClass().add(VBOX);

        BorderPane borderPaneUserArea = new BorderPane();
        borderPaneUserArea.setBottom(setUpSearchArea());
        borderPaneUserArea.setTop(setUpGroupsAndMeetingsArea());

        vBoxHomePageRoot.getChildren().addAll(this.toolBar, borderPaneUserArea);

        return vBoxHomePageRoot;
    }
    ////////////////////////////////////////////////////////////////////

    /*------------------------------ INNER_CLASS -------------------------------*/
    public static class SearchGroupsDialog extends PageDialog{


        ///////////////////////////////////////////////////////////
        public SearchGroupsDialog(String title, String header){
            super(title, header, CREATE_GROUP);
            this.getDialogPane().setContent(this.setUpPopUpRoot());
            this.setResultConverter(this::searchGroupsResult);
        }
        ///////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////
        private Map<String, String> searchGroupsResult(ButtonType buttonType){
            // TO DO
            return Collections.emptyMap();
        }
        ///////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().add(IMG_CONTAINER);

            VBox vBoxPlaceholder = new VBox(new ImageView(GROUP), new Label("No group available"));
            vBoxPlaceholder.getStyleClass().add(HBOX);

            // with "setItems(ObservableList)" is possible to change elems
            ListView<HBox> listViewGroups = new ListView<>();
            listViewGroups.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listViewGroups.setPlaceholder(vBoxPlaceholder);

            vBoxRoot.getChildren().addAll(listViewGroups);

            return vBoxRoot;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////


    }
    /*--------------------------------------------------------------------------*/

    /*------------------------------ INNER_CLASS -------------------------------*/
    public static class SearchUsersDialog extends PageDialog{


        ///////////////////////////////////////////////////////////////
        public SearchUsersDialog() {
            super("Users Result", "Users", LINK_REQUESTS);
            this.getDialogPane().setContent(this.setUpPopUpRoot());
            this.setResultConverter(this::searchUsersResult);
        }
        ////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////
        private Map<String, String> searchUsersResult(ButtonType buttonType){
            // TO DO
            return Collections.emptyMap();
        }
        /////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().add(IMG_CONTAINER);

            VBox vBoxPlaceholder = new VBox();
            vBoxPlaceholder.getStyleClass().add(HBOX);

            vBoxPlaceholder.getChildren().addAll(new ImageView(NO_USER), new Label("No user available"));

            ListView<HBox> listViewUsers = new ListView<>();
            listViewUsers.setPlaceholder(vBoxPlaceholder);

            vBoxRoot.getChildren().addAll(listViewUsers);

            return vBoxRoot;
        }
        ///////////////////////////////////////////////////////////////////////////

    }
    /*--------------------------------------------------------------------------*/

    /*------------------------------------- INNER_CLASS ------------------------------------------*/
    public static class GroupDeletionDialog extends PageDialog{

        ///////////////////////////////////
        private final Circle groupImage;
        private final Label groupName;
        private final Label groupNickname;
        ///////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////
        public GroupDeletionDialog(Circle groupImage, Label groupName, Label groupNickname) {
            super("Delete group", "Delete", QUESTION);

            this.groupImage = groupImage;
            this.groupName = groupName;
            this.groupNickname = groupNickname;

            this.getDialogPane().setContent(setUpPopUpRoot());
            this.setResultConverter(this::groupDeletionResult);
        }
        ////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////
        private Map<String, String> groupDeletionResult(ButtonType buttonType){
            if(buttonType == this.getBtnTypeSave()) {
                this.map.put(NICKNAME, this.groupNickname.getText());
                return this.map;
            }
            return Collections.emptyMap();
        }
        /////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxDeletionDialogRoot = new VBox();

            VBox vBoxGroupDetails = new VBox();
            vBoxGroupDetails.getStyleClass().addAll(IMG_CONTAINER);
            vBoxGroupDetails.getChildren().addAll(this.groupName, this.groupNickname);

            HBox hBoxGroupInfo = new HBox();
            hBoxGroupInfo.getStyleClass().addAll(IMG_CONTAINER);
            hBoxGroupInfo.getChildren().addAll(this.groupImage, vBoxGroupDetails);

            vBoxDeletionDialogRoot.getChildren().addAll(hBoxGroupInfo);
            return vBoxDeletionDialogRoot;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////


    }
    /*-------------------------------------------------------------------------------------------------------------*/


    /*--------------------------------- INNER_CLASS --------------------------------*/
    public static class GroupCreationDialog extends PageDialog {

        /////////////////////////////////
        private Label labelGroupImgPath;
        private Circle circleGroupImg;
        /////////////////////////////////

        /////////////////////////////////////
        private TextField textFieldGroupName;
        private TextField textFieldGroupNick;
        /////////////////////////////////////

        ///////////////////////////////////////////////////////////////
        private static GroupCreationDialog groupCreationDialogInstance;
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////////////
        private GroupCreationDialog(){
            super("New group", "New Group", CREATE_GROUP);
            this.getDialogPane().setContent(setUpPopUpRoot());
            this.setResultConverter(this::groupCreationResult);
        }
        ////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        private Map<String, String> groupCreationResult(ButtonType buttonType) {
            if(buttonType == this.getBtnTypeSave()) {
                if(this.textFieldGroupName.getText().isEmpty()
                        || this.textFieldGroupNick.getText().isEmpty()
                        || this.labelGroupImgPath.getText().isEmpty())
                    return Collections.emptyMap();
                this.map.put(NAME, this.textFieldGroupName.getText());
                this.map.put(NICKNAME, this.textFieldGroupNick.getText());
                this.map.put(IMAGE, this.labelGroupImgPath.getText());
                return this.map;
            }
            return Collections.emptyMap();
        }
        ////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot(){
            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().addAll(VBOX, HBOX);

            circleGroupImg = new Circle(40);
            circleGroupImg.setFill(new ImagePattern(new Image(UPLOAD_PHOTO)));
            circleGroupImg.getStyleClass().add(CIRCLE);
            circleGroupImg.setOnMouseClicked(dialogHandler);

            labelGroupImgPath = new Label();

            textFieldGroupName = new TextField();
            textFieldGroupName.setPromptText("Name");

            textFieldGroupNick = new TextField();
            textFieldGroupNick.setPromptText("Nickname");

            vBoxRoot.getChildren().addAll(circleGroupImg, textFieldGroupName, textFieldGroupNick);

            return vBoxRoot;
        }
        ///////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////
        public Circle getCircleGroupImg() {
            return this.circleGroupImg;
        }
        /////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////
        public Label getLabelGroupImgPath(){
            return this.labelGroupImgPath;
        }
        //////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        public TextField getTextFieldGroupName(){
            return this.textFieldGroupName;
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        public TextField getTextFieldGroupNick() {
            return this.textFieldGroupNick;
        }
        ////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////
        public static GroupCreationDialog getGroupCreationDialogInstance(){
            if(groupCreationDialogInstance == null)
                groupCreationDialogInstance = new GroupCreationDialog();
            return groupCreationDialogInstance;
        }
        ////////////////////////////////////////////////////////////////////
    }
    /*------------------------------------------------------------------------------*/


    /*-------------------------------- INNER_CLASS ---------------------------------*/
    public static class LinkRequestsDialog extends PageDialog{

        /////////////////////////////////////////////////////////////
        private static LinkRequestsDialog linkRequestsDialogInstance;
        /////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////
        private LinkRequestsDialog() {
            super("Link requests", "Requests", LINK_REQUESTS);
            this.getDialogPane().setContent(setUpPopUpRoot());
            this.setResultConverter(this::linkRequestsResult);
        }
        //////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////
        private Map<String, String> linkRequestsResult(ButtonType buttonType){
            // TO DO
            return Collections.emptyMap();
        }
        ///////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().add(IMG_CONTAINER);

            VBox vBoxPlaceholder = new VBox(new ImageView(NO_USER), new Label("No user available"));
            vBoxPlaceholder.getStyleClass().add(HBOX);

            ListView<HBox> listViewUserRequests = new ListView<>();
            listViewUserRequests.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listViewUserRequests.setPlaceholder(vBoxPlaceholder);

            vBoxRoot.getChildren().add(listViewUserRequests);

            return vBoxRoot;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////
        public static LinkRequestsDialog getLinkRequestsDialogInstance() {
            if(linkRequestsDialogInstance == null)
                linkRequestsDialogInstance = new LinkRequestsDialog();
            return linkRequestsDialogInstance;
        }
        ///////////////////////////////////////////////////////////////////

    }
    /*------------------------------------------------------------------------------*/

    /*-------------------------------- INNER_CLASS ---------------------------------*/
    public static class MeetingGalleryDialog extends PageDialog{

        /////////////////////////////////////
        private final List<String> listPhoto;
        /////////////////////////////////////

        /////////////////////////////////////////////////////////////
        public MeetingGalleryDialog(List<String> listPhoto) {
            super("Meeting Gallery", "Gallery", GALLERY);
            this.listPhoto = listPhoto;
            this.getDialogPane().setContent(this.setUpPopUpRoot());
        }
        /////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().add(IMG_CONTAINER);

            ObservableList<ImageView> observableListImage = FXCollections.observableArrayList();
            ListView<ImageView> listViewImage = new ListView<>();
            listViewImage.setItems(observableListImage);

            for(String photo : this.listPhoto)
                observableListImage.add(new ImageView(photo));

            vBoxRoot.getChildren().add(listViewImage);

            return vBoxRoot;
        }
        ////////////////////////////////////////////////////////////////////////////////////////
    }
    /*------------------------------------------------------------------------------*/

    /*-------------------------------- INNER_CLASS ---------------------------------*/
    public static class GroupMemberProfileDialog extends PageDialog{

        //////////////////////////////
        private final String fullName;
        private final String nickname;
        private final String image;
        //////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////
        public GroupMemberProfileDialog(String fullName, String nickname, String image) {
            super("Member", "Member", PROFILE);
            this.fullName = fullName;
            this.nickname = nickname;
            this.image = image;
            this.getDialogPane().setContent(this.setUpPopUpRoot());
        }
        /////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {
            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().addAll(IMG_CONTAINER, HBOX);

            Label labelFullName = new Label(this.fullName);
            Label labelNickname = new Label(this.nickname);

            Circle circleImage = new Circle(40);
            circleImage.getStyleClass().add(CIRCLE);
            circleImage.setFill(new ImagePattern(new Image(this.image)));

            vBoxRoot.getChildren().addAll(circleImage, labelFullName, labelNickname);

            return vBoxRoot;
        }
        ////////////////////////////////////////////////////////////////////////////
    }
    /*------------------------------------------------------------------------------*/

    /*---------------------- INNER_CLASS -----------------------*/
    public static class UserProfileDialog extends PageDialog {

        ///////////////////////////////////////////////////////////
        private static UserProfileDialog userProfileDialogInstance;
        ///////////////////////////////////////////////////////////

        //////////////////////////////////
        private Button btnLinkInvitations;
        private Button btnNickname;
        private Button btnPassword;
        ////////////////////////////

        ////////////////////////////
        private Label labelNickname;
        private Label labelFullName;
        ////////////////////////////

        //////////////////////////////
        private String imgProfilePath;
        private String oldImgProfilePath;
        /////////////////////////////////

        ////////////////////////////////
        private Circle circleImgProfile;
        ////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////
        private UserProfileDialog(){
            super("Profile", "Profile", PROFILE);
            this.getDialogPane().setContent(setUpPopUpRoot());
            this.setResultConverter(this::userProfileResult);
        }
        ///////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private Map<String, String> userProfileResult(ButtonType buttonType){
            if(buttonType == this.getBtnTypeSave()) {
                if(this.imgProfilePath != null && !this.imgProfilePath.equals(UPLOAD_PHOTO))
                    this.oldImgProfilePath = this.imgProfilePath;
                this.map.put(IMAGE, this.imgProfilePath);
                return this.map;
            }
            else {
                if(this.imgProfilePath != null && !this.imgProfilePath.equals(UPLOAD_PHOTO))
                    this.imgProfilePath = this.oldImgProfilePath;
                if(!this.oldImgProfilePath.equals(UPLOAD_PHOTO))
                    this.circleImgProfile.setFill(new ImagePattern(new Image(FILE + this.oldImgProfilePath)));
                else
                    this.circleImgProfile.setFill(new ImagePattern(new Image(this.oldImgProfilePath)));
                return Collections.emptyMap();
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {
            VBox vBoxRoot = new VBox();
            vBoxRoot.getStyleClass().addAll(VBOX, HBOX);

            circleImgProfile = new Circle(40);
            circleImgProfile.getStyleClass().add(CIRCLE);
            circleImgProfile.setFill(new ImagePattern(new Image(UPLOAD_PHOTO)));
            circleImgProfile.setOnMouseClicked(dialogHandler);

            labelNickname = new Label("(@nickname)");
            labelFullName = new Label("Name Surname");
            oldImgProfilePath = UPLOAD_PHOTO;

            btnLinkInvitations = new Button("link invitations");
            btnLinkInvitations.getStyleClass().add(DIALOG_BUTTONS);
            btnLinkInvitations.setOnMouseClicked(dialogHandler);

            VBox vBoxProfileContainer = new VBox(labelNickname, labelFullName, circleImgProfile, btnLinkInvitations);
            vBoxProfileContainer.getStyleClass().add(HBOX);

            btnNickname = new Button(NICKNAME);
            btnNickname.getStyleClass().add(DIALOG_BUTTONS);
            btnNickname.setOnMouseClicked(dialogHandler);

            btnPassword = new Button("password");
            btnPassword.getStyleClass().add(DIALOG_BUTTONS);
            btnPassword.setOnMouseClicked(dialogHandler);

            HBox hBoxUserPassContainer = new HBox(btnNickname, btnPassword);
            hBoxUserPassContainer.getStyleClass().add(HBOX);

            vBoxRoot.getChildren().addAll(vBoxProfileContainer, hBoxUserPassContainer);

            return vBoxRoot;
        }
        ////////////////////////////////////////////////////////////////////////////////////////


        /*------------------- INNER_INNER_CLASS -------------------*/
        public static class UsernameDialog extends PageDialog {

            /////////////////////////////////////////////////////
            private static UsernameDialog usernameDialogInstance;
            /////////////////////////////////////////////////////

            ///////////////////////////////////////
            private TextField textFieldNewNickname;
            private Label labelCurrNickname;
            ///////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////
            private UsernameDialog(){
                super("Change nickname","Nickname", USERNAME);
                this.getDialogPane().setContent(setUpPopUpRoot());
                this.setResultConverter(this::usernameResult);
            }
            ////////////////////////////////////////////////////////////////////////////////////////////

            /////////////////////////////////////////////////////////////////////////////////////////////////
            private Map<String, String> usernameResult(ButtonType buttonType){
                if(buttonType == this.getBtnTypeSave() && !this.textFieldNewNickname.getText().isEmpty()) {
                    this.map.put("newNickname", this.textFieldNewNickname.getText());
                    return this.map;
                }
                return Collections.emptyMap();
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////
            @Override
            protected VBox setUpPopUpRoot() {
                VBox vBoxRoot = new VBox();
                vBoxRoot.getStyleClass().addAll(VBOX, HBOX);

                labelCurrNickname = new Label("Nickname1");

                textFieldNewNickname = new TextField();
                textFieldNewNickname.setPromptText("New nickname");

                vBoxRoot.getChildren().addAll(labelCurrNickname, textFieldNewNickname);

                return vBoxRoot;
            }
            ///////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////
            public TextField getTextFieldNewNickname(){
                return this.textFieldNewNickname;
            }
            ////////////////////////////////////////////////////////////////////////////////

            //////////////////////////////////////////////////////////////////////
            public Label getLabelCurrNickname() {
                return this.labelCurrNickname;
            }
            //////////////////////////////////////////////////////////////////////


            /////////////////////////////////////////////////////////
            public static UsernameDialog getUsernameDialogInstance(){
                if(usernameDialogInstance == null)
                    usernameDialogInstance = new UsernameDialog();
                return usernameDialogInstance;
            }
            /////////////////////////////////////////////////////////

        }
        /*---------------------------------------------------------*/


        /*------------------- INNER_INNER_CLASS -------------------*/

        public static class PasswordDialog extends PageDialog {

            /////////////////////////////////////////////////////
            private static PasswordDialog passwordDialogInstance;
            /////////////////////////////////////////////////////

            ////////////////////////////////////////
            private TextField textFieldCurrPassword;
            private TextField textFieldNewPassword;
            ///////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            private PasswordDialog(){
                super("Change password","Password", PASSWORD);
                this.getDialogPane().setContent(setUpPopUpRoot());
                this.setResultConverter(this::passwordResult);
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////
            private Map<String, String> passwordResult(ButtonType buttonType){
                if(buttonType == this.getBtnTypeSave()) {
                    this.map.put("currPassword", this.textFieldCurrPassword.getText());
                    this.map.put("newPassword", this.textFieldNewPassword.getText());
                    return this.map;
                }
                return Collections.emptyMap();
            }
            ///////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            protected VBox setUpPopUpRoot() {

                VBox vBoxRoot = new VBox();
                vBoxRoot.getStyleClass().addAll(VBOX, HBOX);

                textFieldCurrPassword = new PasswordField();
                textFieldCurrPassword.setPromptText("Current password");

                textFieldNewPassword = new PasswordField();
                textFieldNewPassword.setPromptText("New password");

                TextField textFieldConfirmNewPassword = new PasswordField();
                textFieldConfirmNewPassword.setPromptText("Confirm new password");

                vBoxRoot.getChildren().addAll(textFieldCurrPassword, textFieldNewPassword, textFieldConfirmNewPassword);

                return vBoxRoot;
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////


            /////////////////////////////////////////////////////////
            public static PasswordDialog getPasswordDialogInstance(){
                if(passwordDialogInstance == null)
                    passwordDialogInstance = new PasswordDialog();
                return passwordDialogInstance;
            }
            /////////////////////////////////////////////////////////

        }

        /*---------------------------------------------------------*/


        ///////////////////////////////////////////////////////////
        public Button getBtnNickname() {
            return this.btnNickname;
        }
        ///////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////
        public Button getBtnPassword() {
            return this.btnPassword;
        }
        ///////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////
        public Button getBtnLinkInvitations() {
            return this.btnLinkInvitations;
        }
        /////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////////////
        public void setImgProfilePath(String imgProfilePath) {
            this.imgProfilePath = imgProfilePath;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        public Label getLabelFullName() {
            return this.labelFullName;
        }
        //////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        public Label getLabelNickname() {
            return this.labelNickname;
        }
        //////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////
        public Circle getCircleImgProfile() {
            return this.circleImgProfile;
        }
        /////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////
        public static UserProfileDialog getUserProfileDialogInstance(){
            if(userProfileDialogInstance == null)
                userProfileDialogInstance = new UserProfileDialog();
            return userProfileDialogInstance;
        }
        ////////////////////////////////////////////////////////////////

    }
    /*----------------------------------------------------------*/

    ///////////////////////////////////////////////////////////////////////////
    public static Label getLabelGoogleMapState(){
        return labelGoogleMapSate;
    }
    ///////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    public Circle getCircleGroupFocused() {
        return this.circleGroupFocused;
    }
    /////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    public Button getBtnLinkRequests() {
        return this.btnLinkRequests;
    }
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    public ListView<VBox> getListViewGroups(){
        return this.listViewGroups;
    }
    /////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public ListView<HBox> getListViewMeetings(){
        return this.listViewMeetings;
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public ObservableList<HBox> getObservableListMeetings() {
        return this.observableListMeetings;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    public ObservableList<VBox> getObservableListGroups() {
        return this.observableListGroups;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    public Button getBtnProposeMeeting(){
        return this.btnProposeMeeting;
    }
    ///////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public ToolBar getToolBar() {
        return this.toolBar;
    }
    ////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public static boolean isToolBarPresent(){
        return toolBarPresent;
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    public static HomePageEventHandler<MouseEvent> getHandler(){
        return handler;
    }
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void initGroupView(UserBean currUserBean, GroupBean groupBean){
        VBox vBoxGroupPreview = new VBox();
        vBoxGroupPreview.getStyleClass().addAll(IMG_CONTAINER);

        HBox hBoxGroupDetails = new HBox();
        hBoxGroupDetails.getStyleClass().addAll(IMG_CONTAINER);

        Circle circleGroupImage = new Circle(40);
        circleGroupImage.setFill(new ImagePattern(new Image(FILE + groupBean.getImage())));

        Label labelGroupName = new Label("Name:    " + groupBean.getName());
        Label labelGroupNick = new Label("Nickname:    " + groupBean.getNickname());
        Label labelOwnerNick = new Label("Owner:   ");
        Label labelMembers = new Label("Members");

        Hyperlink hyperlinkOwner = new Hyperlink();
        String ownerNick = groupBean.getOwner().getNickname();
        if(currUserBean.getNickname().equals(ownerNick))
            hyperlinkOwner.setText("YOU");
        else
            hyperlinkOwner.setText("@" + ownerNick);
        hyperlinkOwner.setOnMouseClicked(new HyperLinkGroupMemberHandler<>(groupBean.getNickname(), ownerNick));

        HBox hBoxOwner = new HBox(labelOwnerNick, hyperlinkOwner);
        hBoxOwner.getStyleClass().add(HBOX);

        VBox vBoxLabelContainer = new VBox(labelGroupName, labelGroupNick);
        vBoxLabelContainer.getStyleClass().addAll(IMG_CONTAINER);

        hBoxGroupDetails.getChildren().addAll(circleGroupImage, vBoxLabelContainer);

        FlowPane flowPaneMembers = new FlowPane();
        for(Map.Entry<String, UserBean> entry : groupBean.getMapMembers().entrySet()){
            String userNick = entry.getValue().getNickname();
            Hyperlink hyperlinkUserNick = new Hyperlink("@"+userNick);
            hyperlinkUserNick.setOnMouseClicked(new HyperLinkGroupMemberHandler<>(groupBean.getNickname(), userNick));
            flowPaneMembers.getChildren().add(hyperlinkUserNick);
        }

        VBox vBoxMembers = new VBox();
        vBoxMembers.getStyleClass().addAll(IMG_CONTAINER);
        vBoxMembers.getChildren().addAll(labelMembers, flowPaneMembers);

        vBoxGroupPreview.getChildren().addAll(hBoxOwner, hBoxGroupDetails, vBoxMembers);

        this.observableListGroups.add(vBoxGroupPreview);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // to add "YOU" when currUser is the scheduler of target meeting such as group owner
    public void initMeetingView(GroupBean groupBean, MeetingBean meetingBean){
        Label labelMeetingID = new Label("#"+meetingBean.getId());
        Label labelScheduler = new Label("Scheduler:   @" + meetingBean.getScheduler().getNickname());
        Label labelMeetingName = new Label(meetingBean.getName());
        Label labelDate = new Label(meetingBean.getDate());
        Label labelTime = new Label(meetingBean.getTime());
        Label labelJoiners = new Label("Joiners");
        Label labelChoice = new Label("Join:   ");

        Circle circleMeetingImage = new Circle(40);
        circleMeetingImage.setFill(new ImagePattern(new Image(FILE + meetingBean.getImage())));

        Button btnGallery = new Button("Gallery");
        btnGallery.setOnMouseClicked(new MeetingGalleryEventHandler<>(meetingBean.getId(), groupBean.getNickname()));

        FlowPane flowPaneJoiners = new FlowPane();
        for(Map.Entry<String, UserBean> entry : meetingBean.getJoiners().entrySet())
            flowPaneJoiners.getChildren().add(new Label("@"+entry.getValue().getNickname()));

        ToggleGroup toggleGroupChoice = new ToggleGroup();
        RadioButton radioButtonYes = new RadioButton("Yes");
        RadioButton radioButtonNo = new RadioButton("No");

        /* it's ok only this radio button because when it is selected is fired up the logic to add current user into
         * this meeting, while when it is selected the other radio button this radio button is unselected and so it
         * is fired up the logic to remove current user from this meeting */
        radioButtonYes.selectedProperty().addListener(new MeetingChoiceChangeListener(meetingBean.getId()));

        String currUserNick = UserProfileDialog.getUserProfileDialogInstance().getLabelNickname().getText();
        if(meetingBean.getJoiners().get(currUserNick).getNickname() != null)
            radioButtonYes.setSelected(true);
        else
            radioButtonNo.setSelected(true);
        radioButtonYes.setToggleGroup(toggleGroupChoice);
        radioButtonNo.setToggleGroup(toggleGroupChoice);


        HBox hBoxMeetingView = new HBox();
        hBoxMeetingView.getStyleClass().add(IMG_CONTAINER);
        hBoxMeetingView.getChildren().addAll(circleMeetingImage, labelMeetingName);

        HBox hBoxTimeTable = new HBox();
        hBoxTimeTable.getStyleClass().add(IMG_CONTAINER);
        hBoxTimeTable.getChildren().addAll(labelDate, labelTime);

        HBox hBoxJoinChoice = new HBox();
        hBoxJoinChoice.getStyleClass().add(IMG_CONTAINER);
        hBoxJoinChoice.getChildren().addAll(labelChoice, radioButtonYes, radioButtonNo);

        VBox vBoxMeetingContainer = new VBox();
        vBoxMeetingContainer.getStyleClass().add(IMG_CONTAINER);
        vBoxMeetingContainer.getChildren().addAll(labelMeetingID, labelScheduler, hBoxMeetingView, hBoxTimeTable, btnGallery);

        VBox vBoxJoinersContainer = new VBox();
        vBoxJoinersContainer.getStyleClass().add(IMG_CONTAINER);
        vBoxJoinersContainer.getChildren().addAll(labelJoiners, flowPaneJoiners, hBoxJoinChoice);

        HBox hBoxMainContainer = new HBox();
        hBoxMainContainer.getStyleClass().add(IMG_CONTAINER);
        hBoxMainContainer.getChildren().addAll(vBoxMeetingContainer, vBoxJoinersContainer);

        this.observableListMeetings.add(hBoxMainContainer);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////
    public static HomePage getHomePageInstance(Toolbar toolbar){
        if(homePageInstance == null && toolbar != null)
            homePageInstance = new HomePage(toolbar);
        toolBarPresent = true;
        return homePageInstance;
    }
    /////////////////////////////////////////////////////////////

}
