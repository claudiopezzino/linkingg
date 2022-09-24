package view.controllerui.first;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import view.graphicalui.first.Container;
import view.graphicalui.first.FirstMain;
import view.graphicalui.first.HomePage;
import view.graphicalui.first.HomePage.UserProfileDialog;
import view.graphicalui.first.HomePage.GroupCreationDialog;
import view.graphicalui.first.HomePage.GroupDeletionDialog;
import view.graphicalui.first.HomePage.LinkRequestsDialog;
import view.graphicalui.first.HomePage.SearchGroupsDialog;
import view.graphicalui.first.HomePage.SearchUsersDialog;

import java.util.Map;
import java.util.Optional;

import static view.graphicalui.first.ListViewGroupItems.*;
import static view.graphicalui.first.ListViewGroupItems.groupDetails.*;
import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.Page.*;
import static view.graphicalui.first.constcontainer.Image.*;
import static view.graphicalui.first.constcontainer.Protocol.FILE;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.*;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.profileBoxItems.*;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.RADIO_BUTTONS_CONTAINER;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.SEARCH_FIELDS;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.radioButtonsContainerItems.RADIO_BUTTON_GROUPS;
import static view.graphicalui.first.toolbaritems.HomeToolbarItems.searchBarItems.searchFieldsItems.SEARCH_BUTTON;


public class HomePageEventHandler<T extends MouseEvent> implements EventHandler<T> {

