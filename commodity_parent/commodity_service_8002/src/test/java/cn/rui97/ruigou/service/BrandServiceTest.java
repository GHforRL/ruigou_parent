package cn.rui97.ruigou.service;

import cn.rui97.ruigou.CommodityService_8002;
import cn.rui97.ruigou.domain.Brand;
import cn.rui97.ruigou.query.BrandQuery;
import cn.rui97.ruigou.util.PageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommodityService_8002.class )
public class BrandServiceTest {

    @Autowired
    private IBrandService brandService;

     @Test
      public void test() throws Exception{
         BrandQuery query = new BrandQuery();
         query.setKeyword("七");
         PageList<Brand> pagelist = brandService.selectPageList(query);
         System.out.println(pagelist.getTotal());
         List<Brand> rows = pagelist.getRows();
         for (Brand row : rows) {
             System.out.println(row);
             System.out.println(row.getCommodityType());

         }


     }
}
