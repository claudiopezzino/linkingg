package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.first.constcontainer.Css.TOOL_ELEMENTS;

public class InfoBarDecorator extends Decorator{

    /////////////////////////
    private HBox hBoxInfoBar;
    /////////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    public InfoBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpInfoBar();
    }
    /////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    private void setUpInfoBar(){

        /*-----------------------------------------------*/
        List<Hyperlink> hyperlinkList = new ArrayList<>();
        /*-----------------------------------------------*/

        /*----------------------------------------------*/
        Hyperlink about = new Hyperlink("About Us");
        Hyperlink overview = new Hyperlink("Overview");
        Hyperlink help = new Hyperlink("Help");
        /*----------------------------------------------*/

        /*-------------------------*/
        hyperlinkList.add(about);
        hyperlinkList.add(overview);
        hyperlinkList.add(help);
        /*-------------------------*/

        /*----------------------------------------------------------------------*/
        hyperlinkList.forEach(hyperlink -> hyperlink.setOnMouseClicked(handler));
        /*----------------------------------------------------------------------*/

        /*-------------------------------------------------------------------------*/
        hBoxInfoBar = new HBox(about, new Separator(), overview, new Separator(), help);
        hBoxInfoBar.getStyleClass().add(TOOL_ELEMENTS);
        /*-------------------------------------------------------------------------*/

    }
    ///////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(hBoxInfoBar, new Separator());
        return toolBar;
    }
    ////////////////////////////////////////////////////////////

}
