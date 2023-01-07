import control.UserLoginController;
import control.controlexceptions.InternalException;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import view.bean.BeanError;
import view.bean.UserSignUpBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestUserLoginController {

    /////////////////////////////////////////////////////////////////////////////////////////
    // Test if registration phase return (nickname,password) pair
    @Test
    void testRegistration() throws InternalException, BeanError {
        boolean done = false;

        String nickname = "namesurname_0";
        String password = "passwordFor_namesurname_0";

        UserSignUpBean userSignUpBean = new UserSignUpBean();
        userSignUpBean.setName("name");
        userSignUpBean.setSurname("surname");
        userSignUpBean.setAddress("Via Tomaso,79,Pisa,Pisa");
        userSignUpBean.setEmail("namesurname@gmail.com");
        userSignUpBean.setCell("3496574890");
        userSignUpBean.setAccount("Standard");

        UserLoginController userLoginController = new UserLoginController();
        Pair<String, String> credentials = userLoginController.registration(userSignUpBean);

        if(credentials.getKey().equals(nickname) && credentials.getValue().equals(password))
            done = true;

        assertTrue(done);
    }
    /////////////////////////////////////////////////////////////////////////////////////////

}
