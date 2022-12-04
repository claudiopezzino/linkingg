package model;

import model.modelexceptions.NoEntityException;
import model.subjects.User;

import java.util.HashMap;
import java.util.Map;

public final class UserCollector {

    ///////////////////////////////////////////////////////////////////
    private static final Map<String, User> MAP_USERS = new HashMap<>();
    ///////////////////////////////////////////////////////////////////


    /////////////////////////
    private UserCollector(){}
    /////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    public static User getUserInstance(String userNick) throws NoEntityException {
        if(userNick != null) {
            User user = MAP_USERS.get(userNick);
            if (user != null)
                return user;
            throw new NoEntityException();
        }
        throw new NoEntityException();
    }
    ///////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public static void addUserInstance(User user){
        if(user != null) {
            String userNick = user.credentials().getKey();
            MAP_USERS.putIfAbsent(userNick, user);
        }
    }
    //////////////////////////////////////////////////////

}
