package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupRequest implements AbstractState{

    ///////////////////////////////////////////////////////////
    private static StateGroupRequest stateGroupRequestInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateGroupRequest(){}
    /////////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK)) {
            home.initTargetGroups();
            home.searchFilter();
        }

        else if(home.getPrompt().getText().equals(PREV)) {
            if(home.getTargetGroups() == null || home.getTargetGroups().isEmpty())
                home.searchInput();
            else{
                home.getTargetGroups().remove(home.getTargetGroups().size() - 1);
                home.searchInput();
                home.applySearch(home.getSearchTarget());
                for(String group : home.getTargetGroups())
                    home.getScreen().appendText(group + ",  ");
            }
        }

        else if(home.getPrompt().getText().equals(LINK_REQUEST)){
            home.sendReqToTargetGroups(); // to add appropriate param
            multiGroupsReqMsg();
            home.restoreScreen();
        }
        else if(!home.getPrompt().getText().isEmpty())
            home.insertGroupTarget(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static StateGroupRequest getStateGroupRequestInstance(){
        if(stateGroupRequestInstance == null)
            stateGroupRequestInstance = new StateGroupRequest();
        return stateGroupRequestInstance;
    }
    ///////////////////////////////////////////////////////////////

}
