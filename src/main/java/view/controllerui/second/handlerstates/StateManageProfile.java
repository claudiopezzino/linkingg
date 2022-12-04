package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateManageProfile implements AbstractState{

    /////////////////////////////////////////////////////////////
    private static StateManageProfile stateManageProfileInstance;
    /////////////////////////////////////////////////////////////


    //////////////////////////////
    private StateManageProfile(){}
    //////////////////////////////


    ///////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {

        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(NICKNAME))
            home.usernameChangeMode();

        else if(home.getPrompt().getText().equals(PASSWORD))
            home.passwordChangeMode();

        else if(home.getPrompt().getText().equals(LINK_INVITATIONS))
            home.showLinkInvitations();

        else if(home.getPrompt().getText().equals(IMAGE_PROFILE))
            infoMsg();

        else
            errorMsg();

        home.getPrompt().clear();

    }
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////
    public static StateManageProfile getStateManageProfileInstance() {
        if(stateManageProfileInstance == null)
            stateManageProfileInstance = new StateManageProfile();
        return stateManageProfileInstance;
    }
    ///////////////////////////////////////////////////////////////////

}
