package model.db.dbqueries;

import model.DeviceFields;
import model.UserFields;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceDAOQueries {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String INSERT_DEVICE = "INSERT INTO devices(ip, port, users_nickname) VALUES (?,?,?)";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_SINGLE_DEVICE = "SELECT * FROM devices WHERE ip = ? AND port = ?";
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_CURR_USER_DEVICES = "SELECT * FROM devices WHERE port != ? AND users_nickname = ?";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USER_DEVICES = "SELECT * FROM devices WHERE users_nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String DELETE_SINGLE_DEVICE = "DELETE FROM devices WHERE ip = ? AND port = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////
    private DeviceDAOQueries(){}
    ////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insertDevice(PersistencyDB db, Connection connection, Map<String, String> mapDeviceInfo) throws DBException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(INSERT_DEVICE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, mapDeviceInfo.get(DeviceFields.IP));
            preparedStatement.setInt(2, Integer.parseInt(mapDeviceInfo.get(DeviceFields.PORT)));
            preparedStatement.setString(3, mapDeviceInfo.get(UserFields.NICKNAME));

            preparedStatement.executeUpdate();

        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, String> selectSingleDevice(PersistencyDB db, Connection connection, String ip, int port) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement(SELECT_SINGLE_DEVICE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, ip);
            preparedStatement.setInt(2, port);

            resultSet = preparedStatement.executeQuery();
            return unpackDeviceInfo(resultSet);

        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectCurrUserDevices(PersistencyDB db, Connection connection, String userNick, int port) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement(SELECT_CURR_USER_DEVICES,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, port);
            preparedStatement.setString(2, userNick);

            resultSet = preparedStatement.executeQuery();

            return wrapListDevicesInfo(resultSet);

        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectUserDevices(PersistencyDB db, Connection connection, String groupOwnerNick) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USER_DEVICES,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, groupOwnerNick);
            resultSet = preparedStatement.executeQuery();

            return wrapListDevicesInfo(resultSet);
        }
        catch(SQLException sqlException){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void deleteDevice(PersistencyDB db, Connection connection, String ip, String port) throws DBException{
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(DELETE_SINGLE_DEVICE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, ip);
            preparedStatement.setInt(2, Integer.parseInt(port));

            preparedStatement.executeUpdate();
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> unpackDeviceInfo(ResultSet resultSet) throws SQLException{
        resultSet.first(); // needed

        Map<String, String> mapDeviceInfo = new HashMap<>();

        String ipAddress = resultSet.getString(DeviceFields.IP);
        int portNumber = resultSet.getInt(DeviceFields.PORT);

        mapDeviceInfo.put(DeviceFields.IP, ipAddress);
        mapDeviceInfo.put(DeviceFields.PORT, String.valueOf(portNumber));

        return mapDeviceInfo;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapListDevicesInfo(ResultSet resultSet) throws SQLException{
        List<String> listDevicesInfo = new ArrayList<>();

        String details;
        String separator = "-";

        while(resultSet.next()){
            details = resultSet.getString(DeviceFields.IP) + separator + resultSet.getInt(DeviceFields.PORT);
            listDevicesInfo.add(details);
        }

        return listDevicesInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

}
