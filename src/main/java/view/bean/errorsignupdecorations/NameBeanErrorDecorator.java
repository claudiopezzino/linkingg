package view.bean.errorsignupdecorations;

import view.bean.BeanError;
import view.bean.BeanErrorDecorator;

public class NameBeanErrorDecorator extends BeanErrorDecorator {

    /////////////////////////////////////////////////////
    private static final String NAME_ERROR = "Name \n\n";
    /////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    public NameBeanErrorDecorator(BeanError beanError) {
        super(beanError, NAME_ERROR);
    }
    /////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////
    @Override
    public String displayErrors(){
        String errors = super.displayErrors();
        errors += this.getMessage();
        return errors;
    }
    //////////////////////////////////////////

}
