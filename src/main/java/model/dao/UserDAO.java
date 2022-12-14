package model.dao;

import control.controlexceptions.InternalException;
import model.*;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbqueries.UserDAOQueries;
import model.db.dbexceptions.DuplicatedRecordException;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.UserFields.*;

public class UserDAO implements BaseDAO{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void createEntity(Map<String, String> mapCreationInfo) throws DuplicatedEntityException, InternalException {
        // try to save new user into db if nickname doesn't exist yet
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            UserDAOQueries.insertUser(db, connection, mapCreationInfo);
            db.closeConnection();
        }
        catch (DuplicatedRecordException e) {
            throw new DuplicatedEntityException(NICKNAME, mapCreationInfo.get(NICKNAME));
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by "filter.get" method is-a-kind-of desired Class */
    @Override
    public <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException, NoEntityException{

        User user;

        Map<String, String> mapUserInfo = new HashMap<>();
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if (type == Filter.CREDENTIALS)
                mapUserInfo = UserDAOQueries.selectUserByNickAndPass(db, connection,
                        (String) filter.get(UserFields.NICKNAME),
                        (String) filter.get(UserFields.PASSWORD));

            else if(type == Filter.GROUP_OWNER || type == Filter.MEETING_OWNER)
                mapUserInfo = UserDAOQueries.selectUserByNickname(db, connection, (String) filter.get(UserFields.NICKNAME));

            db.closeConnection(); // evaluate if remain it opened because it will be needed after User fetch by credentials

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        user = this.makeUserEntity(mapUserInfo);

        return user;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Map<String, Object> readEntities(Map<String, V> filter, Filter type) throws InternalException {
        Map<String, Object> mapUsers;

        List<String> listUsersInfo = new ArrayList<>();

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.GROUP_NICKNAME)
                listUsersInfo = UserDAOQueries.selectUsersIntoGroup(db, connection,
                        (String) filter.get(GroupFields.NICKNAME));

            else if(type == Filter.MEETING_ID)
                listUsersInfo = UserDAOQueries.selectUsersIntoMeeting(db, connection,
                        (String) filter.get(MeetingFields.ID));

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        mapUsers =  makeUserEntities(listUsersInfo);

        return mapUsers;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException {
        // TO DO
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////
    private User makeUserEntity(Map<String, String> mapUserInfo){
        String userNick = mapUserInfo.get(UserFields.NICKNAME);

        User user;
        try{
            user = UserCollector.getUserInstance(userNick);
        }catch (NoEntityException noEntityException){
            user = new User(mapUserInfo);
            UserCollector.addUserInstance(user);
        }
        return user;
    }
    ///////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    private Map<String, Object> makeUserEntities(List<String> listUsersInfo){
        Map<String, Object> mapUsers = new HashMap<>();

        Map<String, String> mapUserInfo = new HashMap<>();
        String[] userTokens;
        User user;
        for(String userInfo : listUsersInfo) {
            userTokens = userInfo.split("-");

            mapUserInfo.put(UserFields.NICKNAME, userTokens[0]);
            mapUserInfo.put(UserFields.PASSWORD, userTokens[1]);
            mapUserInfo.put(UserFields.NAME, userTokens[2]);
            mapUserInfo.put(UserFields.SURNAME, userTokens[3]);
            mapUserInfo.put(UserFields.ADDRESS, userTokens[4]);
            mapUserInfo.put(UserFields.MAIL, userTokens[5]);
            mapUserInfo.put(UserFields.CELL, userTokens[6]);
            mapUserInfo.put(UserFields.ACCOUNT, userTokens[7]);
            mapUserInfo.put(UserFields.IMAGE, userTokens[8]);

            user = this.makeUserEntity(mapUserInfo);

            mapUsers.put(mapUserInfo.get(UserFields.NICKNAME), user);
        }
        return mapUsers;
    }
    /////////////////////////////////////////////////////////////////////////

}
