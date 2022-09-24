package view.controllerui.second.handlerstates;

import view.graphicalui.second.Home;

import static view.controllerui.second.Message.*;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMemberInvitation implements AbstractState{

    ///////////////////////////////////////////////////////////////////
    private static StateMemberInvitation stateMemberInvitationInstance;
    ///////////////////////////////////////////////////////////////////


    /////////////////////////////////
    private StateMemberInvitation(){}
    /////////////////////////////////


    ////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK)) {
            home.initTargetGroups();
            home.searchFilter();
        }

        else if(home.getPrompt().getText().equals(PREV)) {
            if(home.getTargetGroups() == null || home.getTargetGroups().isEmpty()) {
                home.searchInput();
                home.applySearch(home.getSearchTarget());
            }else{
                home.getTargetGroups().remove(home.getTargetGroups().size() - 1);
                home.searchInput();
                home.applySearch(home.getSearchTarget());
                home.checkInvitationGroups(home.getNewGroupMember());
                for(String group : home.getTargetGroups())
                    home.getScreen().appendText(group + ",  ");
            }
        }

        else if(home.getPrompt().getText().equals(LINK_INVITATION)){
            home.inviteMemberIntoGroups();
            singleInvitationMsg();
        }
        else if (!home.getPrompt().getText().isEmpty())
            home.insertGroupTarget(home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    ////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    public static StateMemberInvitation getStateMemberInvitationInstance(){
        if(stateMemberInvitationInstance == null)
            stateMemberInvitationInstance = new StateMemberInvitation();
        return stateMemberInvitationInstance;
    }
    ///////////////////////////////////////////////////////////////////////

}
