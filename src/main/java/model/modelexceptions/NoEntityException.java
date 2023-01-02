package model.modelexceptions;

public class NoEntityException extends Exception{

    //////////////////////////////////////////////////////////////
    private static final String NO_ENTITY = "Entity unavailable.";
    //////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////
    public NoEntityException(){
        super(NO_ENTITY);
    }
    ////////////////////////////////////////////////
}
