package control.controlutilities;

import model.Filter;
import model.Link;
import model.subjects.*;
import view.bean.observers.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class SecureObjectInputStream extends ObjectInputStream {

    ///////////////////////////////////////////////////////////
    private final List<String> listOfClass = new ArrayList<>();
    ///////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////
    // probably it's better to insert class names inside a file
    public SecureObjectInputStream(InputStream in) throws IOException {
        super(in);

        this.listOfClass.add(Enum.class.getName());
        this.listOfClass.add(Filter.class.getName());

        this.listOfClass.add(String.class.getName());
        this.listOfClass.add(Integer.class.getName());
        this.listOfClass.add(Boolean.class.getName());

        this.listOfClass.add(HashMap.class.getName());
        this.listOfClass.add(Collections.emptyMap().getClass().getName());

        this.listOfClass.add(User.class.getName());
        this.listOfClass.add(UserBean.class.getName());

        this.listOfClass.add(Group.class.getName());
        this.listOfClass.add(GroupBean.class.getName());

        this.listOfClass.add(Meeting.class.getName());
        this.listOfClass.add(MeetingBean.class.getName());

        this.listOfClass.add(Subject.class.getName());
        this.listOfClass.add(Observer.class.getName());

        this.listOfClass.add(Link.class.getName());
        this.listOfClass.add(LinkRequest.class.getName());
        this.listOfClass.add(LinkRequestBean.class.getName());
    }
    ////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException{
        if(!this.listOfClass.contains(osc.getName()))
            throw new InvalidClassException("Unauthorized deserialization", osc.getName());
        return super.resolveClass(osc);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

}
