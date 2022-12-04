package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.BACK;


public class StateMeetingJoinersView implements AbstractState{

    ///////////////////////////////////////////////////////////////////////
    private static StateMeetingJoinersView stateMeetingJoinersViewInstance;
    ///////////////////////////////////////////////////////////////////////


    ///////////////////////////////////
    private StateMeetingJoinersView(){}
    ///////////////////////////////////


    /////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.showMeetingDetails(home.getMeetingId());
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    public static StateMeetingJoinersView getStateMeetingJoinersViewInstance() {
        if(stateMeetingJoinersViewInstance == null)
            stateMeetingJoinersViewInstance = new StateMeetingJoinersView();
        return stateMeetingJoinersViewInstance;
    }
    ////////////////////////////////////////////////////////////////////////////

}
