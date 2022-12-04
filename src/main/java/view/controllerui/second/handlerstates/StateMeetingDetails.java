package view.controllerui.second.handlerstates;

import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMeetingDetails implements AbstractState{

    ///////////////////////////////////////////////////////////////
    private static StateMeetingDetails stateMeetingDetailsInstance;
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////
    private StateMeetingDetails(){}
    ///////////////////////////////


    /////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.listMeetings();

        else if(home.getPrompt().getText().equals(YES)){
            acceptMeetingMsg();
            home.listMeetings();

        }else if(home.getPrompt().getText().equals(NO)){
            refuseMeetingMsg();
            home.listMeetings();

        }else if(home.getPrompt().getText().equals(PARTICIPANTS)) {
            this.initListMeetingJoiners(home);
            home.showMeetingParticipants();
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initListMeetingJoiners(Home home){
        String groupNick = home.getGroupNickname();
        String meetingID = home.getMeetingId();

        MeetingBean meetingBean = Shell.getShellHandler().getMapGroupBean().get(groupNick).getMapMeetings().get(meetingID);

        List<String> listMeetingJoiners = new ArrayList<>();

        String joinerNick;
        Map<String, UserBean> mapMeetingJoiners = meetingBean.getJoiners();
        for (Map.Entry<String, UserBean> map : mapMeetingJoiners.entrySet()){
            joinerNick = map.getValue().getNickname();
            listMeetingJoiners.add(joinerNick);
        }
        home.setListMeetingJoiners(listMeetingJoiners);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateMeetingDetails getStateMeetingDetailsInstance(){
        if(stateMeetingDetailsInstance == null)
            stateMeetingDetailsInstance = new StateMeetingDetails();
        return stateMeetingDetailsInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
