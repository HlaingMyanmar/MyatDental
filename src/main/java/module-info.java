module org.sspd.myatdental {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires spring.context;
    requires org.hibernate.orm.core;
    requires spring.beans;
    requires jakarta.persistence;
    requires java.naming;
    requires com.zaxxer.hikari;
    requires spring.tx;
    requires spring.orm;
    requires spring.core;
    requires jakarta.validation;
    requires org.hibernate.validator;
    requires spring.jdbc;
    requires org.apache.poi.ooxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires java.desktop;
    requires static lombok;
    requires org.slf4j;
    requires jakarta.transaction;
    requires jakarta.cdi;
    requires jfxtras.controls;
    requires com.google.protobuf;
    requires kotlin.stdlib;
    requires javafx.graphics;



    opens org.sspd.myatdental.App to javafx.fxml ,spring.core, spring.beans, org.hibernate.orm.core;
    exports org.sspd.myatdental.App;

    opens org.sspd.myatdental.Configuration to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    exports org.sspd.myatdental.Configuration;

    opens org.sspd.myatdental.dentistsoptions.model to javafx.fxml,javafx.base, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.dentistsoptions.model to javafx.fxml;

    opens org.sspd.myatdental.dentistsoptions.controller to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.dentistsoptions.controller to javafx.fxml;

    opens org.sspd.myatdental.dentistsoptions.impl to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.dentistsoptions.impl to javafx.fxml;

    opens org.sspd.myatdental.dentistsoptions.service to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.dentistsoptions.service to javafx.fxml;

    opens org.sspd.myatdental.ErrorHandler.Validation to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.ErrorHandler.Validation to javafx.fxml;

    opens org.sspd.myatdental.alert to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.alert to javafx.fxml;

    opens org.sspd.myatdental.treatmentoptions.model to javafx.fxml,javafx.base, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.treatmentoptions.model to javafx.fxml;

    opens org.sspd.myatdental.treatmentoptions.controller to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.treatmentoptions.controller to javafx.fxml;

    opens org.sspd.myatdental.treatmentoptions.impl to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.treatmentoptions.impl to javafx.fxml;

    opens org.sspd.myatdental.treatmentoptions.service to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.treatmentoptions.service to javafx.fxml;

    opens org.sspd.myatdental.appointmentsoptions.model to javafx.base,javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.appointmentsoptions.model to javafx.fxml;

    opens org.sspd.myatdental.appointmentsoptions.controller to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.appointmentsoptions.controller to javafx.fxml;

    opens org.sspd.myatdental.appointmentsoptions.impl to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.appointmentsoptions.impl to javafx.fxml;

    opens org.sspd.myatdental.appointmentsoptions.service to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator,java.base;
    exports org.sspd.myatdental.appointmentsoptions.service to javafx.fxml;



    opens org.sspd.myatdental.patientoptions.model to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.patientoptions.model to javafx.fxml;

    exports org.sspd.myatdental.patientoptions.controller to javafx.fxml;
    opens org.sspd.myatdental.patientoptions.controller to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.patientoptions.impl to javafx.fxml;
    opens org.sspd.myatdental.patientoptions.impl to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.patientoptions.service to javafx.fxml;
    opens org.sspd.myatdental.patientoptions.service to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.ErrorHandler to javafx.fxml;
    opens org.sspd.myatdental.ErrorHandler to java.base, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.useroptions to javafx.fxml;
    opens org.sspd.myatdental.useroptions  to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;


    opens org.sspd.myatdental.deletion.model to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core,org.hibernate.validator;
    exports org.sspd.myatdental.deletion.model to javafx.fxml;

    exports org.sspd.myatdental.deletion.controller to javafx.fxml;
    opens org.sspd.myatdental.deletion.controller to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.deletion.impl to javafx.fxml;
    opens org.sspd.myatdental.deletion.impl to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

    exports org.sspd.myatdental.deletion.service to javafx.fxml;
    opens org.sspd.myatdental.deletion.service to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;


    exports org.sspd.myatdental.DateTime to javafx.fxml;
    opens org.sspd.myatdental.DateTime to javafx.fxml, org.hibernate.orm.core, org.hibernate.validator, spring.beans, spring.core;

}