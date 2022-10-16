package model.db.dbqueries;

import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;

import java.sql.*;
import java.util.Map;

import static model.UserFields.*;

public class UserDAOQueries {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String INSERT_USER = "INSERT INTO users(nickname, password, name, surname, address, mail, cell, account, image) VALUES (?,?,?,?,?,?,?,?,?)";
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USER_BY_NICKNAME = "SELECT * FROM users WHERE nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////
    private UserDAOQueries(){}
    //////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insertUser(Connection connection, Map<String, String> userInfo) throws DuplicatedRecordException, DBException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(INSERT_USER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            boolean resultSetPresent = selectUserByNickname(connection, userInfo.get(NICKNAME));
            if(resultSetPresent)
                throw new DuplicatedRecordException(NICKNAME);
            initInsertStatement(preparedStatement, userInfo);
            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException){
            throw new DBException();
        }finally {
            closeOpenedResources(preparedStatement, null);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static boolean selectUserByNickname(Connection connection, String nickname) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_NICKNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();
            return resultSet.first();
        }catch(SQLException sqlException){
            throw new DBException();
        }finally{
            closeOpenedResources(preparedStatement, resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // needed due to Sonar warning
    private static void closeOpenedResources(PreparedStatement preparedStatement, ResultSet resultSet) throws DBException {
        try{
            if(preparedStatement != null)
                preparedStatement.close();
            if(resultSet != null)
                resultSet.close();
        }catch(SQLException sqlException){
            throw new DBException();
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

}
