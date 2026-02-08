package com.php.design_patten_demo.config;

import com.php.design_patten_demo.pattern.observer.OrderSubject;
import com.php.design_patten_demo.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private OrderSubject orderSubject;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        orderSubject.attach(notificationService);
    }
}
