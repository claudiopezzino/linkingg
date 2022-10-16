module view.graphicalui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires java.sql;

    opens view.graphicalui.first to javafx.fxml;
    exports view.graphicalui.first;

    opens view.graphicalui.second to javafx.fxml;
    exports view.graphicalui.second;

    exports view.controllerui.first; // due to warning for handlers in SignPage.java
    exports view.controllerui.second;
    exports view.graphicalui.second.meetingquestionstates; // due to warning for getStateMachine method of Home
    exports view.controllerui.second.handlerstates;
    exports view.graphicalui.first.constcontainer;
    opens view.graphicalui.first.constcontainer to javafx.fxml; // due to warning for AbstractState
}