package control;

import control.controlexceptions.InternalException;
import control.controlutilities.CopyException;
import control.controlutilities.Messenger;
import control.controlutilities.MessengerException;
import control.tasks.Listener;
import control.tasks.tasksexceptions.ListenerException;
import javafx.util.Pair;
import model.*;
import model.dao.BaseDAO;
import model.dao.FactoryDAO;
import model.modelexceptions.DuplicatedEntityException;
import model.modelexceptions.NoEntityException;
import model.subjects.Group;
import model.subjects.LinkRequest;
import model.subjects.Meeting;
import model.subjects.User;
import view.bean.*;
import view.bean.observers.GroupBean;
import view.bean.observers.LinkRequestBean;
import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;

import java.util.*;

import static model.dao.DAO.*;
import static view.ConstAdapter.*;


public class ManageCommunityController {

    ////////////////////////////////
    private Listener threadListener;
    ////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////
    private User fetchGroupMember(String userNick) throws InternalException {
        Map<String, String> mapNewMemberInfo = new HashMap<>();
        mapNewMemberInfo.put(UserFields.NICKNAME, userNick);

        User groupMember;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();

        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);
        try{
            groupMember = (User) baseDAO.readEntity(mapNewMemberInfo, Filter.USER_NICKNAME);
        }catch (NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        return groupMember;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, User> fetchGroupMembers(String groupNick) throws InternalException {
        Map<String, String> mapGroupInfo = new HashMap<>();
        mapGroupInfo.put(GroupFields.NICKNAME, groupNick);

        Map<String, Object> mapObjects;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();

        BaseDAO baseDAO = factoryDAO.createDAO(USER_DAO);
        try{
            mapObjects = baseDAO.readEntities(mapGroupInfo, Filter.GROUP_NICKNAME);
        }catch (NoEntityException noEntityException){
            mapObjects = new HashMap<>();
        }
        Map<String, User> mapUsers = new HashMap<>();
        for (Map.Entry<String, Object> entry : mapObjects.entrySet())
            mapUsers.put(entry.getKey(), (User) entry.getValue());

        return mapUsers;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    private List<Device> fetchGroupMemberDevices(String userNick) throws InternalException {
        List<Device> listGroupMemberDevices = new ArrayList<>();

        Map<String, String> mapGroupMemberInfo = new HashMap<>();
        mapGroupMemberInfo.put(UserFields.NICKNAME, userNick);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(DEVICE_DAO);

        Map<String, Object> mapObjects;
        try{
            mapObjects = baseDAO.readEntities(mapGroupMemberInfo, Filter.GROUP_MEMBER);
        }catch (NoEntityException noEntityException){
            // group member is not online, so an empty map will return
            mapObjects = new HashMap<>();
        }
        for(Map.Entry<String, Object> deviceEntry : mapObjects.entrySet())
            listGroupMemberDevices.add((Device) deviceEntry.getValue());

        return listGroupMemberDevices;
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void addUserIntoGroup(NewGroupMemberBean newGroupMemberBean) throws InternalException {

        /*------------ GETTING VALUES FROM NEW USER BEAN ------------*/
        String userNick = newGroupMemberBean.getUserNick();
        String groupNick = newGroupMemberBean.getGroupNick();
        String groupOwnerNick = newGroupMemberBean.getGroupOwnerNick();
        /*-----------------------------------------------------------*/


        /*------------------- RETRIEVING GROUP MEMBERS DEVICES -------------------*/
        List<Device> listGroupMembersDevices = new ArrayList<>();
        Map<String, User> mapUsers = this.fetchGroupMembers(groupNick);
        for (Map.Entry<String, User> userEntry : mapUsers.entrySet()){
            String memberNick = userEntry.getValue().credentials().getKey();
            listGroupMembersDevices.addAll(this.fetchGroupMemberDevices(memberNick));
        }
        listGroupMembersDevices.addAll(this.fetchCurrUserDevices(groupOwnerNick));
        /*-------------------------------------------------------------------------*/

        /*-------------- RETRIEVING NEW MEMBER --------------*/
        User newGroupMember = this.fetchGroupMember(userNick); // send to group members
        this.turnIntoUserBean(newGroupMember);
        /*---------------------------------------------------*/


        /*--- ADDING RELATIONSHIP BETWEEN NEW USER AND TARGET GROUP ---*/
        Map<String, String> mapNewMemberInfo = new HashMap<>();
        mapNewMemberInfo.put(UserFields.NICKNAME, userNick);
        mapNewMemberInfo.put(UserInfo.GROUP_NICK, groupNick);
        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(GROUP_DAO);
        baseDAO.updateEntity(mapNewMemberInfo, Filter.NEW_GROUP_MEMBER);
        /*-------------------------------------------------------------*/

        /*----- DELETING LINK REQUEST FROM NEW MEMBER TO TARGET GROUP -----*/
        Map<String, String> mapLinkRequestInfo = new HashMap<>();
        mapLinkRequestInfo.put(LinkRequestFields.USERS_NICKNAME, userNick);
        mapLinkRequestInfo.put(LinkRequestFields.GROUPS_NICKNAME, groupNick);
        baseDAO = factoryDAO.createDAO(LINK_REQUEST_DAO);
        baseDAO.updateEntity(mapLinkRequestInfo, Filter.NOTHING);
        /*-----------------------------------------------------------------*/


        /*-------------------------- RETRIEVING NEW GROUP MEMBER DEVICES -------------------------*/
        List<Device> listNewMemberDevices = new ArrayList<>(this.fetchGroupMemberDevices(userNick));
        /*----------------------------------------------------------------------------------------*/

        /*------ RETRIEVING TARGET GROUP ------*/
        Group group = this.fetchGroup(groupNick); // send to new user
        this.turnIntoGroupBean(group);
        /*-------------------------------------*/


        /*------------------------------ FORWARDING PHASE ------------------------------*/
        List<Object> listObjects = new ArrayList<>();
        listObjects.add(newGroupMember);
        listObjects.add(groupNick);

        this.deliverToEndUser(listGroupMembersDevices, listObjects, Filter.GROUP_JOIN);

        List<Group> listGroups = new ArrayList<>();
        listGroups.add(group);

        this.deliverToEndUser(listNewMemberDevices, listGroups, Filter.GROUP_CREATION);
        /*------------------------------------------------------------------------------*/


    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private <E> void deliverToEndUser(List<Device> listDevices, List<E> listElem, Filter filter) throws InternalException {
        for (Device device : listDevices) {
            try {
                Messenger.sendMessage(device, listElem, filter);
            }catch (MessengerException messengerException){
                throw new InternalException(messengerException.getMessage());
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void makeAndDeliverLinkRequest(LinkRequestCreationBean linkRequestCreationBean) throws InternalException{
        Map<String, String> mapLinkRequestInfo = new HashMap<>();
        mapLinkRequestInfo.put(LinkRequestFields.USERS_NICKNAME, linkRequestCreationBean.getUserNick());
        mapLinkRequestInfo.put(LinkRequestFields.GROUPS_NICKNAME, linkRequestCreationBean.getGroupNick());

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(LINK_REQUEST_DAO);

        try {
            baseDAO.createEntity(mapLinkRequestInfo);
        }catch (DuplicatedEntityException duplicatedEntityException){
            throw new InternalException(duplicatedEntityException.getMessage());
        }

        LinkRequest linkRequest;
        try{
            linkRequest = (LinkRequest) baseDAO.readEntity(mapLinkRequestInfo, Filter.NOTHING);
        }catch(NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        this.turnIntoLinkRequestBean(linkRequest);

        List<LinkRequest> listLinkRequests = new ArrayList<>();
        listLinkRequests.add(linkRequest);

        // fetch group owner devices and send them LinkRequest instance so that they can update their local list
        Group group = this.fetchGroup(linkRequest.destination());
        List<Device> listDevices = this.fetchGroupMemberDevices(group.owner().credentials().getKey());

        this.deliverToEndUser(listDevices, listLinkRequests, Filter.LINK_REQUEST);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    private Group fetchGroup(String groupNick) throws InternalException{
        Group group;

        Map<String, String> mapGroupInfo = new HashMap<>();
        mapGroupInfo.put(GroupFields.NICKNAME, groupNick);

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(GROUP_DAO);

        try{
            group = (Group) baseDAO.readEntity(mapGroupInfo, Filter.GROUP_NICKNAME);
        }catch(NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        return group;
    }
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<GroupFilteredBean> findGroupsByFilter(SearchFilterBean searchFilterBean) throws InternalException{
        Map<String, String> mapGroupsFilter = new HashMap<>();
        mapGroupsFilter.put(UserFields.NICKNAME, searchFilterBean.getCurrUserNick());

        Map<String, Object> mapObjects = new HashMap<>();
        Map<String, Group> mapGroups = new HashMap<>();

        List<GroupFilteredBean> listGroupFilteredBean = new ArrayList<>();

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(GROUP_DAO);

        String filter = searchFilterBean.getFilter();
        try {
            switch (filter) {
                case ADAPTED_FILTER_NAME:
                    mapGroupsFilter.put(GroupFields.NAME, searchFilterBean.getFilterName());
                    mapObjects = baseDAO.readEntities(mapGroupsFilter, Filter.GROUP_NAME);
                    break;

                case ADAPTED_FILTER_NICKNAME:
                    mapGroupsFilter.put(UserInfo.GROUP_NICK, searchFilterBean.getFilterName());
                    mapObjects = baseDAO.readEntities(mapGroupsFilter, Filter.GROUP_NICKNAME);
                    break;

                case ADAPTED_FILTER_PROVINCE:
                    mapGroupsFilter.put(UserFields.PROVINCE, searchFilterBean.getFilterName());
                    mapObjects = baseDAO.readEntities(mapGroupsFilter, Filter.GROUP_PROVINCE);
                    break;

                default:
                    break;
            }

            for(Map.Entry<String, Object> entry : mapObjects.entrySet())
                mapGroups.put(entry.getKey(), (Group) entry.getValue());

            for (Map.Entry<String, Group> entry : mapGroups.entrySet()){
                GroupFilteredBean groupFilteredBean = this.turnIntoGroupFilteredBean(entry.getValue());
                listGroupFilteredBean.add(groupFilteredBean);
            }

        }catch (NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        return listGroupFilteredBean;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    private GroupFilteredBean turnIntoGroupFilteredBean(Group group){
        GroupFilteredBean groupFilteredBean = new GroupFilteredBean();

        String nickname = group.nickname();
        String name = group.name();
        String image = group.imageProfile();

        groupFilteredBean.setNickname(nickname);
        groupFilteredBean.setName(name);
        groupFilteredBean.setImage(image);

        return groupFilteredBean;
    }
    /////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    public void setUpGroup(GroupCreationBean groupCreationBean) throws InternalException{
        Map<String, String> mapGroupInfo = this.fetchGroupCreationInfo(groupCreationBean);

        boolean nickExists = true;

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(GROUP_DAO);

        int numCopy = 1;
        while (nickExists) {
            try {
                baseDAO.createEntity(mapGroupInfo);
                nickExists = false;
            } catch (DuplicatedEntityException e) {
                this.changeGroupNick(mapGroupInfo, numCopy);
                numCopy++;
            }
        }

        Group group;
        try{
            group = (Group) baseDAO.readEntity(mapGroupInfo, Filter.GROUP_NICKNAME);
        }catch(NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        this.turnIntoGroupBean(group);

        List<Group> listGroups = new ArrayList<>();
        listGroups.add(group);

        List<Device> listCurrUserDevices = this.fetchCurrUserDevices(mapGroupInfo.get(GroupFields.OWNER));

        this.deliverToEndUser(listCurrUserDevices, listGroups, Filter.GROUP_CREATION);

    }
    //////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, String> fetchGroupCreationInfo(GroupCreationBean groupCreationBean){
        Map<String, String> mapGroupInfo = new HashMap<>();

        mapGroupInfo.put(GroupFields.NICKNAME, groupCreationBean.getNickname());
        mapGroupInfo.put(GroupFields.NAME, groupCreationBean.getName());
        mapGroupInfo.put(GroupFields.IMAGE, groupCreationBean.getImage());
        mapGroupInfo.put(GroupFields.OWNER, groupCreationBean.getOwner());

        return mapGroupInfo;
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    private void changeGroupNick(Map<String, String> mapGroupInfo, int numCopy){
        String regex = "_";

        // first time there won't be regex
        String[] nickTokens = mapGroupInfo.get(GroupFields.NICKNAME).split(regex);
        String newGroupNick = nickTokens[0] + regex + numCopy;

        mapGroupInfo.replace(GroupFields.NICKNAME, newGroupNick);
    }
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    private List<Device> fetchCurrUserDevices(String userNick) throws InternalException{
        List<Device> listCurrUserDevices = new ArrayList<>();
        listCurrUserDevices.add(this.threadListener.getUserDevice());  // current device

        Map<String, String> mapCurrUserInfo = new HashMap<>();

        mapCurrUserInfo.put(UserFields.NICKNAME, userNick);
        mapCurrUserInfo.put(DeviceFields.PORT,
                String.valueOf(this.threadListener.getUserDevice().portNumber()));

        FactoryDAO factoryDAO = FactoryDAO.getSingletonInstance();
        BaseDAO baseDAO = factoryDAO.createDAO(DEVICE_DAO);

        Map<String, Object> mapCurrUserDevices;
        try{
            mapCurrUserDevices = baseDAO.readEntities(mapCurrUserInfo, Filter.USER_NICKNAME);
        }catch (NoEntityException noEntityException){
            throw new InternalException(noEntityException.getMessage());
        }

        for (Map.Entry<String, Object> entry : mapCurrUserDevices.entrySet())
            listCurrUserDevices.add((Device) entry.getValue());

        return listCurrUserDevices;
    }
    /////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserSignInBean setUpSignUpPhase(LoginController loginController, UserSignUpBean userSignUpBean) throws InternalException {
        Pair<String, String> userCredentials = loginController.registration(userSignUpBean);

        UserSignInBean userSignInBean = new UserSignInBean();

        try{
            userSignInBean.setNickname(userCredentials.getKey());
        }catch(BeanError beanError){
            throw new InternalException(beanError.displayErrors());
        }

        try{
            userSignInBean.setPassword(userCredentials.getValue());
        }catch(BeanError beanError){
            throw new InternalException(beanError.displayErrors());
        }

        return userSignInBean;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* even though the developer knows the logic, it would be better to insert an "instanceof" check
     * to verify that Object instance returned by "access" method is-a-kind-of desired Class */
    public Map<String, Object> setUpSignInPhase(LoginController loginController, UserSignInBean userSignInBean) throws InternalException {

        Map<String, Object> mapEntities = loginController.access(userSignInBean);

        Map<String, Object> mapBeans = new HashMap<>();

        User user = null;
        Map<String, Group> mapGroup = new HashMap<>();

        for(Map.Entry<String, Object> entry : mapEntities.entrySet()){
            if(entry.getValue() instanceof Group) { // discard User instance
                Group group = (Group) entry.getValue();
                GroupBean groupBean =  this.turnIntoGroupBean(group);
                mapGroup.put(entry.getKey(), group);
                mapBeans.put(groupBean.getNickname(), groupBean);
            }
            else {
                user = (User) entry.getValue();
                UserBean userBean = this.turnIntoUserBean(user);
                mapBeans.put(userBean.getNickname(), userBean);
            }
        }

        // Run a Thread in listening mode to catch Entities' changes and make up Observer pattern among them and Beans
        this.threadListener = new Listener(mapGroup, user);
        //this.threadListener.setDaemon(true); // doing this the application will stop also this thread other than itself
        this.threadListener.start();

        return mapBeans;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    private GroupBean turnIntoGroupBean(Group group) throws InternalException {
        GroupBean groupBean = new GroupBean();

        groupBean.setSubject(group); // due to Observer Pattern
        group.attach(groupBean); // due to Observer Pattern

        groupBean.setNickname(group.nickname());
        groupBean.setName(group.name());
        groupBean.setImage(group.imageProfile());
        groupBean.setOwner(this.turnIntoUserBean(group.owner()));

        Map<String, UserBean> mapUserBean = new HashMap<>();
        for (Map.Entry<String, User> entry : group.members().entrySet())
            mapUserBean.put(entry.getKey(), this.turnIntoUserBean(entry.getValue()));
        groupBean.setMapMembers(mapUserBean);

        Map<String, LinkRequestBean> mapLinkRequestsBean = new HashMap<>();
        for(Map.Entry<String, LinkRequest> entry : group.linkRequests().entrySet())
            mapLinkRequestsBean.put(entry.getKey(), this.turnIntoLinkRequestBean(entry.getValue()));

        Map<String, MeetingBean> mapMeetingBean = new HashMap<>();
        for(Map.Entry<String, Meeting> entry : group.plannedMeeting().entrySet())
            mapMeetingBean.put(entry.getKey(), this.turnIntoMeetingBean(entry.getValue()));

        try{
            groupBean.setMapMeetings(mapMeetingBean);
            groupBean.setMapLinkRequests(mapLinkRequestsBean);
        }catch(CopyException copyException){
            throw new InternalException(copyException.getMessage());
        }

        return groupBean;
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    private MeetingBean turnIntoMeetingBean(Meeting meeting){
        MeetingBean meetingBean = new MeetingBean();

        meetingBean.setSubject(meeting); // due to Observer Pattern
        meeting.attach(meetingBean); // due to Observer Pattern

        meetingBean.setId(meeting.location().getKey());
        meetingBean.setName(meeting.location().getValue());
        meetingBean.setRating(meeting.assessment());
        meetingBean.setDate(meeting.timeTable().getKey());
        meetingBean.setTime(meeting.timeTable().getValue());
        meetingBean.setImage(meeting.mainPicture());
        meetingBean.setPhotos(meeting.gallery());
        meetingBean.setScheduler(this.turnIntoUserBean(meeting.scheduler()));

        Map<String, UserBean> mapUserBean = new HashMap<>();
        for(Map.Entry<String, User> entry : meeting.joiners().entrySet())
            mapUserBean.put(entry.getKey(), this.turnIntoUserBean(entry.getValue()));
        meetingBean.setJoiners(mapUserBean);

        return meetingBean;
    }
    //////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////
    private UserBean turnIntoUserBean(User user){
        UserBean userBean;
        try{
            userBean = UserBeanCollector.getUserBeanInstance(user.credentials().getKey());
        }catch(NoUserBeanException noUserBeanException){
            userBean = new UserBean();

            userBean.setSubject(user); // due to Observer Pattern
            user.attach(userBean); // due to Observer Pattern

            userBean.setNickname(user.credentials().getKey());
            userBean.setName(user.fullName().getKey());
            userBean.setSurname(user.fullName().getValue());
            userBean.setImageProfile(user.imageProfile());

            UserBeanCollector.addUserBeanInstance(userBean);
        }

        return userBean;
    }
    /////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    private LinkRequestBean turnIntoLinkRequestBean(LinkRequest linkRequest){
        LinkRequestBean linkRequestBean = new LinkRequestBean();

        //to add for Observer Patter: linkRequestBean.setSubject(linkRequest)
        linkRequest.attach(linkRequestBean); // due to Observer Pattern

        linkRequestBean.setGroupNick(linkRequest.destination());
        linkRequestBean.setUserNick(linkRequest.source());

        linkRequestBean.setUserName(linkRequest.sourceDetails().get(UserFields.NAME));
        linkRequestBean.setUserSurname(linkRequest.sourceDetails().get(UserFields.SURNAME));
        linkRequestBean.setUserImagePath(linkRequest.sourceDetails().get(UserFields.IMAGE));

        return linkRequestBean;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    public void stopListener() throws InternalException {
        if(this.threadListener != null) {
            try{
                this.threadListener.shutdown();
            }catch (ListenerException listenerException){
                throw new InternalException(listenerException.getMessage());
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////

}
