package view.graphicalui.first.toolbardecorations;

import javafx.scene.control.ToolBar;
import view.graphicalui.first.Toolbar;

public abstract class Decorator implements Toolbar {

    //////////////////////////////
    private final Toolbar toolbar;
    //////////////////////////////

    ////////////////////////////////////////////////////////////////
    protected Decorator(Toolbar toolbar){
        this.toolbar = toolbar;
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    @Override
    public ToolBar draw(){
        return this.toolbar.draw();
    }
    //////////////////////////////////////////////////////

}