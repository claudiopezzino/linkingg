package view.graphicalui.second.meetingquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.Home;
import view.graphicalui.second.QuestionStateMachine;

public class QuestionAddrToStartSearch implements AbstractQuestion {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String ADDRESS_WHERE_TO_START_SEARCHING = "\n\n \u2022 Address where to start searching:   ";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private static QuestionAddrToStartSearch questionAddrToStartSearchInstance;
    ///////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////
    private QuestionAddrToStartSearch(){}
    /////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Home.getHomeInstance().getScreen().appendText(ADDRESS_WHERE_TO_START_SEARCHING);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine){
        stateMachine.setQuestion(QuestionRadiusOfAction.getQuestionRadiusOfActionInstance());
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////
    public static QuestionAddrToStartSearch getQuestionAddrToStartSearchInstance(){
        if(questionAddrToStartSearchInstance == null)
            questionAddrToStartSearchInstance = new QuestionAddrToStartSearch();
        return questionAddrToStartSearchInstance;
    }
    ////////////////////////////////////////////////////////////////////////////////

}