package view.graphicalui.second;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


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