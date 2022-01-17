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
package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.example.common.OrderStatus;

import java.io.Serializable;

@TableName("p_order")
public class Order implements Serializable {

    public Order() {
    }

    private static final long serialVersionUID = 8808445618244633180L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 订单状态
     */
    private OrderStatus status;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 总金额
     */
    private Double totalPrice;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order(Long userId, Long productId, OrderStatus status, Integer amount, Double totalPrice) {
        this.userId = userId;
        this.productId = productId;
        this.status = status;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }
}