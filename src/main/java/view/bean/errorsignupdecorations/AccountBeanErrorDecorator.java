package view.bean.errorsignupdecorations;

import view.bean.BeanError;
import view.bean.BeanErrorDecorator;


public class AccountBeanErrorDecorator extends BeanErrorDecorator {

    ///////////////////////////////////////////////////////////
    private static final String ACCOUNT_ERROR = "Account \n\n";
    ///////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    public AccountBeanErrorDecorator(BeanError beanError) {
        super(beanError, ACCOUNT_ERROR);
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////
    @Override
    public String displayErrors(){
        String errors = super.displayErrors();
        errors += this.getMessage();
        return errors;
    }
    //////////////////////////////////////////

}
