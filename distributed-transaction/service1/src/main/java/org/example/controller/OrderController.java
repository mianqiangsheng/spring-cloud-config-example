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
package org.example.controller;

import io.swagger.annotations.Api;
import org.example.dto.PlaceOrderRequest;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Api(value = "/order", tags = "下订单")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public String placeOrder(@Validated @RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request);
        return "下单成功";
    }

    /**
     * 测试商品库存不足-异常回滚
     * @return
     */
    @PostMapping("/test1")
    public String test1(Integer type) {
        //商品单价10元，库存20个,用户余额50元，模拟一次性购买22个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 22,type));
        return "下单成功";
    }

    /**
     * 测试用户账户余额不足-异常回滚
     * @return
     */
    @PostMapping("/test2")
    public String test2(Integer type) {
        //商品单价10元，库存20个，用户余额50元，模拟一次性购买6个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 6,type));
        return "下单成功";
    }
}