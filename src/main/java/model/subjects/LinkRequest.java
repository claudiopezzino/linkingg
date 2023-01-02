package model.subjects;

import model.Link;

import java.util.Collections;
import java.util.Map;

public class LinkRequest extends Subject implements Link {

    ///////////////////////////////
    private final String groupNick;
    private final String userNick;
    ///////////////////////////////

    //////////////////////////////////////////////
    private final Map<String, String> mapUserInfo; // image, name, surname
    //////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////
    public LinkRequest(String source, String destination, Map<String, String> mapSourceDetails){
        this.groupNick = destination;
        this.userNick = source;
        this.mapUserInfo = mapSourceDetails;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////
    @Override
    public String destination() {
        return this.groupNick;
    }
    ///////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    @Override
    public String source() {
        return this.userNick;
    }
    /////////////////////////////////////////////////

    ////////////////////////////////////////////
    @Override
    public Map<String, String> sourceDetails() {
        if (this.mapUserInfo.isEmpty())
            return Collections.emptyMap();
        return this.mapUserInfo;
    }
    ////////////////////////////////////////////
}
