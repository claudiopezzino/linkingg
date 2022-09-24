package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMembersView implements AbstractState{

    /////////////////////////////////////////////////////////
    private static StateMembersView stateMembersViewInstance;
    /////////////////////////////////////////////////////////


    ////////////////////////////
    private StateMembersView(){}
    ////////////////////////////


    //////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.groupOptions(home.getGroupNickname());

        else if(home.getPrompt().getText().equals(REMOVAL))
            home.membersRemovalMode();

        else if(!home.getPrompt().getText().isEmpty()
                && !home.getPrompt().getText().equals(PREV))
            home.showMemberDetails(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////
    public static StateMembersView getStateMembersViewInstance(){
        if(stateMembersViewInstance == null)
            stateMembersViewInstance = new StateMembersView();
        return stateMembersViewInstance;
    }
    //////////////////////////////////////////////////////////////

}
