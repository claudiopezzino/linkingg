package view;

import javafx.concurrent.Task;
import view.boundary.UserManageCommunityBoundary;

/* Alert Class depends on the specific framework with which GUI has been developed,
 * in this case Alert Class is-a-kind-of Task because of JavaFX */
public class AlertTask extends Task<Void> {

    //////////////////////////////////
    private final String alertMessage;
    //////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    public AlertTask(String alertMessage){
        this.alertMessage = alertMessage;
    }
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////
    @Override
    protected Void call() {
        UserManageCommunityBoundary.alertUser(alertMessage);
        return null;
    }
    ////////////////////////////////////////////////////////

}
