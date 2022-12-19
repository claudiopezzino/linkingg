package view.bean;

import view.bean.errorsignupdecorations.*;

import static view.bean.Regex.*;

public class UserSignUpBean {

    ////////////////////////
    private String name;
    private String surname;
    private String address;
    private String email;
    private String cell;
    private String account;
    ////////////////////////

    //////////////////////////////////////////////
    BeanError beanError = new ConcreteBeanError();
    //////////////////////////////////////////////


    /*--------------------- GETTER ----------------------*/
    /////////////////////////////////////////////
    public String getName() {
        return this.name;
    }
    /////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String getSurname() {
        return this.surname;
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String getAddress() {
        return this.address;
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////
    public String getEmail() {
        return this.email;
    }
    ///////////////////////////////////////////////

    /////////////////////////////////////////////
    public String getCell() {
        return this.cell;
    }
    /////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public String getAccount() {
        return this.account;
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////
    public String getProvince(){
        String[] addressTokens = this.address.split(",");
        return addressTokens[3];
    }
    ///////////////////////////////////////////////////////////


    /*---------------------------- SETTER ----------------------------*/
    //////////////////////////////////////////////////////////
    public void setName(String name) throws BeanError {
        if(isMatching(name, ALPHA_STRING))
            this.name = name;
        else{
            beanError = new NameBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setSurname(String surname) throws BeanError {
        if(isMatching(surname, ALPHA_STRING))
            this.surname = surname;
        else{
            beanError = new SurnameBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setAddress(String address) throws BeanError {
        if(isMatching(address, ADDRESS))
            this.address = address;
        else{
            beanError = new AddressBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setEmail(String email) throws BeanError {
        if(isMatching(email, MAIL))
            this.email = email;
        else{
            beanError = new EmailBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setCell(String cell) throws BeanError {
        if(isMatching(cell, CELL))
            this.cell = cell;
        else{
            beanError = new CellBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////
    public void setAccount(String account) throws BeanError {
        if(isMatching(account, ACCOUNT))
            this.account = account;
        else{
            beanError = new AccountBeanErrorDecorator(beanError);
            throw beanError;
        }
    }
    //////////////////////////////////////////////////////////

}
