package control;

import control.controlexceptions.InternalException;
import javafx.util.Pair;
import view.bean.UserSignUpBean;

public interface LoginController {
    Pair<String, String> registration(UserSignUpBean user) throws InternalException;
}
