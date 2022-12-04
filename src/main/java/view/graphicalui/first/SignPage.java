package view.graphicalui.first;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static view.graphicalui.first.constcontainer.Css.*;
import static view.graphicalui.first.constcontainer.Image.*;


public abstract class SignPage extends Parent {

    //////////////////////////
    protected ToolBar toolBar;
    //////////////////////////

    /////////////////////////////
    protected Button btnGoogle;
    protected Button btnFacebook;
    /////////////////////////////


    ////////////////////////////////////////
    protected abstract VBox setUpSignForm();
    ////////////////////////////////////////

    ////////////////////////////////////////////////////
    public ToolBar getToolBar() {
        return this.toolBar;
    }
    ////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    protected HBox setUpGoogleFacebookAccess(){
        HBox hBoxContainer = new HBox();
        hBoxContainer.getStyleClass().add(TOOL_ELEMENTS);

        btnGoogle = new Button("Google", new ImageView(GOOGLE_ACCESS));
        btnGoogle.getStyleClass().add(SIGN_BUTTONS);

        btnFacebook = new Button("Facebook", new ImageView(FACEBOOK_ACCESS));
        btnFacebook.getStyleClass().add(SIGN_BUTTONS);

        hBoxContainer.getChildren().addAll(btnGoogle, new Separator(), btnFacebook);

        return hBoxContainer;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

}
