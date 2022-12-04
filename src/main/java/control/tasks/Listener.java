package control.tasks;

import control.controlexceptions.InternalException;
import control.tasks.tasksexceptions.ListenerException;
import model.Filter;
import model.subjects.Group;
import model.subjects.User;
import model.UserFields;
import model.dao.BaseDAO;
import model.dao.FactoryDAO;
import view.AlertTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import static model.dao.DAO.USER_DAO;


public class Listener extends Thread {

    //////////////////////////////////////////
    private volatile boolean isRunning = true;
    //////////////////////////////////////////

    //////////////////////////////////
    private ServerSocket serverSocket;
    //////////////////////////////////

    /////////////////////
    private Integer port;
    /////////////////////

    ///////////////////////////////////////////
    private final Map<String, Group> mapGroups;
    private final User user;
    ///////////////////////////////////////////


    //////////////////////////////////////////////////////////
    public Listener(Map<String, Group> mapGroups, User user) {
        this.mapGroups = mapGroups;
        this.user = user;
    }
    //////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {

        try {
            this.setUpPort();
        } catch (ListenerException | InternalException exception) {
            new Thread(new AlertTask(exception.getMessage())); // to change if GUI's framework change
        }

        while(isRunning){
            try {
                this.waitAndSpawn();
            } catch (ListenerException listenerException) {
                new Thread(new AlertTask(listenerException.getMessage())); // to change if GUI's framework change
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    private void waitAndSpawn() throws ListenerException {
        try{
            Socket socket = this.serverSocket.accept();
            Thread threadLoader = new Thread(new Loader(socket, this.mapGroups, this.user));
            threadLoader.setDaemon(true);
            threadLoader.start();
        }catch(SocketException socketException){
            this.isRunning = false;
        }
        catch(IOException ioException){
            throw new ListenerException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////
    private void setUpPort() throws InternalException, ListenerException {
        try{
            this.serverSocket = new ServerSocket(0); // ephemeral port
            this.port = this.serverSocket.getLocalPort();
            this.saveIpAndPortIntoDB();

        }catch(IOException ioException){
            throw new ListenerException();
        }
    }
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private void saveIpAndPortIntoDB() throws InternalException {
        Map<String, Object> mapUserInfo = new HashMap<>();
        mapUserInfo.put(UserFields.NICKNAME, this.user.credentials().getKey());
        mapUserInfo.put(UserFields.IP, "127.0.0.1");
        mapUserInfo.put(UserFields.PORT, this.port);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);

        baseDAO.updateEntity(mapUserInfo, Filter.IP_AND_PORT);
    }
    ///////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////
    public void shutdown() throws ListenerException {
        try{
            this.serverSocket.close();
        }catch (IOException ioException){
            throw new ListenerException();
        }
    }
    //////////////////////////////////////////////////

}
