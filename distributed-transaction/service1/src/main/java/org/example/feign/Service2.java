package org.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service2", url = "http://127.0.0.1:8902")
public interface Service2 {

    /**
     * 扣减库存
     *
     * @param productId 商品 ID
     * @param amount    扣减数量
     * @return 商品总价
     */
    @RequestMapping(value = "reduceStock", method = RequestMethod.POST)
    Double reduceStock(@RequestParam("productId") Long productId, @RequestParam("amount") Integer amount);
}
