package model.dao;

import control.controlexceptions.InternalException;

import java.sql.Connection;
import java.util.*;

import control.controlutilities.CopyException;
import model.*;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;
import model.db.dbqueries.GroupDAOQueries;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.Group;
import model.subjects.Meeting;
import model.subjects.User;

import static model.UserFields.NICKNAME;
import static model.dao.DAO.MEETING_DAO;
import static model.dao.DAO.USER_DAO;


public class GroupDAO implements BaseDAO{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void createEntity(Map<String, String> mapCreationInfo) throws DuplicatedEntityException, InternalException {
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            GroupDAOQueries.insertGroup(db, connection, mapCreationInfo);
            db.closeConnection();
        }catch (DuplicatedRecordException e) {
            throw new DuplicatedEntityException(NICKNAME, mapCreationInfo.get(NICKNAME));
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException {
        Map<String, String> mapGroupInfo = new HashMap<>();
        Group group;
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.GROUP_NICKNAME)
                mapGroupInfo = GroupDAOQueries.selectGroupByNickname(db, connection, (String) filter.get(GroupFields.NICKNAME));

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        group = this.makeGroupEntity(mapGroupInfo, Collections.emptyMap(), Collections.emptyMap());

        return group;
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

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.GROUP_NAME || type == Filter.GROUP_NICKNAME || type == Filter.GROUP_PROVINCE){

                if(type == Filter.GROUP_NAME)
                    listGroupsInfo = GroupDAOQueries.selectGroupsByFilter(db, connection,
                            (String) filter.get(GroupFields.NAME), (String) filter.get(UserFields.NICKNAME), type);

                else if(type == Filter.GROUP_NICKNAME)
                    listGroupsInfo = GroupDAOQueries.selectGroupsByFilter(db, connection,
                            (String) filter.get(UserInfo.GROUP_NICK), (String) filter.get(UserFields.NICKNAME), type);

                else // type == Filter.GROUP_PROVINCE
                    listGroupsInfo = GroupDAOQueries.selectGroupsByFilter(db, connection,
                            (String) filter.get(UserFields.PROVINCE), (String) filter.get(UserFields.NICKNAME), type);

                for (String groupInfo : listGroupsInfo){
                    mapGroupInfo = this.unpackGroupInfo(groupInfo);
                    Group group = this.makeGroupEntity(mapGroupInfo, Collections.emptyMap(), Collections.emptyMap());
                    mapGroups.put(mapGroupInfo.get(GroupFields.NICKNAME), group);
                }
            }

            else if(type == Filter.USER_NICKNAME) {

                listSupport = GroupDAOQueries.selectGroups(db, connection, (String) filter.get(UserFields.NICKNAME), UserInfo.GROUP_OWNER);

                listGroupsInfo = new ArrayList<>(listSupport);

                listSupport = GroupDAOQueries.selectGroups(db, connection, (String) filter.get(UserFields.NICKNAME), UserInfo.GROUP_MEMBER);

                listGroupsInfo.addAll(listSupport);

                // set up a method as UserDAO called "makeGroupEntities"
                for (String groupInfo : listGroupsInfo) {

                    mapGroupInfo = this.unpackGroupInfo(groupInfo);

                    mapGroupMembers = this.fetchGroupMembers(mapGroupInfo.get(GroupFields.NICKNAME));
                    mapGroupMeetings = this.fetchGroupMeetings(mapGroupInfo.get(GroupFields.NICKNAME));

                    Group group = this.makeGroupEntity(mapGroupInfo, mapGroupMembers, mapGroupMeetings);

                    mapGroups.put(mapGroupInfo.get(GroupFields.NICKNAME), group);
                }
            }

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Group makeGroupEntity(Map<String, String> mapGroupDetails, Map<String, User> mapMembers, Map<String, Meeting> mapMeetings) throws InternalException{
        User owner;
        Group group;
        try{
            owner = this.fetchGroupOwner(mapGroupDetails.get(GroupFields.OWNER));
            group = new Group(mapGroupDetails, owner, mapMembers, mapMeetings);
        }catch(NoEntityException | CopyException e){
            throw new InternalException(e.getMessage());
        }
        return group;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
