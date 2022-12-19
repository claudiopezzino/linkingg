package control;

import control.controlexceptions.InternalException;

import java.util.*;

import javafx.util.Pair;
import model.*;
import model.dao.BaseDAO;
import model.dao.FactoryDAO;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.User;
import view.bean.UserSignInBean;
import view.bean.UserSignUpBean;

import static model.Filter.CREDENTIALS;
import static model.UserFields.*;
import static model.dao.DAO.GROUP_DAO;
import static model.dao.DAO.USER_DAO;


public class UserLoginController implements LoginController{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Pair<String, String> registration(UserSignUpBean userSignUpBean) throws InternalException {
        Pair<String, String> userCredentials = makeUserCredentials(userSignUpBean.getName(), userSignUpBean.getSurname());

        Map<String, String> userInfo = this.fetchUserInfo(userSignUpBean);
        userInfo.put(NICKNAME, userCredentials.getKey());
        userInfo.put(PASSWORD, userCredentials.getValue());

        this.registerUser(userInfo);

        return userCredentials;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Map<String, Object> access(UserSignInBean user) throws InternalException {
        Map<String, Object> mapPageElements = new HashMap<>();

        User owner = this.fetchUserByCredentials(user.getNickname(), user.getPassword());
        mapPageElements.put(GroupFields.OWNER, owner); // doesn't matter key because not necessary for Controller

        Map<String, Object> mapObjects = this.fetchGroupsByUserNickname(owner.credentials().getKey());
        mapPageElements.putAll(mapObjects);

        return mapPageElements;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, Object> fetchGroupsByUserNickname(String nickname) throws InternalException {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(UserFields.NICKNAME, nickname);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(GROUP_DAO);

        try {
            return baseDAO.readEntities(userInfo, Filter.USER_NICKNAME);
        }catch(NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by "readEntity" method is-a-kind-of desired Class */
    private User fetchUserByCredentials(String nickname, String password) throws InternalException{
        Map<String, String> credentials = new HashMap<>();
        credentials.put(UserFields.NICKNAME, nickname);
        credentials.put(UserFields.PASSWORD, password);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);
        try {
            return (User) baseDAO.readEntity(credentials, CREDENTIALS);
        }catch(NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    private Map<String, String> fetchUserInfo(UserSignUpBean userSignUpBean){
        Map<String, String> userInfo = new HashMap<>();

        userInfo.put(NAME, userSignUpBean.getName());
        userInfo.put(SURNAME, userSignUpBean.getSurname());
        userInfo.put(ADDRESS, userSignUpBean.getAddress());
        userInfo.put(MAIL, userSignUpBean.getEmail());
        userInfo.put(CELL, userSignUpBean.getCell());
        userInfo.put(ACCOUNT, userSignUpBean.getAccount());
        userInfo.put(PROVINCE, userSignUpBean.getProvince());

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
    private void registerUser(Map<String, String> userInfo) throws InternalException {

        boolean nickExists = true;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        int numCopy = 1;
        while (nickExists){
            try{
                baseDAO.createEntity(userInfo);
                nickExists = false;
            }
            catch(DuplicatedEntityException e){
                this.changeUserCredentials(userInfo, numCopy);
                numCopy++;
            }
        }
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
