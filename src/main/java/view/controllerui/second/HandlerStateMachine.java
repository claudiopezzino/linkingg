package view.controllerui.second;

import view.controllerui.second.handlerstates.AbstractState;
import view.graphicalui.second.Home;


public class HandlerStateMachine {

    ////////////////////////////
    private AbstractState state;
    ////////////////////////////

    //////////////////////////////////////////////////////////////
    public void checkCmd(Home home){
        this.state.checkCmd(home);
    }
    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    public AbstractState getState() {
        return this.state;
    }
    //////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    public void setState(AbstractState concreteState) {
        this.state = concreteState;
    }
    /////////////////////////////////////////////////////////////////////////////////

}
