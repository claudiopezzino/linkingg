package view.graphicalui.first;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import view.graphicalui.first.toolbardecorations.*;


public abstract class Container {

    ///////////////////////////////
    private static ScrollPane root;
    ///////////////////////////////

    //////////////////////
    private Container(){}
    //////////////////////

    ///////////////////////////////////////////////
    public static ScrollPane getRoot(Page page){
        if(root == null)
            root = new ScrollPane();
        root.setContent(buildContent(page));
        return root;
    }
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private static Parent buildContent(Page page){
        switch (page){
            case WELCOME_PAGE:
                return WelcomePage.getWelcomePageInstance(getToolBar(page));

            case SIGN_UP_PAGE:
                return SignupPage.getSignupPageInstance(getToolBar(page));

            case SIGN_IN_PAGE:
                return SigninPage.getSigninPageInstance(getToolBar(page));

            case HOME:
                return HomePage.getHomePageInstance(getToolBar(page));

            case MEETING_CHOICE_PAGE:
                return MeetingPage.getMeetingPageInstance(getToolBar(page));

            default:   // never executed but required by Sonar
                return null;
        }
    }
    ///////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    private static Toolbar getToolBar(Page page){

        switch (page){
            case WELCOME_PAGE:
                if(WelcomePage.isToolBarPresent())
                    return null;
                return createToolBar(page);

            case SIGN_UP_PAGE:
                if(SignupPage.isToolBarPresent())
                    return null;
                return createToolBar(page);

            case SIGN_IN_PAGE:
                if(SigninPage.isToolBarPresent())
                    return null;
                return createToolBar(page);

            case HOME:
                if(HomePage.isToolBarPresent())
                    return null;
                return createToolBar(page);

            case MEETING_CHOICE_PAGE:
                if(MeetingPage.isToolBarPresent())
                    return null;
                return createToolBar(page);

            default:   // never executed but required by Sonar
                return createToolBar(page);
        }

    }
    ///////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Toolbar createToolBar(Page page){

        switch (page){
            case WELCOME_PAGE:
                return new SignBarDecorator(new InfoBarDecorator(new SocialBarDecorator(new ConcreteToolbar(), WelcomePage.getHandler()), WelcomePage.getHandler()), WelcomePage.getHandler());

            case SIGN_UP_PAGE:
                return new UndoRedoBarDecorator(new MainBarDecorator(new TitleBoxDecorator(new SocialBarDecorator(new ConcreteToolbar(), SignupPage.getHandler()), new Text("Sign Up")), SignupPage.getHandler()), SignupPage.getHandler());

            case SIGN_IN_PAGE:
                return new UndoRedoBarDecorator(new MainBarDecorator(new TitleBoxDecorator(new SocialBarDecorator(new ConcreteToolbar(), SigninPage.getHandler()), new Text("Sign In")), SigninPage.getHandler()), SigninPage.getHandler());

            case HOME:
                return new ProfileBoxDecorator(new SearchBarDecorator(new GroupButtonDecorator(new ConcreteToolbar(), HomePage.getHandler()), HomePage.getHandler()), HomePage.getHandler());

            case MEETING_CHOICE_PAGE:
                return new ProfileBoxDecorator(new MainBarDecorator(new TitleBoxDecorator(new SharingButtonDecorator(new ConcreteToolbar(), MeetingPage.getHandler()), new Text("Meeting Point Choice")), MeetingPage.getHandler()), MeetingPage.getHandler());

            default:   // never executed but required by Sonar
                return new ConcreteToolbar();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
