package control.tasks;

import control.controlexceptions.InternalException;
import control.tasks.tasksexceptions.ListenerException;
import javafx.application.Platform;
import model.Device;
import model.DeviceFields;
import model.Filter;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
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

import static model.dao.DAO.DEVICE_DAO;


public class Listener extends Thread {

    //////////////////////////////////////////
    private volatile boolean isRunning = true;
    //////////////////////////////////////////

    //////////////////////////////////
    private ServerSocket serverSocket;
    //////////////////////////////////

    //////////////////////////
    private Device userDevice;
    //////////////////////////

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
            this.initServerSocket();
        } catch (ListenerException | InternalException exception) {
            // to change if GUI's framework change
            Platform.runLater(new AlertTask(exception.getMessage()));
        }

        while(isRunning){
            try {
                this.waitAndSpawn();
            } catch (ListenerException listenerException) {
                // to change if GUI's framework change
                Platform.runLater(new AlertTask(listenerException.getMessage()));
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

    //////////////////////////////////////////////////////////////////////////////
    private void initServerSocket() throws InternalException, ListenerException {
        try{
            this.serverSocket = new ServerSocket(0); // ephemeral port
            Integer port = this.serverSocket.getLocalPort();
            this.setUpUserDevice(port);

        }catch(IOException ioException){
            throw new ListenerException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    private void setUpUserDevice(Integer port) throws InternalException {
        Map<String, String> mapDeviceInfo = new HashMap<>();

        mapDeviceInfo.put(UserFields.NICKNAME, this.user.credentials().getKey());
        mapDeviceInfo.put(DeviceFields.IP, "127.0.0.1");
        mapDeviceInfo.put(DeviceFields.PORT, String.valueOf(port));

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(DEVICE_DAO);

        try{
            baseDAO.createEntity(mapDeviceInfo);
            this.userDevice = (Device) baseDAO.readEntity(mapDeviceInfo, Filter.IP_AND_PORT);
        }catch(DuplicatedEntityException | NoEntityException entityException){
            throw new InternalException(entityException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////
    public void shutdown() throws ListenerException {
        try{
            this.removeDevice();
            this.serverSocket.close();
        }catch (IOException ioException){
            throw new ListenerException();
        }
    }
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    private void removeDevice() throws ListenerException {
        Map<String, String> mapDeviceInfo = new HashMap<>();
        mapDeviceInfo.put(DeviceFields.IP, this.userDevice.ipAddress());
        mapDeviceInfo.put(DeviceFields.PORT, String.valueOf(this.userDevice.portNumber()));

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(DEVICE_DAO);

        try{
            baseDAO.updateEntity(mapDeviceInfo, Filter.IP_AND_PORT);
        }catch (InternalException internalException){
            throw new ListenerException();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    public Device getUserDevice(){
        return this.userDevice;
    }
    /////////////////////////////////////////////////////////

}
