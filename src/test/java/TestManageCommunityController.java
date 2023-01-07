import control.ManageCommunityController;
import control.controlexceptions.InternalException;
import org.junit.jupiter.api.Test;
import view.bean.GroupFilteredBean;
import view.bean.SearchFilterBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestManageCommunityController {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void testFindGroupsByFilter() throws InternalException {
        boolean rightAmount = false;

        String filter = "name";
        String filterName = "name";
        String currUserNick = "francescoesposito_0";

        int numFilteredGroups = 0;
        int numUnknownGroups = 4;

        SearchFilterBean searchFilterBean = new SearchFilterBean();
        searchFilterBean.setFilter(filter);
        searchFilterBean.setFilterName(filterName);
        searchFilterBean.setCurrUserNick(currUserNick);

        ManageCommunityController manageCommunityController = new ManageCommunityController();
        List<GroupFilteredBean> listGroups = manageCommunityController.findGroupsByFilter(searchFilterBean);

        for(GroupFilteredBean ignored : listGroups)
            numFilteredGroups++;

        if (numFilteredGroups == numUnknownGroups)
            rightAmount = true;

        assertTrue(rightAmount);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

}
