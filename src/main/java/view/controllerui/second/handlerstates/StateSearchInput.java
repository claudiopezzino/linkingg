package view.controllerui.second.handlerstates;

import control.controlexceptions.InternalException;
import javafx.util.Pair;
import view.bean.GroupFilteredBean;
import view.bean.SearchFilterBean;
import view.boundary.UserManageCommunityBoundary;
import view.graphicalui.second.Home;
import view.graphicalui.second.Shell;

import java.util.ArrayList;
import java.util.List;

import static view.ConstAdapter.*;
import static view.controllerui.second.Message.errorMsg;
import static view.controllerui.second.Message.infoErrorMsg;
import static view.graphicalui.second.DefaultCommands.*;


public class StateSearchInput implements AbstractState{

    /////////////////////////////////////////////////////////
    private static StateSearchInput stateSearchInputInstance;
    /////////////////////////////////////////////////////////


    ////////////////////////////
    private StateSearchInput(){}
    ////////////////////////////


    /////////////////////////////////////////////////////
    @Override
    public void checkCmd(Home home) {
        if(home.getPrompt().getText().equals(BACK))
            home.searchFilter();

        else if(!home.getPrompt().getText().isEmpty()
                && !home.getPrompt().getText().equals(PREV))
            this.searchGroups(home, home.getPrompt().getText());

        else
            errorMsg();

        home.getPrompt().clear();
    }
    /////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void searchGroups(Home home, String filterName){

        SearchFilterBean searchFilterBean = new SearchFilterBean();

        switch (home.getCurrSearchFilter()) {
            case NAME:
                searchFilterBean.setFilter(ADAPTED_FILTER_NAME);
                break;
            case NICKNAME:
                searchFilterBean.setFilter(ADAPTED_FILTER_NICKNAME);
                break;
            case PROVINCE:
                searchFilterBean.setFilter(ADAPTED_FILTER_PROVINCE);
                break;
        }

        searchFilterBean.setFilterName(filterName);
        searchFilterBean.setCurrUserNick(Shell.getShellHandler().getCurrUserBean().getNickname());

        List<GroupFilteredBean> listGroupFilteredBean = new ArrayList<>();
        UserManageCommunityBoundary userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();
        try {
            // because sign-up and sign-in may not be done with this boundary
            if(userManageCommunityBoundary == null)
                Shell.getShellHandler().setUserManageCommunityBoundary(new UserManageCommunityBoundary());
            userManageCommunityBoundary = Shell.getShellHandler().getUserManageCommunityBoundary();

            listGroupFilteredBean = userManageCommunityBoundary.searchGroupsByFilter(searchFilterBean);
        }catch(InternalException internalException){
            infoErrorMsg(internalException.getMessage());
        }

        List<Pair<String, String>> listOtherGroups = new ArrayList<>();
        for (GroupFilteredBean groupFilteredBean : listGroupFilteredBean)
            listOtherGroups.add(new Pair<>(groupFilteredBean.getNickname(), groupFilteredBean.getName()));
        home.setListOtherGroups(listOtherGroups);

        home.applySearch(filterName);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////
    public static StateSearchInput getStateSearchInput() {
        if(stateSearchInputInstance == null)
            stateSearchInputInstance = new StateSearchInput();
        return stateSearchInputInstance;
    }
    //////////////////////////////////////////////////////////

}
