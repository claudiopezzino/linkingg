package view.graphicalui.first;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import view.controllerui.first.DialogEventHandler;

import java.util.HashMap;
import java.util.Map;

import static view.graphicalui.first.constcontainer.Css.CANCEL_BUTTON;
import static view.graphicalui.first.constcontainer.Css.DIALOG_BUTTONS;


public abstract class PageDialog extends Dialog<Map<String, String>> {

    ////////////////////////////////////////////////////
    protected Map<String, String> map = new HashMap<>();
    ////////////////////////////////////////////////////

    ///////////////////////////////
    private ButtonType btnTypeSave;
    ///////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    protected DialogEventHandler<MouseEvent> dialogHandler = new DialogEventHandler<>();
    ////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    protected PageDialog(String title, String header, String graphic){
        this.getDialogPane().getStylesheets().add("FirstMainFX.css");
        this.setUpSkeleton(title, header, graphic);
    }
    ///////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////
    private void setUpSkeleton(String title, String header, String graphic){
        this.setTitle(title);
        this.setHeaderText(header);
        this.setGraphic(new ImageView(graphic));
        this.setUpBtnTypes();
    }
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void setUpBtnTypes(){
        btnTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTypeDismiss = new ButtonType("Dismiss", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(btnTypeSave, btnTypeDismiss);

        Node nodeSave = this.getDialogPane().lookupButton(btnTypeSave);
        Node nodeDismiss = this.getDialogPane().lookupButton(btnTypeDismiss);

        nodeSave.getStyleClass().add(DIALOG_BUTTONS);
        nodeDismiss.getStyleClass().addAll(DIALOG_BUTTONS, CANCEL_BUTTON);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////
    protected abstract VBox setUpPopUpRoot();
    ////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    public ButtonType getBtnTypeSave() {
        return this.btnTypeSave;
    }
    ///////////////////////////////////////////////////////////////
}
