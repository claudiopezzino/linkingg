package model.subjects;

import view.bean.observers.Observer;

import java.io.Serializable;
import java.util.Map;


// Serializable needed to send Subject instance over socket stream
public abstract class Subject implements Serializable {

    // observer list not necessary
    //////////////////////////
    private Observer observer;
    //////////////////////////


    // needed to deliver Entity's Observer (Bean) to its Bean (counterpart)
    ///////////////////////////////////////////////////////
    public Observer getObserver() {
        return this.observer;
    }
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    public void attach(Observer observer){
        this.observer = observer;
    }
    ///////////////////////////////////////////////////////////////////

    // detach method not needed

    ////////////////////////////////////////////////////////////////////////////////////
    protected <V> void notifyObserver(Map<String, V> map){
            this.observer.update(map);
    }
    ////////////////////////////////////////////////////////////////////////////////////

}
