package view.graphicalui.second;

public class QuestionStateMachine {

    //////////////////////////////////
    private AbstractQuestion question;
    //////////////////////////////////


    //////////////////////////////////////////////////////////
    public void displayQuestion(){
        this.question.display();
    }
    //////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public void nextQuestion(){
        this.question.next(this);
    }
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public AbstractQuestion getQuestion(){
        return this.question;
    }
    ///////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setQuestion(AbstractQuestion concreteQuestion){
        this.question = concreteQuestion;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

}
