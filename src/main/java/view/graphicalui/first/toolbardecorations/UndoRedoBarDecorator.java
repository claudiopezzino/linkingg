package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Toolbar;


import static view.graphicalui.first.constcontainer.Css.SIGN_BUTTONS;
import static view.graphicalui.first.constcontainer.Css.TOOL_ELEMENTS;


public class UndoRedoBarDecorator extends Decorator{

    /////////////////////////////
    private HBox hBoxUndoRedoBar;
    /////////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    public UndoRedoBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler){
        super(toolbar);
        this.handler = handler;
        this.setUpUndoRedoBar();
    }
    ////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    private void setUpUndoRedoBar(){

        /*------------------------------------------*/
        Button buttonRedo = new Button("Redo");
        buttonRedo.getStyleClass().add(SIGN_BUTTONS);
        buttonRedo.setOnMouseClicked(handler);
        /*------------------------------------------*/

        /*------------------------------------------*/
        Button buttonUndo = new Button("Undo");
        buttonUndo.getStyleClass().add(SIGN_BUTTONS);
        buttonUndo.setOnMouseClicked(handler);
        /*------------------------------------------*/

        /*----------------------------------------------------------------*/
        hBoxUndoRedoBar = new HBox(buttonRedo, new Separator(), buttonUndo);
        hBoxUndoRedoBar.getStyleClass().add(TOOL_ELEMENTS);
        /*----------------------------------------------------------------*/

    }
    /////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().add(hBoxUndoRedoBar);
        return toolBar;
    }
    /////////////////////////////////////////////

}
