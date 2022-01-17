package org.example.feign;

import org.springframework.stereotype.Component;

@Component
public class Service2Hystric implements Service2 {


    @Override
    public Double reduceStock(Long productId, Integer amount) {
        return null;
    }
}
