package view.graphicalui.first.toolbardecorations;

import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;
import view.graphicalui.first.Toolbar;

import static view.graphicalui.first.constcontainer.Css.SIGNS_TITLE;


public class TitleBoxDecorator extends Decorator{

    ////////////////////////
    private final Text text;
    ////////////////////////

    ///////////////////////////////////////////////////////
    public TitleBoxDecorator(Toolbar toolbar, Text text) {
        super(toolbar);
        this.text = text;
        text.getStyleClass().add(SIGNS_TITLE);
    }
    ///////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(text, new Separator());
        return toolBar;
    }
    /////////////////////////////////////////////////////

}
