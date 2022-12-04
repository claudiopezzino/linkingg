package view.controllerui.first;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.WelcomePage;

import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.toolbaritems.WelcomeToolbarItems.*;
import static view.graphicalui.first.toolbaritems.WelcomeToolbarItems.SignBarItems.*;


public class WelcomePageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {

        if( event.getEventType().equals(MouseEvent.MOUSE_CLICKED) ) {

            // same approach for the other items in case of their employment
            HBox signBar = (HBox) WelcomePage.getWelcomePageInstance(null).getToolBar().getItems().get(SIGN_BAR.getIndex());

            if( event.getSource().equals(signBar.getChildren().get(SIGN_IN_BUTTON.getIndex())) )
                FirstMain.getCurrScene().setRoot(Container.getRoot(SIGN_IN_PAGE));

            else if( event.getSource().equals(signBar.getChildren().get(SIGN_UP_BUTTON.getIndex())) )
                FirstMain.getCurrScene().setRoot(Container.getRoot(SIGN_UP_PAGE));

            else
                Dialog.errorDialog("Something went wrong, please try later.");
        }
    }

}
