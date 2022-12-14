package model.db.dbqueries;

import model.ImageProfile;
import model.MeetingFields;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.MeetingFields.*;

public class MeetingDAOQueries {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_MEETINGS_INTO_GROUP =
            "SELECT * FROM meetings JOIN meetings_into_groups ON id = meetings_id WHERE groups_nickname = ?";
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SELECT_GALLERY =
            "SELECT meetings_gallery.image FROM meetings JOIN meetings_gallery ON meetings.id = meetings_gallery.meeting WHERE meeting = ?";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////
    private MeetingDAOQueries(){}
    /////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectMeetingsIntoGroup(PersistencyDB db, Connection connection, String groupNick) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_MEETINGS_INTO_GROUP,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, groupNick);
            resultSet = preparedStatement.executeQuery();

            return wrapMeetingInfo(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> selectGallery(PersistencyDB db, Connection connection, int meetingID) throws DBException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(SELECT_GALLERY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, meetingID);
            resultSet = preparedStatement.executeQuery();

            return wrapGallery(resultSet);
        }catch(SQLException sqlException){
            throw new DBException();
        }finally {
            db.closePreparedStatement(preparedStatement);
            db.closeResultSet(resultSet);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapGallery(ResultSet resultSet) throws SQLException, DBException {
        List<String> listGallery = new ArrayList<>();

        Blob blob;
        String imgPath;
        int num = 1;
        while(resultSet.next()){
            blob = resultSet.getBlob(MeetingFields.IMAGE);
            imgPath = ImageProfile.fromBlobToString(Integer.toString(resultSet.getInt(ID)), blob, "m"+num+"_");
            listGallery.add(imgPath);
            ++num;
        }
        return listGallery;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<String> wrapMeetingInfo(ResultSet resultSet) throws SQLException, DBException {

        if(!resultSet.first())
            return Collections.emptyList();

        List<String> listMeetingInfo = new ArrayList<>();

        String details;
        String separator = "-";

        Blob blob;
        String imgPath;

        resultSet.first(); // replace cursor
        do {
            blob = resultSet.getBlob(MeetingFields.IMAGE);
            imgPath = ImageProfile.fromBlobToString(Integer.toString(resultSet.getInt(MeetingFields.ID)), blob, "m_");

            details = resultSet.getInt(MeetingFields.ID) + separator + resultSet.getString(MeetingFields.NAME) + separator
                    + resultSet.getString(MeetingFields.SCHEDULER) + separator + resultSet.getInt(MeetingFields.RATING) + separator
                    + resultSet.getDate(MeetingFields.DATE) + separator + resultSet.getTime(MeetingFields.TIME) + separator + imgPath;

            listMeetingInfo.add(details);
        }while(resultSet.next());

        return listMeetingInfo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
