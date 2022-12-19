package model.db.dbqueries;

import model.Filter;
import model.GroupFields;
import model.ImageProfile;
import model.UserInfo;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbexceptions.DuplicatedRecordException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static model.Filter.*;
import static model.ImageProfile.*;


public class GroupDAOQueries {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_OWNED_BY_USER = "SELECT * FROM crowd WHERE owner = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NOT_OWNED_BY_USER =
            "SELECT * FROM crowd JOIN users_into_groups ON nickname = groups_nickname WHERE users_nickname = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String INSERT_GROUP = "INSERT INTO crowd(nickname, name, image, owner) VALUES (?,?,?,?)";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUP_BY_NICKNAME = "SELECT * FROM crowd WHERE nickname = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NAME_OWNED_BY_USER = "SELECT name FROM crowd WHERE owner = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NAME_NOT_OWNED_BY_USER =
            "SELECT name FROM crowd JOIN users_into_groups ON nickname = groups_nickname WHERE users_nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_BY_NAME = "SELECT * FROM crowd WHERE name LIKE ? AND name NOT IN " +
            "(" + SELECT_GROUPS_NAME_OWNED_BY_USER + ") AND name NOT IN (" + SELECT_GROUPS_NAME_NOT_OWNED_BY_USER + ")";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NICKNAME_OWNED_BY_USER = "SELECT nickname FROM crowd WHERE owner = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NICKNAME_NOT_OWNED_BY_USER =
            "SELECT nickname FROM crowd JOIN users_into_groups ON nickname = groups_nickname WHERE users_nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_BY_NICKNAME = "SELECT * FROM crowd WHERE nickname LIKE ? AND nickname NOT IN " +
            "(" + SELECT_GROUPS_NICKNAME_OWNED_BY_USER + ") AND nickname NOT IN (" + SELECT_GROUPS_NICKNAME_NOT_OWNED_BY_USER + ")";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_BY_PROVINCE =
            "SELECT * FROM crowd JOIN users ON owner = users.nickname WHERE province LIKE ? AND " +
                    "crowd.nickname NOT IN (" + SELECT_GROUPS_NICKNAME_OWNED_BY_USER +") AND " +
                    "crowd.nickname NOT IN (" + SELECT_GROUPS_NICKNAME_NOT_OWNED_BY_USER + ")";
    ////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////
    private GroupDAOQueries(){}
    ///////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insertGroup(PersistencyDB db, Connection connection, Map<String, String> groupInfo) throws DuplicatedRecordException, DBException{
        PreparedStatement preparedStatement = null;
        Map<String, String> mapGroupInfo;

        try(FileInputStream is = ImageProfile.fromStringToInputStream(groupInfo.get(GroupFields.IMAGE))){
            preparedStatement = connection.prepareStatement(INSERT_GROUP,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            mapGroupInfo = selectGroupByNickname(db, connection, groupInfo.get(GroupFields.NICKNAME));
            if(!mapGroupInfo.isEmpty())
                throw new DuplicatedRecordException(GroupFields.NICKNAME);

            preparedStatement.setString(1, groupInfo.get(GroupFields.NICKNAME));
            preparedStatement.setString(2, groupInfo.get(GroupFields.NAME));
            preparedStatement.setBinaryStream(3, is, ImageProfile.imageSize(groupInfo.get(GroupFields.IMAGE)));
            preparedStatement.setString(4, groupInfo.get(GroupFields.OWNER));

            preparedStatement.executeUpdate();
        }
        catch(SQLException | IOException exception){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, String> selectGroupByNickname(PersistencyDB db, Connection connection, String nickname) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_GROUP_BY_NICKNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, nickname);
            resultSet = preparedStatement.executeQuery();
            return unpackGroupInfo(resultSet);
        }
        catch(SQLException sqlException){
            throw new DBException();
        }finally{
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectGroups(PersistencyDB db, Connection connection, String userNick, String membership) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<String> listGroupsInfo = new ArrayList<>();
        try{

            if(membership.equals(UserInfo.GROUP_OWNER)) {
                preparedStatement = connection.prepareStatement(SELECT_GROUPS_OWNED_BY_USER,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                preparedStatement.setString(1, userNick);
                resultSet = preparedStatement.executeQuery();
                listGroupsInfo.addAll(wrapGroupsInfo(resultSet));
            }

            else if(membership.equals(UserInfo.GROUP_MEMBER)) {
                preparedStatement = connection.prepareStatement(SELECT_GROUPS_NOT_OWNED_BY_USER,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                preparedStatement.setString(1, userNick);
                resultSet = preparedStatement.executeQuery();
                listGroupsInfo.addAll(wrapGroupsInfo(resultSet));
            }

        }
        catch(SQLException sqlException){
            throw new DBException();
        }
        finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }

        return listGroupsInfo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectGroupsByFilter(PersistencyDB db, Connection connection, String filterName, String userNick, Filter filter) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            if(filter == GROUP_NAME)
                preparedStatement = connection.prepareStatement(SELECT_GROUPS_BY_NAME,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            else if(filter == GROUP_NICKNAME)
                preparedStatement = connection.prepareStatement(SELECT_GROUPS_BY_NICKNAME,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            else // filter == GROUP_PROVINCE
                preparedStatement = connection.prepareStatement(SELECT_GROUPS_BY_PROVINCE,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


            initSelectGroupsByFilterStatement(preparedStatement, userNick, filterName);
            resultSet = preparedStatement.executeQuery();
            return wrapGroupsInfo(resultSet);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapGroupsInfo(ResultSet resultSet) throws SQLException, DBException {

        List<String> listGroupsInfo = new ArrayList<>();

        if(!resultSet.first())
            return Collections.emptyList();

        String details;
        String separator = "-";

        Blob blob;
        String imgPath;

        resultSet.first(); // replace cursor
        do {

            blob = resultSet.getBlob(GroupFields.IMAGE);
            imgPath = fromBlobToString(resultSet.getString(GroupFields.NICKNAME), blob, "g_");

            details = resultSet.getString(GroupFields.NICKNAME) + separator + resultSet.getString(GroupFields.NAME) +
                    separator + imgPath + separator + resultSet.getString(GroupFields.OWNER);

            listGroupsInfo.add(details);
        }while(resultSet.next());

        return listGroupsInfo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static Map<String, String> unpackGroupInfo(ResultSet resultSet) throws SQLException, DBException{
        Map<String, String> mapGroupInfo = new HashMap<>();

        if(!resultSet.first())
            return Collections.emptyMap();

        resultSet.first(); // replace cursor

        String nickname = resultSet.getString(GroupFields.NICKNAME);
        String name = resultSet.getString(GroupFields.NAME);
        String owner = resultSet.getString(GroupFields.OWNER);
        String image = ImageProfile.fromBlobToString(nickname, resultSet.getBlob(GroupFields.IMAGE), "g_");

        mapGroupInfo.put(GroupFields.NICKNAME, nickname);
        mapGroupInfo.put(GroupFields.NAME, name);
        mapGroupInfo.put(GroupFields.IMAGE, image);
        mapGroupInfo.put(GroupFields.OWNER, owner);

        return mapGroupInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void initSelectGroupsByFilterStatement(PreparedStatement preparedStatement, String userNick, String filterName) throws SQLException{
        preparedStatement.setString(1, "%" + filterName + "%");
        preparedStatement.setString(2, userNick);
        preparedStatement.setString(3, userNick);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
