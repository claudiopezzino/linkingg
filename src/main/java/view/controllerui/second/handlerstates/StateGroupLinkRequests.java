package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;

public class StateGroupLinkRequests implements AbstractState{

    /////////////////////////////////////////////////////////////////////
    private static StateGroupLinkRequests stateGroupLinkRequestsInstance;
    /////////////////////////////////////////////////////////////////////

    //////////////////////////////////
    private StateGroupLinkRequests(){}
    //////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.groupOptions(home.getGroupNickname());

        else if(home.getPrompt().getText().equals(PREV)
                && home.getGroupMembers() != null)
            home.linkRequestsPreviousList();

        else if(home.getPrompt().getText().equals(ACCEPT)) {
            home.initGroupMembers();
            acceptRequestsMsg();
            home.groupOptions(home.getGroupNickname());
        }

        else if(!home.getPrompt().getText().isEmpty())
            home.addTargetIntoList(home.getPrompt().getText(), home.getGroupMembers());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    public static StateGroupLinkRequests getStateGroupLinkRequestsInstance() {
        if(stateGroupLinkRequestsInstance == null)
            stateGroupLinkRequestsInstance = new StateGroupLinkRequests();
        return stateGroupLinkRequestsInstance;
    }
    ///////////////////////////////////////////////////////////////////////////
}
