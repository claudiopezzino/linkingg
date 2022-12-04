package view.controllerui.first;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import view.graphicalui.first.PageDialog;

import java.util.Collections;
import java.util.Map;

import static view.graphicalui.first.constcontainer.Css.HBOX;
import static view.graphicalui.first.constcontainer.Css.VBOX;
import static view.graphicalui.first.constcontainer.Image.*;

public final class Dialog {

    //////////////////
    private Dialog(){}
    //////////////////

    /*--------------------- INNER_CLASS ---------------------*/
    private static class InfoErrorDialog extends PageDialog {

        ///////////////////////////////////////////////////////
        private static InfoErrorDialog infoErrorDialogInstance;
        ///////////////////////////////////////////////////////

        //////////////////////////
        private Label labelErrors;
        //////////////////////////

        //////////////////////////////////////////////////////////
        private InfoErrorDialog() {
            super("Error Panel", "Error", ERROR);
            this.getDialogPane().setContent(this.setUpPopUpRoot());
            this.setResultConverter(this::infoErrorResult);
        }
        ///////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        private Map<String, String> infoErrorResult(ButtonType buttonType){
            return Collections.emptyMap();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {
            VBox vBoxPopUpRoot = new VBox();
            vBoxPopUpRoot.getStyleClass().addAll(VBOX, HBOX);

            labelErrors = new Label();
            vBoxPopUpRoot.getChildren().add(labelErrors);

            return vBoxPopUpRoot;
        }
        ////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////
        public Label getLabelErrors() {
            return this.labelErrors;
        }
        //////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////
        public static InfoErrorDialog getInfoErrorDialogInstance() {
            if(infoErrorDialogInstance == null)
                infoErrorDialogInstance = new InfoErrorDialog();
            return infoErrorDialogInstance;
        }
        //////////////////////////////////////////////////////////////

    }
    /*----------------------------------------------------------------*/

    ////////////////////////////////////////////////////////////////////////////////////
    public static void errorDialog(String errors){
        InfoErrorDialog infoErrorDialog = InfoErrorDialog.getInfoErrorDialogInstance();
        infoErrorDialog.getLabelErrors().setText(errors);
        infoErrorDialog.showAndWait();
    }
    ////////////////////////////////////////////////////////////////////////////////////


    /*----------------------------- INNER_CLASS -----------------------------*/
    public static class WaitDialog extends PageDialog {

        /////////////////////////////////////////////
        private static WaitDialog waitDialogInstance;
        /////////////////////////////////////////////


        //////////////////////////////////////////////////////////////
        private WaitDialog() {
            super("Waiting", "Loading in progress", WAITING);
            this.getDialogPane().setContent(this.setUpPopUpRoot());
            this.setResultConverter(this::waitResult);
            this.disableClosure();
            this.makeButtonsUnusable();
        }
        //////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////
        private void disableClosure(){
            this.getDialogPane().getScene().getWindow()
                    .addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, Event::consume);
            this.makeButtonsUnusable();
        }
        //////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////////////
        private void makeButtonsUnusable(){
            Button btnSave = (Button) this.getDialogPane().lookupButton(this.getBtnTypeSave());
            Button btnDismiss = (Button) this.getDialogPane().lookupButton(this.getBtnTypeDismiss());

            btnSave.addEventFilter(ActionEvent.ACTION, Event::consume);
            btnDismiss.addEventFilter(ActionEvent.ACTION, Event::consume);
        }
        /////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////
        private Map<String, String> waitResult(ButtonType buttonType){
            return Collections.emptyMap();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {
            VBox vBoxPopUpRoot = new VBox();
            vBoxPopUpRoot.getStyleClass().addAll(VBOX, HBOX);
            vBoxPopUpRoot.getChildren()
                    .add(new Label("Wait, please. Loading in progress... \n\n"));
            return vBoxPopUpRoot;
        }
        ////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////
        public static WaitDialog getWaitDialogInstance() {
            if(waitDialogInstance == null)
                waitDialogInstance = new WaitDialog();
            return waitDialogInstance;
        }
        ///////////////////////////////////////////////////
    }
    /*-------------------------------------------------------------------------------*/

    //////////////////////////////////////////////////////////////////////////////////
    public static void openWaitDialog(){
        WaitDialog.getWaitDialogInstance().show();
    }
    //////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    public static void closeWaitDialog(){
        WaitDialog.getWaitDialogInstance().close();
    }
    ////////////////////////////////////////////////////////////////////////////////////
}
