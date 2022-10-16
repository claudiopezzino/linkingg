package view.graphicalui.first;

import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;

import static view.graphicalui.first.constcontainer.Image.LOGO;

public class ConcreteToolbar implements Toolbar {

    //////////////////////////////////////////////
    private final ToolBar toolBar = new ToolBar();
    //////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public ConcreteToolbar(){
        this.toolBar.getItems().addAll(new ImageView(LOGO), new Separator());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        return this.toolBar;
    }
    //////////////////////////////////////////////
}