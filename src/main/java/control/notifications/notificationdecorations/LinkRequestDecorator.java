package control.notifications.notificationdecorations;

import control.notifications.Notification;

public class LinkRequestDecorator extends NotificationDecorator{

    ////////////////////////////
    private final String source;
    private final String dest;
    ////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    private static final String USER = "User @";
    private static final String LINK_REQUEST_TO = " has sent a link request to ";
    private static final String GROUP = "Group @";
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    public LinkRequestDecorator(Notification notification, String source, String dest) {
        super(notification);

        this.source = source;
        this.dest = dest;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    public String displayMessage(){
        String message = super.displayMessage();
        message += USER + this.source + LINK_REQUEST_TO + GROUP + this.dest + NEW_LINE;
        return message;
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
