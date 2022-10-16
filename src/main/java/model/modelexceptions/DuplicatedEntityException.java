package model.modelexceptions;

public class DuplicatedEntityException extends Exception{

    ////////////////////////////////////////////////////////////////////////////
    private static final String WARNING = "Following field already exists ->\t";
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DuplicatedEntityException(String field, String value){
        super(WARNING + field + " : " + value);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

}
