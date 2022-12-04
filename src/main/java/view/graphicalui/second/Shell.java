package view.graphicalui.second;

import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import view.controllerui.second.KeyEventHandler;


public abstract class Shell extends Parent {

    /////////////////////////////////////////////////////////////////////////////////////////////////
    protected static final String LEGEND =
            "+------------------------- LEGEND --------------------------+\n\n" +
            "              Command                           Meaning         \n\n" +
            "             \u2022 back                          come back to previous page\n\n" +
            "             \u2022 prev                           come back to previous step\n\n" +
            "             \u2022 exit                             come back to welcome page\n\n" +
            "+-------------------------------------------------------------+\n\n"
            ;
    /////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////
    protected HBox titleContainer;
    protected TextArea screen;
    protected TextField prompt;
    ///////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////
    // so that every Shell's son can refer same handler instance
    protected static final KeyEventHandler<KeyEvent> handler = new KeyEventHandler<>();
    ///////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////
    public static KeyEventHandler<KeyEvent> getShellHandler(){
        return handler;
    }
    /////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public TextArea getScreen(){
        return this.screen;
    }
    ////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public TextField getPrompt(){
        return this.prompt;
    }
    /////////////////////////////////////////////////////

    //////////////////////////////////////
    public abstract void restoreScreen();
    /////////////////////////////////////

    //////////////////////////////////////
    protected abstract void setUpScreen();
    //////////////////////////////////////

    //////////////////////////////////////////////////////////
    protected void setUpTitle(Text title) {
        title.getStyleClass().add("page-title");
        this.titleContainer = new HBox(title);
        titleContainer.getStyleClass().add("title-container");
    }
    //////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    protected void setUpPrompt(){
        this.prompt = new TextField();
        prompt.setOnKeyReleased(handler);
        prompt.setPromptText("Type here a command placed after its arrow...");
    }
    ///////////////////////////////////////////////////////////////////////////

}
