package model.dao;

import control.controlexceptions.InternalException;
import model.User;
import model.db.dbconnection.PersistencyDB;
import model.db.dbexceptions.DBException;
import model.db.dbqueries.UserDAOQueries;
import model.db.dbexceptions.DuplicatedRecordException;
import model.modelexceptions.DuplicatedEntityException;

import java.sql.Connection;
import java.util.Map;

import static model.UserFields.NICKNAME;

public class UserDAO implements BaseDAO{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Object createEntity(Map<String, String> creationInfo) throws DuplicatedEntityException, InternalException {
        this.saveUserIntoDB(creationInfo);
        return new User(creationInfo);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void saveUserIntoDB(Map<String, String> userInfo) throws DuplicatedEntityException, InternalException {
        try{
            PersistencyDB db = PersistencyDB.getSingletonInstance();
            Connection connection = db.getConnection();
            UserDAOQueries.insertUser(connection, userInfo);
            db.closeConnection();
        }
        catch (DuplicatedRecordException e) {
            throw new DuplicatedEntityException(NICKNAME, userInfo.get(NICKNAME));
        }
        catch(DBException dbException){
            throw new InternalException(dbException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
