package view.graphicalui.second;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.graphicalui.second.signupquestionstates.QuestionName;

import java.util.ArrayList;


public class Signup extends Sign {

    /////////////////////////////////////
    private static Signup signupInstance;
    /////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    private Signup(){
        this.stateMachine.setQuestion(QuestionName.getQuestionNameInstance());
        this.userInfo = new ArrayList<>();
        this.setUpTitle(new Text("Sign Up"));
        this.setUpPrompt();
        this.setUpScreen();
        this.getChildren().add(this.setUpSignupRoot());
    }
    ///////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private VBox setUpSignupRoot(){
        VBox root = new VBox();
        root.getChildren().addAll(this.titleContainer, this.screen, this.prompt);
        return root;
    }
    //////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void restoreScreen() {
        this.stateMachine.setQuestion(QuestionName.getQuestionNameInstance());
        super.restoreScreen();
    }
    ///////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void prevScreen() {
        this.stateMachine.setQuestion(QuestionName.getQuestionNameInstance());
        super.prevScreen();
    }
    //////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////
    public static Signup getSignupInstance(){
        if(signupInstance == null)
            signupInstance = new Signup();
        return signupInstance;
    }
    ///////////////////////////////////////////

}
