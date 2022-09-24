package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.meetingProposalMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMeetingProposal implements AbstractState{

    /////////////////////////////////////////////////////////////////
    private static StateMeetingProposal stateMeetingProposalInstance;
    /////////////////////////////////////////////////////////////////


    ////////////////////////////////
    private StateMeetingProposal(){}
    ////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.listMeetings();

        else if(home.getPrompt().getText().equals(PREV) && !home.getMeetingFilters().isEmpty())
            home.restorePrevQuestionsScreen();


        else if( home.getPrompt().getText().equals(CONFIRM) && home.getStateMachine().getQuestion() == null ){
            home.shareMeeting();
            home.listMeetings();
            meetingProposalMsg();
        }
        else if( !home.getPrompt().getText().isEmpty() && home.getStateMachine().getQuestion() != null &&
                !home.getPrompt().getText().equals(PREV)) {
            /* insert a block try/catch to handle wrong input by user
             * wrong input will be valuated by Beans (checking syntax) */
            home.saveUserAnswer(home.getPrompt().getText());
            home.getScreen().appendText(home.getPrompt().getText());
            home.displayQuestion();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////
    public static StateMeetingProposal getStateMeetingProposalInstance(){
        if(stateMeetingProposalInstance == null)
            stateMeetingProposalInstance = new StateMeetingProposal();
        return stateMeetingProposalInstance;
    }
    //////////////////////////////////////////////////////////////////////

}
