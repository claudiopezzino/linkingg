package model.dao;

import control.controlexceptions.InternalException;

import java.sql.Connection;
import java.util.*;

import control.controlutilities.CopyException;
import model.*;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbqueries.GroupDAOQueries;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.Group;
import model.subjects.Meeting;
import model.subjects.User;

import static model.dao.DAO.MEETING_DAO;
import static model.dao.DAO.USER_DAO;

public class GroupDAO implements BaseDAO{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Object createEntity(Map<String, String> creationInfo) throws DuplicatedEntityException, InternalException {
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException, NoEntityException {
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Map<String, Object> readEntities(Map<String, V> filter, Filter type) throws InternalException, NoEntityException {
        Map<String, Object> mapGroups = new HashMap<>();

        List<String> listGroupsInfo; // collect all groups information like a chain (info1-info2-info3-info4 : 1 group)
        List<String> listSupport;

        Map<String, String> mapGroupInfo;

        Map<String, User> mapGroupMembers;
        Map<String, Meeting> mapGroupMeetings;

        User groupOwner;

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.USER_NICKNAME) {
                listSupport = GroupDAOQueries.selectGroups(db, connection, (String) filter.get(UserFields.NICKNAME), UserInfo.GROUP_OWNER);

                listGroupsInfo = new ArrayList<>(listSupport);

                listSupport = GroupDAOQueries.selectGroups(db, connection, (String) filter.get(UserFields.NICKNAME), UserInfo.GROUP_MEMBER);

                listGroupsInfo.addAll(listSupport);

                for (String groupInfo : listGroupsInfo) {

                    mapGroupInfo = this.unpackGroupInfo(groupInfo);

                    mapGroupMembers = this.fetchGroupMembers(mapGroupInfo.get(GroupFields.NICKNAME));
                    mapGroupMeetings = this.fetchGroupMeetings(mapGroupInfo.get(GroupFields.NICKNAME));

                    groupOwner = this.fetchGroupOwner(mapGroupInfo.get(GroupFields.OWNER));

                    mapGroups.put(mapGroupInfo.get(GroupFields.NICKNAME),
                            new Group(mapGroupInfo, groupOwner, mapGroupMembers, mapGroupMeetings));
                }
            }

            db.closeConnection();

        }catch(DBException | CopyException exception){
            throw new InternalException(exception.getMessage());
        }

        return mapGroups;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException {
        // TO DO
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    private Map<String, String> unpackGroupInfo(String groupInfo){
        Map<String, String> mapGroupInfo = new HashMap<>();

        String[] infos = groupInfo.split("-");

        mapGroupInfo.put(GroupFields.NICKNAME, infos[0]);
        mapGroupInfo.put(GroupFields.NAME, infos[1]);
        mapGroupInfo.put(GroupFields.IMAGE, infos[2]);
        mapGroupInfo.put(GroupFields.OWNER, infos[3]);

        return mapGroupInfo;
    }
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, User> fetchGroupMembers(String groupNick) throws InternalException, NoEntityException {
        Map<String, User> mapUsers = new HashMap<>();

        Map<String, Object> mapObjects = this.fetchGroupInfo(groupNick, USER_DAO);
        for(Map.Entry<String, Object> entry : mapObjects.entrySet())
            mapUsers.put(entry.getKey(), (User) entry.getValue());

        return mapUsers;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, Meeting> fetchGroupMeetings(String groupNick) throws InternalException, NoEntityException {
        Map<String, Meeting> mapMeetings = new HashMap<>();

        Map<String, Object> mapObjects = this.fetchGroupInfo(groupNick, MEETING_DAO);
        for(Map.Entry<String, Object> entry : mapObjects.entrySet())
            mapMeetings.put(entry.getKey(), (Meeting) entry.getValue());

        return mapMeetings;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, Object> fetchGroupInfo(String groupNick, DAO dao) throws InternalException, NoEntityException {
        Map<String, String> objectInfo = new HashMap<>();
        objectInfo.put(GroupFields.NICKNAME, groupNick);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(dao);

        return baseDAO.readEntities(objectInfo, Filter.GROUP_NICKNAME);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private User fetchGroupOwner(String userNick) throws InternalException, NoEntityException {
        Map<String, String> mapGroupInfo = new HashMap<>();
        mapGroupInfo.put(UserFields.NICKNAME, userNick);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        return (User) baseDAO.readEntity(mapGroupInfo, Filter.GROUP_OWNER);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////

}
