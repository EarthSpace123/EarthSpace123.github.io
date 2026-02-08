package com.php.design_patten_demo.pattern.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderSubject {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void attach(OrderObserver observer) {
        observers.add(observer);
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notify(String orderNo, String message) {
        for (OrderObserver observer : observers) {
            observer.update(orderNo, message);
        }
    }
}
