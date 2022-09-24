package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.first.constcontainer.Css.BUTTONS_PREVIEW;
import static view.graphicalui.first.constcontainer.Css.TOOL_ELEMENTS;
import static view.graphicalui.first.constcontainer.Image.*;


public class SocialBarDecorator extends Decorator{

    ///////////////////////////
    private HBox hBoxSocialBar;
    ///////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    public SocialBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpSocialBar();
    }
    ///////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private void setUpSocialBar(){

        /*-----------------------------------------*/
        List<Button> buttonList = new ArrayList<>();
        /*-----------------------------------------*/

        /*--------------------------------*/
        Button btnYoutube = new Button();
        Button btnInstagram = new Button();
        Button btnFacebook = new Button();
        Button btnTwitter = new Button();
        /*--------------------------------*/

        /*---------------------------------------------------------*/
        btnYoutube.setGraphic(new ImageView(YOUTUBE));
        btnInstagram.setGraphic(new ImageView(INSTAGRAM));
        btnFacebook.setGraphic(new ImageView(FACEBOOK));
        btnTwitter.setGraphic(new ImageView(TWITTER));
        /*---------------------------------------------------------*/

        /*--------------------------*/
        buttonList.add(btnYoutube);
        buttonList.add(btnInstagram);
        buttonList.add(btnFacebook);
        buttonList.add(btnTwitter);
        /*--------------------------*/

        /*----------------------------------------------*/
        buttonList.forEach(button -> {
            button.getStyleClass().add(BUTTONS_PREVIEW);
            button.setOnMouseClicked(handler);
        });
        /*----------------------------------------------*/

        /*---------------------------------------------------------------------------------------------------------------------------*/
        hBoxSocialBar = new HBox(btnFacebook, new Separator(), btnTwitter, new Separator(), btnInstagram, new Separator(), btnYoutube);
        hBoxSocialBar.getStyleClass().add(TOOL_ELEMENTS);
        /*----------------------------------------------------------------------------------------------------------------------------*/
    }
    /////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(hBoxSocialBar, new Separator());
        return toolBar;
    }
    ///////////////////////////////////////////////////////////////
}
