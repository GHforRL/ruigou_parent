package cn.rui97.ruigou.service;

import cn.rui97.ruigou.domain.User;
import cn.rui97.ruigou.query.UserQuery;
import cn.rui97.ruigou.util.PageList;
import com.baomidou.mybatisplus.service.IService;

/**
 * @Auther: rui
 * @Date: 2019/1/27 16:42
 * @Description:
 */
public interface IUserService extends IService<User> {
    PageList<User> selectPageList(UserQuery query);
}
