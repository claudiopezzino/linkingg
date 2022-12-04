package view.bean;

import view.bean.observers.UserBean;

import java.util.HashMap;
import java.util.Map;

public final class UserBeanCollector {

    ///////////////////////////////////////////////////////////////////////////
    private static final Map<String, UserBean> MAP_USER_BEAN = new HashMap<>();
    ///////////////////////////////////////////////////////////////////////////


    /////////////////////////////
    private UserBeanCollector(){}
    /////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////
    public static UserBean getUserBeanInstance(String userNick) throws NoUserBeanException {
        if(userNick != null){
            UserBean userBean = MAP_USER_BEAN.get(userNick);
            if(userBean != null)
                return userBean;
            throw new NoUserBeanException();
        }
        throw new NoUserBeanException();
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    public static void addUserBeanInstance(UserBean userBean){
        if(userBean != null){
            String userBeanNick = userBean.getNickname();
            MAP_USER_BEAN.putIfAbsent(userBeanNick, userBean);
        }
    }
    ////////////////////////////////////////////////////////////

}
