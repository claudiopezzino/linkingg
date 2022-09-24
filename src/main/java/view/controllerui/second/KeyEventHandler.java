package view.controllerui.second;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.graphicalui.second.*;
import view.graphicalui.second.signinquestionstates.QuestionEnd;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class KeyEventHandler<T extends Event> implements EventHandler<T> {

    ///////////////////////////////////////////////////////////////////////////
    private final HandlerStateMachine stateMachine = new HandlerStateMachine();
    ///////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    public HandlerStateMachine getStateMachine() {
        return this.stateMachine;
    }
    //////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(T event) {

        KeyEvent keyEvent = (KeyEvent) event;
        if( keyEvent.getCode().equals(KeyCode.ENTER) ){

            if( event.getSource().equals(Welcome.getWelcomeInstance().getPrompt()) )
                this.welcomeHandle(Welcome.getWelcomeInstance());

            else if( event.getSource().equals(Signup.getSignupInstance().getPrompt()) )
                this.signupHandle(Signup.getSignupInstance());

            else if( event.getSource().equals(Signin.getSigninInstance().getPrompt()) )
                this.signinHandle(Signin.getSigninInstance());

            else if( event.getSource().equals(Home.getHomeInstance().getPrompt()) ) {
                this.homeHandle(Home.getHomeInstance());
            }

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    private void welcomeHandle(Welcome welcome){
        if( welcome.getPrompt().getText().equals(SIGNUP) ) {
            Signup.getSignupInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signup.getSignupInstance());
        }

        else if( welcome.getPrompt().getText().equals(SIGNIN) ) {
            Signin.getSigninInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signin.getSigninInstance());
        }

        else if( welcome.getPrompt().getText().equals(HELP)
                || welcome.getPrompt().getText().equals(SOCIAL)
                || welcome.getPrompt().getText().equals(ABOUT)
                || welcome.getPrompt().getText().equals(OVERVIEW) )

            infoMsg();

        else
            errorMsg();

        welcome.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void signupHandle(Signup signup){
        if( signup.getPrompt().getText().equals(EXIT) || signup.getPrompt().getText().equals(BACK) ) {

            signup.setSignMode(true);
            signup.getPrompt().clear();
            signup.restoreScreen();
            SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
        }

        else if(signup.getPrompt().getText().equals(PREV) && !signup.getUserInfo().isEmpty())
            signup.prevScreen();

        else if(signup.getPrompt().getText().equals(CONFIRM) && signup.getStateMachine().getQuestion() == null){

            this.makeUserCredentials(signup);
            signup.setSignMode(true);
            Signin.getSigninInstance().displaySignOptions();
            SecondMain.getCurrScene().setRoot(Signin.getSigninInstance());
            signup.getPrompt().clear();
            signup.restoreScreen();
        }

        else if(signup.getSignMode().equals(true) && signup.getPrompt().getText().equals(MANUAL)){
            signup.setSignMode(false);
            signup.restoreScreen();
            signup.displayQuestion();
            signup.getPrompt().clear();
        }

        else if( signup.getPrompt().getText().equals(BUSINESS_OWNER) || signup.getPrompt().getText().equals(PREMIUM)
                || signup.getPrompt().getText().equals(GOOGLE) || signup.getPrompt().getText().equals(FACEBOOK))
            infoMsg();

        else if(!signup.getPrompt().getText().isEmpty() && !signup.getPrompt().getText().equals(PREV)
                &&  signup.getStateMachine().getQuestion() != null && signup.getSignMode().equals(false)){

            signup.fillUserInfo(signup.getPrompt().getText());
            signup.getScreen().appendText(signup.getPrompt().getText());
            signup.displayQuestion();
            signup.getPrompt().clear();
        }

        else errorMsg();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void signinHandle(Signin signin){

        if( signin.getPrompt().getText().equals(EXIT) || signin.getPrompt().getText().equals(BACK) ) {

            signin.setSignMode(true);
            signin.getPrompt().clear();
            signin.restoreScreen();
            SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
        }

        else if(signin.getPrompt().getText().equals(PREV) && !signin.getUserInfo().isEmpty())
            signin.prevScreen();

        else if(signin.getPrompt().getText().equals(CONFIRM) && signin.getStateMachine().getQuestion() == null){

            signin.setSignMode(true);
            signin.getPrompt().clear();
            signin.restoreScreen();
            SecondMain.getCurrScene().setRoot(Home.getHomeInstance());
        }

        else if(signin.getSignMode().equals(true) && signin.getPrompt().getText().equals(MANUAL)){
            signin.setSignMode(false);
            signin.restoreScreen();
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else if(signin.getPrompt().getText().equals(GOOGLE)
                || signin.getPrompt().getText().equals(FACEBOOK))
            infoMsg();

        else if( !signin.getPrompt().getText().isEmpty() && !signin.getPrompt().getText().equals(PREV)
                && signin.getStateMachine().getQuestion() != null && signin.getSignMode().equals(false)
                && !signin.getStateMachine().getQuestion().equals(QuestionEnd.getQuestionEndInstance())){

            signin.fillUserInfo(signin.getPrompt().getText());
            signin.getScreen().appendText(signin.getPrompt().getText());
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else if(!signin.getPrompt().getText().isEmpty() && !signin.getPrompt().getText().equals(PREV)
                && signin.getStateMachine().getQuestion() != null && signin.getSignMode().equals(false)
                && signin.getStateMachine().getQuestion().equals(QuestionEnd.getQuestionEndInstance())){

            signin.fillUserInfo(signin.getPrompt().getText());
            signin.getScreen().appendText("*".repeat(signin.getPrompt().getText().length()));
            signin.displayQuestion();
            signin.getPrompt().clear();
        }

        else errorMsg();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    private void makeUserCredentials(Sign signup){
        String name = signup.getUserInfo().get(0);
        String surname = signup.getUserInfo().get(1);

        String fullName = name + " " + surname;

        String username = name.toLowerCase() + surname.toLowerCase();
        String password = "password";

        credentialMsg(username, password);

        this.assignUserCredentials(fullName, username, password);
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private void assignUserCredentials(String fullName, String nickname, String password){

        Home.setCurrUserFullName(fullName);

        Home.getHomeInstance().setCurrUserNickname(nickname);
        Home.getHomeInstance().setCurrUserPassword(password);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    private void homeHandle(Home home){

        /*------------------------------------------*/
        if(home.getPrompt().getText().equals(EXIT))
            this.moveBackToWelcomePage(home);
        /*------------------------------------------*/

        /*--------------------------------------------------*/
        else if(home.getPrompt().getText().equals(HOME_PAGE))
            this.moveBackToHomePage(home);
        /*--------------------------------------------------*/

        /*----------------------------------*/
        else
            this.stateMachine.checkCmd(home);
        /*----------------------------------*/

    }
    ///////////////////////////////////////////////////////////


    ////////////////////////////////////////////
    private void moveBackToHomePage(Home home){
        home.restoreScreen();
        home.getPrompt().clear();
    }
    ////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    private void moveBackToWelcomePage(Home home){
        SecondMain.getCurrScene().setRoot(Welcome.getWelcomeInstance());
        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////////

}
