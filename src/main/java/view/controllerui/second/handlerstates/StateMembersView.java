package view.controllerui.second.handlerstates;

import view.bean.observers.UserBean;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;

import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoErrorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateMembersView implements AbstractState{

    /////////////////////////////////////////////////////////
    private static StateMembersView stateMembersViewInstance;
    /////////////////////////////////////////////////////////


    ////////////////////////////
    private StateMembersView(){}
    ////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.groupOptions(home.getGroupNickname());

        else if(home.getPrompt().getText().equals(REMOVAL))
            home.membersRemovalMode();

        else if(!home.getPrompt().getText().isEmpty()
                && !home.getPrompt().getText().equals(PREV)) {

            if(Shell.getShellHandler().getMapGroupBean().get(home.getGroupNickname())
                    .getMapMembers().get(home.getPrompt().getText()) != null) {

                this.initListMemberDetails(home, home.getPrompt().getText());
                home.showMemberDetails(home.getPrompt().getText());
            }
            else
                infoErrorMsg("No user available with given nickname into group:   "
                        + home.getGroupNickname());
        }
        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initListMemberDetails(Home home, String userNick){
        List<String> listMemberDetails = new ArrayList<>();

        String groupNick = home.getGroupNickname();

        UserBean userBean = Shell.getShellHandler().getMapGroupBean().get(groupNick).getMapMembers().get(userNick);

        String userName = userBean.getName();
        String userSurname = userBean.getSurname();

        listMemberDetails.add("Name:   " + userName);
        listMemberDetails.add("Surname:   " + userSurname);
        listMemberDetails.add("Nickname:   " + userNick);

        // evaluate the possibility to show also user image profile into a dialog

        home.setListDetails(listMemberDetails);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////
    public static StateMembersView getStateMembersViewInstance(){
        if(stateMembersViewInstance == null)
            stateMembersViewInstance = new StateMembersView();
        return stateMembersViewInstance;
    }
    //////////////////////////////////////////////////////////////

}
