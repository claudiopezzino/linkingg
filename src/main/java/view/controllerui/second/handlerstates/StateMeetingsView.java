package view.controllerui.second.handlerstates;

import view.bean.observers.MeetingBean;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoErrorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMeetingsView implements AbstractState{

    ///////////////////////////////////////////////////////////
    private static StateMeetingsView stateMeetingsViewInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateMeetingsView(){}
    /////////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.groupOptions(home.getGroupNickname());

        else if(home.getPrompt().getText().equals(DELETION))
            home.meetingsDeletionMode();

        else if(home.getPrompt().getText().equals(PROPOSE_MEETING)) {
            home.startMeetingProposal();
            home.displayQuestion();
        }
        else if(!home.getPrompt().getText().isEmpty()
                && !home.getPrompt().getText().equals(PREV)) {

            if(Shell.getShellHandler().getMapGroupBean().get(home.getGroupNickname())
                    .getMapMeetings().get(home.getPrompt().getText()) != null) {

                this.initListMeetingDetails(home, home.getPrompt().getText());
                home.showMeetingDetails(home.getPrompt().getText());
            }
            else
                infoErrorMsg("No meeting available with given ID into group:   "
                        + home.getGroupNickname());
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initListMeetingDetails(Home home, String meetingID){
        List<String> listMeetingDetails = new ArrayList<>();

        String groupNick = home.getGroupNickname();

        MeetingBean meetingBean = Shell.getShellHandler().getMapGroupBean().get(groupNick).getMapMeetings().get(meetingID);

        String currUserNick = Shell.getShellHandler().getCurrUserBean().getNickname();

        String name = meetingBean.getName();
        String rating = meetingBean.getRating();
        String date = meetingBean.getDate();
        String time = meetingBean.getTime();
        String schedulerNick = meetingBean.getScheduler().getNickname();

        listMeetingDetails.add("ID:   " + meetingID);
        listMeetingDetails.add("Name:   " + name);
        listMeetingDetails.add("Rating:   " +rating);
        listMeetingDetails.add("Date:   " +date);
        listMeetingDetails.add("Time:   " +time);

        if(currUserNick.equals(schedulerNick))
            listMeetingDetails.add("Scheduler:   YOU");
        else
            listMeetingDetails.add("Scheduler:   @" + schedulerNick);

        home.setListDetails(listMeetingDetails);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    public static StateMeetingsView getStateMeetingsViewInstance(){
        if(stateMeetingsViewInstance == null)
            stateMeetingsViewInstance = new StateMeetingsView();
        return stateMeetingsViewInstance;
    }
    ////////////////////////////////////////////////////////////////

}
