package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.BACK;


public class StateMemberDetails implements AbstractState{

    /////////////////////////////////////////////////////////////
    private static StateMemberDetails stateMemberDetailsInstance;
    /////////////////////////////////////////////////////////////


    //////////////////////////////
    private StateMemberDetails(){}
    //////////////////////////////


    ////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.listMembers();
        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    public static StateMemberDetails getStateMemberDetailsInstance(){
        if(stateMemberDetailsInstance == null)
            stateMemberDetailsInstance = new StateMemberDetails();
        return stateMemberDetailsInstance;
    }
    //////////////////////////////////////////////////////////////////

}
