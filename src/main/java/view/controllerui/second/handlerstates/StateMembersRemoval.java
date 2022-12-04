package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;


import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.membersRemovalMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMembersRemoval implements AbstractState{

    ///////////////////////////////////////////////////////////////
    private static StateMembersRemoval stateMembersRemovalInstance;
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////
    private StateMembersRemoval(){}
    ///////////////////////////////


    ////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV))
            home.membersPreviousBlacklist();

        else if(home.getPrompt().getText().equals(REMOVE)) {
            // try to add an error message
            home.initBlacklist();
            membersRemovalMsg();
            home.listMembers();
        }
        else if(!home.getPrompt().getText().isEmpty() &&
                !home.getPrompt().getText().equals(BACK))
            home.addTargetIntoList(home.getPrompt().getText(), home.getBlacklist());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateMembersRemoval getStateMembersRemovalInstance(){
        if(stateMembersRemovalInstance == null)
            stateMembersRemovalInstance = new StateMembersRemoval();
        return stateMembersRemovalInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
