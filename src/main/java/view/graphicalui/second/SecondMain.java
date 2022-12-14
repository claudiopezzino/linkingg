package view.graphicalui.second;

import control.controlexceptions.InternalException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.boundary.UserManageCommunityBoundary;

import static view.controllerui.second.Message.infoErrorMsg;


public class SecondMain extends Application {

    ///////////////////////////
    private static Scene scene;
    ///////////////////////////


    /////////////////////////////////
    @Override
    public void start(Stage stage) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    public static void main(String[] args){
        Application.launch(args);
    }
    /////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void stop(){
        UserManageCommunityBoundary userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();
        if(userManageCommunityBoundary != null && userManageCommunityBoundary.hasStartedSignIn()) {
            try {
                userManageCommunityBoundary.freeResources();
            } catch (InternalException internalException) {
                infoErrorMsg(internalException.getMessage());
            }
        }
        // Otherwise, call freeResources of second boundary
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    @Override
    public void init(){
        setCurrScene(new Scene(Welcome.getWelcomeInstance()));
        scene.getStylesheets().add("SecondMainFX.css");
    }
    ///////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public static void setCurrScene(Scene currScene){
        scene = currScene;
    }
    ////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public static Scene getCurrScene(){
        return scene;
    }
    /////////////////////////////////////////////////////
}
