package view.bean;

public abstract class BeanError extends Exception {

    ///////////////////////////////////////////////////
    protected BeanError(String error){
        super(error);
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////
    public abstract String displayErrors();
    ///////////////////////////////////////

}