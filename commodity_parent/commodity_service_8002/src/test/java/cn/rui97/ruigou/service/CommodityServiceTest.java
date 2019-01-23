package cn.rui97.ruigou.service;

import cn.rui97.ruigou.CommodityService_8002;
import cn.rui97.ruigou.domain.Commodity;
import cn.rui97.ruigou.query.CommodityQuery;
import cn.rui97.ruigou.util.PageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommodityService_8002.class )
public class CommodityServiceTest {

    @Autowired
    private ICommodityService commodityService;

     @Test
      public void test() throws Exception{

         PageList<Commodity> page = commodityService.selectPageList(new CommodityQuery());
         System.out.println(page.getTotal());
         for (Commodity commodity : page.getRows()) {
             System.out.println(commodity);
             System.out.println(commodity.getCommodityType());
             System.out.println(commodity.getBrand());
             System.out.println(commodity.getCommodityExt());
         }

     }
}
