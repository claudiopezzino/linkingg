package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import view.graphicalui.first.Toolbar;

import static view.graphicalui.first.constcontainer.Css.SIGN_BUTTONS;
import static view.graphicalui.first.constcontainer.Image.CREATE_GROUP;


public class GroupButtonDecorator extends Decorator{

    ///////////////////////////
    private Button btnNewGroup;
    ///////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    public GroupButtonDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpBtnNewGroup();
    }
    /////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    private void setUpBtnNewGroup(){
        btnNewGroup = new Button("New group", new ImageView(CREATE_GROUP));
        btnNewGroup.getStyleClass().add(SIGN_BUTTONS);
        btnNewGroup.setOnMouseClicked(handler);
    }
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(btnNewGroup, new Separator());
        return toolBar;
    }
    /////////////////////////////////////////////////////////////

}
