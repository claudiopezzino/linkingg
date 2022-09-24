package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.first.constcontainer.Css.SIGN_BUTTONS;
import static view.graphicalui.first.constcontainer.Css.TOOL_ELEMENTS;


public class SignBarDecorator extends Decorator{

    /////////////////////
    private HBox signBar;
    /////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public SignBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpSignBar();
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    private void setUpSignBar(){

        /*-----------------------------------------*/
        List<Button> buttonList = new ArrayList<>();
        /*-----------------------------------------*/

        /*--------------------------------------*/
        Button btnSignIn = new Button("Sign In");
        Button btnSignUp = new Button("Sign Up");
        /*--------------------------------------*/

        /*----------------------*/
        buttonList.add(btnSignIn);
        buttonList.add(btnSignUp);
        /*----------------------*/

        /*------------------------------------------*/
        buttonList.forEach(button -> {
            button.getStyleClass().add(SIGN_BUTTONS);
            button.setOnMouseClicked(handler);
        });
        /*------------------------------------------*/

        /*-------------------------------------------------------*/
        signBar = new HBox(btnSignUp, new Separator(), btnSignIn);
        signBar.getStyleClass().add(TOOL_ELEMENTS);
        /*-------------------------------------------------------*/

    }
    /////////////////////////////////////////////////////////////////////

    ////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().add(signBar);
        return toolBar;
    }
    ////////////////////////////////////

}
