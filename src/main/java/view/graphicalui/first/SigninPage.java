package view.graphicalui.first;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import view.controllerui.first.SigninPageEventHandler;

import static view.graphicalui.first.constcontainer.Css.*;


public class SigninPage extends SignPage {

    //////////////////////////////////////////////
    private static SigninPage signinPageInstance;
    //////////////////////////////////////////////

    //////////////////////////////////////
    private static boolean toolBarPresent;
    //////////////////////////////////////

    /////////////////////////
    private Button btnSignIn;
    /////////////////////////

    ////////////////////////////////////
    private TextField textFieldUsername;
    private TextField textFieldPassword;
    ////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private static final SigninPageEventHandler<MouseEvent> handler1 = new SigninPageEventHandler<>();
    /////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////
    private SigninPage(Toolbar toolbar){
        this.getChildren().add(this.setUpSignInPage(toolbar));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    protected VBox setUpSignForm() {
        VBox vBoxFormContainer = new VBox();
        vBoxFormContainer.getStyleClass().addAll(VBOX, HBOX);

        Label label = new Label("OR");

        GridPane gridPaneForm = new GridPane();
        gridPaneForm.getStyleClass().add(FORM);

        Label labelNickname = new Label("Nickname");
        Label labelPassword = new Label("Password");

        textFieldUsername = new TextField();
        textFieldUsername.setPromptText("willsmith...");

        textFieldPassword = new PasswordField();
        textFieldPassword.setPromptText("****************");

        btnSignIn = new Button("Sign In");
        btnSignIn.getStyleClass().add(SIGN_BUTTONS);
        btnSignIn.setOnMouseClicked(handler1);

        gridPaneForm.add(labelNickname, 0, 0);
        gridPaneForm.add(labelPassword, 0, 1);

        gridPaneForm.add(textFieldUsername, 1, 0, 8, 1);
        gridPaneForm.add(textFieldPassword, 1, 1, 8, 1);

        gridPaneForm.add(btnSignIn, 8, 3);

        vBoxFormContainer.getChildren().addAll(this.setUpGoogleFacebookAccess(), label, gridPaneForm);

        btnGoogle.setOnMouseClicked(handler1);
        btnFacebook.setOnMouseClicked(handler1);

        return vBoxFormContainer;

    }
    /////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////
    private VBox setUpSignInPage(Toolbar toolbar){
        this.toolBar = toolbar.draw();
        VBox vBoxSignInRoot = new VBox();
        vBoxSignInRoot.getChildren()
                .addAll(this.toolBar, this.setUpSignForm());
        vBoxSignInRoot.getStyleClass().add(VBOX);
        return vBoxSignInRoot;
    }
    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    public Button getBtnSignIn() {
        return this.btnSignIn;
    }
    /////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public TextField getTextFieldUsername(){
        return this.textFieldUsername;
    }
    //////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public TextField getTextFieldPassword(){
        return this.textFieldPassword;
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public static boolean isToolBarPresent(){
        return toolBarPresent;
    }
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    public static SigninPageEventHandler<MouseEvent> getHandler(){
        return handler1;
    }
    //////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    public static SigninPage getSigninPageInstance(Toolbar toolbar){
        if(signinPageInstance == null && toolbar != null)
            signinPageInstance = new SigninPage(toolbar);
        toolBarPresent = true;
        return signinPageInstance;
    }
    /////////////////////////////////////////////////////////////////

}
