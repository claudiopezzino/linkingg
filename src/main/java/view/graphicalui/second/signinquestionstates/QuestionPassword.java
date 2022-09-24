package view.graphicalui.second.signinquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.QuestionStateMachine;
import view.graphicalui.second.Signin;


public class QuestionPassword implements AbstractQuestion {

    //////////////////////////////////////////////////////////////////
    private static final String PASSWORD = "\n\n \u2022 Password:   ";
    //////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    private static QuestionPassword questionPasswordInstance;
    /////////////////////////////////////////////////////////


    /////////////////////////////
    private QuestionPassword(){}
    /////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Signin.getSigninInstance().getScreen().appendText(PASSWORD);
    }
    //////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionEnd.getQuestionEndInstance());
    }
    ///////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static QuestionPassword getQuestionPasswordInstance() {
        if(questionPasswordInstance == null)
            questionPasswordInstance = new QuestionPassword();
        return questionPasswordInstance;
    }
    ///////////////////////////////////////////////////////////////

}
