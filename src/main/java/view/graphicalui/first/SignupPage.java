package view.graphicalui.first;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import view.controllerui.first.SignupPageEventHandler;

import java.util.Collections;
import java.util.Map;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.ALERT;


public class SignupPage extends SignPage {

    /////////////////////////////////////////////
    private static SignupPage signupPageInstance;
    /////////////////////////////////////////////

    //////////////////////////////////////
    private static boolean toolBarPresent;
    //////////////////////////////////////

    /////////////////////////
    private Button btnSignUp;
    /////////////////////////

    ////////////////////////////////////
    private TextField textFieldName;
    private TextField textFieldSurname;
    private TextField textFieldAddress;
    private TextField textFieldMail;
    private TextField textFieldPhone;
    ///////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private static final SignupPageEventHandler<MouseEvent> handler1 = new SignupPageEventHandler<>();
    /////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////
    private SignupPage(Toolbar toolbar){
        this.getChildren().add(this.setUpSignupPage(toolbar));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    @Override
    protected VBox setUpSignForm() {
        VBox vBoxFormContainer = new VBox();
        vBoxFormContainer.getStyleClass().addAll(VBOX, HBOX);

        Label label = new Label("OR");

        GridPane gridPaneForm = new GridPane();

        btnSignUp = new Button("Sign Up");
        btnSignUp.getStyleClass().add(SIGN_BUTTONS);
        btnSignUp.setOnMouseClicked(handler1);

        gridPaneForm.getStyleClass().add(FORM);

        Label labelName = new Label("Name");
        Label labelSurname = new Label("Surname");
        Label labelAddress = new Label("Address");
        Label labelMail = new Label("E-mail"); //regex
        Label labelPhone = new Label("Cell"); //regex
        Label labelAccount = new Label("Account");


        textFieldName = new TextField();
        textFieldName.setPromptText("Will");

        textFieldSurname = new TextField();
        textFieldSurname.setPromptText("Smith");

        textFieldAddress = new TextField();
        textFieldAddress.setPromptText("address, nr, city, province");

        textFieldMail = new TextField();
        textFieldMail.setPromptText("willsmith@gmail.com");

        textFieldPhone = new TextField();
        textFieldPhone.setPromptText("3334669981");



        ToggleGroup toggleGroupAccount = new ToggleGroup();
        RadioButton radioButtonPremium = new RadioButton("Premium");
        RadioButton radioButtonStandard = new RadioButton("Standard");
        RadioButton radioButtonMerchant = new RadioButton("Business owner");
        radioButtonStandard.setSelected(true);
        radioButtonPremium.setToggleGroup(toggleGroupAccount);
        radioButtonStandard.setToggleGroup(toggleGroupAccount);
        radioButtonMerchant.setToggleGroup(toggleGroupAccount);

        radioButtonMerchant.setDisable(true); // TO DELETE UPON CAPABILITY IMPLEMENTATION
        radioButtonPremium.setDisable(true); // TO DELETE UPON CAPABILITY IMPLEMENTATION

        gridPaneForm.add(labelName, 0, 0);
        gridPaneForm.add(labelSurname, 0, 1);
        gridPaneForm.add(labelAddress, 0, 2);
        gridPaneForm.add(labelMail, 0, 3);
        gridPaneForm.add(labelPhone, 0, 4);
        gridPaneForm.add(labelAccount, 0, 5);

        gridPaneForm.add(btnSignUp, 6,7);

        gridPaneForm.add(textFieldName, 1, 0, 6,1);
        gridPaneForm.add(textFieldSurname, 1, 1, 6, 1);
        gridPaneForm.add(textFieldAddress,1, 2, 6, 1);
        gridPaneForm.add(textFieldMail, 1, 3, 6, 1);
        gridPaneForm.add(textFieldPhone, 1, 4, 6, 1);
        gridPaneForm.add(radioButtonMerchant, 2, 5);
        gridPaneForm.add(radioButtonPremium, 4, 5);
        gridPaneForm.add(radioButtonStandard, 6, 5);

        vBoxFormContainer.getChildren().addAll(this.setUpGoogleFacebookAccess(), label, gridPaneForm);

        btnGoogle.setOnMouseClicked(handler1);
        btnFacebook.setOnMouseClicked(handler1);

        return vBoxFormContainer;

    }
    ///////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////
    private VBox setUpSignupPage(Toolbar toolbar){
        this.toolBar = toolbar.draw();
        VBox signupRoot = new VBox();
        signupRoot.getChildren().addAll(this.toolBar, this.setUpSignForm());
        signupRoot.getStyleClass().add(VBOX);
        return signupRoot;
    }
    /////////////////////////////////////////////////////////////////////////

    /*--------------------- INNER CLASS ---------------------*/

    public static class CredentialDialog extends PageDialog{

        /////////////////////////////////////////////////////////
        private static CredentialDialog credentialDialogInstance;
        /////////////////////////////////////////////////////////

        ////////////////////////////
        private Label labelNickname;
        private Label labelPassword;
        ////////////////////////////


        //////////////////////////////////////////////////////////////////////////////////////////////
        private CredentialDialog(){
            super("Credentials","Personal credentials", ALERT);
            this.getDialogPane().setContent(this.setUpPopUpRoot());
            this.setResultConverter(this::credentialResult);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////
        private Map<String, String> credentialResult(ButtonType buttonType){
            if(buttonType == this.getBtnTypeSave()) {
                this.map.put("nickname", labelNickname.getText());
                this.map.put("password", labelPassword.getText());
                return this.map;
            }
            return Collections.emptyMap();
        }
        /////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        protected VBox setUpPopUpRoot() {

            VBox vBoxPopUpRoot = new VBox();
            vBoxPopUpRoot.getStyleClass().addAll(VBOX, HBOX);

            Label labelInstructions = new Label("Please, safeguard the following credentials:");

            labelNickname = new Label();
            labelPassword = new Label();

            Label labelReminder = new Label("In case of loss you will need to redo the registration phase!");

            VBox vBoxUserPassContainer = new VBox(labelNickname, labelPassword);
            vBoxUserPassContainer.getStyleClass().add(HBOX);

            vBoxPopUpRoot.getChildren().addAll(labelInstructions, vBoxUserPassContainer, labelReminder);

            return vBoxPopUpRoot;
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        public Label getLabelNickname() {
            return this.labelNickname;
        }
        //////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        public Label getLabelPassword() {
            return this.labelPassword;
        }
        //////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////
        public static CredentialDialog getCredentialDialogInstance() {
            if(credentialDialogInstance == null)
                credentialDialogInstance = new CredentialDialog();
            return credentialDialogInstance;
        }
        //////////////////////////////////////////////////////////////

    }
    /*-------------------------------------------------------*/


    ///////////////////////////////////////////////////////
    public Button getBtnSignUp(){
        return this.btnSignUp;
    }
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public TextField getTextFieldName(){
        return this.textFieldName;
    }
    ///////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public TextField getTextFieldSurname(){
        return this.textFieldSurname;
    }
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public TextField getTextFieldAddress() {
        return this.textFieldAddress;
    }
    ////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    public TextField getTextFieldMail() {
        return this.textFieldMail;
    }
    //////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    public TextField getTextFieldPhone(){
        return this.textFieldPhone;
    }
    ////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////
    public static boolean isToolBarPresent(){
        return toolBarPresent;
    }
    ////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    public static SignupPageEventHandler<MouseEvent> getHandler(){
        return handler1;
    }
    //////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    public static SignupPage getSignupPageInstance(Toolbar toolbar){
        if(signupPageInstance == null && toolbar != null)
            signupPageInstance = new SignupPage(toolbar);
        toolBarPresent = true;
        return signupPageInstance;
    }
    /////////////////////////////////////////////////////////////////

}
