package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

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

        }else if(home.getPrompt().getText().equals(PARTICIPANTS))
            home.showMeetingParticipants();

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateMeetingDetails getStateMeetingDetailsInstance(){
        if(stateMeetingDetailsInstance == null)
            stateMeetingDetailsInstance = new StateMeetingDetails();
        return stateMeetingDetailsInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
