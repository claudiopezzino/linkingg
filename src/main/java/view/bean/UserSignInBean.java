package view.bean;

import view.bean.errorsignindecorations.NicknameBeanErrorDecorator;
import view.bean.errorsignindecorations.PasswordBeanErrorDecorator;

import static view.bean.Regex.GENERIC_STRING;

public class UserSignInBean {

    ////////////////////////
    private String nickname;
    private String password;
    ////////////////////////

    //////////////////////////////////////////////////////
    private BeanError beanError = new ConcreteBeanError();
    //////////////////////////////////////////////////////


    /////////////////////////////////////////////////////
    public String getNickname() {
        return this.nickname;
    }
    /////////////////////////////////////////////////////

    /////////////////////////////////////////////////////
    public String getPassword() {
        return this.password;
    }
    /////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////
    public void setNickname(String nickname) throws BeanError{
        if(Regex.isMatching(nickname, GENERIC_STRING))
            this.nickname = nickname;
        else{
            beanError = new NicknameBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    /////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////
    public void setPassword(String password) throws BeanError{
        if(Regex.isMatching(password, GENERIC_STRING))
            this.password = password;
        else{
            beanError = new PasswordBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    /////////////////////////////////////////////////////////////

}