    @Override
    public void handle(T event) {

        if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

            VBox profileBox = (VBox) HomePage.getHomePageInstance(null).getToolBar().getItems().get(PROFILE_BOX.getIndex());
            HBox searchBar = (HBox) HomePage.getHomePageInstance(null).getToolBar().getItems().get(SEARCH_BAR.getIndex());

            HBox searchFields = (HBox) searchBar.getChildren().get(SEARCH_FIELDS.getIndex());

            if(event.getSource().equals(profileBox.getChildren().get(LINK_SIGNOUT.getIndex())))
                FirstMain.getCurrScene().setRoot(Container.getRoot(WELCOME_PAGE));

            else if(event.getSource().equals(profileBox.getChildren().get(IMAGE_USER.getIndex())))
                this.manageUserProfile();

            else if(event.getSource().equals(searchFields.getChildren().get(SEARCH_BUTTON.getIndex())))
                this.manageSearch(searchBar);

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getToolBar().getItems().get(NEW_GROUP_BUTTON.getIndex())))
                this.manageGroupCreation();

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getBtnProposeMeeting()))
                FirstMain.getCurrScene().setRoot(Container.getRoot(MEETING_CHOICE_PAGE));

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getBtnDeleteGroup())
                    && HomePage.getHomePageInstance(null).getListViewGroups().getFocusModel().getFocusedItem() != null)
                this.manageGroupDeletion();

            else if(event.getSource().equals(HomePage.getHomePageInstance(null).getBtnLinkRequests())
                    && HomePage.getHomePageInstance(null).getListViewGroups().getFocusModel().getFocusedItem() != null){
                this.manageLinkRequests();
            }

            else if(!event.getSource().equals(HomePage.getHomePageInstance(null).getBtnDeleteGroup())
                    && !event.getSource().equals(HomePage.getHomePageInstance(null).getBtnLinkRequests())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "work in progress...");
                alert.showAndWait();
            }

        }

    }

    /*-------------------------------------- INNER-CLASS --------------------------------------*/
    public static class ListViewHandler<T extends MouseEvent> implements EventHandler<T>{

        ///////////////////////////////////////////////////
        private static ListViewHandler<MouseEvent> handler;
        ///////////////////////////////////////////////////

        ///////////////////////////
        private ListViewHandler(){}
        ///////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void handle(T event) {
            if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
                    && HomePage.getHomePageInstance(null).getListViewGroups().getSelectionModel().getSelectedItem() != null){

                HomePage homePage = HomePage.getHomePageInstance(null);
                ListView<HBox> listViewGroups = homePage.getListViewGroups();
                HBox hBoxSelectedItem = listViewGroups.getSelectionModel().getSelectedItem();
                Circle circleGroupImageSelected = (Circle) hBoxSelectedItem.getChildren().get(GROUP_IMAGE.getIndex());
                Paint paintImageGroup = circleGroupImageSelected.getFill();
                homePage.getCircleGroupFocused().setFill(paintImageGroup);

            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////
        public static ListViewHandler<MouseEvent> getListViewHandler(){
            if(handler == null)
                handler = new ListViewHandler<>();
            return handler;
        }
        ////////////////////////////////////////////////////////////////

    }
    /*-------------------------------------------------------------------------------------------*/

    /*----------------------------------------- INNER-CLASS -----------------------------------------*/
    public static class GoogleMapsChangeListener<T extends Worker.State> implements ChangeListener<T>{
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            if (newValue == Worker.State.SUCCEEDED)
                HomePage.getLabelGoogleMapState().setText("Google Maps");
            else
                HomePage.getLabelGoogleMapState().setText("Loading...");
        }
    }
    /*-----------------------------------------------------------------------------------------------*/

    ///////////////////////////////////////////////////////////////////////////////
    private void manageUserProfile(){
        UserProfileDialog dialog = UserProfileDialog.getUserProfileDialogInstance();
        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().isEmpty())
            this.changeUserImageProfile(result.get());
    }
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void changeUserImageProfile(Map<String, String> result){
        VBox homePageProfileBox = (VBox) HomePage.getHomePageInstance(null)
                .getToolBar().getItems().get(PROFILE_BOX.getIndex());
        Circle circleHomePageImageUser = (Circle) homePageProfileBox.getChildren().get(IMAGE_USER.getIndex());

        if(result.get(IMG) != null && !result.get(IMG).equals(UPLOAD_PHOTO))
            circleHomePageImageUser.setFill(new ImagePattern(new Image(FILE + result.get(IMG))));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private void manageGroupCreation(){
        GroupCreationDialog dialog = GroupCreationDialog.getGroupCreationDialogInstance();
        Optional<Map<String, String>> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().isEmpty())
            this.createGroup(result.get());
        dialog.getCircleGroupImg().setFill(new ImagePattern(new Image(UPLOAD_PHOTO)));
        dialog.getLabelGroupImgPath().setText("");
        dialog.getTextFieldGroupName().setText("");
        dialog.getTextFieldGroupNick().setText("");
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void createGroup(Map<String, String> result){

        if(result.get("name") != null && result.get("nickname") != null && result.get(IMG) != null) {

            HomePage homePage = HomePage.getHomePageInstance(null);

            HBox hBoxGroupPreview = new HBox();
            hBoxGroupPreview.getStyleClass().addAll(IMG_CONTAINER);

            Circle circleGroupImage = new Circle(40);
            circleGroupImage.setFill(new ImagePattern(new Image(FILE + result.get(IMG))));

            Label labelGroupName = new Label("Name:    " + result.get("name"));
            Label labelGroupNick = new Label("Nickname:    " + result.get("nickname"));

            VBox vBoxLabelContainer = new VBox(labelGroupName, labelGroupNick);
            vBoxLabelContainer.getStyleClass().addAll(IMG_CONTAINER);

            hBoxGroupPreview.getChildren().addAll(circleGroupImage, vBoxLabelContainer);

            ObservableList<HBox> observableListGroups = homePage.getObservableListGroups();
            observableListGroups.add(hBoxGroupPreview);

        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageGroupDeletion(){
        HomePage homePage = HomePage.getHomePageInstance(null);
        ListView<HBox> listViewGroups = homePage.getListViewGroups();

        HBox hBoxSelectedItem = listViewGroups.getFocusModel().getFocusedItem();
        VBox vBoxGroupDetails = (VBox) hBoxSelectedItem.getChildren().get(GROUP_DETAILS.getIndex());

        Circle circleGroupImage = (Circle) hBoxSelectedItem.getChildren().get(GROUP_IMAGE.getIndex());
        Label labelGroupName = (Label) vBoxGroupDetails.getChildren().get(GROUP_NAME.getIndex());
        Label labelGroupNickname = (Label) vBoxGroupDetails.getChildren().get(GROUP_NICKNAME.getIndex());

        Circle circleGroupImageDouble = new Circle(40, circleGroupImage.getFill());
        Label labelGroupNameDouble = new Label(labelGroupName.getText());
        Label labelGroupNicknameDouble = new Label(labelGroupNickname.getText());

        GroupDeletionDialog groupDeletionDialog = new GroupDeletionDialog(circleGroupImageDouble, labelGroupNameDouble, labelGroupNicknameDouble);
        Optional<Map<String, String>> result = groupDeletionDialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty())
            this.removeGroup(homePage, listViewGroups, hBoxSelectedItem);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private void removeGroup(HomePage homePage, ListView<HBox> listViewGroups, HBox hBoxSelectedItem){
        listViewGroups.getItems().remove(hBoxSelectedItem);
        homePage.getCircleGroupFocused().setFill(new ImagePattern(new Image(CREATE_GROUP)));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private void manageLinkRequests(){
        LinkRequestsDialog dialog = LinkRequestsDialog.getLinkRequestsDialogInstance();
        dialog.showAndWait();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void manageSearch(HBox searchBar){
        GridPane radioButtonsContainer = (GridPane) searchBar.getChildren().get(RADIO_BUTTONS_CONTAINER.getIndex());
        RadioButton radioButton = (RadioButton) radioButtonsContainer.getChildren().get(RADIO_BUTTON_GROUPS.getIndex());

        RadioButton selectedToggle = (RadioButton) radioButton.getToggleGroup().getSelectedToggle();
        if(selectedToggle.getText().equals("Groups")){
            SearchGroupsDialog searchGroupsDialog = new SearchGroupsDialog("Groups result", "Groups");
            searchGroupsDialog.showAndWait();
        }
        else if(selectedToggle.getText().equals("Users")){
            SearchUsersDialog searchUsersDialog = new SearchUsersDialog();
            searchUsersDialog.showAndWait();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
