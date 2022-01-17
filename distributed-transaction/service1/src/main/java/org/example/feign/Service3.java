package org.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service3")
public interface Service3 {

    /**
     * @param userId 用户 ID
     * @param price  扣减金额
     */
    @RequestMapping(value = "reduceBalance", method = RequestMethod.POST)
    void reduceBalance(@RequestParam("userId") Long userId, @RequestParam("price") Double price);
}
