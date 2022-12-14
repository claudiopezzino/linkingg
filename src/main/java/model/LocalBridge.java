package model;

// needed for NotificationTask
public class LocalBridge {

    /////////////////////////////////////////////
    private Integer port;
    private static final String IP = "127.0.0.1";
    /////////////////////////////////////////////

    /////////////////////////////
    private boolean init = false;
    private boolean done = false;
    /////////////////////////////

    ///////////////////////////////////////////////////////////
    public synchronized Integer readPort(){
        while (!init){
            try {
                wait(); // wait till the port is initialised
            }catch (InterruptedException interruptedException){
                Thread.currentThread().interrupt();
            }
        }
        return this.port;
    }
    ///////////////////////////////////////////////////////////

    ////////////////////////
    public String readIp(){
        return IP;
    }
    ////////////////////////

    /////////////////////////////////////////////////
    public synchronized void initPort(Integer port){
        this.port = port;
        this.init = true;
        notifyAll();
    }
    /////////////////////////////////////////////////

    /////////////////////////////////////
    public synchronized void endBridge(){
        this.done = true;
        notifyAll();
    }
    /////////////////////////////////////

    ////////////////////////////////////////////////////////////
    public synchronized void closeBridge(){
        while (!done){
            try {
                wait(); // wait till task has finished
            }catch (InterruptedException interruptedException){
                Thread.currentThread().interrupt();
            }
        }
    }
    ////////////////////////////////////////////////////////////

}
