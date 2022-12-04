package control.tasks.tasksexceptions;

public class LoaderException extends Exception{

    //////////////////////////////////////////////////////////////////////////////////////////
    private static final String NO_UPDATE = "Update lost... restart the application, please.";
    //////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////
    public LoaderException(){
        super(NO_UPDATE);
    }
    //////////////////////////////////////////////

}
