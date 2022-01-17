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
package example.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import example.entity.Product;
import example.mapper.ProductMapper;
import example.service.ProductService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Resource
    private ProductMapper productMapper;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务
     */
    @DS("database3")
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Double reduceStock(Long productId, Integer amount) {
        log.info("=============PRODUCT START=================");
        log.info("当前 XID: {}", RootContext.getXID());

        // 检查库存
        Product product = productMapper.selectById(productId);
        Assert.notNull(product, "商品不存在");
        Integer stock = product.getStock();
        log.info("商品编号为 {} 的库存为{},订单商品数量为{}", productId, stock, amount);

        if (stock < amount) {
            log.warn("商品编号为{} 库存不足，当前库存:{}", productId, stock);
            throw new RuntimeException("库存不足");
        }
        log.info("开始扣减商品编号为 {} 库存,单价商品价格为{}", productId, product.getPrice());
        // 扣减库存
        int currentStock = stock - amount;
        product.setStock(currentStock);
        productMapper.updateById(product);
        double totalPrice = product.getPrice() * amount;
        log.info("扣减商品编号为 {} 库存成功,扣减后库存为{}, {} 件商品总价为 {} ", productId, currentStock, amount, totalPrice);
        log.info("=============PRODUCT END=================");
        return totalPrice;
    }
}