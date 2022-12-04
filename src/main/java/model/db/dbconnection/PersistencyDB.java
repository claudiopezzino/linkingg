package model.db.dbconnection;

import model.db.dbexceptions.DBException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PersistencyDB {

    //////////////////////////////
    private Connection connection;
    //////////////////////////////

    ////////////////////////
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    ///////////////////////////

    ///////////////////////////////////////////////
    private static PersistencyDB singletonInstance;
    ///////////////////////////////////////////////


    /////////////////////////////////////////////
    private PersistencyDB() throws DBException {
        this.fetchDBConfigProperties();
    }
    /////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    private void fetchDBConfigProperties() throws DBException {
        Properties dbConfigProperties = new Properties();
        try (InputStream fileConfigProperties = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            dbConfigProperties.load(fileConfigProperties);
            this.dbUrl = dbConfigProperties.getProperty("dbUrl");
            this.dbUser = dbConfigProperties.getProperty("dbUser");
            this.dbPassword = dbConfigProperties.getProperty("dbPassword");
        }catch (IOException ioException){
            throw new DBException();
        }
    }
    /////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////
    public Connection getConnection() throws DBException {
        if (connection == null)
            try {
                connection = this.openConnection();
            } catch (SQLException sqlException) {
                throw new DBException();
            }
        return connection;
    }
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private Connection openConnection() throws SQLException {
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        return this.connection;
    }
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    public void closeConnection() throws DBException {
        if(this.connection != null)
            try{
                this.connection.close();
                this.connection = null; // due to getConnection returns a closed one
            }catch (SQLException sqlException){
                throw new DBException();
            }
    }
    ////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    public void closePreparedStatement(PreparedStatement preparedStatement) throws DBException {
        try{
            if(preparedStatement != null)
                preparedStatement.close();
        }catch (SQLException sqlException){
            throw new DBException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public void closeResultSet(ResultSet resultSet) throws DBException{
        try{
            if(resultSet != null)
                resultSet.close();
        } catch (SQLException sqlException) {
            throw new DBException();
        }
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    public static PersistencyDB getSingletonInstance() throws DBException{
        if(singletonInstance == null)
            singletonInstance = new PersistencyDB();
        return singletonInstance;
    }
    ///////////////////////////////////////////////////////////////////////

}
