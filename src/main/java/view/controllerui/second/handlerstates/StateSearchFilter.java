package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateSearchFilter implements AbstractState{

    ///////////////////////////////////////////////////////////
    private static StateSearchFilter stateSearchFilterInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateSearchFilter(){}
    /////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.searchMode();

        else if(home.getPrompt().getText().equals(PROVINCE)
                || home.getPrompt().getText().equals(NAME)
                || home.getPrompt().getText().equals(NICKNAME)){
            home.setCurrSearchFilter(Home.getHomeInstance().getPrompt().getText());
            home.searchInput();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    ///////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    public static StateSearchFilter getStateSearchFilterInstance(){
        if(stateSearchFilterInstance == null)
            stateSearchFilterInstance = new StateSearchFilter();
        return stateSearchFilterInstance;
    }
    ////////////////////////////////////////////////////////////////

}
