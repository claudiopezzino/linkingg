import control.UserLoginController;
import control.controlexceptions.InternalException;
import javafx.util.Pair;
import model.subjects.User;
import org.junit.jupiter.api.Test;
import view.bean.BeanError;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;

import java.util.Map;

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

    /////////////////////////////////////////////////////////////////////////////////////////
    // Test if user exists and num of their groups are the right ones
    @Test
    void testAccess() throws InternalException, BeanError {
        boolean access = false;

        String nickname = "francescoesposito_0";
        String password = "passwordFor_francescoesposito_0";

        int numCurrentGroups = 0;
        int numExpectedGroups = 6;

        boolean userExist = false;

        UserSignInBean userSignInBean = new UserSignInBean();
        userSignInBean.setNickname(nickname);
        userSignInBean.setPassword(password);

        UserLoginController userLoginController = new UserLoginController();
        Map<String, Object> mapObjects = userLoginController.access(userSignInBean);

        for (Map.Entry<String, Object> object : mapObjects.entrySet()){
            if(object.getValue() instanceof User)
                userExist = true;
            else
                numCurrentGroups++;
        }

        if(userExist && numCurrentGroups == numExpectedGroups)
            access = true;

        assertTrue(access);
    }
    /////////////////////////////////////////////////////////////////////////////////////////

}
