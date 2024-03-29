package view.graphicalui.second.signupquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.QuestionStateMachine;
import view.graphicalui.second.Signup;

public class QuestionName implements AbstractQuestion {

    ///////////////////////////////////////////////////////////
    private static final String NAME = "\n\n \u2022 Name:   ";
    ///////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    private static QuestionName questionNameInstance;
    /////////////////////////////////////////////////


    ////////////////////////
    private QuestionName(){}
    ////////////////////////


    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Signup.getSignupInstance().getScreen().appendText(NAME);
    }
    //////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionSurname.getQuestionSurnameInstance());
    }
    ///////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////
    public static QuestionName getQuestionNameInstance(){
        if(questionNameInstance == null)
            questionNameInstance = new QuestionName();
        return questionNameInstance;
    }
    //////////////////////////////////////////////////////

}
