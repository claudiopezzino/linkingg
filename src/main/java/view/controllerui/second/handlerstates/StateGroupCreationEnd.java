package view.controllerui.second.handlerstates;

import javafx.util.Pair;
import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.groupCreationMsg;
import static view.graphicalui.second.DefaultCommands.*;

public class StateGroupCreationEnd implements AbstractState{

    ///////////////////////////////////////////////////////////////////
    private static StateGroupCreationEnd stateGroupCreationEndInstance;
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////
    private StateGroupCreationEnd(){}
    //////////////////////////////////

    /////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV)){
            home.setUpGroupInfo();
            home.displayQuestion();
            home.getScreen().appendText(home.getGroupName());
            home.saveGroupName(home.getGroupName());
            home.displayQuestion();
        }

        else if (home.getPrompt().getText().equals(CONFIRM)) {
            home.getOwnGroupList().add(new Pair<>(home.getGroupNickname(), home.getGroupName()));
            groupCreationMsg();
            home.restoreScreen();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public static StateGroupCreationEnd getStateGroupCreationEndInstance() {
        if(stateGroupCreationEndInstance == null)
            stateGroupCreationEndInstance = new StateGroupCreationEnd();
        return stateGroupCreationEndInstance;
    }
    //////////////////////////////////////////////////////////////////////////
}
