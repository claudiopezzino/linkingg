package view.graphicalui.first.toolbardecorations;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import view.graphicalui.first.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.HomePageFields.*;
import static view.graphicalui.first.constcontainer.Image.LENS;


public class SearchBarDecorator extends Decorator{

    ///////////////////////
    private HBox searchBar;
    ///////////////////////

    ///////////////////////////////////////////////
    private final EventHandler<MouseEvent> handler;
    ///////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    public SearchBarDecorator(Toolbar toolbar, EventHandler<MouseEvent> handler) {
        super(toolbar);
        this.handler = handler;
        this.setUpSearchBar();
    }
    ////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////
    private void setUpSearchBar(){

        /*---------------------------------------------------------*/
        TextField textFieldSearch = new TextField();
        textFieldSearch.setPromptText("Name, Nickname or Province");
        /*---------------------------------------------------------*/

        /*---------------------------------------------------*/
        Button btnSearch = new Button();
        btnSearch.setGraphic(new ImageView(LENS));
        btnSearch.getStyleClass().add(BUTTONS_PREVIEW);
        btnSearch.setOnMouseClicked(handler);
        /*---------------------------------------------------*/

        /*---------------------------------------------------------*/
        HBox hBoxSearchComponents = new HBox(textFieldSearch, btnSearch);
        hBoxSearchComponents.getStyleClass().add(HBOX);
        /*---------------------------------------------------------*/

        /*---------------------------------------------------------*/
        List<RadioButton> radioButtonTargetList = new ArrayList<>();
        List<RadioButton> radioButtonFilterList = new ArrayList<>();
        /*---------------------------------------------------------*/

        /*---------------------------------------------------------*/
        GridPane gridPaneRadioButtonContainer = new GridPane();
        gridPaneRadioButtonContainer.getStyleClass().add(HOME_RADIOS);
        /*---------------------------------------------------------*/

        /*----------------------------------------------------------*/
        RadioButton radioButtonGroups = new RadioButton(GROUPS);
        RadioButton radioButtonPeople = new RadioButton(USERS);
        RadioButton radioButtonPages = new RadioButton(PAGES);
        /*----------------------------------------------------------*/

        /*--------------------------------------------------------------*/
        RadioButton radioButtonLocation = new RadioButton(FILTER_PROVINCE);
        RadioButton radioButtonName = new RadioButton(FILTER_NAME);
        RadioButton radioButtonUsername = new RadioButton(FILTER_NICKNAME);
        /*--------------------------------------------------------------*/

        /*------------------------------------------*/
        radioButtonTargetList.add(radioButtonGroups);
        radioButtonTargetList.add(radioButtonPeople);
        radioButtonTargetList.add(radioButtonPages);
        /*------------------------------------------*/

        /*--------------------------------------------*/
        radioButtonFilterList.add(radioButtonLocation);
        radioButtonFilterList.add(radioButtonName);
        radioButtonFilterList.add(radioButtonUsername);
        /*--------------------------------------------*/

        /*-----------------------------------------------------------------------------------*/
        ToggleGroup targetScope = new ToggleGroup();
        radioButtonTargetList.forEach(radioButton -> radioButton.setToggleGroup(targetScope));
        /*-----------------------------------------------------------------------------------*/

        /*-----------------------------------------------------------------------------------*/
        ToggleGroup filterScope = new ToggleGroup();
        radioButtonFilterList.forEach(radioButton -> radioButton.setToggleGroup(filterScope));
        /*-----------------------------------------------------------------------------------*/

        /*----------------------------------*/
        radioButtonPeople.setSelected(true);
        radioButtonUsername.setSelected(true);
        /*----------------------------------*/

        /*-----------------------------------------------------------------------------*/
        for (int i = 0; i < radioButtonTargetList.size(); i++)
            gridPaneRadioButtonContainer.add(radioButtonTargetList.get(i),i,0);
        /*------------------------------------------------------------------------------*/

        /*-------------------------------------------------------------------------------*/
        for (int i = 0; i < radioButtonFilterList.size(); i++)
            gridPaneRadioButtonContainer.add(radioButtonFilterList.get(i), i, 1);
        /*-------------------------------------------------------------------------------*/

        /*---------------------------------------------------------------------*/
        searchBar = new HBox(gridPaneRadioButtonContainer, hBoxSearchComponents);
        searchBar.getStyleClass().addAll(HBOX, IMG_CONTAINER);
        /*---------------------------------------------------------------------*/

    }
    ////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    @Override
    public ToolBar draw() {
        ToolBar toolBar = super.draw();
        toolBar.getItems().addAll(searchBar, new Separator());
        return toolBar;
    }
    ///////////////////////////////////////////////////////////

}
