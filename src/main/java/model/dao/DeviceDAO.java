package model.dao;

import control.controlexceptions.InternalException;

import java.sql.Connection;
import java.util.*;

import model.*;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbqueries.DeviceDAOQueries;


public class DeviceDAO implements BaseDAO{

    /////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void createEntity(Map<String, String> creationInfo) throws InternalException {
        try {
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            DeviceDAOQueries.insertDevice(db, connection, creationInfo);
            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Object readEntity(Map<String, V> filter, Filter type) throws InternalException {

        Device device;

        Map<String, String> mapDeviceInfo = new HashMap<>();

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.IP_AND_PORT)
                mapDeviceInfo = DeviceDAOQueries.selectSingleDevice(db, connection,
                        (String) filter.get(DeviceFields.IP), Integer.parseInt((String) filter.get(DeviceFields.PORT)));

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        device = new Device(mapDeviceInfo.get(DeviceFields.IP), Integer.parseInt(mapDeviceInfo.get(DeviceFields.PORT)));

        return device;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> Map<String, Object> readEntities(Map<String, V> filter, Filter type) throws InternalException {
        Map<String, Object> mapDevices;

        List<String> listDevicesInfo = new ArrayList<>();

        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();

            if(type == Filter.USER_NICKNAME)
                listDevicesInfo = DeviceDAOQueries.selectCurrUserDevices(db, connection,
                        (String) filter.get(UserFields.NICKNAME), Integer.parseInt((String) filter.get(DeviceFields.PORT)));

            else if (type == Filter.GROUP_OWNER)
                listDevicesInfo = DeviceDAOQueries.selectGroupOwnerDevices(db, connection, (String) filter.get(GroupFields.OWNER));

            db.closeConnection();

        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }

        mapDevices = this.makeDevicesEntities(listDevicesInfo);

        return mapDevices;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <V> void updateEntity(Map<String, V> filter, Filter type) throws InternalException {
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            if(type == Filter.IP_AND_PORT) {
                DeviceDAOQueries.deleteDevice(db, connection,
                        (String) filter.get(DeviceFields.IP), (String) filter.get(DeviceFields.PORT));
            }
            db.closeConnection();
        }catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    private Map<String, Object> makeDevicesEntities(List<String> listDevicesInfo){
        Map<String, Object> mapDevices = new HashMap<>();

        String ip;
        int port;

        int num = 1;

        String[] deviceTokens;
        for(String deviceInfo : listDevicesInfo) {
            deviceTokens = deviceInfo.split("-");

            ip = deviceTokens[0];
            port = Integer.parseInt(deviceTokens[1]);

            mapDevices.put(UserInfo.DEVICE+num, new Device(ip, port));
            num++;
        }

        return mapDevices;
    }
    //////////////////////////////////////////////////////////////////////////////

}
