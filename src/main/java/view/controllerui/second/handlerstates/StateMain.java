package view.controllerui.second.handlerstates;

import javafx.util.Pair;
import view.bean.observers.GroupBean;
import view.bean.observers.UserBean;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static view.graphicalui.second.DefaultCommands.*;
import static view.controllerui.second.Message.*;


public class StateMain implements AbstractState{

    ///////////////////////////////////////////
    private static StateMain stateMainInstance;
    ///////////////////////////////////////////


    /////////////////////
    private StateMain(){}
    /////////////////////


    //////////////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(SEARCH))
            home.searchMode();

        else if(home.getPrompt().getText().equals(CREATE_GROUP)){
            home.setUpGroupInfo();
            home.displayQuestion();
        }
        else if(home.getPrompt().getText().equals(VIEW_GROUPS)) {
            this.initListGroups(home);
            home.listGroups();
        }
        else if(home.getPrompt().getText().equals(MANAGE_PROFILE))
            home.manageProfile();

        else // also, PREV and BACK cmd
            errorMsg();

        home.getPrompt().clear();
    }
    //////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private void initListGroups(Home home){
        String groupName;
        String groupNick;
        String ownerNick;

        UserBean currUserBean = Shell.getShellHandler().getCurrUserBean();
        Map<String, GroupBean> mapGroupBean = Shell.getShellHandler().getMapGroupBean();

        List<Pair<String, String>> listOwnGroups = new ArrayList<>();
        List<Pair<String, String>> listOtherGroups = new ArrayList<>();
        for (Map.Entry<String, GroupBean> map : mapGroupBean.entrySet()) {
            groupNick = map.getValue().getNickname();
            groupName = map.getValue().getName();
            ownerNick = map.getValue().getOwner().getNickname();

            if(ownerNick.equals(currUserBean.getNickname()))
                listOwnGroups.add(new Pair<>(groupNick, groupName));
            else
                listOtherGroups.add(new Pair<>(groupNick, groupName));
        }
        home.setListOwnGroups(listOwnGroups);
        home.setListOtherGroups(listOtherGroups);
    }
    ////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////
    public static StateMain getStateMainInstance(){
        if(stateMainInstance == null)
            stateMainInstance = new StateMain();
        return stateMainInstance;
    }
    ////////////////////////////////////////////////

}
