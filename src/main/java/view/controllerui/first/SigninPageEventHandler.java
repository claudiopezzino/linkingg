package view.controllerui.first;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.SigninPage;

import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.*;
import static view.graphicalui.first.toolbaritems.SignToolbarItems.MainBarItems.*;


public class SigninPageEventHandler <T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {

        HBox mainBar = (HBox) SigninPage.getSigninPageInstance(null).getToolBar().getItems().get(MAIN_BAR.getIndex());

        if(event.getSource().equals(mainBar.getChildren().get(HOME_BUTTON.getIndex())))
            FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

        else if(event.getSource().equals(SigninPage.getSigninPageInstance(null).getBtnSignIn()))
            FirstMain.getCurrScene().setRoot(Container.getRoot(HOME));

        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Work in progress...");
            alert.showAndWait();
        }

        this.clearSigninCredentials(SigninPage.getSigninPageInstance(null));

    }

    ///////////////////////////////////////////////////////////
    private void clearSigninCredentials(SigninPage signinPage){
        signinPage.getTextFieldUsername().clear();
        signinPage.getTextFieldPassword().clear();
    }
    ///////////////////////////////////////////////////////////
}
