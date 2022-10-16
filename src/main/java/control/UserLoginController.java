package control;

import control.controlexceptions.InternalException;
import javafx.util.Pair;
import model.User;
import model.dao.BaseDAO;
import model.dao.FactoryDAO;
import model.modelexceptions.DuplicatedEntityException;
import view.bean.UserSignUpBean;

import java.util.HashMap;
import java.util.Map;

import static model.UserFields.*;
import static model.dao.DAO.USER_DAO;

public class UserLoginController implements LoginController{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Pair<String, String> registration(UserSignUpBean userSignUpBean) throws InternalException {
        Pair<String, String> userCredentials = makeUserCredentials(userSignUpBean.getName(), userSignUpBean.getSurname());

        Map<String, String> userInfo = this.fetchUserInfo(userSignUpBean);
        userInfo.put(NICKNAME, userCredentials.getKey());
        userInfo.put(PASSWORD, userCredentials.getValue());

        User newUser = this.registerUser(userInfo);

        return newUser.credentials();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    private Map<String, String> fetchUserInfo(UserSignUpBean userSignUpBean){
        Map<String, String> userInfo = new HashMap<>();

        userInfo.put(NAME, userSignUpBean.getName());
        userInfo.put(SURNAME, userSignUpBean.getSurname());
        userInfo.put(ADDRESS, userSignUpBean.getAddress());
        userInfo.put(MAIL, userSignUpBean.getEmail());
        userInfo.put(CELL, userSignUpBean.getCell());
        userInfo.put(ACCOUNT, userSignUpBean.getAccount());

        return userInfo;
    }
    //////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    private Pair<String, String> makeUserCredentials(String name, String surname){
        String nickname = name.toLowerCase()+surname.toLowerCase()+"_0";
        String password = "passwordFor_"+nickname;

        return new Pair<>(nickname, password);
    }
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    private User registerUser(Map<String, String> userInfo) throws InternalException {

        boolean nickExists = true;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        int numCopy = 1;
        User user = null;
        while (nickExists){
            try{
                user = (User) baseDAO.createEntity(userInfo);
                nickExists = false;
            }
            catch(DuplicatedEntityException e){
                this.changeUserCredentials(userInfo, numCopy);
                numCopy++;
            }
        }
        return user;
    }
    ///////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    private void changeUserCredentials(Map<String, String> userInfo, int numCopy){
        String[] nickTokens = userInfo.get(NICKNAME).split("_");
        String[] passTokens = userInfo.get(PASSWORD).split("_");

        userInfo.replace(NICKNAME, nickTokens[0]+"_"+numCopy);
        userInfo.replace(PASSWORD, passTokens[0]+"_"+userInfo.get(NICKNAME));
    }
    ////////////////////////////////////////////////////////////////////////////////

}
