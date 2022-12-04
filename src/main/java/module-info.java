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

    exports control.tasks.tasksexceptions; // due to sonar for freeResources method inside userManageCommunityBoundary
    exports model.modelexceptions; // due to warning into UserCollector for NoEntityException
    exports model.db.dbexceptions; // due to Sonar Warning inside ImageProfile class
    exports model; // due to Sonar Warning for updateNickname method of user entity
    exports control.controlutilities; // due to Sonar Warning for CopyException
    exports model.subjects; // due to Warning for setSubject method
    exports view.bean.observers; // due to setGroupBean method inside HomePageEventHandler
    exports control.controlexceptions; // as consequence of SignInPageEventHandler modification
    exports view.bean; // as consequence of SignInPageEventHandler modification
    exports view.boundary; // due to Sonar Warning for Boundary as param for method of SignInPageEventHandler
    exports view.controllerui.first; // due to warning for handlers in SignPage.java
    exports view.controllerui.second;
    exports view.graphicalui.second.meetingquestionstates; // due to warning for getStateMachine method of Home
    exports view.controllerui.second.handlerstates;
    exports view.graphicalui.first.constcontainer;
    opens view.graphicalui.first.constcontainer to javafx.fxml;
    exports view.graphicalui.first.listviewitems;
    opens view.graphicalui.first.listviewitems to javafx.fxml; // due to warning for AbstractState
}