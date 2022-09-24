package view.graphicalui.first;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import view.controllerui.first.MeetingPageEventHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.LOCATION;


public class MeetingPage extends Parent {

    ////////////////////////////////////////////////
    private static MeetingPage meetingPageInstance;
    ////////////////////////////////////////////////

    ////////////////////////
    private ToolBar toolBar;
    ////////////////////////

    //////////////////////////////////////
    private static boolean toolBarPresent;
    //////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private static final MeetingPageEventHandler<MouseEvent> handler = new MeetingPageEventHandler<>();
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private MeetingPage(Toolbar toolbar){
        this.getChildren().add(this.setUpMeetingPage(toolbar));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////
    private VBox setUpMeetingChoiceArea(){

        Label labelFirstNote = new Label("1. Answer all the questions.\n\n\n");
        Label labelSecondNote = new Label("2. Take a circular zone in the map.");
        Label labelThirdNote = new Label("3. Click the button to start searching.\n\n");
        Label labelFourthNote = new Label("4. Click on a marker as your meeting target.");
        Label labelFifthNote = new Label("5. Click the share button in the toolbar.");


        Button btnOptions = new Button("View best options", new ImageView(LOCATION));
        btnOptions.getStyleClass().add(SIGN_BUTTONS);
        btnOptions.setOnMouseClicked(handler);

        VBox vBoxFirstSetOfNotes = new VBox(labelFirstNote, labelSecondNote);

        VBox vBoxSecondSetOfNotes = new VBox(labelThirdNote, btnOptions);
        vBoxSecondSetOfNotes.getStyleClass().addAll(HBOX);

        HBox hBoxThirdSetOfNotes = new HBox(labelFourthNote, labelFifthNote);
        hBoxThirdSetOfNotes.getStyleClass().addAll(VBOX, HBOX);

        HBox hBoxNotes = new HBox(vBoxFirstSetOfNotes, new Separator(), vBoxSecondSetOfNotes);
        hBoxNotes.getStyleClass().addAll(VBOX, HBOX);

        WebView webViewMap = new WebView();
        WebEngine webEngine = webViewMap.getEngine();
        webEngine.load("https://www.google.it");

        VBox vBoxMeetingChoice = new VBox();
        vBoxMeetingChoice.getChildren().addAll(hBoxNotes, webViewMap, hBoxThirdSetOfNotes);
        vBoxMeetingChoice.getStyleClass().addAll(HBOX, VBOX);

        return vBoxMeetingChoice;

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    private ListView<VBox> setUpQuestionsForm(ObservableList<VBox> contents){
        ListView<VBox> questionsForm = new ListView<>();
        questionsForm.setItems(contents);
        return questionsForm;
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    private List<RadioButton> initRadioButtonList(ToggleGroup toggleGroup, String... strings){
        List<RadioButton> radioButtonList = new ArrayList<>();
        for (String type : strings) {
            RadioButton radioButton = new RadioButton(type);
            radioButton.setToggleGroup(toggleGroup);
            radioButtonList.add(radioButton);
        }
        radioButtonList.get(0).setSelected(true);
        return radioButtonList;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* try to use Formatter class to format user input according to the needs
    * e.g. Date, Time, Number field etc */
    private ObservableList<VBox> setUpQuestionSetContainer(){

        /* with " ToggleGroup.getSelectedToggle() " it will be possible to get the selected RadioButton */

        /*-------------------------------------------------------------------------------------------------------------*/
        VBox vboxFirstQuestion = new VBox();
        vboxFirstQuestion.getStyleClass().add(VBOX);

        Label labelFirstQuestion = new Label("\u2022 Type of meeting");

        VBox vBoxFirstQuestionChoices = new VBox();
        vBoxFirstQuestionChoices.getStyleClass().add(VBOX);

        ToggleGroup toggleGroupFirstQuestionChoices = new ToggleGroup();

        List<RadioButton> radioButtonsTypeOfMeetingChoices = initRadioButtonList(toggleGroupFirstQuestionChoices, "Breakfast", "Lunch", "Dinner", "Relaxation", "Work", "Disco", "Cinema");

        for(RadioButton radioButton : radioButtonsTypeOfMeetingChoices)
            vBoxFirstQuestionChoices.getChildren().add(radioButton);

        vboxFirstQuestion.getChildren().addAll(labelFirstQuestion, vBoxFirstQuestionChoices);
        /*-------------------------------------------------------------------------------------------------------------*/

        /*------------------------------------------------------------------------------------------------------------*/
        VBox vBoxSecondQuestion = new VBox();
        vBoxSecondQuestion.getStyleClass().add(VBOX);

        Label labelSecondQuestion = new Label("\u2022 Type of vehicle");

        VBox vBoxSecondQuestionChoices = new VBox();
        vBoxSecondQuestionChoices.getStyleClass().add(VBOX);

        ToggleGroup toggleGroupSecondQuestionChoices = new ToggleGroup();

        List<RadioButton> radioButtonsTypeOfVehicleChoices = initRadioButtonList(toggleGroupSecondQuestionChoices, "Feet", "Bicycle", "Car", "Bus", "Metro", "Train", "Airplane");

        for(RadioButton radioButton : radioButtonsTypeOfVehicleChoices)
            vBoxSecondQuestionChoices.getChildren().add(radioButton);

        vBoxSecondQuestion.getChildren().addAll(labelSecondQuestion, vBoxSecondQuestionChoices);
        /*-------------------------------------------------------------------------------------------------------------*/

        /*----------------------------------------------------------------------------------*/
        VBox vboxThirdQuestion = new VBox();
        vboxThirdQuestion.getStyleClass().add(VBOX);
        Label labelFourthQuestion = new Label("\u2022 Budget");
        TextField textFieldFourthQuestion = new TextField();
        textFieldFourthQuestion.setPromptText("50..");
        vboxThirdQuestion.getChildren().addAll(labelFourthQuestion, textFieldFourthQuestion);
        /*-----------------------------------------------------------------------------------*/

        /*-------------------------------------------------------------------------------*/
        VBox vboxFourthQuestion = new VBox();
        vboxFourthQuestion.getStyleClass().add(VBOX);
        Label labelFifthQuestion = new Label("\u2022 Time");
        TextField textFieldFifthQuestion = new TextField();
        textFieldFifthQuestion.setPromptText("19:00");
        vboxFourthQuestion.getChildren().addAll(labelFifthQuestion, textFieldFifthQuestion);
        /*--------------------------------------------------------------------------------*/

        /*--------------------------------------------------------------------------------*/
        VBox vBoxFifthQuestion = new VBox();
        vBoxFifthQuestion.getStyleClass().add(VBOX);
        Label labelSixthQuestion = new Label("\u2022 Date");
        DatePicker datePickerSixthQuestion = new DatePicker(LocalDate.now());
        datePickerSixthQuestion.setEditable(false);
        vBoxFifthQuestion.getChildren().addAll(labelSixthQuestion, datePickerSixthQuestion);
        /*---------------------------------------------------------------------------------*/

        /*---------------------------------------------------------------------------------------------------------------*/
        ObservableList<VBox> questions = FXCollections.observableArrayList();
        questions.addAll(vboxFirstQuestion, vBoxSecondQuestion, vboxThirdQuestion, vboxFourthQuestion, vBoxFifthQuestion);
        /*---------------------------------------------------------------------------------------------------------------*/

        return questions;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private VBox setUpMeetingPage(Toolbar toolbar){

        this.toolBar = toolbar.draw();

        VBox vBoxChoiceArea = new VBox();
        vBoxChoiceArea.getStyleClass().add(HBOX);
        vBoxChoiceArea.getChildren().addAll(setUpQuestionsForm(setUpQuestionSetContainer()), setUpMeetingChoiceArea());

        VBox vBoxMeetingPageRoot = new VBox();
        vBoxMeetingPageRoot.getChildren()
                .addAll(this.toolBar, vBoxChoiceArea);
        vBoxMeetingPageRoot.getStyleClass().add(VBOX);

        return vBoxMeetingPageRoot;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////////////////////////////////
    public static MeetingPageEventHandler<MouseEvent> getHandler(){
        return handler;
    }
    //////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////
    public static MeetingPage getMeetingPageInstance(Toolbar toolbar){
        if(meetingPageInstance == null && toolbar != null)
            meetingPageInstance = new MeetingPage(toolbar);
        toolBarPresent = true;
        return meetingPageInstance;
    }
    ///////////////////////////////////////////////////////////////////

}
