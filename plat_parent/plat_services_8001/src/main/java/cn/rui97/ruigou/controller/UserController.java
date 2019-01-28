package cn.rui97.ruigou.controller;

import cn.rui97.ruigou.domain.User;
import cn.rui97.ruigou.query.UserQuery;
import cn.rui97.ruigou.service.IUserService;
import cn.rui97.ruigou.util.AjaxResult;
import cn.rui97.ruigou.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: rui
 * @Date: 2019/1/27 18:33
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 保存和修改公用的
     * @param user  传递的实体
     * @return Ajaxresult转换结果
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public AjaxResult save(@RequestBody User user){
        try {
            if(user.getId()!=null){
                userService.updateById(user);
            }else{
                userService.insert(user);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存用户失败！"+e.getMessage());
        }
    }

    //删除方法优化（可以删一条也可以删多条）
    @RequestMapping(value="/{ids}",method=RequestMethod.DELETE)
    public AjaxResult deleteMany(@PathVariable("ids") Long[] ids){
        try {
            for (Long id : ids) {
                userService.deleteById(id);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除用户失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User get(@PathVariable(value="id",required=true) Long id) {
        return userService.selectById(id);
    }

    /**
     * 查看所有的员工信息
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<User> list(){
        return userService.selectList(null);
    }

    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<User> json(@RequestBody UserQuery query) {
        return  userService.selectPageList(query);
    }
}
