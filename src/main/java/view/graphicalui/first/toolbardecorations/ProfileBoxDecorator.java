package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.graphicalui.first.Toolbar;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.PROFILE;


public class ProfileBoxDecorator extends Decorator{

    /////////////////////////
    private VBox vBoxProfile;
    /////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    public ProfileBoxDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpProfile();
    }
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    private void setUpProfile(){

        Circle circleImgProfile = new Circle(20);
        circleImgProfile.setFill(new ImagePattern(new Image(PROFILE)));
        circleImgProfile.getStyleClass().add(CIRCLE);
        circleImgProfile.setOnMouseClicked(handler);

        Hyperlink hyperlinkSignOut = new Hyperlink("Sign Out");
        hyperlinkSignOut.setOnMouseClicked(handler);

        vBoxProfile = new VBox(circleImgProfile, hyperlinkSignOut);
        vBoxProfile.getStyleClass().addAll(HBOX);

    }
    //////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().add(vBoxProfile);
        return toolBar;
    }
    ////////////////////////////////////////

}
