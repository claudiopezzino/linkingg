package model.dao;

import control.controlexceptions.InternalException;

import java.sql.Connection;
import model.Filter;
import model.GroupFields;
import model.LinkRequestFields;
import model.UserFields;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;
import model.db.dbqueries.LinkRequestDAOQueries;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.LinkRequest;
import model.subjects.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.dao.DAO.USER_DAO;

public class LinkRequestDAO implements BaseDAO{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void createEntity(Map<String, String> creationInfo) throws DuplicatedEntityException, InternalException {
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            LinkRequestDAOQueries.insertLinkRequest(db, connection, creationInfo);
            db.closeConnection();
        }
        catch(DuplicatedRecordException duplicatedRecordException){
            throw new DuplicatedEntityException(LinkRequestFields.GROUPS_NICKNAME,
                    creationInfo.get(LinkRequestFields.GROUPS_NICKNAME));
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException, NoEntityException {
        LinkRequest linkRequest = null;

        Map<String, String> mapSourceDetails;

        Map<String, String> mapLinkRequestInfo;
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.NOTHING) {
                mapLinkRequestInfo = LinkRequestDAOQueries.selectLinkRequest(db, connection,
                        (String) filter.get(LinkRequestFields.USERS_NICKNAME),
                        (String) filter.get(LinkRequestFields.GROUPS_NICKNAME));

                if(mapLinkRequestInfo.isEmpty())
                    throw new NoEntityException();

                mapSourceDetails = this.fetchSourceDetails(mapLinkRequestInfo.get(LinkRequestFields.USERS_NICKNAME));

                linkRequest = new LinkRequest(mapLinkRequestInfo.get(LinkRequestFields.USERS_NICKNAME),
                        mapLinkRequestInfo.get(LinkRequestFields.GROUPS_NICKNAME), mapSourceDetails);
            }

            db.closeConnection();
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        return linkRequest;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Map<String, Object> readEntities(Map<String, V> filter, Filter type) throws InternalException, NoEntityException {
        Map<String, Object> mapLinkRequests = new HashMap<>();

        List<String> listUsersNick;
        Map<String, String> mapUserInfo;
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.GROUP_NICKNAME){
                listUsersNick = LinkRequestDAOQueries.selectUsersNickname(db, connection, (String) filter.get(GroupFields.NICKNAME));

                if(listUsersNick.isEmpty())
                    throw new NoEntityException(); // GroupDAO caller must init LinkRequest map object with an empty one

                String groupNick = (String) filter.get(GroupFields.NICKNAME);
                for (String userNick : listUsersNick){
                    mapUserInfo = this.fetchSourceDetails(userNick);
                    mapLinkRequests.put(userNick, new LinkRequest(userNick, groupNick, mapUserInfo));
                }
            }

            db.closeConnection();
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        return mapLinkRequests;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException {

        try {
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if (type == Filter.NOTHING)
                LinkRequestDAOQueries.deleteLinkRequest(db, connection,
                        (String) filter.get(LinkRequestFields.USERS_NICKNAME),
                        (String) filter.get(LinkRequestFields.GROUPS_NICKNAME));

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, String> fetchSourceDetails(String userNick) throws InternalException {
        Map<String, String> mapUserDetails = new HashMap<>();

        Map<String, String> mapSourceInfo = new HashMap<>();
        mapSourceInfo.put(UserFields.NICKNAME, userNick);

        User user;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);
        try{
            user = (User) baseDAO.readEntity(mapSourceInfo, Filter.LINK_REQUEST_SOURCE);

            mapUserDetails.put(UserFields.IMAGE, user.imageProfile());
            mapUserDetails.put(UserFields.NAME, user.fullName().getKey());
            mapUserDetails.put(UserFields.SURNAME, user.fullName().getValue());

            return mapUserDetails;
        }catch (NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////
}
