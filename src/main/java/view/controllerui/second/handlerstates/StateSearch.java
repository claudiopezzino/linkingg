package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.graphicalui.second.DefaultCommands.*;
import static view.controllerui.second.Message.*;


public class StateSearch implements AbstractState{

    ///////////////////////////////////////////////
    private static StateSearch stateSearchInstance;
    ///////////////////////////////////////////////


    ///////////////////////
    private StateSearch(){}
    ///////////////////////


    /////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {

        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(GROUPS)) {
            home.setCurrSearchMode(home.getPrompt().getText());
            home.searchFilter();
        }
        else if(home.getPrompt().getText().equals(PAGES)
                || home.getPrompt().getText().equals(PEOPLE))
            infoMsg();

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////
    public static StateSearch getStateSearchInstance(){
        if(stateSearchInstance == null)
            stateSearchInstance = new StateSearch();
        return stateSearchInstance;
    }
    ////////////////////////////////////////////////////

}
