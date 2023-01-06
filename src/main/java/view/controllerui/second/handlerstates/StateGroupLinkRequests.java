package view.controllerui.second.handlerstates;

import control.controlexceptions.InternalException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;
import view.bean.NewGroupMemberBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.List;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;

public class StateGroupLinkRequests implements AbstractState, EventHandler<ActionEvent> {

    /////////////////////////////////////////////////////////////////////
    private static StateGroupLinkRequests stateGroupLinkRequestsInstance;
    /////////////////////////////////////////////////////////////////////

    //////////////////////////////////
    private StateGroupLinkRequests(){}
    //////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.groupOptions(home.getGroupNickname());

        // because of check in StateGroupOptions, there is no need to do the same here
        else if(!home.getPrompt().getText().isEmpty() &&
                !home.getPrompt().getText().equals(PREV)) {

            if(isUserInList(home, home.getPrompt().getText()))
                confirmationMsg(this);
            else
                wrongMatchMsg(home.getPrompt().getText());
        }

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    private boolean isUserInList(Home home, String userNick){
        List<Pair<String, String>> listRequests = home.getListLinkRequests();
        for (Pair<String, String> request : listRequests) {
            if (request.getKey().equals(userNick))
                return true;
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    public static StateGroupLinkRequests getStateGroupLinkRequestsInstance() {
        if(stateGroupLinkRequestsInstance == null)
            stateGroupLinkRequestsInstance = new StateGroupLinkRequests();
        return stateGroupLinkRequestsInstance;
    }
    ///////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handle(ActionEvent event) {
        Home home = Home.getHomeInstance();

        String userNick = home.getPrompt().getText();
        String groupNick = home.getGroupNickname();
        String groupOwnerNick = Shell.getShellHandler().getCurrUserBean().getNickname();

        NewGroupMemberBean newGroupMemberBean = new NewGroupMemberBean();
        newGroupMemberBean.setUserNick(userNick);
        newGroupMemberBean.setGroupNick(groupNick);
        newGroupMemberBean.setGroupOwnerNick(groupOwnerNick);

        UserManageCommunityBoundary userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();
        try{
            // because sign-up and sign-in may not be done with this boundary
            if(userManageCommunityBoundary == null)
                Shell.getShellHandler().setUserManageCommunityBoundary(new UserManageCommunityBoundary());
            userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();

            userManageCommunityBoundary.acceptLinkRequest(newGroupMemberBean);

            this.reload(home, userNick);

        }catch (InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    private void reload(Home home, String newMemberNick){
        this.removeUserFromListRequests(home, newMemberNick);
        home.showLinkRequests(home.getGroupNickname());
        home.addTargetIntoList(newMemberNick, home.getGroupMembers());
    }
    //////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    private void removeUserFromListRequests(Home home, String userNick){
        List<Pair<String, String>> listRequests = home.getListLinkRequests();
        listRequests.removeIf(request -> request.getKey().equals(userNick));
    }
    ////////////////////////////////////////////////////////////////////////

}
