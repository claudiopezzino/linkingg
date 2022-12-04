package model.db.dbqueries;

import model.GroupFields;
import model.UserInfo;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.ImageProfile.*;

public class GroupDAOQueries {

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_OWNED_BY_USER = "SELECT * FROM crowd WHERE owner = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GROUPS_NOT_OWNED_BY_USER =
            "SELECT * FROM crowd JOIN users_into_groups ON nickname = groups_nickname WHERE users_nickname = ?";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////
    private GroupDAOQueries(){}
    ///////////////////////////


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
            System.out.println("Code: " + sqlException.getErrorCode() + "<--->" + "Message: " + sqlException.getMessage());
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
    private static List<String> wrapGroupsInfo(ResultSet resultSet) throws SQLException, DBException {

        if(!resultSet.first())
            return Collections.emptyList();

        List<String> listGroupsInfo = new ArrayList<>();

        String details;
        String separator = "-";

        Blob blob;
        String imgPath;
        while(resultSet.next()) {

            blob = resultSet.getBlob(GroupFields.IMAGE);
            imgPath = fromBlobToString(resultSet.getString(GroupFields.NICKNAME), blob, "g_");

            details = resultSet.getString(GroupFields.NICKNAME) + separator + resultSet.getString(GroupFields.NAME) +
                    separator + imgPath + separator + resultSet.getString(GroupFields.OWNER);

            listGroupsInfo.add(details);
        }

        return listGroupsInfo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
