package view.controllerui.second.handlerstates;

import control.controlexceptions.InternalException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;
import view.bean.LinkRequestCreationBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;


import java.util.List;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupRequest implements AbstractState, EventHandler<ActionEvent> {

    ///////////////////////////////////////////////////////////
    private static StateGroupRequest stateGroupRequestInstance;
    ///////////////////////////////////////////////////////////


    /////////////////////////////
    private StateGroupRequest(){}
    /////////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK)) {
            home.initTargetGroups();
            home.searchFilter();
        }

        else if(!home.getPrompt().getText().isEmpty()) {
            if(isGroupInList(home, home.getPrompt().getText()))
                confirmationMsg(this);
            else
                wrongMatchMsg(home.getPrompt().getText());
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    private boolean isGroupInList(Home home, String groupNick){
        List<Pair<String, String>> listGroups = home.getListOtherGroups();
        for (Pair<String, String> group : listGroups){
            if (group.getKey().equals(groupNick))
                return true;
        }
        return false;
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    private void removeGroup(Home home, String groupNick){
        List<Pair<String, String>> listGroups = home.getListOtherGroups();
        listGroups.removeIf(group -> group.getKey().equals(groupNick));
    }
    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////
    private void reload(Home home, String lastInput){
        this.removeGroup(home, lastInput);
        home.insertGroupTarget(lastInput);
        home.searchInput();
        home.applySearch(home.getSearchTarget());
        home.displayTargetGroups();
    }
    /////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    public static StateGroupRequest getStateGroupRequestInstance(){
        if(stateGroupRequestInstance == null)
            stateGroupRequestInstance = new StateGroupRequest();
        return stateGroupRequestInstance;
    }
    ///////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(ActionEvent event) {

        String currUserNick = Shell.getShellHandler().getCurrUserBean().getNickname();
        String groupNick = Home.getHomeInstance().getPrompt().getText();

        LinkRequestCreationBean linkRequestCreationBean = new LinkRequestCreationBean();
        linkRequestCreationBean.setUserNick(currUserNick);
        linkRequestCreationBean.setGroupNick(groupNick);

        // userManageCommunityBoundary exists because of "Search Input" executed before this step
        UserManageCommunityBoundary userManageCommunityBoundary =
                Shell.getShellHandler().getUserManageCommunityBoundary();
        try{
            userManageCommunityBoundary.sendLinkRequestToGroup(linkRequestCreationBean);

            Home home = Home.getHomeInstance();
            this.reload(home, home.getPrompt().getText());

        }catch (InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

}
