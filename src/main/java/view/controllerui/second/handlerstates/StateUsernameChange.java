package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.usernameChangedMsg;
import static view.graphicalui.second.DefaultCommands.*;

public class StateUsernameChange implements AbstractState{

    ///////////////////////////////////////////////////////////////
    private static StateUsernameChange stateUsernameChangeInstance;
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////
    private StateUsernameChange(){}
    ///////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.manageProfile();

        else if(home.getPrompt().getText().equals(PREV))
            home.usernameChangeMode();

        else if(home.getPrompt().getText().equals(CONFIRM) && home.getStateMachine().getQuestion() == null){
            usernameChangedMsg();
            home.resetOldNickname();
            home.restoreScreen();
        }

        else if(!home.getPrompt().getText().isEmpty() && home.getStateMachine().getQuestion() != null){
            home.getScreen().appendText(home.getPrompt().getText());
            home.saveNewUsername(home.getPrompt().getText());
            home.displayQuestion();
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StateUsernameChange getStateUsernameChangeInstance(){
        if(stateUsernameChangeInstance == null)
            stateUsernameChangeInstance = new StateUsernameChange();
        return stateUsernameChangeInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
