package view.graphicalui.first;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import view.controllerui.first.HomePageEventHandler;
import view.controllerui.first.HomePageEventHandler.*;
import view.controllerui.first.HomePageEventHandler.GoogleMapsChangeListener;

import java.util.Collections;
import java.util.Map;

import static view.graphicalui.first.constcontainer.Css.*;
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
    private ListView<HBox> listViewGroups;
    private ObservableList<HBox> observableListGroups;
    //////////////////////////////////////////////////

    //////////////////////////////
    private Button btnDeleteGroup;
    private Button btnLinkRequests;
    ///////////////////////////////


    //////////////////////////////////////////////////
    private static final String NICKNAME = "nickname";   // due tue Sonar warning
    //////////////////////////////////////////////////


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
        this.btnDeleteGroup = new Button("Delete group", new ImageView(DELETE_GROUP));
        this.btnLinkRequests = new Button("View requests", new ImageView(LINK_REQUESTS));

        this.btnDeleteGroup.getStyleClass().addAll(SIGN_BUTTONS);
        this.btnLinkRequests.getStyleClass().addAll(SIGN_BUTTONS);

        this.btnDeleteGroup.setOnMouseClicked(handler);
        this.btnLinkRequests.setOnMouseClicked(handler);

        HBox hBoxBtnOpts = new HBox(this.btnDeleteGroup, btnLinkRequests);
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

        ListView<String> listViewMeetings = new ListView<>();
        VBox vBoxMeetingsPreview = new VBox();
        ImageView imageViewMeetings = new ImageView(MEETING);
        Label labelMeetings = new Label("No meeting available");
        vBoxMeetingsPreview.getChildren().addAll(imageViewMeetings, labelMeetings);
        vBoxMeetingsPreview.getStyleClass().add(HBOX);
        listViewMeetings.setPlaceholder(vBoxMeetingsPreview);

        vBoxMeetingArea.getChildren().addAll(this.setUpBtnDeleteMeeting(), listViewMeetings);

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
                this.map.put("name", this.textFieldGroupName.getText());
                this.map.put(NICKNAME, this.textFieldGroupNick.getText());
                this.map.put(IMG, this.labelGroupImgPath.getText());
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
                this.map.put(IMG, this.imgProfilePath);
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

    /////////////////////////////////////////////////////////////////
    public Button getBtnDeleteGroup() {
        return this.btnDeleteGroup;
    }
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    public ListView<HBox> getListViewGroups(){
        return this.listViewGroups;
    }
    /////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    public ObservableList<HBox> getObservableListGroups(){
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


    /////////////////////////////////////////////////////////////
    public static HomePage getHomePageInstance(Toolbar toolbar){
        if(homePageInstance == null && toolbar != null)
            homePageInstance = new HomePage(toolbar);
        toolBarPresent = true;
        return homePageInstance;
    }
    /////////////////////////////////////////////////////////////

}
