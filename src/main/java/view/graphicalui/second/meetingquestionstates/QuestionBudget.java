package view.graphicalui.second.meetingquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.Home;
import view.graphicalui.second.QuestionStateMachine;


public class QuestionBudget implements AbstractQuestion {

    /////////////////////////////////////////////////////////////////////////////////
    private static final String BUDGET = "\n\n \u2022 Budget [only one integer]:   ";
    /////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    private static QuestionBudget questionBudgetInstance;
    /////////////////////////////////////////////////////


    //////////////////////////
    private QuestionBudget(){}
    //////////////////////////


    ////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Home.getHomeInstance().getScreen().appendText(BUDGET);
    }
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionTime.getQuestionTimeInstance());
    }
    //////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////
    public static QuestionBudget getQuestionBudgetInstance(){
        if(questionBudgetInstance == null)
            questionBudgetInstance = new QuestionBudget();
        return questionBudgetInstance;
    }
    //////////////////////////////////////////////////////////

}
