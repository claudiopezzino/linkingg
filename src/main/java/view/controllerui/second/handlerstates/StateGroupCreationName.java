package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.BACK;
import static view.graphicalui.second.DefaultCommands.PREV;

public class StateGroupCreationName implements AbstractState{

    /////////////////////////////////////////////////////////////////////
    private static StateGroupCreationName stateGroupCreationNameInstance;
    /////////////////////////////////////////////////////////////////////

    //////////////////////////////////
    private StateGroupCreationName(){}
    //////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if (!home.getPrompt().getText().isEmpty() && !home.getPrompt().getText().equals(PREV)) {
            home.saveGroupName(home.getPrompt().getText());
            home.getScreen().appendText(home.getGroupName());
            home.displayQuestion();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    public static StateGroupCreationName getStateGroupCreationNameInstance() {
        if(stateGroupCreationNameInstance == null)
            stateGroupCreationNameInstance = new StateGroupCreationName();
        return stateGroupCreationNameInstance;
    }
    ///////////////////////////////////////////////////////////////////////////

}
