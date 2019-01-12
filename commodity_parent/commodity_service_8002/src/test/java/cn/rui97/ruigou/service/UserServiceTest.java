package cn.rui97.ruigou.service;

import cn.rui97.ruigou.CommodityService_8002;
import cn.rui97.ruigou.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommodityService_8002.class )
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testAdd() throws Exception{

        for (int i = 0;i<20;i++){
            if (i%2==0){
                User user = new User();
                user.setName("ls"+i);
                userService.insert(user);
            }
            else{
                User user = new User();
                user.setName("zs"+i);
                userService.insert(user);
            }
        }

    }

     @Test
      public void test() throws Exception{

         List<User> users = userService.selectList(null);
         System.out.println(users);

     }
}
