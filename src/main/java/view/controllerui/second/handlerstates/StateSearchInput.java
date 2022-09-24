package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.BACK;
import static view.graphicalui.second.DefaultCommands.PREV;


public class StateSearchInput implements AbstractState{

    /////////////////////////////////////////////////////////
    private static StateSearchInput stateSearchInputInstance;
    /////////////////////////////////////////////////////////


    ////////////////////////////
    private StateSearchInput(){}
    ////////////////////////////


    /////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.searchFilter();

        else if(!home.getPrompt().getText().isEmpty() && !home.getPrompt().getText().equals(PREV))
            home.applySearch(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////
    public static StateSearchInput getStateSearchInput() {
        if(stateSearchInputInstance == null)
            stateSearchInputInstance = new StateSearchInput();
        return stateSearchInputInstance;
    }
    //////////////////////////////////////////////////////////

}
