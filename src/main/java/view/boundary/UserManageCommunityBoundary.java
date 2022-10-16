package view.boundary;

import control.ManageCommunityController;
import control.UserLoginController;
import control.controlexceptions.InternalException;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;

// LIKE A FACADE PATTERN - SET OF API
public class UserManageCommunityBoundary {

    public UserSignInBean registerIntoSystem(UserSignUpBean userInfo) throws InternalException {
        ManageCommunityController manageCommunityController = new ManageCommunityController();
        return manageCommunityController.setUpRegistrationPhase(new UserLoginController(), userInfo);
    }
}
