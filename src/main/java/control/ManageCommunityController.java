package control;

import control.controlexceptions.InternalException;
import javafx.util.Pair;
import view.bean.BeanError;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;

public class ManageCommunityController {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserSignInBean setUpRegistrationPhase(LoginController loginController, UserSignUpBean userSignUpBean) throws InternalException {
        Pair<String, String> userCredentials = loginController.registration(userSignUpBean);

        UserSignInBean userSignInBean = new UserSignInBean();

        try{
            userSignInBean.setNickname(userCredentials.getKey());
        }catch(BeanError beanError){
            throw new InternalException(beanError.displayErrors());
        }

        try{
            userSignInBean.setPassword(userCredentials.getValue());
        }catch(BeanError beanError){
            throw new InternalException(beanError.displayErrors());
        }

        return userSignInBean;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
