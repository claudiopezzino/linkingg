package model.db.dbqueries;

import model.ImageProfile;
import model.UserFields;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;
import model.modelexceptions.NoEntityException;

import java.sql.*;
import java.util.*;

import static model.ImageProfile.fromBlobToString;
import static model.UserFields.*;

public class UserDAOQueries {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String INSERT_USER =
            "INSERT INTO users(nickname, password, name, surname, address, mail, cell, account, image) VALUES (?,?,?,?,?,?,?,?,?)";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USER_BY_NICKNAME = "SELECT * FROM users WHERE nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USER_BY_NICK_AND_PASS = "SELECT * FROM users WHERE nickname = ? and password = ?";
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USERS_INTO_GROUP =
            "SELECT * FROM users JOIN users_into_groups ON nickname = users_nickname WHERE groups_nickname = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USERS_INTO_MEETING =
            "SELECT * FROM users JOIN users_into_meetings ON nickname = users_nickname WHERE meetings_id = ?";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////
    private UserDAOQueries(){}
    //////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insertUser(PersistencyDB db, Connection connection, Map<String, String> userInfo) throws DuplicatedRecordException, DBException {
        PreparedStatement preparedStatement = null;
        Map<String, String> mapUserInfo;
        try{
            preparedStatement = connection.prepareStatement(INSERT_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            mapUserInfo = selectUserByNickname(db, connection, userInfo.get(NICKNAME));
            if(!mapUserInfo.isEmpty()) // user already exists
                throw new DuplicatedRecordException(NICKNAME);
            initInsertStatement(preparedStatement, userInfo);
            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, String> selectUserByNickAndPass(PersistencyDB db, Connection connection, String userNick, String userPass) throws DBException, NoEntityException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_NICK_AND_PASS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, userNick);
            preparedStatement.setString(2, userPass);

            resultSet =  preparedStatement.executeQuery();
            if(!resultSet.first())
                throw new NoEntityException();

            resultSet.first(); // replace cursor
            return unpackUserInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectUsersIntoGroup(PersistencyDB db, Connection connection, String groupNick) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USERS_INTO_GROUP,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, groupNick);
            resultSet = preparedStatement.executeQuery();

            return wrapListUsersInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectUsersIntoMeeting(PersistencyDB db, Connection connection, String meetingID) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USERS_INTO_MEETING,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, Integer.parseInt(meetingID));
            resultSet = preparedStatement.executeQuery();

            return wrapListUsersInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapListUsersInfo(ResultSet resultSet) throws SQLException, DBException{
        List<String> listUsersInfo = new ArrayList<>();

        String details;
        String separator = "-";

        if(!resultSet.first())
            return Collections.emptyList();

        Blob blob;
        String imgPath;

        resultSet.first();
        do{
            blob = resultSet.getBlob(UserFields.IMAGE);
            imgPath = fromBlobToString(resultSet.getString(UserFields.NICKNAME), blob, "u_");

            details = resultSet.getString(UserFields.NICKNAME) + separator + resultSet.getString(UserFields.PASSWORD) + separator
                    + resultSet.getString(UserFields.NAME) + separator + resultSet.getString(UserFields.SURNAME) + separator
                    + resultSet.getString(UserFields.ADDRESS) + separator + resultSet.getString(UserFields.MAIL) + separator
                    + resultSet.getString(UserFields.CELL) + separator + resultSet.getString(UserFields.ACCOUNT) + separator
                    + imgPath;

            listUsersInfo.add(details);
        }while(resultSet.next());

        return listUsersInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, String> selectUserByNickname(PersistencyDB db, Connection connection, String nickname) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_NICKNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();
            return unpackUserInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally{
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void initInsertStatement(PreparedStatement preparedStatement, Map<String, String> userInfo) throws SQLException {
        preparedStatement.setString(1, userInfo.get(NICKNAME));
        preparedStatement.setString(2, userInfo.get(PASSWORD));
        preparedStatement.setString(3, userInfo.get(NAME));
        preparedStatement.setString(4, userInfo.get(SURNAME));
        preparedStatement.setString(5, userInfo.get(ADDRESS));
        preparedStatement.setString(6, userInfo.get(MAIL));
        preparedStatement.setString(7, userInfo.get(CELL));
        preparedStatement.setString(8, userInfo.get(ACCOUNT));
        preparedStatement.setBinaryStream(9, null);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> unpackUserInfo(ResultSet resultSet) throws SQLException, DBException {
        Map<String, String> userInfo = new HashMap<>();

        if(!resultSet.first())
            return Collections.emptyMap();

        resultSet.first(); // replace cursor

        String nickname = resultSet.getString(UserFields.NICKNAME);
        String password = resultSet.getString(UserFields.PASSWORD);
        String name = resultSet.getString(UserFields.NAME);
        String surname = resultSet.getString(UserFields.SURNAME);
        String address = resultSet.getString(UserFields.ADDRESS);
        String mail = resultSet.getString(UserFields.MAIL);
        String cell = resultSet.getString(UserFields.CELL);
        String account = resultSet.getString(UserFields.ACCOUNT);
        String image = ImageProfile.fromBlobToString(nickname, resultSet.getBlob(UserFields.IMAGE), "u_");

        userInfo.put(UserFields.NICKNAME, nickname);
        userInfo.put(UserFields.PASSWORD, password);
        userInfo.put(UserFields.NAME, name);
        userInfo.put(UserFields.SURNAME, surname);
        userInfo.put(UserFields.ADDRESS, address);
        userInfo.put(UserFields.MAIL, mail);
        userInfo.put(UserFields.CELL, cell);
        userInfo.put(UserFields.ACCOUNT, account);
        userInfo.put(UserFields.IMAGE, image);

        return userInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
}
