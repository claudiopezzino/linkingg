package view.graphicalui.first;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.controllerui.first.WelcomePageEventHandler;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.*;


public class WelcomePage extends Parent {

    ///////////////////////////////////////////////
    private static WelcomePage welcomePageInstance;
    ///////////////////////////////////////////////

    ////////////////////////
    private ToolBar toolBar;
    ////////////////////////

    //////////////////////////////////////
    private static boolean toolBarPresent;
    //////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private static final WelcomePageEventHandler<MouseEvent> handler = new WelcomePageEventHandler<>();
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private WelcomePage(Toolbar toolbar){
        this.getChildren().add(this.setUpWelcomePage(toolbar));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////
    private HBox setUpSystemPreview(){

        HBox hBoxPreview = new HBox();
        HBox hBoxInfo = new HBox();

        VBox vBoxFirstImageSet = new VBox();
        VBox vBoxSecondImageSet = new VBox();

        ImageView imageViewInfo = new ImageView(OVERVIEW_INSTRUCTIONS);

        /*---------------------------------------------------*/
        Button btnPeople = new Button();
        btnPeople.setGraphic(new ImageView(PEOPLE));
        btnPeople.setOnMouseClicked(handler);
        btnPeople.getStyleClass().add(BUTTONS_PREVIEW);
        /*---------------------------------------------------*/

        /*-------------------------------------------------------*/
        Button btnMeetings = new Button();
        btnMeetings.setGraphic(new ImageView(APPOINTMENT));
        btnMeetings.setOnMouseClicked(handler);
        btnMeetings.getStyleClass().add(BUTTONS_PREVIEW);
        /*-------------------------------------------------------*/

        /*--------------------------------------------------------*/
        Button btnFriends = new Button();
        btnFriends.setGraphic(new ImageView(FRIENDSHIP));
        btnFriends.setOnMouseClicked(handler);
        btnFriends.getStyleClass().add(BUTTONS_PREVIEW);
        /*--------------------------------------------------------*/

        /*---------------------------------------------------*/
        Button btnGroups = new Button();
        btnGroups.setGraphic(new ImageView(MULTI_GROUPS));
        btnGroups.setOnMouseClicked(handler);
        btnGroups.getStyleClass().add(BUTTONS_PREVIEW);
        /*----------------------------------------------------*/

        hBoxInfo.getChildren().add(imageViewInfo);

        vBoxFirstImageSet.getChildren().addAll(btnPeople, btnMeetings);
        vBoxSecondImageSet.getChildren().addAll(btnFriends, btnGroups);

        hBoxPreview.getChildren().addAll(vBoxFirstImageSet, hBoxInfo, vBoxSecondImageSet);

        hBoxInfo.getStyleClass().add(HBOX);
        hBoxPreview.getStyleClass().add(HBOX);

        vBoxFirstImageSet.getStyleClass().add(IMG_CONTAINER);
        vBoxSecondImageSet.getStyleClass().add(IMG_CONTAINER);

        return hBoxPreview;
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////
    private VBox setUpWelcomePage(Toolbar toolbar){
        this.toolBar = toolbar.draw();
        VBox vBoxWelcomePageRoot = new VBox();
        vBoxWelcomePageRoot.getChildren().addAll(this.toolBar, setUpSystemPreview());
        vBoxWelcomePageRoot.getStyleClass().add(VBOX);
        return vBoxWelcomePageRoot;
    }
    ///////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    public static WelcomePageEventHandler<MouseEvent> getHandler(){
        return handler;
    }
    //////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public ToolBar getToolBar(){
        return this.toolBar;
    }
    ////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public static boolean isToolBarPresent(){
        return toolBarPresent;
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public static WelcomePage getWelcomePageInstance(Toolbar toolbar){
        if(welcomePageInstance == null && toolbar != null)
            welcomePageInstance = new WelcomePage(toolbar);
        toolBarPresent = true;
        return welcomePageInstance;
    }
    ///////////////////////////////////////////////////////////////////

}
