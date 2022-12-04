package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoErrorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupsView implements AbstractState{

    ///////////////////////////////////////////////////////
    private static StateGroupsView stateGroupsViewInstance;
    ///////////////////////////////////////////////////////


    ///////////////////////////
    private StateGroupsView(){}
    ///////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(DELETION))
            home.groupsDeletionMode(DELETION);

        else if(!home.getPrompt().getText().isEmpty()
                && !home.getPrompt().getText().equals(PREV)) {
            if(Shell.getShellHandler().getMapGroupBean().get(home.getPrompt().getText()) != null)
                home.groupOptions(home.getPrompt().getText());
            else
                infoErrorMsg("No group available with given nickname.");
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////
    public static StateGroupsView getStateGroupsViewInstance(){
        if(stateGroupsViewInstance == null)
            stateGroupsViewInstance = new StateGroupsView();
        return stateGroupsViewInstance;
    }
    ////////////////////////////////////////////////////////////

}
