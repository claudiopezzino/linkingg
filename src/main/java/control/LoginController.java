package control;

import control.controlexceptions.InternalException;
import javafx.util.Pair;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;

import java.util.Map;

public interface LoginController {
    Pair<String, String> registration(UserSignUpBean user) throws InternalException;
    Map<String, Object> access(UserSignInBean user) throws InternalException;
}
