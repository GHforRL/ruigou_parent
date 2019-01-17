package cn.rui97.ruigou.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "RUIGOU-COMMON",fallbackFactory = RedisClientFallbackFactory.class )//服务提供者的名称
public interface RedisClient {

    @RequestMapping(value = "/redis",method = RequestMethod.POST)
    void set(@RequestParam("key") String key, @RequestParam("value") String value);
    @RequestMapping(value = "/redis",method = RequestMethod.GET)
    String get(@RequestParam("key") String key);

}
