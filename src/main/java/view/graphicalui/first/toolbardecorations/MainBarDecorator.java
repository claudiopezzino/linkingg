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

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.HOME;
import static view.graphicalui.first.constcontainer.Image.REFRESH;

public class MainBarDecorator extends Decorator{

    /////////////////////////
    private HBox hBoxMainBar;
    /////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    public MainBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpMainBar();
    }
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    private void setUpMainBar(){

        /*-----------------------------------------*/
        List<Button> buttonList = new ArrayList<>();
        /*-----------------------------------------*/

        /*---------------------------*/
        Button btnHome = new Button();
        Button btnRefresh = new Button();
        /*---------------------------*/

        /*--------------------------------------------------*/
        btnRefresh.setGraphic(new ImageView(REFRESH));
        btnHome.setGraphic(new ImageView(HOME));
        /*--------------------------------------------------*/

        /*---------------------*/
        buttonList.add(btnHome);
        buttonList.add(btnRefresh);
        /*---------------------*/

        /*---------------------------------------------*/
        buttonList.forEach(button -> {
            button.getStyleClass().add(BUTTONS_PREVIEW);
            button.setOnMouseClicked(handler);
        });
        /*---------------------------------------------*/

        /*----------------------------------------------------------*/
        hBoxMainBar = new HBox(btnHome, new Separator(), btnRefresh);
        hBoxMainBar.getStyleClass().addAll(HBOX, TOOL_ELEMENTS);
        /*----------------------------------------------------------*/

    }
    ///////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(hBoxMainBar, new Separator());
        return toolBar;
    }
    ////////////////////////////////////////////////////////////

}
