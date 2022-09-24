package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateSearchEnd implements AbstractState{

    /////////////////////////////////////////////////////
    private static StateSearchEnd stateSearchEndInstance;
    /////////////////////////////////////////////////////


    //////////////////////////
    private StateSearchEnd(){}
    //////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.searchFilter();

        else if(home.getPrompt().getText().equals(PREV))
            home.searchInput();

        else if (!home.getPrompt().getText().isEmpty())
            home.checkInvitationGroups(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////
    public static StateSearchEnd getStateSearchEndInstance(){
        if(stateSearchEndInstance == null)
            stateSearchEndInstance = new StateSearchEnd();
        return stateSearchEndInstance;
    }
    //////////////////////////////////////////////////////////

}
