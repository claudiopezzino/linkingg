package view.graphicalui.second;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.graphicalui.second.signinquestionstates.QuestionNickname;

import java.util.ArrayList;


public class Signin extends Sign {

    /////////////////////////////////////
    private static Signin signinInstance;
    /////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////
    private Signin(){
        this.stateMachine.setQuestion(QuestionNickname.getQuestionNicknameInstance());
        this.userInfo = new ArrayList<>();
        this.setUpTitle(new Text("Sign in"));
        this.setUpPrompt();
        this.setUpScreen();
        this.getChildren().add(this.setUpSigninRoot());
    }
    ///////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private VBox setUpSigninRoot(){
        VBox root = new VBox();
        root.getChildren().addAll(this.titleContainer, this.screen, this.prompt);
        return root;
    }
    /////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void restoreScreen() {
        this.stateMachine.setQuestion(QuestionNickname.getQuestionNicknameInstance());
        super.restoreScreen();
    }
    //////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    @Override
    public void prevScreen() {
        this.stateMachine.setQuestion(QuestionNickname.getQuestionNicknameInstance());
        super.prevScreen();
    }
    //////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////
    public static Signin getSigninInstance(){
        if(signinInstance == null)
            signinInstance = new Signin();
        return signinInstance;
    }
    //////////////////////////////////////////

}
