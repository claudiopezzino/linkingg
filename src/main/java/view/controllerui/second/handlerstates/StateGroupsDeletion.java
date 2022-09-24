package view.controllerui.second.handlerstates;

import javafx.util.Pair;
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
            this.assembleAllGroups(home);
            home.startRemoval(home.getBlacklist(), home.getFullGroupList());
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

    /////////////////////////////////////////////////////////////////////////
    private void assembleAllGroups(Home home){
        for(Pair<String, String> ownGroupInfo : home.getOwnGroupList())
            home.getFullGroupList().add(ownGroupInfo);
        for(Pair<String, String> joinedGroupInfo : home.getJoinedGroupList())
            home.getFullGroupList().add(joinedGroupInfo);
    }
    /////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateGroupsDeletion getStateGroupsDeletionInstance(){
        if(stateGroupsDeletionInstance == null)
            stateGroupsDeletionInstance = new StateGroupsDeletion();
        return stateGroupsDeletionInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
