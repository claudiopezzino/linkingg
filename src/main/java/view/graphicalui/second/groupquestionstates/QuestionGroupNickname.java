package view.graphicalui.second.groupquestionstates;

import view.graphicalui.second.AbstractQuestion;
import view.graphicalui.second.Home;
import view.graphicalui.second.QuestionStateMachine;

public class QuestionGroupNickname implements AbstractQuestion {

    ////////////////////////////////////////////////////////////////////////
    private static final String GROUP_NICKNAME = "\n\n \u2022 Nickname:   ";
    ////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    private static QuestionGroupNickname questionGroupNicknameInstance;
    ///////////////////////////////////////////////////////////////////


    /////////////////////////////////
    private QuestionGroupNickname(){}
    /////////////////////////////////


    //////////////////////////////////////////////////////////////////
    @Override
    public void display() {
        Home.getHomeInstance().getScreen().appendText(GROUP_NICKNAME);
    }
    //////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    @Override
    public void next(QuestionStateMachine stateMachine) {
        stateMachine.setQuestion(QuestionEnd.getQuestionEndInstance());
    }
    ////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////
    public static QuestionGroupNickname getQuestionGroupNicknameInstance() {
        if(questionGroupNicknameInstance == null)
            questionGroupNicknameInstance = new QuestionGroupNickname();
        return questionGroupNicknameInstance;
    }
    ////////////////////////////////////////////////////////////////////////
}
