package view;

import control.controlutilities.SecureObjectInputStream;
import javafx.application.Platform;
import javafx.concurrent.Task;
import model.Filter;
import model.LocalBridge;
import view.bean.observers.GroupBean;
import view.boundary.UserManageCommunityBoundary;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class NotificationTask extends Task<Void> {

    ////////////////////////////
    private final Filter filter;
    ////////////////////////////

    //////////////////////////////////////////
    private final LocalBridge localBridge;
    //////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public NotificationTask(LocalBridge localBridge, Filter filter){
        this.localBridge = localBridge;
        this.filter = filter;
    }
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected Void call() {

        try(ServerSocket serverSocket = new ServerSocket(0)){

            this.localBridge.initPort(serverSocket.getLocalPort());

            Socket socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            SecureObjectInputStream secureOis = new SecureObjectInputStream(is);

            if (this.filter == Filter.GROUP_CREATION) {

                GroupBean groupBean = (GroupBean) secureOis.readObject();

                this.localBridge.endBridge();

                Platform.runLater(new Updater(groupBean));

                this.closeResources(socket, is, secureOis);

            }

        }catch(IOException | ClassNotFoundException ioException){
            Platform.runLater(new AlertTask(ioException.getMessage()));
        }

        return null;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////
    private void closeResources(Socket socket, InputStream is, SecureObjectInputStream secureOis){
        try{
            socket.close();
            is.close();
            secureOis.close();
        }catch (IOException ioException){
            Platform.runLater(new AlertTask("Some internal error occurred"));
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////


    /*----------------------------------------- INNER_CLASS -----------------------------------------*/
    public static class Updater implements Runnable{

        //////////////////////////////////
        private final GroupBean groupBean;
        //////////////////////////////////

        /////////////////////////////////////
        public Updater(GroupBean groupBean){
            this.groupBean = groupBean;
        }
        /////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void run() {
            UserManageCommunityBoundary userManageCommunityBoundary = new UserManageCommunityBoundary();
            userManageCommunityBoundary.notifyNewGroup(this.groupBean);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
    }

    /*--------------------------------------------------------------------------------------------------*/

}
