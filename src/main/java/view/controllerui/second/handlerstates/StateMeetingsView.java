package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
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
                && !home.getPrompt().getText().equals(PREV))
            home.showMeetingDetails(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    public static StateMeetingsView getStateMeetingsViewInstance(){
        if(stateMeetingsViewInstance == null)
            stateMeetingsViewInstance = new StateMeetingsView();
        return stateMeetingsViewInstance;
    }
    ////////////////////////////////////////////////////////////////

}
