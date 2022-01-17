package org.example.service.impl;


import org.example.feign.Service2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@EnableFeignClients(clients = Service2.class)
public class OrderServiceImplTest {

    @Autowired
    private Service2 stockService;

    @Test
    public void test() throws Exception {
        stockService.reduceStock(1L,1);
    }
}