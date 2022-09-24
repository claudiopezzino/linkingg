package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupOptions implements AbstractState{

    ///////////////////////////////////////////////////////////
    private static StateGroupOptions stateGroupOptionsInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateGroupOptions(){}
    /////////////////////////////


    //////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.listGroups();

        else if(home.getPrompt().getText().equals(MEMBERS))
            home.listMembers();

        else if(home.getPrompt().getText().equals(MEETINGS))
            home.listMeetings();

        // Verify if group' nickname is owned by current user in the handler class
        else if(home.getPrompt().getText().equals(LINK_REQUESTS))
            home.showLinkRequests(home.getGroupNickname());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static StateGroupOptions getStateGroupOptionsInstance(){
        if(stateGroupOptionsInstance == null)
            stateGroupOptionsInstance = new StateGroupOptions();
        return stateGroupOptionsInstance;
    }
    ///////////////////////////////////////////////////////////////

}
