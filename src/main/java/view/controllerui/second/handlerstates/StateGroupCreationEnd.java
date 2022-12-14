package view.controllerui.second.handlerstates;

import control.controlexceptions.InternalException;
import view.bean.GroupCreationBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.first.constcontainer.Image;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.Objects;

import static view.controllerui.second.Message.*;
import static view.graphicalui.first.constcontainer.Image.GROUP;
import static view.graphicalui.second.DefaultCommands.*;


public class StateGroupCreationEnd implements AbstractState{

    ///////////////////////////////////////////////////////////////////
    private static StateGroupCreationEnd stateGroupCreationEndInstance;
    ///////////////////////////////////////////////////////////////////

    //////////////////////////////////
    private StateGroupCreationEnd(){}
    //////////////////////////////////

    /////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.restoreScreen();

        else if(home.getPrompt().getText().equals(PREV)){
            home.setUpGroupInfo();
            home.displayQuestion();
            home.getScreen().appendText(home.getGroupName());
            home.saveGroupName(home.getGroupName());
            home.displayQuestion();
        }

        else if (home.getPrompt().getText().equals(CONFIRM)) {
            home.restoreScreen();
            this.createGroup(home.getGroupName(), home.getGroupNickname());
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createGroup(String groupName, String groupNick){
        GroupCreationBean groupCreationBean = new GroupCreationBean();

        groupCreationBean.setName(groupName);
        groupCreationBean.setNickname(groupNick);
        groupCreationBean.setImage("src/main/resources/"+GROUP); // adjust this one
        groupCreationBean.setOwner(Shell.getShellHandler().getCurrUserBean().getNickname());

        UserManageCommunityBoundary userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();
        try{
            // because sign-up and sign-in may not be done with this boundary
            if(userManageCommunityBoundary == null)
                Shell.getShellHandler().setUserManageCommunityBoundary(new UserManageCommunityBoundary());
            userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();
            userManageCommunityBoundary.createGroup(groupCreationBean);
            groupCreationMsg();
        }catch(InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    public static StateGroupCreationEnd getStateGroupCreationEndInstance() {
        if(stateGroupCreationEndInstance == null)
            stateGroupCreationEndInstance = new StateGroupCreationEnd();
        return stateGroupCreationEndInstance;
    }
    //////////////////////////////////////////////////////////////////////////
}
