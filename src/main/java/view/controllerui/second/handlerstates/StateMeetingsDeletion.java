package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.meetingDeletionMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMeetingsDeletion implements AbstractState{

    ///////////////////////////////////////////////////////////////////
    private static StateMeetingsDeletion stateMeetingsDeletionInstance;
    ///////////////////////////////////////////////////////////////////


    /////////////////////////////////
    private StateMeetingsDeletion(){}
    /////////////////////////////////


    //////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV))
            home.meetingsPreviousBlacklist();

        else if(home.getPrompt().getText().equals(DELETE)) {
            home.startRemoval(home.getBlacklist(), home.getGroupMeetingList());
            home.initBlacklist();
            meetingDeletionMsg();
            home.listMeetings();
        }
        else if(!home.getPrompt().getText().isEmpty())
            home.addTargetIntoList(home.getPrompt().getText(), home.getBlacklist());
        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////
    public static StateMeetingsDeletion getStateMeetingsDeletionInstance(){
        if(stateMeetingsDeletionInstance == null)
            stateMeetingsDeletionInstance = new StateMeetingsDeletion();
        return stateMeetingsDeletionInstance;
    }
    ////////////////////////////////////////////////////////////////////////

}
