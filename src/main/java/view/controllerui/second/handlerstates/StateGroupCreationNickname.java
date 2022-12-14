package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.graphicalui.second.DefaultCommands.BACK;
import static view.graphicalui.second.DefaultCommands.PREV;

public class StateGroupCreationNickname implements AbstractState{

    /////////////////////////////////////////////////////////////////////////////
    private static StateGroupCreationNickname stateGroupCreationNicknameInstance;
    /////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////
    private StateGroupCreationNickname(){}
    //////////////////////////////////////

    /////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV)) {
            home.setUpGroupInfo();
            home.displayQuestion();
        }

        else if (!home.getPrompt().getText().isEmpty()) {
            home.saveGroupNickname(home.getPrompt().getText());
            home.getScreen().appendText(home.getGroupNickname());
            // alert user that image profile capability has some problem
            home.displayQuestion();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    public static StateGroupCreationNickname getStateGroupCreationNicknameInstance() {
        if(stateGroupCreationNicknameInstance == null)
            stateGroupCreationNicknameInstance = new StateGroupCreationNickname();
        return stateGroupCreationNicknameInstance;
    }
    ///////////////////////////////////////////////////////////////////////////////////

}
