package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.domain.User;
import cn.rui97.ruigou.service.IUserService;
import cn.rui97.ruigou.util.AjaxResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 参数: User
     * 返回值:登录是否成功 AjaxResult
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    //以后传递对象前太都要用json对象{},[],后台通过(@RequestBody
    public AjaxResult login(@RequestBody User user){

        if(user!=null){
            User userdb = userService.selectOne(new EntityWrapper<User>().eq("username", user.getUsername()));
            //正常来说需要到数据库通过用户名查询用户,查询出来判断是否为空,如果不为空还要比对密码
            if (userdb.getUsername().equals(user.getUsername())&&userdb.getPassword().equals(user.getPassword())){
                Object resultObj = JSONObject.toJSON(userdb);
                return AjaxResult.me().setResultObj(resultObj);
            }
            return AjaxResult.me().setSuccess(false).setMessage("用户名或密码错误!");
        }
        return AjaxResult.me().setSuccess(false).setMessage("用户名不存在！");

    }
}
