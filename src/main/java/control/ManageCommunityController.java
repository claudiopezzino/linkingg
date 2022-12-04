package control;

import control.controlexceptions.InternalException;
import control.controlutilities.CopyException;
import control.tasks.Listener;
import control.tasks.tasksexceptions.ListenerException;
import javafx.util.Pair;
import model.subjects.Group;
import model.subjects.Meeting;
import model.subjects.User;
import view.bean.*;
import view.bean.observers.GroupBean;
import view.bean.observers.MeetingBean;
import view.bean.observers.UserBean;

import java.util.HashMap;
import java.util.Map;


public class ManageCommunityController {

    ////////////////////////////////
    private Listener threadListener;
    ////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public UserSignInBean setUpRegistrationPhase(LoginController loginController, UserSignUpBean userSignUpBean) throws InternalException {
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
        this.threadListener.setDaemon(true); // doing this the application will stop also this thread other than itself
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

        Map<String, MeetingBean> mapMeetingBean = new HashMap<>();
        for(Map.Entry<String, Meeting> entry : group.plannedMeeting().entrySet())
            mapMeetingBean.put(entry.getKey(), this.turnIntoMeetingBean(entry.getValue()));
        try{
            groupBean.setMapMeetings(mapMeetingBean);
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
