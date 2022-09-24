package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import view.graphicalui.first.Toolbar;

import static view.graphicalui.first.constcontainer.Css.SIGN_BUTTONS;
import static view.graphicalui.first.constcontainer.Image.LOCATION;


public class SharingButtonDecorator extends Decorator{

    ///////////////////////////////
    private Button btnShareMeeting;
    ///////////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    public SharingButtonDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler){
        super(toolbar);
        this.handler = handler;
        this.setUpBtnSharing();
    }
    //////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    private void setUpBtnSharing(){
        btnShareMeeting = new Button("Share meeting", new ImageView(LOCATION));
        btnShareMeeting.getStyleClass().add(SIGN_BUTTONS);
        btnShareMeeting.setOnMouseClicked(handler);
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(btnShareMeeting, new Separator());
        return toolBar;
    }
    ////////////////////////////////////////////////////////////////

}
