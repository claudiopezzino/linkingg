package view.graphicalui.second.signupquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.QuestionStateMachine;
import view.graphicalui.second.Signup;

public class QuestionCell implements AbstractQuestion {

    ///////////////////////////////////////////////////////////
    private static final String CELL = "\n\n \u2022 Cell:   ";
    ///////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    private static QuestionCell questionCellInstance;
    /////////////////////////////////////////////////


    ////////////////////////
    private QuestionCell(){}
    ////////////////////////


    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Signup.getSignupInstance().getScreen().appendText(CELL);
    }
    //////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionAccount.getQuestionAccountInstance());
    }
    ///////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////
    public static QuestionCell getQuestionCellInstance() {
        if(questionCellInstance == null)
            questionCellInstance = new QuestionCell();
        return questionCellInstance;
    }
    //////////////////////////////////////////////////////

}
