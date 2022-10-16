package view.graphicalui.second.signinquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.QuestionStateMachine;
import view.graphicalui.second.Signin;


public class QuestionNickname implements AbstractQuestion {

    //////////////////////////////////////////////////////////////////
    private static final String NICKNAME = "\n\n \u2022 Nickname:   ";
    //////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    private static QuestionNickname questionNicknameInstance;
    /////////////////////////////////////////////////////////


    ////////////////////////////
    private QuestionNickname(){}
    ////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Signin.getSigninInstance().getScreen().appendText(NICKNAME);
    }
    //////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionPassword.getQuestionPasswordInstance());
    }
    /////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static QuestionNickname getQuestionNicknameInstance() {
        if(questionNicknameInstance == null)
            questionNicknameInstance = new QuestionNickname();
        return questionNicknameInstance;
    }
    ///////////////////////////////////////////////////////////////

}