package cn.rui97.ruigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Auther: rui
 * @Date: 2019/1/12 22:31
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
public class CommodityService_8002 {
    public static void main(String[] args) {
        SpringApplication.run(CommodityService_8002.class);
    }
}
