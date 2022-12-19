package view.graphicalui.second;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import view.controllerui.second.handlerstates.*;
import view.graphicalui.second.groupquestionstates.QuestionGroupName;
import view.graphicalui.second.meetingquestionstates.QuestionTypeOfMeeting;
import view.graphicalui.second.passwordquestionstates.QuestionCurrentPassword;
import view.graphicalui.second.usernamequestionstates.QuestionNewUsername;

import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.second.DefaultCommands.*;


public class Home extends Shell{

    /////////////////////////////////
    private static Home homeInstance;
    /////////////////////////////////

    ///////////////////////////////////////
    private static String currUserFullName;
    ///////////////////////////////////////

    ////////////////////////////////
    private String currUserNickname;
    private String oldUserNickname;
    ///////////////////////////////

    ///////////////////////////////
    private String oldUserPassword;    // copy of userPassword before update
    private String currUserPassword;   // to fetch from DB
    ////////////////////////////////

    //////////////////////////
    private String welcomeMsg;
    //////////////////////////

    ///////////////////////////////////////////////////////////////////
    private static final String MAIN_OPTIONS = "\n\n\n->   search\n\n" +
            "->   create group\n\n" +
            "->   view groups\n\n " +
            "->   manage profile\n\n\n\n";
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////
    private String infoSearchMode;
    private String infoSearchType;
    //////////////////////////////

    //////////////////////////////
    private String currSearchMode;
    private String currSearchFilter;
    ////////////////////////////////

    ////////////////////////////
    private String searchTarget;
    private String newGroupMember;
    private List<String> targetGroups;
    //////////////////////////////////

    ///////////////////////////////
    private List<String> blacklist;
    ///////////////////////////////

    //////////////////////////////////
    private List<String> groupMembers;
    ///////////////////////////////////

    ////////////////////////////////////
    private List<String> meetingFilters;
    ////////////////////////////////////

    //////////////////////////////////////////
    private QuestionStateMachine stateMachine;
    //////////////////////////////////////////

    //////////////////////////////
    private String groupOwnerNick;
    //////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private List<Pair<String, String>> listOwnGroups;
    private List<Pair<String, String>> listOtherGroups;
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    private List<Pair<String, String>> listOwnMeetings;
    private List<Pair<String, String>> listOtherMeetings;
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    private List<Pair<String, String>> listGroupMembers;
    ////////////////////////////////////////////////////

    ////////////////////////////////////////
    private List<String> listMeetingJoiners;
    ////////////////////////////////////////

    /////////////////////////////////
    // both member and meeting details
    private List<String> listDetails;
    /////////////////////////////////

    //////////////////////////
    private String groupName;
    private String groupNickname;
    /////////////////////////////

    /////////////////////////
    private String meetingId;
    /////////////////////////

