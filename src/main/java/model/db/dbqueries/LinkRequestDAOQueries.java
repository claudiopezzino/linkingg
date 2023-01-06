package model.db.dbqueries;

import model.LinkRequestFields;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LinkRequestDAOQueries {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String INSERT_LINK_REQUEST = "INSERT INTO requests(users_nickname, groups_nickname) VALUES (?, ?)";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_LINK_REQUEST = "SELECT * FROM requests WHERE users_nickname = ? AND groups_nickname = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_USERS_NICKNAME = "SELECT users_nickname FROM requests WHERE groups_nickname = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String DELETE_LINK_REQUEST = "DELETE FROM requests WHERE users_nickname = ? AND groups_nickname = ?";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////
    private LinkRequestDAOQueries(){}
    /////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insertLinkRequest(PersistencyDB db, Connection connection, Map<String, String> mapLinkRequestInfo) throws DuplicatedRecordException, DBException {
        PreparedStatement preparedStatement = null;
        Map<String, String> mapRequestInfo;
        try{
            preparedStatement = connection.prepareStatement(INSERT_LINK_REQUEST,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String userNick = mapLinkRequestInfo.get(LinkRequestFields.USERS_NICKNAME);
            String groupNick = mapLinkRequestInfo.get(LinkRequestFields.GROUPS_NICKNAME);

            mapRequestInfo = selectLinkRequest(db, connection, userNick, groupNick);
            if (!mapRequestInfo.isEmpty())
                throw new DuplicatedRecordException(LinkRequestFields.GROUPS_NICKNAME);

            preparedStatement.setString(1, userNick);
            preparedStatement.setString(2, groupNick);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, String> selectLinkRequest(PersistencyDB db, Connection connection, String userNick, String groupNick) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_LINK_REQUEST,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, userNick);
            preparedStatement.setString(2, groupNick);
            resultSet = preparedStatement.executeQuery();
            return unpackLinkRequestInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally{
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectUsersNickname(PersistencyDB db, Connection connection, String groupNick) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_USERS_NICKNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, groupNick);
            resultSet = preparedStatement.executeQuery();
            return wrapUsersNick(resultSet);
        }
        catch(SQLException sqlException){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void deleteLinkRequest(PersistencyDB db, Connection connection, String userNick, String groupNick) throws DBException{
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(DELETE_LINK_REQUEST,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, userNick);
            preparedStatement.setString(2, groupNick);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> unpackLinkRequestInfo(ResultSet resultSet) throws SQLException{
        Map<String, String> mapLinkRequestInfo = new HashMap<>();

        if(!resultSet.first())
            return Collections.emptyMap();

        resultSet.first(); // replace cursor

        String userNick = resultSet.getString(LinkRequestFields.USERS_NICKNAME);
        String groupNick = resultSet.getNString(LinkRequestFields.GROUPS_NICKNAME);

        mapLinkRequestInfo.put(LinkRequestFields.USERS_NICKNAME, userNick);
        mapLinkRequestInfo.put(LinkRequestFields.GROUPS_NICKNAME, groupNick);

        return mapLinkRequestInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapUsersNick(ResultSet resultSet) throws SQLException{
        List<String> listUsersNick = new ArrayList<>();

        if(!resultSet.first())
            return Collections.emptyList();

        resultSet.first(); // replace cursor

        do{
            String userNick = resultSet.getString(LinkRequestFields.USERS_NICKNAME);
            listUsersNick.add(userNick);
        }while (resultSet.next());

        return listUsersNick;
    }
    ////////////////////////////////////////////////////////////////////////////////////

}
