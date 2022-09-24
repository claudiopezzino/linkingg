package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;
import view.graphicalui.second.passwordquestionstates.QuestionCurrentPassword;
import view.graphicalui.second.passwordquestionstates.QuestionEnd;
import view.graphicalui.second.passwordquestionstates.QuestionNewPassword;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class StatePasswordChange implements AbstractState{

    ///////////////////////////////////////////////////////////////
    private static StatePasswordChange statePasswordChangeInstance;
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////
    private StatePasswordChange(){}
    ///////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.manageProfile();

        else if(home.getPrompt().getText().equals(PREV) &&
                home.getStateMachine().getQuestion() instanceof QuestionNewPassword)

            home.passwordChangeMode();

        else if(home.getPrompt().getText().equals(PREV) &&
                home.getStateMachine().getQuestion() instanceof QuestionEnd){

            home.passwordChangeMode();
            home.revertToOldPassword();
            home.getScreen().appendText("*".repeat(home.getCurrUserPassword().length()));
            home.getStateMachine().nextQuestion();
            home.getStateMachine().displayQuestion();

        }

        else if(home.getPrompt().getText().equals(CONFIRM) &&
                home.getStateMachine().getQuestion() instanceof QuestionEnd){

            passwordChangedMsg();
            home.restoreScreen();
        }

        else if(!home.getPrompt().getText().isEmpty() &&
                home.getStateMachine().getQuestion() instanceof QuestionCurrentPassword){

            if(home.getPrompt().getText().equals(home.getCurrUserPassword())){

                home.getScreen().appendText("*".repeat(home.getPrompt().getText().length()));
                home.getStateMachine().nextQuestion();
                home.getStateMachine().displayQuestion();
            }
            else
                wrongPasswordMsg();


        }
        else if(!home.getPrompt().getText().isEmpty() &&
                home.getStateMachine().getQuestion() instanceof QuestionNewPassword){

            home.getScreen().appendText("*".repeat(home.getPrompt().getText().length()));
            home.saveNewPassword(home.getPrompt().getText());
            home.getStateMachine().nextQuestion();
            home.getStateMachine().displayQuestion();
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static StatePasswordChange getStatePasswordChangeInstance() {
        if(statePasswordChangeInstance == null)
            statePasswordChangeInstance = new StatePasswordChange();
        return statePasswordChangeInstance;
    }
    ////////////////////////////////////////////////////////////////////

}