    ///////////////////////////////////////////
    // End Of Name
    private static final String EON = ")   \n\t";
    // End Of Line
    private static final String EOL = "\n\n\n";
    ///////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    private Home(){
        // MAIN
        this.setUpTitle(new Text("Home"));
        this.setUpPrompt();
        this.setUpScreen();
        this.getChildren().add(this.setUpHomeRoot());
        handler.getStateMachine().setState(StateMain.getStateMainInstance());
    }
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    private VBox setUpHomeRoot(){
        VBox root = new VBox();
        root.getChildren().addAll(this.titleContainer, this.screen, this.prompt);
        return root;
    }
    /////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    @Override
    public void setUpScreen() {
        this.screen = new TextArea();
        this.screen.setEditable(false);
        this.welcomeMsg = "\nHello, " + currUserFullName +
                "!  [Type 'home page' to return into home]\n\n\n" +
                "In this page you can ";
        this.screen.setText(LEGEND + welcomeMsg + MAIN_OPTIONS);
        this.screen.setFocusTraversable(false);
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    @Override
    public void restoreScreen(){
        // MAIN
        this.screen.setText(LEGEND + welcomeMsg + MAIN_OPTIONS);
        this.screen.setFocusTraversable(false);
        handler.getStateMachine().setState(StateMain.getStateMainInstance());
    }
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public void manageProfile(){
        // MANAGE_PROFILE
        this.screen.setText(LEGEND + welcomeMsg + "manage \n\n\n" +
                "->   nickname\n\n" +
                "->   password\n\n" +
                "->   image profile\n\n"+
                "->   link invitations\n\n\n");
        handler.getStateMachine()
                .setState(StateManageProfile.getStateManageProfileInstance());
    }
    //////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showLinkInvitations(){
        if(this.targetGroups == null)
            this.initTargetGroups(); // due to sonar warning

        this.screen.setText(LEGEND + welcomeMsg + " manage your link invitations for groups");
        this.screen.appendText("\n\n\n results available here...\n\n\n");

        this.screen.appendText("\u2022 Type groups nickname to mark them as possible groups to join. \n\n" +
                "\u2022 Type 'accept' to join selected groups. \n\n\n");

        this.screen.appendText("Groups' nickname:   ");

        handler.getStateMachine().setState(StateUserLinkInvitations.getStateUserLinkInvitationsInstance());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    public void linkInvitationsPreviousList(){
        if(this.targetGroups == null || this.targetGroups.isEmpty())
            this.showLinkInvitations();
        else{
            this.targetGroups.remove(this.targetGroups.size() - 1);
            this.showLinkInvitations();
            for (String target : this.targetGroups)
                this.screen.appendText(target + ",  ");
        }
    }
    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public void usernameChangeMode(){
        // USERNAME_CHANGE
        if(this.oldUserNickname != null && !this.currUserNickname.equals(this.oldUserNickname))
            this.currUserNickname = this.oldUserNickname;
        this.stateMachine = new QuestionStateMachine();
        this.stateMachine.setQuestion(QuestionNewUsername.getQuestionNewUsernameInstance());
        this.screen.setText(LEGEND + welcomeMsg + "manage your nickname\n\n\n" +
                " \u2022  Current nickname:  " + this.currUserNickname);
        this.stateMachine.displayQuestion();
        this.stateMachine.nextQuestion();
        handler.getStateMachine().setState(StateUsernameChange.getStateUsernameChangeInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void passwordChangeMode(){
        // PASSWORD_CHANGE
        this.stateMachine = new QuestionStateMachine();
        this.stateMachine.setQuestion(QuestionCurrentPassword.getQuestionCurrentPasswordInstance());
        this.screen.appendText(LEGEND + welcomeMsg + "manage your password\n\n\n");
        this.stateMachine.displayQuestion();
        handler.getStateMachine().setState(StatePasswordChange.getStatePasswordChangeInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    public void saveNewUsername(String newNickname){
        // develop logic
        this.oldUserNickname = this.currUserNickname;
        this.currUserNickname = newNickname;
    }
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public void resetOldNickname(){
        this.oldUserNickname = null;
    }
    ///////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    public void saveNewPassword(String newPassword){
        // develop logic
        this.oldUserPassword = this.currUserPassword;
        this.currUserPassword = newPassword;
    }
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    public void revertToOldPassword(){
        this.currUserPassword = this.oldUserPassword;
    }
    ///////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    public void searchMode(){
        // SEARCH
        this.currSearchMode = null;
        this.infoSearchMode = welcomeMsg + "search ";
        this.screen.setText(LEGEND + this.infoSearchMode +
                "\n\n->   groups\n\n" +
                "->   people\n\n" +
                "->   pages\n\n\n\n\n"
        );
        handler.getStateMachine().setState(StateSearch.getStateSearchInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////
    public void searchFilter(){
        // SEARCH_FILTER
        this.currSearchFilter = null;
        this.infoSearchType = this.infoSearchMode + this.currSearchMode;
        this.screen.setText(LEGEND + this.infoSearchType +
                " by\n\n" +
                "->   province\n\n" +
                "->   name\n\n" +
                "->   nickname\n\n\n\n\n"
        );
        handler.getStateMachine().setState(StateSearchFilter.getStateSearchFilterInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    public void searchInput(){
        // SEARCH_INPUT
        this.screen.setText(LEGEND
                + this.infoSearchType
                + " by " + this.currSearchFilter
                + "\n\nType a "
                + this.currSearchFilter
                + " to search all the "
                + this.currSearchMode
                + " that match your filter: ");
        handler.getStateMachine().setState(StateSearchInput.getStateSearchInput());
    }
    ////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public void applySearch(String searchTarget){
        // to search into DB
        this.searchTarget = searchTarget;
        this.screen.appendText(this.searchTarget + "\n\n\t");

        for(Pair<String, String> otherGroupInfo : this.listOtherGroups)
            this.screen.appendText(otherGroupInfo.getValue() + " (@"+otherGroupInfo.getKey()+EON);

        this.screen.appendText("\n\n\nFollow these steps to send ");
        /* based on currSearchMode value it will be possible to send link invitation
        * to people for own groups or send link request to join other groups */
        if(this.currSearchMode.equals(GROUPS)){
            // GROUP_REQUEST
            this.screen.appendText("link request asking to join groups of your interest\n\n" +
                    "\u2022 Type one group nickname at a time.\n\n" +
                    "\u2022 Type 'link request' to make known chosen groups about you.\n\n\n" +
                    "Groups:  ");
            handler.getStateMachine().setState(StateGroupRequest.getStateGroupRequestInstance());
        }
        else{  // equals(PEOPLE)
            // SEARCH_END
            this.screen.appendText("link invitation asking "+ PEOPLE + " to join your groups\n\n" +
                    "\u2022 Type one person's nickname.\n\n" +
                    "\u2022 Type one or more groups of yours.\n\n" +
                    "\u2022 Type 'link invitation' to make known chosen person about your groups.\n\n\n" +
                    "Person:  ");
            handler.getStateMachine().setState(StateSearchEnd.getStateSearchEndInstance());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkInvitationGroups(String newGroupMember){
        // MEMBER_INVITATION
        /* check if parameter is a valid input
        * check which groups of yours is unknown for them */
        this.newGroupMember = newGroupMember;
        this.screen.appendText(this.newGroupMember + "\n\n" +
                this.newGroupMember +
                " doesn't know: " +
                "[list of unknown groups]\n\n" +
                "Groups:  ");
        handler.getStateMachine().setState(StateMemberInvitation.getStateMemberInvitationInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public void insertGroupTarget(String groupTarget){
        if(this.targetGroups == null)
            this.targetGroups = new ArrayList<>();
        this.targetGroups.add(groupTarget);
        this.screen.appendText(groupTarget + ",  ");
    }
    ////////////////////////////////////////////////////

    ////////////////////////////////////////
    public void inviteMemberIntoGroups(){
        /* add member into target list */
        this.targetGroups = null;
        this.searchInput();
        this.applySearch(this.searchTarget);
    }
    ////////////////////////////////////////

    /////////////////////////////////////
    public void sendReqToTargetGroups(){
        /* move the logic into the appropriate Boundary
        * called by KeyEventHandler (graphic controller)
        * adding targetGroups list as param */
        this.targetGroups = null;
    }
    /////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void setUpGroupInfo(){
        // GROUP_CREATION_NAME
        this.stateMachine = new QuestionStateMachine();
        this.stateMachine.setQuestion(QuestionGroupName.getQuestionGroupNameInstance());
        this.screen.setText(LEGEND + welcomeMsg + "create group\n");
        handler.getStateMachine().setState(StateGroupCreationName.getStateGroupCreationNameInstance());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveGroupName(String groupName){
        // GROUP_CREATION_NICKNAME
        this.groupName = groupName;
        handler.getStateMachine().setState(StateGroupCreationNickname.getStateGroupCreationNicknameInstance());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void saveGroupNickname(String groupNickname){
        // GROUP_CREATION_END
        this.groupNickname = groupNickname;
        handler.getStateMachine().setState(StateGroupCreationEnd.getStateGroupCreationEndInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void listGroups(){
        // GROUPS_VIEW
        this.screen.setText(LEGEND + welcomeMsg + "view your groups\n\n\n" +
                "Others:  \n\t");

        for(Pair<String, String> otherGroupInfo : this.listOtherGroups)
            this.screen.appendText(otherGroupInfo.getValue() + " (@"+otherGroupInfo.getKey()+EON);

        this.screen.appendText("\n\nOwns:  \n\t");

        for(Pair<String, String> ownGroupInfo : this.listOwnGroups)
            this.screen.appendText(ownGroupInfo.getValue() + " (@"+ownGroupInfo.getKey()+EON);

        this.screen.appendText("\n\n\n\u2022 Type a group nickname to see its members, meetings and link requests.\n\n" +
                "\u2022 Type 'deletion' to add groups nickname into the blacklist; " +
                "once you have done type 'delete' to finalise your choice.\n\n\n");

        handler.getStateMachine().setState(StateGroupsView.getStateGroupsViewInstance());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void groupsDeletionMode(String mode){
        // GROUPS_DELETION
        if(this.blacklist == null)
            this.initBlacklist();// due to sonar warning
        this.showBlacklist(mode);
        handler.getStateMachine().setState(StateGroupsDeletion.getStateGroupsDeletionInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showBlacklist(String mode){
        this.screen.appendText("Blacklist [" + mode + "]" + ":  ");
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    public void addTargetIntoList(String target, List<String> list){
        /* check if user input is a right one */
        list.add(target);
        this.screen.appendText(target + ",  ");
    }
    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public void meetingsPreviousBlacklist(){
        if( this.blacklist == null || this.blacklist.isEmpty() )
            this.listMeetings();
        else{
            this.blacklist.remove(this.blacklist.size()-1);
            this.listMeetings();
            this.meetingsDeletionMode();
            for(String meeting : this.blacklist)
                this.screen.appendText(meeting + ",  ");
        }
    }
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public void membersPreviousBlacklist(){
        if( this.blacklist == null || this.blacklist.isEmpty() )
            this.listMembers();
        else{
            this.blacklist.remove(this.blacklist.size()-1);
            this.listMembers();
            this.membersRemovalMode();
            for(String member : this.blacklist)
                this.screen.appendText(member + ",  ");
        }
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public void groupsPreviousBlacklist(String mode){
        if( this.blacklist == null || this.blacklist.isEmpty() )
            this.listGroups();
        else{
            this.blacklist.remove(this.blacklist.size()-1);
            this.listGroups();
            this.groupsDeletionMode(mode);
            for(String group : this.blacklist)
                this.screen.appendText(group + ",  ");
        }
    }
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void groupOptions(String groupNickname){
        // GROUP_OPTIONS
        this.groupNickname = groupNickname;
        this.screen.setText(LEGEND + welcomeMsg +
                " choose one from the following options for the group:   " + this.groupNickname +
                "\n\n\n->   meetings\n\n" +
                "->   members\n\n" +
                "->   link requests\n\n\n");
        handler.getStateMachine().setState(StateGroupOptions.getStateGroupOptionsInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void listMembers(){
        // MEMBERS_VIEW
        this.screen.setText(LEGEND + welcomeMsg + "view members for the group:   " + this.groupNickname +
                "\n\n\nOwner:   " + this.groupOwnerNick + "\n\nMembers:   \n\t");

        for (Pair<String, String> memberInfo : this.listGroupMembers)
            this.screen.appendText(memberInfo.getValue() + " (@"+memberInfo.getKey()+EON);

        this.screen.appendText("\n\n\n\u2022 Type owner's or a member's nickname to see their details.\n\n" +
                "\u2022 Type 'removal' to add members into the blacklist, " +
                " once you have done type 'remove' to finalise your choice.\n\n\n");

        handler.getStateMachine().setState(StateMembersView.getStateMembersViewInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public void membersRemovalMode(){
        // MEMBERS_REMOVAL
        if(this.blacklist == null)
            this.initBlacklist(); // due to sonar warning
        this.showBlacklist(REMOVAL);
        handler.getStateMachine().setState(StateMembersRemoval.getStateMembersRemovalInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void showMemberDetails(String targetMember){
        // MEMBER_DETAILS
        /* remember to add group of targetMember */
        this.screen.setText(LEGEND + welcomeMsg + "view details for the member:   " + targetMember +
                ", belonging to the group:   " + this.groupNickname + EOL);

        for(String memberDetail : this.listDetails)
            this.screen.appendText(memberDetail + "\n\n");

        handler.getStateMachine().setState(StateMemberDetails.getStateMemberDetailsInstance());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showLinkRequests(String groupNickname){
        if(this.groupMembers == null)
            this.initGroupMembers(); // due to sonar warning

        this.screen.setText(LEGEND + welcomeMsg + " view link requests for the group:   " + groupNickname);
        this.screen.appendText("\n\n\n results available here...\n\n\n");

        this.screen.appendText("\u2022 Type users nickname to mark them as possible group members. \n\n" +
                "\u2022 Type 'accept' to add selected users into your group. \n\n\n");

        this.screen.appendText("Users' nickname:   ");

        handler.getStateMachine().setState(StateGroupLinkRequests.getStateGroupLinkRequestsInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    public void linkRequestsPreviousList(){
        if(this.groupMembers == null || this.groupMembers.isEmpty())
            this.showLinkRequests(this.groupNickname);
        else{
            this.groupMembers.remove(this.groupMembers.size()-1);
            this.showLinkRequests(this.groupNickname);
            for(String target : this.groupMembers)
                this.screen.appendText(target + ",  ");
        }
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void listMeetings(){
        // MEETINGS_VIEW
        this.screen.setText(LEGEND + welcomeMsg + "view meetings for the group:   " + this.groupNickname +
                "\n\n\nOthers:  \n\t");

        for(Pair<String, String> otherMeetingInfo : this.listOtherMeetings)
            this.screen.appendText(otherMeetingInfo.getValue() + " (#"+otherMeetingInfo.getKey()+EON);

        this.screen.appendText("\n\nOwns:  \n\t");

        for(Pair<String, String> ownMeetingInfo : this.listOwnMeetings)
            this.screen.appendText(ownMeetingInfo.getValue() + " (#"+ownMeetingInfo.getKey()+EON);

        this.screen.appendText("\n\n\n\u2022 Type a meeting id to see its details.\n\n" +
                "\u2022 Type 'deletion' to add your meetings into the blacklist, " +
                "once you have done type 'delete' to finalise your choice.\n\n" +
                "\u2022 Type 'propose meeting' to schedule a meeting.\n\n\n");

        handler.getStateMachine().setState(StateMeetingsView.getStateMeetingsViewInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void startMeetingProposal(){
        // MEETING_PROPOSAL
        this.stateMachine = new QuestionStateMachine();
        this.stateMachine.setQuestion(QuestionTypeOfMeeting.getQuestionTypeOfMeetingInstance());
        if(this.meetingFilters == null)
            this.meetingFilters = new ArrayList<>();
        this.screen.setText(LEGEND + welcomeMsg + "propose a meeting to the group " +
                this.groupNickname + " answering following questions\n\n\n");
        handler.getStateMachine().setState(StateMeetingProposal.getStateMeetingProposalInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////
    public void saveUserAnswer(String filter){
        /* after user types radius of action it will start research */
        this.meetingFilters.add(filter);
    }
    ///////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    // // to put together with displayGroupQuestion
    public void restorePrevQuestionsScreen(){
        this.meetingFilters.remove(this.meetingFilters.size() - 1);
        this.startMeetingProposal();
        for(String answer : this.meetingFilters){
            this.stateMachine.displayQuestion();
            this.screen.appendText(answer);
            this.stateMachine.nextQuestion();
        }
        this.stateMachine.displayQuestion();
        this.stateMachine.nextQuestion();
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////
    public void shareMeeting(){
        /* develop the logic */
        this.meetingFilters = null;
    }
    ////////////////////////////////

    ////////////////////////////////////////
    public void displayQuestion(){
        this.stateMachine.displayQuestion();
        this.stateMachine.nextQuestion();
    }
    ////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showMeetingDetails(String meetingId){
        // MEETING_DETAILS
        this.meetingId = meetingId;
        this.screen.setText(LEGEND + welcomeMsg + "view details for the meeting:   " + meetingId +
                ", planned for the group:   " + this.groupNickname + EOL);

        for (String meetingDetail : this.listDetails)
            this.screen.appendText(meetingDetail + "\n\n");

        /* insert here a way to delete own meetings and abandon the others */
        this.screen.appendText("\n\n\n\u2022 Type 'yes' to take part at this meeting.\n\n" +
                "\u2022 Type 'no' to don't take part at this meeting.\n\n" +
                "\u2022 Type 'participants' to view who will take part at this meeting.\n\n\n");

        handler.getStateMachine().setState(StateMeetingDetails.getStateMeetingDetailsInstance());
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showMeetingParticipants(){
        this.screen.setText(LEGEND + welcomeMsg + "view participants for the meeting:   " + this.meetingId +
                ", planned for the group:   " + this.groupNickname + EOL);

        for (String joinerNick : this.listMeetingJoiners)
            this.screen.appendText("@" + joinerNick + "\n\n");

        handler.getStateMachine().setState(StateMeetingJoinersView.getStateMeetingJoinersViewInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void meetingsDeletionMode(){
        // MEETINGS_DELETION
        if(this.blacklist == null)
            this.initBlacklist();  // due to sonar warning
        this.showBlacklist(DELETION);
        handler.getStateMachine().setState(StateMeetingsDeletion.getStateMeetingsDeletionInstance());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////
    public void setCurrSearchMode(String mode){ this.currSearchMode = mode; }
    /////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public void setCurrSearchFilter(String type){ this.currSearchFilter = type; }
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public String getCurrSearchFilter() {
        return this.currSearchFilter;
    }
    /////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public String getGroupName(){ return this.groupName; }
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    public String getGroupNickname(){ return this.groupNickname; }
    //////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    public String getMeetingId() {
        return this.meetingId;
    }
    ///////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    public String getSearchTarget() { return searchTarget; }
    ////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    public QuestionStateMachine getStateMachine(){
        return this.stateMachine;
    }
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    public List<String> getMeetingFilters() { return this.meetingFilters; }
    ///////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public List<String> getTargetGroups(){
        return this.targetGroups;
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public String getNewGroupMember() {
        return this.newGroupMember;
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public List<String> getGroupMembers() {
        return this.groupMembers;
    }
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public void initTargetGroups(){
        this.targetGroups = new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public void initGroupMembers(){
        this.groupMembers = new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public List<String> getBlacklist() {
        return this.blacklist;
    }
    /////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public void initBlacklist() {
        this.blacklist = new ArrayList<>();
    }
    ///////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public String getCurrUserPassword() {
        return this.currUserPassword;
    }
    /////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setCurrUserPassword(String currUserPassword) {
        this.currUserPassword = currUserPassword;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setCurrUserFullName(String userFullName) {
        currUserFullName = userFullName;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setCurrUserNickname(String currUserNickname) {
        this.currUserNickname = currUserNickname;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void setGroupOwnerNick(String groupOwnerNick) {
        this.groupOwnerNick = groupOwnerNick;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListOwnGroups(List<Pair<String, String>> listOwnGroups){
        this.listOwnGroups = listOwnGroups;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListOtherGroups(List<Pair<String, String>> listOtherGroups) {
        this.listOtherGroups = listOtherGroups;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListOwnMeetings(List<Pair<String, String>> listOwnMeetings) {
        this.listOwnMeetings = listOwnMeetings;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListOtherMeetings(List<Pair<String, String>> listOtherMeetings) {
        this.listOtherMeetings = listOtherMeetings;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListGroupMembers(List<Pair<String, String>> listGroupMembers){
        this.listGroupMembers = listGroupMembers;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListMeetingJoiners(List<String> listMeetingJoiners) {
        this.listMeetingJoiners = listMeetingJoiners;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setListDetails(List<String> listMemberDetails){
        this.listDetails = listMemberDetails;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////
    public static Home getHomeInstance(){
        if(homeInstance == null)
            homeInstance = new Home();
        return homeInstance;
    }
    //////////////////////////////////////

}
