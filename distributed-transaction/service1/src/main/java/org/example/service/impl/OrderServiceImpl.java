/*
 * Copyright © ${project.inceptionYear} organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.example.common.OrderStatus;
import org.example.dto.PlaceOrderRequest;
import org.example.entity.Order;
import org.example.feign.Service2;
import org.example.feign.Service3;
import org.example.mapper.OrderMapper;
import org.example.service.AccountService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private Service2 stockService;
    @Autowired
    private Service3 balanceService;

    @Autowired
    private ProductService productService;
    @Autowired
    private AccountService accountService;

    @DS("master")
    @Override
//    @Transactional
    @GlobalTransactional(rollbackFor = {Throwable.class})
    public void placeOrder(PlaceOrderRequest request) {
        log.info("=============ORDER START=================");
        Long userId = request.getUserId();
        Long productId = request.getProductId();
        Integer amount = request.getAmount();
        Integer type = request.getType();
        log.info("收到下单请求,用户:{}, 商品:{},数量:{}", userId, productId, amount);

        log.info("当前 XID: {}", RootContext.getXID());

        Order order = new Order(userId,productId, OrderStatus.INIT,amount,null);

        orderMapper.insert(order);
        log.info("订单一阶段生成，等待扣库存付款中");
        // 扣减库存并计算总价
        Double totalPrice = null;
        if (1 == type){
            totalPrice = productService.reduceStock(productId, amount);
        }else if (2 == type){
            totalPrice = stockService.reduceStock(productId, amount);
        }
        // 扣减余额
        if (1 == type){
            accountService.reduceBalance(userId, totalPrice);
        }else if (2 == type){
            balanceService.reduceBalance(userId, totalPrice);
        }

        order.setStatus(OrderStatus.SUCCESS);
        order.setTotalPrice(totalPrice);
        orderMapper.updateById(order);
        log.info("订单已成功下单");
        log.info("=============ORDER END=================");
    }
}