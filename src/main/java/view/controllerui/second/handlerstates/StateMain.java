package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.graphicalui.second.DefaultCommands.*;
import static view.controllerui.second.Message.*;


public class StateMain implements AbstractState{

    ///////////////////////////////////////////
    private static StateMain stateMainInstance;
    ///////////////////////////////////////////


    /////////////////////
    private StateMain(){}
    /////////////////////


    //////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(SEARCH))
            home.searchMode();

        else if(home.getPrompt().getText().equals(CREATE_GROUP)){
            home.setUpGroupInfo();
            home.displayQuestion();
        }
        else if(home.getPrompt().getText().equals(VIEW_GROUPS))
            home.listGroups();

        else if(home.getPrompt().getText().equals(MANAGE_PROFILE))
            home.manageProfile();

        else // also, PREV and BACK cmd
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////
    public static StateMain getStateMainInstance(){
        if(stateMainInstance == null)
            stateMainInstance = new StateMain();
        return stateMainInstance;
    }
    ////////////////////////////////////////////////

}
