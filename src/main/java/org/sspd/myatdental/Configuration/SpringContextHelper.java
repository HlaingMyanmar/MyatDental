package org.sspd.myatdental.Configuration;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper {
    @Getter
    private static final ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
}
