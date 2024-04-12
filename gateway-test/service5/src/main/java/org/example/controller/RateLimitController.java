package org.example.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.example.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/19 15:38
 */
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 按资源名称限流，需要指定限流处理逻辑
     */
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        return new CommonResult("按资源名称限流", 200);
    }

    /**
     * 按URL限流，有默认的限流处理逻辑
     */
    @GetMapping("/byUrl")
    @SentinelResource(value = "byUrl",blockHandler = "handleException")
    public CommonResult byUrl() throws InterruptedException {
        return new CommonResult("按url限流", 200);
    }

    @GetMapping("/byUrl1")
    public CommonResult byUrl1() throws InterruptedException {
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity("http://localhost:8906/service5/rateLimit/moren", CommonResult.class);
        return responseEntity.getBody();
    }

    @GetMapping("/moren")
    public CommonResult moren() {
        return new CommonResult("moren限流", 200);
    }

    public CommonResult handleException(BlockException exception){
        return new CommonResult(exception.getClass().getCanonicalName(),500);
    }
}
