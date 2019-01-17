package cn.rui97.ruigou.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisClientFallbackFactory implements FallbackFactory<RedisClient> {
    @Override
    public RedisClient create(Throwable throwable) {
        return new RedisClient() {
            @Override
            public void set(String key,String value) {

            }

            @Override
            public String get(String key) {
                return null;
            }
        };
    }
}
