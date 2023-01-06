package view.controllerui.second.handlerstates;

import javafx.util.Pair;
import view.bean.observers.LinkRequestBean;
import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoErrorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupOptions implements AbstractState{

    ///////////////////////////////////////////////////////////
    private static StateGroupOptions stateGroupOptionsInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateGroupOptions(){}
    /////////////////////////////


    //////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.listGroups();

        else if(home.getPrompt().getText().equals(MEMBERS)) {
            this.initListGroupMembers(home);
            home.listMembers();
        }
        else if(home.getPrompt().getText().equals(MEETINGS)) {
            this.initListMeetings(home);
            home.listMeetings();
        }
        // Verify if group's nickname is owned by current user in the handler class
        else if(home.getPrompt().getText().equals(LINK_REQUESTS)) {
            if(isCurrUserOwner(home)) {
                this.initListLinkRequests(home);
                home.showLinkRequests(home.getGroupNickname());
            }else
                infoErrorMsg("Only the owner can view this content.");
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    private boolean isCurrUserOwner(Home home){
        String groupNick = home.getGroupNickname();
        String groupOwnerNick = Shell.getShellHandler().getMapGroupBean()
                .get(groupNick).getOwner().getNickname();
        String currUserNick = Shell.getShellHandler().getCurrUserBean().getNickname();

        return currUserNick.equals(groupOwnerNick);
    }
    /////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    private void initListLinkRequests(Home home){
        String userNick;
        String userName;
        String userSurname;

        String groupNick = home.getGroupNickname();

        Map<String, LinkRequestBean> mapRequests = Shell.getShellHandler()
                .getMapGroupBean().get(groupNick).getMapLinkRequests();

        List<Pair<String, String>> listLinkRequests = new ArrayList<>();
        for (Map.Entry<String, LinkRequestBean> entry : mapRequests.entrySet()){
            userNick = entry.getValue().getUserNick();
            userName = entry.getValue().getUserName();
            userSurname = entry.getValue().getUserSurname();

            listLinkRequests.add(new Pair<>(userNick, userName + " " + userSurname));
        }

        home.setListLinkRequests(listLinkRequests);
    }
    ////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    private void initListMeetings(Home home){
        String meetingID;
        String meetingName;
        String schedulerNick;

        String selectedGroupNick = home.getGroupNickname();
        String currUserNick = Shell.getShellHandler().getCurrUserBean().getNickname();

        Map<String, MeetingBean> mapMeetingBean = Shell.getShellHandler().getMapGroupBean()
                .get(selectedGroupNick).getMapMeetings();

        List<Pair<String, String>> listOwnMeetings = new ArrayList<>();
        List<Pair<String, String>> listOtherMeetings = new ArrayList<>();
        for (Map.Entry<String, MeetingBean> map : mapMeetingBean.entrySet()){
            meetingID = map.getValue().getId();
            meetingName = map.getValue().getName();
            schedulerNick = map.getValue().getScheduler().getNickname();

            if(schedulerNick.equals(currUserNick))
                listOwnMeetings.add(new Pair<>(meetingID, meetingName));
            else
                listOtherMeetings.add(new Pair<>(meetingID, meetingName));
        }
        home.setListOwnMeetings(listOwnMeetings);
        home.setListOtherMeetings(listOtherMeetings);
    }
    /////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initListGroupMembers(Home home){
        String groupNick = home.getGroupNickname();

        String groupOwnerNick = Shell.getShellHandler().getMapGroupBean().get(groupNick).getOwner().getNickname();
        String currUserNick = Shell.getShellHandler().getCurrUserBean().getNickname();

        if(currUserNick.equals(groupOwnerNick))
            home.setGroupOwnerNick("YOU");
        else
            home.setGroupOwnerNick("@" + groupOwnerNick);

        Map<String, UserBean> mapUserBean = Shell.getShellHandler().getMapGroupBean().get(groupNick).getMapMembers();

        List<Pair<String, String>> listGroupMembers = new ArrayList<>();

        String memberNick;
        String memberFullName;
        for(Map.Entry<String, UserBean> map : mapUserBean.entrySet()){
            memberNick = map.getValue().getNickname();
            memberFullName = map.getValue().getName() + " " + map.getValue().getSurname();
            listGroupMembers.add(new Pair<>(memberNick, memberFullName));
        }
        home.setListGroupMembers(listGroupMembers);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static StateGroupOptions getStateGroupOptionsInstance(){
        if(stateGroupOptionsInstance == null)
            stateGroupOptionsInstance = new StateGroupOptions();
        return stateGroupOptionsInstance;
    }
    ///////////////////////////////////////////////////////////////

}
