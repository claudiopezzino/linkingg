package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.acceptInvitationsMsg;
import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateUserLinkInvitations implements AbstractState{

    /////////////////////////////////////////////////////////////////////////
    private static StateUserLinkInvitations stateUserLinkInvitationsInstance;
    /////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////
    private StateUserLinkInvitations(){}
    ////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.manageProfile();

        else if(home.getPrompt().getText().equals(PREV))
            home.linkInvitationsPreviousList();

        else if(home.getPrompt().getText().equals(ACCEPT)){
            home.initTargetGroups();
            acceptInvitationsMsg();
            home.manageProfile();
        }

        else if(!home.getPrompt().getText().isEmpty())
            home.addTargetIntoList(home.getPrompt().getText(), home.getTargetGroups());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    public static StateUserLinkInvitations getStateUserLinkInvitationsInstance() {
        if(stateUserLinkInvitationsInstance == null)
            stateUserLinkInvitationsInstance = new StateUserLinkInvitations();
        return stateUserLinkInvitationsInstance;
    }
    ///////////////////////////////////////////////////////////////////////////////

}
