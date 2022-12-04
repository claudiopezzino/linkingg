package model.dao;

import control.controlexceptions.InternalException;

import java.sql.Connection;

import model.*;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbqueries.MeetingDAOQueries;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.Meeting;
import model.subjects.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.dao.DAO.USER_DAO;


public class MeetingDAO implements BaseDAO{

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
        Map<String, Object> mapMeetings = new HashMap<>();

        List<String> listMeetingsInfo;

        Map<String, String> mapMeetingInfo;
        List<String> listPhotos;
        User scheduler;
        Map<String, User> mapJoiners;

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.GROUP_NICKNAME) {
                listMeetingsInfo = MeetingDAOQueries.selectMeetingsIntoGroup(db, connection, (String) filter.get(GroupFields.NICKNAME));
                for(String meetingInfo : listMeetingsInfo){
                    mapMeetingInfo = this.unpackMeetingInfo(meetingInfo);
                    listPhotos = MeetingDAOQueries.selectGallery(db, connection, Integer.parseInt(mapMeetingInfo.get(MeetingFields.ID)));
                    scheduler = this.fetchScheduler(mapMeetingInfo.get(MeetingFields.SCHEDULER));
                    mapJoiners = this.fetchMeetingJoiners(mapMeetingInfo.get(MeetingFields.ID));

                    // Meeting instance respect
                    mapMeetings.put(mapMeetingInfo.get(MeetingFields.ID), new Meeting(mapMeetingInfo, listPhotos, scheduler, mapJoiners));
                }
            }

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
        return mapMeetings;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException {
        // TO DO
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    private Map<String, String> unpackMeetingInfo(String meetingInfo){
        Map<String, String> mapMeetingInfo = new HashMap<>();

        String[] infos = meetingInfo.split("-");

        mapMeetingInfo.put(MeetingFields.ID, infos[0]);
        mapMeetingInfo.put(MeetingFields.NAME, infos[1]);
        mapMeetingInfo.put(MeetingFields.SCHEDULER, infos[2]);
        mapMeetingInfo.put(MeetingFields.RATING, infos[3]);
        mapMeetingInfo.put(MeetingFields.DATE, infos[4]);
        mapMeetingInfo.put(MeetingFields.TIME, infos[5]);
        mapMeetingInfo.put(MeetingFields.IMAGE, infos[6]);

        return mapMeetingInfo;

    }
    //////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    private User fetchScheduler(String userNick) throws NoEntityException, InternalException {
        Map<String, String> mapSchedulerInfo = new HashMap<>();
        mapSchedulerInfo.put(UserFields.NICKNAME, userNick);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        return (User) baseDAO.readEntity(mapSchedulerInfo, Filter.MEETING_OWNER);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, User> fetchMeetingJoiners(String meetingID) throws NoEntityException, InternalException {
        Map<String, User> mapJoiners = new HashMap<>();
        Map<String, Object> mapObjects;

        Map<String, String> mapMeetingInfo = new HashMap<>();
        mapMeetingInfo.put(MeetingFields.ID, meetingID);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        mapObjects = baseDAO.readEntities(mapMeetingInfo, Filter.MEETING_ID);
        for (Map.Entry<String, Object> entry : mapObjects.entrySet())
            mapJoiners.put(entry.getKey(), (User) entry);

        return mapJoiners;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
