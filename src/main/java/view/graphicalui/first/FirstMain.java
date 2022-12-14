package view.graphicalui.first;

import control.controlexceptions.InternalException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.boundary.UserManageCommunityBoundary;

import static view.controllerui.first.Dialog.errorDialog;
import static view.graphicalui.first.Page.*;


public class FirstMain extends Application {

    ///////////////////////////
    private static Scene scene;
    ///////////////////////////


    ////////////////////////////////////////////////////////////////////
    public static void main(String[] args){
        Application.launch(args);
    }
    ////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////
    @Override
    public void init(){
        setCurrScene(new Scene(Container.getRoot(WELCOME_PAGE)));
        scene.getStylesheets().add("FirstMainFX.css");
    }
    /////////////////////////////////////////////////////////////

    ////////////////////////////////////
    @Override
    public void start(Stage stage) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    ///////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void stop(){
        UserManageCommunityBoundary userManageCommunityBoundary = HomePage.getHandler().getUserManageCommunityBoundary();
        if(userManageCommunityBoundary != null && userManageCommunityBoundary.hasStartedSignIn()) {
            try{
                userManageCommunityBoundary.freeResources();
            }catch(InternalException internalException){
                errorDialog(internalException.getMessage());
            }
        }
        // Otherwise, call freeResources of second boundary
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public static Scene getCurrScene(){
        return scene;
    }
    /////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public static void setCurrScene(Scene currScene){
        scene = currScene;
    }
    ////////////////////////////////////////////////////////////////////////

}
