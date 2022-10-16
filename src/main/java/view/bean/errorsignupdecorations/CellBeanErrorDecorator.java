package view.bean.errorsignupdecorations;

import view.bean.BeanError;
import view.bean.BeanErrorDecorator;


public class CellBeanErrorDecorator extends BeanErrorDecorator {

    /////////////////////////////////////////////////////
    private static final String CELL_ERROR = "Cell \n\n";
    /////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    public CellBeanErrorDecorator(BeanError beanError) {
        super(beanError, CELL_ERROR);
    }
    ////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////
    @Override
    public String displayErrors(){
        String errors = super.displayErrors();
        errors += this.getMessage();
        return errors;
    }
    //////////////////////////////////////////

}
