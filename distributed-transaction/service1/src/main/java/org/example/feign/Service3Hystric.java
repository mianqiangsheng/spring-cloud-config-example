package org.example.feign;

import org.springframework.stereotype.Component;

@Component
public class Service3Hystric implements Service3 {

    @Override
    public void reduceBalance(Long userId, Double price) {

    }
}
