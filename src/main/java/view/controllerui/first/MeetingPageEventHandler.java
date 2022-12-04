package view.controllerui.first;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.MeetingPage;

import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.toolbaritems.MeetingChoiceToolbarItems.*;
import static view.graphicalui.first.toolbaritems.MeetingChoiceToolbarItems.MainBarItems.*;
import static view.graphicalui.first.toolbaritems.MeetingChoiceToolbarItems.profileBoxItems.*;


public class MeetingPageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {

        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

            HBox mainBar = (HBox) MeetingPage.getMeetingPageInstance(null).getToolBar().getItems().get(MAIN_BAR.getIndex());
            VBox profileBox = (VBox) MeetingPage.getMeetingPageInstance(null).getToolBar().getItems().get(PROFILE_BOX.getIndex());

            // to remove or make dispatch of listening thread as done with home handler
            if(event.getSource().equals(profileBox.getChildren().get(LINK_SIGNOUT.getIndex())))
                FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

            else if( event.getSource().equals(mainBar.getChildren().get(HOME_BUTTON.getIndex()))
                    || event.getSource().equals(MeetingPage.getMeetingPageInstance(null).getToolBar().getItems().get(SHARING_BUTTON.getIndex())) )

                FirstMain.getCurrScene().setRoot(Container.getRoot(HOME));

            else
                Dialog.errorDialog("Something went wrong, please try later.");


        }

    }

}
