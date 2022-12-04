package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.groupsDeletionMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupsDeletion implements AbstractState{

    ///////////////////////////////////////////////////////////////
    private static StateGroupsDeletion stateGroupsDeletionInstance;
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////
    private StateGroupsDeletion(){}
    ///////////////////////////////


    ///////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV))
            home.groupsPreviousBlacklist(DELETION);

        else if(home.getPrompt().getText().equals(DELETE)) {
            // try to add an error message
            home.initBlacklist();
            groupsDeletionMsg();
            home.listGroups();
        }
        else if( !home.getPrompt().getText().isEmpty())
            home.addTargetIntoList(home.getPrompt().getText(), home.getBlacklist());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ///////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateGroupsDeletion getStateGroupsDeletionInstance(){
        if(stateGroupsDeletionInstance == null)
            stateGroupsDeletionInstance = new StateGroupsDeletion();
        return stateGroupsDeletionInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
