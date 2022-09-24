package view.graphicalui.second;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Welcome extends Shell{

    ///////////////////////////////////////
    private static Welcome welcomeInstance;
    ///////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    private static final String WELCOME_MSG = "\nHello, Guest! \n\n\n" +
            "In this page you can choose from \n\n\n";
    /////////////////////////////////////////////////////////////////////

    //////////////////////////////////////
    private static final String OPTIONS =
            "->   about\n\n" +
                    "->   overview\n\n" +
            "->   help\n\n" +
            "->   social\n\n" +
            "->   signup\n\n" +
            "->   signin\n\n\n";
    //////////////////////////////////////

    ////////////////////////////////////////////////////
    private Welcome(){
        this.setUpTitle(new Text("Welcome"));
        this.setUpPrompt();
        this.setUpScreen();
        this.getChildren().add(this.setUpWelcomeRoot());
    }
    ////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private VBox setUpWelcomeRoot(){
        VBox root = new VBox();
        root.getChildren().addAll(this.titleContainer, this.screen, this.prompt);
        return root;
    }
    /////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    @Override
    public void setUpScreen() {
        this.screen = new TextArea();
        this.screen.setEditable(false);
        this.screen.setText(LEGEND + WELCOME_MSG + OPTIONS);
        this.screen.setFocusTraversable(false);
    }
    ////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void restoreScreen() {
        this.screen.setText(LEGEND + WELCOME_MSG + OPTIONS);
    }
    /////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////
    public static Welcome getWelcomeInstance(){
        if(welcomeInstance == null)
            welcomeInstance = new Welcome();
        return welcomeInstance;
    }
    ////////////////////////////////////////////

}
